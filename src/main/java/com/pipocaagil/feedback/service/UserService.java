package com.pipocaagil.feedback.service;

import com.pipocaagil.feedback.repository.UserRepository;
import com.pipocaagil.feedback.security.Role;
import com.pipocaagil.feedback.security.UserDetailsImpl;
import com.pipocaagil.feedback.security.configuration.SecurityConfiguration;
import com.pipocaagil.feedback.users.User;
import com.pipocaagil.feedback.users.dto.CreateUserDto;
import com.pipocaagil.feedback.users.dto.LoginUserDto;
import com.pipocaagil.feedback.users.dto.RecoveryJwtTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Método responsável por criar um usuário
    public void createUser(CreateUserDto createUserDto) {

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
                .email(createUserDto.email())
                // Codifica a senha do usuário com o algoritmo bcrypt
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                .name(createUserDto.name())
                .cep(createUserDto.cep())
                .cnpj(createUserDto.cnpj())
                .cep(createUserDto.cep())
                .areaAtuacao(createUserDto.areaAtuacao())
                .emailConfirmConfirmado(createUserDto.emailConfirmConfirmado())
                // Atribui ao usuário uma permissão específica
                .roles(List.of(Role.builder().name(createUserDto.role()).build()))
                .build();

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }

    public void enviarEmail(String email) {

        try {
            SimpleMailMessage emailEnviar = new SimpleMailMessage();

            emailEnviar.setFrom("seuemail@gmail.com"); // mesmo e-mail configurado no application.properties
            emailEnviar.setTo(email);
            emailEnviar.setSubject("Código de Verificação");

            Random random = new Random();
            int numero = 100000 + random.nextInt(900000);

            emailEnviar.setText(String.valueOf(numero));

            System.out.println("E-mail enviado com sucesso!");

        } catch (MailAuthenticationException e) {
            System.out.println("Usuário ou senha do SMTP incorretos.");
        } catch (MailException e) {
            System.out.println("Erro ao enviar: " + e.getMessage());
        }
    }



}
