package br.com.letrus.controller;


import br.com.letrus.dto.request.UsuarioRequestDTO;
import br.com.letrus.dto.response.UsuarioResponseDTO;
import br.com.letrus.service.UsuarioServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

   private final UsuarioServiceInterface usuarioServiceInterface;
   
    @PostMapping
    public ResponseEntity criarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity.ok(usuarioServiceInterface.criarUsuario(usuarioRequestDTO));
    }
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuario() {
        return ResponseEntity.ok(usuarioServiceInterface.listarUsuario());
    }

}
