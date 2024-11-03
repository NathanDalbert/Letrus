package br.com.letrus.dto.response;

import java.util.UUID;

public record UsuarioResponseDTO(UUID idUsario, String name, String email) {
}
