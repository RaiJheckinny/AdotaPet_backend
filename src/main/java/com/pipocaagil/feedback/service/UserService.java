package com.pipocaagil.feedback.service;

import com.pipocaagil.feedback.exception.RecursoNaoEncontradoException;
import com.pipocaagil.feedback.repository.UserRepository;
import com.pipocaagil.feedback.security.Role;
import com.pipocaagil.feedback.security.RoleName;
import com.pipocaagil.feedback.security.UserDetailsImpl;
import com.pipocaagil.feedback.security.configuration.SecurityConfiguration;
import com.pipocaagil.feedback.users.User;
import com.pipocaagil.feedback.users.dto.CreateUserDto;
import com.pipocaagil.feedback.users.dto.CreateUserOngDto;
import com.pipocaagil.feedback.users.dto.LoginUserDto;
import com.pipocaagil.feedback.users.dto.RecoveryJwtTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    private final RestTemplate restTemplate = new RestTemplate();


    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Método responsável por criar um usuário da Ong
    public void createUserOng(CreateUserOngDto createUserOngDto) {
        validarCepExistente(createUserOngDto.cep());

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
                .email(createUserOngDto.email())
                // Codifica a senha do usuário com o algoritmo bcrypt
                .password(securityConfiguration.passwordEncoder().encode(createUserOngDto.password()))
                .name(createUserOngDto.name())
                .cep(createUserOngDto.cep())
                .cnpj(createUserOngDto.cnpj())
                .cep(createUserOngDto.cep().replaceAll("[^0-9]", "")) // Opcional: Limpa pontos e hífens antes de salvar                .areaAtuacao(createUserOngDto.areaAtuacao())
                // Atribui ao usuário uma permissão específica
                .roles(List.of(Role.builder().name(RoleName.valueOf("ROLE_ONG")).build()))
                .build();

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }

    // Método responsável por criar um usuário da Comum
    public void createUser(CreateUserDto createUserDto) {
        validarCepExistente(createUserDto.cep());

        // Cria um novo usuário Comum com os dados fornecidos
    User newUser = User.builder()
            .email(createUserDto.email())
            // Codifica a senha do usuário com o algoritmo bcrypt
            .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
            .name(createUserDto.name())
            .cep(createUserDto.cep())
            .cep(createUserDto.cep().replaceAll("[^0-9]", "")) // Opcional: Limpa pontos e hífens antes de salvar                .areaAtuacao(createUserOngDto.areaAtuacao())
            // Atribui ao usuário uma permissão específica
            .roles(List.of(Role.builder().name(RoleName.valueOf("ROLE_USER")).build()))
            .build();

    // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
}

    public Boolean isCnpj(String email,String cnpf) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user.getCnpj().equals(cnpf)){
            return true;
        }

        return false;
    }
    //User Ja ta Cadastrado
    public Boolean isEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Método auxiliar para validar se o CEP existe de fato
    private void validarCepExistente(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (!cepLimpo.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido.");
        }

        String viaCepUrl = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
        String awesomeUrl = "https://cep.awesomeapi.com.br/json/" + cepLimpo;

        // 1 - Tenta ViaCEP
        try {
            Map<?, ?> response = restTemplate.getForObject(viaCepUrl, Map.class);

            if (response != null && !response.containsKey("erro")) {
                return;
            }
        } catch (Exception ignored) {
        }

        // 2 - Se o ViaCEP falhar ou não encontrar, tenta AwesomeAPI
        try {
            Map<?, ?> response = restTemplate.getForObject(awesomeUrl, Map.class);

            // A AwesomeAPI retorna dados quando o CEP existe
            if (response != null
                    && response.get("cep") != null
                    && response.get("address") != null) {
                return;
            }
        } catch (Exception ignored) {
        }

        // 3 - Nenhuma das APIs encontrou o CEP
        throw new RecursoNaoEncontradoException(
                "O CEP " + cep + " não foi encontrado."
        );
    }

}
