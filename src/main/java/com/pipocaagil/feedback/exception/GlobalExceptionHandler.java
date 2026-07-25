package com.pipocaagil.feedback.exception;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("E-mail ou senha inválidos.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Método HTTP não permitido.");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwt(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido ou expirado.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Já existe um registro com esses dados.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabase(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao acessar o banco de dados.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handle404(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Endpoint não encontrado.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Acesso negado.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body("Valor inválido informado.");
    }

    @ExceptionHandler(MailAuthenticationException.class)
    public ResponseEntity<String> handleMailAuthentication(MailAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro de autenticação do servidor de e-mail.");
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<String> handleMail(MailException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao enviar e-mail.");
    }


    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, String>> handleUnknownProperty(UnrecognizedPropertyException ex) {

        Map<String, String> body = new HashMap<>();
        body.put("erro", "Campo '" + ex.getPropertyName() + "' não existe.");
        body.put("campo", ex.getPropertyName());

        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("mensagem", "Campos obrigatórios não informados ou inválidos.");

        String dto = ex.getBindingResult().getObjectName();

        if ("createUserDto".equals(dto)) {

            body.put("jsonExemplo", Map.of(
                    "name", "João da Silva",
                    "email", "joao@email.com",
                    "password", "SenhaSegura123!",
                    "cep", "12345678",
                    "role", "ROLE_USER"
            ));

        } else if ("createUserOngDto".equals(dto)) {

            body.put("jsonExemplo", Map.of(
                    "name", "ONG Esperança",
                    "email", "contato@ong.com",
                    "password", "SenhaSegura123!",
                    "cep", "12345678",
                    "cnpj", "12.345.678/0001-90",
                    "areaAtuacao", "Proteção Animal"
            ));
        }

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        // 1. Trata campos que não existem no DTO (UnrecognizedPropertyException)
        if (ex.getCause() instanceof UnrecognizedPropertyException unknown) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("erro", "Campo não permitido.");
            body.put("campo", unknown.getPropertyName());

            return ResponseEntity.badRequest().body(body);
        }

        // 2. Trata erros de JSON mal formatado
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("erro", "JSON inválido ou mal formatado.");

        String path = request.getRequestURI();

        // Identifica a rota no Java 17
        if (path.contains("/comum")) {
            body.put("exemploFormatacao", Map.of(
                    "name", "João da Silva",
                    "email", "joao@email.com",
                    "password", "SenhaSegura123!",
                    "cep", "12345678"
            ));
        } else if (path.contains("/ong")) {
            body.put("exemploFormatacao", Map.of(
                    "name", "ONG Esperança",
                    "email", "contato@ong.com",
                    "password", "SenhaSegura123!",
                    "cep", "12345678",
                    "cnpj", "12.345.678/0001-90",
                    "areaAtuacao", "Proteção Animal"
            ));
        }

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex, HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("erro", "Rota inválida ou endpoint não encontrado.");
        body.put("path", request.getRequestURI());
        body.put("metodo", request.getMethod()); // Ex: GET, POST
        body.put("sugestao", "Verifique o caminho e a URL informada.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmailNaoEncontrado(EmailNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("erro", "Usuário não encontrado.");
        body.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); // HTTP 404 NOT FOUND
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("erro", "Dado não encontrado.");
        body.put("mensagem", ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(CnpfAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCnpjJaCadastrado(CnpfAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("erro", "Cnpj Ja Cadastrado.");
        body.put("mensagem", ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor.");
    }
}