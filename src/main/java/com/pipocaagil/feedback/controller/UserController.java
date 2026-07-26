package com.pipocaagil.feedback.controller;

import com.pipocaagil.feedback.exception.CnpfAlreadyExistsException;
import com.pipocaagil.feedback.exception.EmailAlreadyExistsException;
import com.pipocaagil.feedback.exception.EmailNotFoundException;
import com.pipocaagil.feedback.service.UserService;
import com.pipocaagil.feedback.users.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {

        if (userService.isEmail(loginUserDto.email())) {
            throw new EmailNotFoundException();
        }

        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/cadastrar/ong")
    public ResponseEntity<Void> createUserOng(@Valid @RequestBody CreateUserOngDto createUserOngDto) {

        if (userService.isEmail(createUserOngDto.email())) {
            throw new EmailAlreadyExistsException();
        }

        if (userService.isCnpj(createUserOngDto.cnpj().replaceAll("\\D", ""))) {
            throw new CnpfAlreadyExistsException();
        }

        userService.createUserOng(createUserOngDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/cadastrar/comum")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        if (userService.isEmail(createUserDto.email())) {
            throw new EmailAlreadyExistsException();
        }

        userService.createUser(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/cep/{cep}")
    public ResponseEntity<RecoveryCepDto> verificationCep(
            @PathVariable String cep) {

        return ResponseEntity.ok(
                new RecoveryCepDto(userService.validarCepExistente(cep))
        );
    }

    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

}
