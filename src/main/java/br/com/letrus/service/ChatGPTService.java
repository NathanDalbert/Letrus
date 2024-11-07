package br.com.letrus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ChatGPTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String completarTexto(String prompt) {
        String url = "https://api.openai.com/v1/completions";

        // Definir os parâmetros da requisição para o modelo GPT-3
        JSONObject requestPayload = new JSONObject();
        requestPayload.put("model", "text-davinci-003");  // Modelo GPT-3
        requestPayload.put("prompt", prompt);  // Texto do usuário
        requestPayload.put("max_tokens", 100);  // Limite de palavras que podem ser completadas
        requestPayload.put("temperature", 0.7);  // Nível de aleatoriedade nas respostas

        // Configurar a requisição HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestPayload.toString(), headers);

        // Enviar requisição e receber a resposta
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        // Extrair o texto completado da resposta
        JSONObject responseJson = new JSONObject(response.getBody());
        return responseJson.getJSONArray("choices").getJSONObject(0).getString("text").trim();
    }
}
