package br.com.letrus.controller;

import br.com.letrus.dto.authentication.AutenticationDTO;
import br.com.letrus.dto.authentication.RegisterDTO;
import br.com.letrus.dto.response.LoginResponseDTO;
import br.com.letrus.infra.security.TokenService;
import br.com.letrus.model.Usuario;
import br.com.letrus.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticationController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public ResponseEntity<?> login(@RequestBody @Valid AutenticationDTO data) {
        var usuarioOptional = usuarioRepository.findByEmail(data.email());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        var usuario = usuarioOptional.get();
        var usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        try {
            var auth = authenticationManager.authenticate(usernamePasswordAuthToken);
            boolean isPasswordMatch = passwordEncoder.matches(data.senha(), usuario.getPassword());

            if (!isPasswordMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
            }

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro na autenticação.");
        }
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
        }

        if (data.senha() == null || data.senha().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula.");
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());

        Usuario user = new Usuario();
        user.setName(data.nome());
        user.setEmail(data.email());
        user.setPassword(encryptedPassword);

        usuarioRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso.");
    }
}
