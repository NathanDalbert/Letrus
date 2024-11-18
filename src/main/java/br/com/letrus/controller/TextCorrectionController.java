package br.com.letrus.controller;

import br.com.letrus.service.LanguageToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text-correction")
public class TextCorrectionController {

    @Autowired
    private LanguageToolService languageToolService;

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/check-grammar")
    public ResponseEntity<?> checkGrammar(@RequestBody String text) {
        try {
            // Chama o serviço para verificar a gramática
            String result = languageToolService.checkGrammar(text);

            // Se a resposta for bem-sucedida, retorna o texto corrigido ou os erros
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta de erro com um status apropriado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o texto: " + e.getMessage());
        }
    }
}
