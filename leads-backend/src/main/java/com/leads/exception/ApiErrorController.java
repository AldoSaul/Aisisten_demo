package com.leads.exception;

import java.util.Map;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        int statusCode = resolveStatusCode(request);
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String path = resolvePath(request);
        String error = switch (status.value()) {
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> status.getReasonPhrase();
        };

        return ResponseEntity.status(status)
            .body(Map.of(
                "status", status.value(),
                "error", error,
                "path", path
            ));
    }

    private int resolveStatusCode(HttpServletRequest request) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusAttr instanceof Integer value) {
            return value;
        }
        if (statusAttr instanceof String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
                return HttpStatus.NOT_FOUND.value();
            }
        }
        // Direct calls to /error do not carry an error status. Treat those as 404.
        return HttpStatus.NOT_FOUND.value();
    }

    private String resolvePath(HttpServletRequest request) {
        Object pathAttr = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (pathAttr instanceof String value && !value.isBlank()) {
            return value;
        }
        return request.getRequestURI();
    }
}
