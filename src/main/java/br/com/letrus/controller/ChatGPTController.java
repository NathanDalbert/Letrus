package br.com.letrus.controller;

import br.com.letrus.service.ChatGPTService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chatgpt")
@CrossOrigin(origins = "http://localhost:63342/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/completar")
    public String completarTexto(@RequestBody String prompt) {
        return chatGPTService.completarTexto(prompt);
    }
}
