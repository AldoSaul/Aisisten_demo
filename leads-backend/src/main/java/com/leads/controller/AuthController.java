package com.leads.controller;

//import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.model.Tenant;
import com.leads.repository.TenantRepository;
import com.leads.service.OAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService     oauthService;
    private final TenantRepository tenantRepo;

    /** El cliente llama aquí con su tenantId → redirige a Facebook Login */
    @GetMapping("/instagram")
    public RedirectView iniciarLogin(@RequestParam String tenantId) {
        String url = oauthService.buildAuthorizationUrl(tenantId);
        return new RedirectView(url);
    }

    /**
     * Facebook redirige aquí con el code.
     * El state contiene el tenantId (o "new" si es un registro nuevo).
     */
    @GetMapping("/callback")
    public ResponseEntity<String> callback(
            @RequestParam String code,
            @RequestParam(required = false) String state) {

        try {
            // 1. Obtener long-lived token
            String longToken = oauthService.exchangeCodeForLongLivedToken(code);

            // 2. Obtener páginas del usuario
            JsonNode pages = oauthService.fetchPages(longToken);
            JsonNode firstPage = pages.path("data").path(0);
            String pageId = firstPage.path("id").asText();
            String pageName = firstPage.path("name").asText();

            // 3. Buscar o crear Tenant
            Tenant tenant = tenantRepo.findByFacebookPageId(pageId)
                .orElseGet(() -> Tenant.builder()
                    .facebookPageId(pageId)
                    .nombre(pageName)
                    .build());

            //tenant.setAccessToken(longToken);
            //tenant.setTokenExpira(LocalDateTime.now().plusDays(60));
            tenantRepo.save(tenant);

            log.info("Tenant conectado: {} (pageId={})", pageName, pageId);
            return ResponseEntity.ok("Cuenta conectada: " + pageName);

        } catch (Exception ex) {
            log.error("Error en OAuth callback: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().body("Error en autorización");
        }
    }
}
