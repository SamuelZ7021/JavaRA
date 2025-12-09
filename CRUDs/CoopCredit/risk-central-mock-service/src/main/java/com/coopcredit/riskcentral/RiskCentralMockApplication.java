package com.coopcredit.riskcentral;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RiskCentralMockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskCentralMockApplication.class, args);
    }
}

@RestController
class RiskController {

    @PostMapping("/risk-evaluation")
    public Map<String, Object> evaluateRisk(@RequestBody RiskRequest request) {
        String document = request.getDocument();

        // Lógica determinista: Hash del documento para generar score consistente (0-100)
        // Documento '123' siempre dará el mismo score.
        int hash = document != null ? document.hashCode() : 0;
        int score = Math.abs(hash % 101); // 0 a 100

        // Simulación de latencia (opcional, para realismo)
        try { Thread.sleep(50); } catch (InterruptedException e) {}

        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        return response;
    }
}

@Data
class RiskRequest {
    private String document;
}