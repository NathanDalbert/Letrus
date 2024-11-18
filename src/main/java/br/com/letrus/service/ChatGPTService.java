package br.com.letrus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
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
        String url = "https://api.openai.com/v1/chat/completions";

        // Criar o corpo da requisição para o modelo GPT-3.5-turbo
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject requestPayload = new JSONObject();
        requestPayload.put("model", "gpt-3.5-turbo");
        requestPayload.put("messages", messages);
        requestPayload.put("max_tokens", 100);
        requestPayload.put("temperature", 0.7);

        // Configurar a requisição HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestPayload.toString(), headers);

        // Enviar requisição e receber a resposta
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        // Verificar se a resposta foi bem-sucedida
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao chamar a API OpenAI: " + response.getStatusCode());
        }

        // Extrair o texto completado da resposta
        JSONObject responseJson = new JSONObject(response.getBody());
        return responseJson.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }
}
