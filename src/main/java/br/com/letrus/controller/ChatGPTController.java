package br.com.letrus.controller;

import br.com.letrus.service.ChatGPTService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat-gpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/completar")
    public ResponseEntity<?> completarTexto(@RequestBody String prompt) {
        try {
            String resposta = chatGPTService.completarTexto(prompt);
            return ResponseEntity.ok().body(Map.of("resposta", resposta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao processar a solicitação", "detalhes", e.getMessage()));
        }
    }

}
