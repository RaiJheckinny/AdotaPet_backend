package com.pipocaagil.feedback.users.dto;

import com.pipocaagil.feedback.security.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        List<Role> roles

) {
}
