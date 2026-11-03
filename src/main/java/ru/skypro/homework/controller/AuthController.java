package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.constants.WebConstants;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

/**
 * Вход и регистрация пользователей.
 */
@CrossOrigin(value = WebConstants.FRONTEND_URL)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Авторизация пользователя", tags = "Авторизация")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Регистрация пользователя", tags = "Регистрация")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
