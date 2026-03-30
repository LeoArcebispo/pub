package com.grupo_5.pub.Controller;

import com.grupo_5.pub.DTO.CadastroRequestDTO;
import com.grupo_5.pub.Infra.Security.TokenService;
import com.grupo_5.pub.DTO.LoginRequestDTO;
import com.grupo_5.pub.DTO.LoginResponseDTO;
import com.grupo_5.pub.DTO.ErrorResponseDTO;

import com.grupo_5.pub.Model.Sessao;
import com.grupo_5.pub.Model.Usuario;
import com.grupo_5.pub.Repository.SessaoRepository;
import com.grupo_5.pub.Repository.UsuarioRepository;

import com.grupo_5.pub.Fila.FilaEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/auth")
public class UsuarioController {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody LoginRequestDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(
                data.getUsername(),
                data.getPassword()
        );

        try {
            authenticationManager.authenticate(usernamePassword);

            Sessao sessao = new Sessao();
            sessao.setNome(data.getUsername());
            sessao.setCriadoEm(LocalDateTime.now());
            sessao.setExpiraEm(LocalDateTime.now().plusSeconds(30));
            sessaoRepository.save(sessao);

            String token = tokenService.generateToken(data.getUsername());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ErrorResponseDTO("Credenciais inválidas"));
        }

    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody CadastroRequestDTO data) {

        if (usuarioRepository.findByUsername(data.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(data.getUsername());
        usuario.setPassword(passwordEncoder.encode(data.getPassword()));

        usuarioRepository.save(usuario);

        FilaEmail.fila.add("Bem-vindo, " + usuario.getUsername());

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
