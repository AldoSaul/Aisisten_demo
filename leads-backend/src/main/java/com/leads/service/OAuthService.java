package com.leads.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.model.Tenant;
import com.leads.repository.TenantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final TenantRepository tenantRepo;
    private final RestTemplate restTemplate;

    @Value("${meta.app-id}")       private String appId;
    @Value("${meta.app-secret}")   private String appSecret;
    @Value("${meta.redirect-uri}") private String redirectUri;
    @Value("${meta.graph-api-url}") private String graphApiUrl;

    /** URL a la que redirigimos al usuario para que autorice */
    public String buildAuthorizationUrl(String state) {
        return "https://www.facebook.com/v19.0/dialog/oauth" +
            "?client_id=" + appId +
            "&redirect_uri=" + redirectUri +
            "&scope=instagram_basic,instagram_manage_messages,pages_messaging,pages_show_list" +
            "&response_type=code" +
            "&state=" + state;
    }

    /** Intercambia el code por un short-lived token y luego lo convierte a long-lived */
    public String exchangeCodeForLongLivedToken(String code) {
        // 1. Short-lived token (1 hora)
        String shortUrl = graphApiUrl + "/oauth/access_token" +
            "?client_id=" + appId +
            "&client_secret=" + appSecret +
            "&redirect_uri=" + redirectUri +
            "&code=" + code;

        JsonNode shortResp = restTemplate.getForObject(shortUrl, JsonNode.class);
        String shortToken  = shortResp != null ? shortResp.path("access_token").asText() : null;
        if (shortToken == null) throw new RuntimeException("No se obtuvo short-lived token");

        // 2. Long-lived token (60 días)
        String longUrl = graphApiUrl + "/oauth/access_token" +
            "?grant_type=fb_exchange_token" +
            "&client_id=" + appId +
            "&client_secret=" + appSecret +
            "&fb_exchange_token=" + shortToken;

        JsonNode longResp = restTemplate.getForObject(longUrl, JsonNode.class);
        return longResp != null ? longResp.path("access_token").asText() : null;
    }

    /** Obtiene las páginas de Facebook asociadas a este token */
    public JsonNode fetchPages(String accessToken) {
        String url = graphApiUrl + "/me/accounts?access_token=" + accessToken;
        return restTemplate.getForObject(url, JsonNode.class);
    }

    /** Refresca un long-lived token antes de que expire */
    public String refreshToken(String expiredToken) {
        String url = graphApiUrl + "/oauth/access_token" +
            "?grant_type=fb_exchange_token" +
            "&client_id=" + appId +
            "&client_secret=" + appSecret +
            "&fb_exchange_token=" + expiredToken;
        JsonNode resp = restTemplate.getForObject(url, JsonNode.class);
        return resp != null ? resp.path("access_token").asText() : null;
    }

    /** Job diario: refresca tokens que van a expirar en los próximos 10 días */
    @Scheduled(cron = "0 0 3 * * *")
    public void autoRefreshTokens() {
        LocalDateTime limite = LocalDateTime.now().plusDays(10);
        List<Tenant> proximos = tenantRepo.findByTokenExpiraBeforeAndActivoTrue(limite);
        log.info("Refrescando {} tokens próximos a expirar", proximos.size());
        for (Tenant tenant : proximos) {
            try {
                String nuevo = refreshToken(tenant.getAccessToken());
                if (nuevo != null) {
                    tenant.setAccessToken(nuevo);
                    tenant.setTokenExpira(LocalDateTime.now().plusDays(60));
                    tenantRepo.save(tenant);
                    log.info("Token renovado para tenant {}", tenant.getId());
                }
            } catch (Exception ex) {
                log.error("Error renovando token del tenant {}: {}", tenant.getId(), ex.getMessage());
            }
        }
    }
}
