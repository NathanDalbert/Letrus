package br.com.letrus.service.mapper;


import br.com.letrus.dto.request.UsuarioRequestDTO;

import br.com.letrus.dto.response.UsuarioResponseDTO;

import br.com.letrus.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UsuarioServiceMapper {

    public Usuario toUsuario(@Valid UsuarioRequestDTO professorRequestDTO) {
        return  Usuario.newUsuario(
                professorRequestDTO.name(),
                professorRequestDTO.email(),
                professorRequestDTO.password()

        );
    }

    public UsuarioResponseDTO toUsuarioResponseDTO( Usuario usuario) {
        return new  UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getName()

        );
    }
}
