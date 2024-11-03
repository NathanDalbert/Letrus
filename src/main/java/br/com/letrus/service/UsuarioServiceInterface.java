package br.com.letrus.service;

import br.com.letrus.dto.request.UsuarioRequestDTO;

import br.com.letrus.dto.response.UsuarioResponseDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceInterface {
    UsuarioResponseDTO criarUsuario(@Valid UsuarioRequestDTO professorRequestDTO);
    List< UsuarioResponseDTO> listarUsuario();
    void deletarUsuario(UUID id);
    UsuarioResponseDTO atualizarUsuario(UUID id,  UsuarioRequestDTO professorRequestDTO);
}
