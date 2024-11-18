package br.com.letrus.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class LanguageToolService {

    private final String API_URL = "https://api.languagetool.org/v2/check";

    public String checkGrammar(String text) {
        RestTemplate restTemplate = new RestTemplate();

        // Definindo os parâmetros para o corpo da requisição
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("text", text);
        body.add("language", "pt-BR");

        // Definindo os cabeçalhos, incluindo o tipo de conteúdo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Criando a entidade para a requisição
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        // Enviando a requisição POST
        ResponseEntity<String> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Imprime a resposta da API para depuração
        System.out.println("Resposta da API: " + response.getBody());

        // Verifica se a resposta foi bem-sucedida
        if (response.getStatusCode().is2xxSuccessful()) {
            // Processar o corpo da resposta para corrigir o texto
            return processCorrectionResponse(response.getBody(), text);
        } else {
            // Tratar erros, por exemplo, se a resposta não for OK
            throw new RuntimeException("Erro ao acessar a API LanguageTool: " + response.getStatusCode());
        }
    }

    // Método que processa a resposta e corrige o texto
    private String processCorrectionResponse(String responseBody, String originalText) {
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Verifica se o campo "matches" está presente
        if (!jsonResponse.has("matches")) {
            System.out.println("Nenhuma correção encontrada.");
            return originalText;  // Retorna o texto original se não houver correções
        }

        JSONArray matches = jsonResponse.getJSONArray("matches");

        String correctedText = originalText;

        // Iterando sobre as correções sugeridas e aplicando ao texto
        for (int i = 0; i < matches.length(); i++) {
            JSONObject match = matches.getJSONObject(i);

            // Substituindo as palavras erradas pelo valor sugerido
            if (match.has("replacements")) {
                JSONArray replacements = match.getJSONArray("replacements");
                String replacement = replacements.getJSONObject(0).getString("value");

                // Extraímos o texto de contexto corretamente
                String contextText = match.getJSONObject("context").getString("text");

                // Debug: Exibir detalhes sobre a correção
                System.out.println("Correção encontrada: " + contextText.substring(match.getInt("offset"), match.getInt("offset") + match.getInt("length")));
                System.out.println("Substituindo por: " + replacement);

                // Verificando se o intervalo de correção é válido
                int offset = match.getInt("offset");
                int length = match.getInt("length");

                // Garantir que o intervalo de substituição não ultrapasse o limite do texto
                if (offset + length <= contextText.length()) {
                    String incorrectWord = contextText.substring(offset, offset + length);

                    // Substitui a palavra incorreta no texto original
                    correctedText = correctedText.replace(incorrectWord, replacement);
                } else {
                    // Caso o intervalo seja inválido, imprime um erro de depuração
                    System.out.println("Erro: Intervalo de substituição inválido para a correção.");
                }
            }
        }

        // Retorna o texto corrigido como string
        return correctedText;
    }
}
