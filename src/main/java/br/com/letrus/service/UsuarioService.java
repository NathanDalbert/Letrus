package br.com.letrus.service;


import br.com.letrus.dto.request.UsuarioRequestDTO;

import br.com.letrus.dto.response.UsuarioResponseDTO;

import br.com.letrus.model.Usuario;

import br.com.letrus.repository.UsuarioRepository;

import br.com.letrus.service.mapper.UsuarioServiceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioServiceInterface {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceMapper usuarioServiceMapper;


    @Override
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO professorRequestDTO) {
        Usuario usuario = usuarioServiceMapper.toUsuario(professorRequestDTO);
        usuarioRepository.save(usuario);
        return usuarioServiceMapper.toUsuarioResponseDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuario() {
        return usuarioRepository.findAll().stream()
                .map(usuarioServiceMapper::toUsuarioResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarUsuario(UUID id) {
        usuarioRepository.deleteById(id);

    }

    @Override
    public UsuarioResponseDTO atualizarUsuario(UUID id, UsuarioRequestDTO professorRequestDTO) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setName(professorRequestDTO.name());
        usuario.setEmail(professorRequestDTO.email());
        usuario.setPassword(professorRequestDTO.password());
        usuarioRepository.save(usuario);
        return usuarioServiceMapper.toUsuarioResponseDTO(usuario);
    }
}
