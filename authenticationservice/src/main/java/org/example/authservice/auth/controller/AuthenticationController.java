package org.example.authservice.auth.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.authservice.auth.requests.AuthenticationRequest;
import org.example.authservice.auth.requests.ForgotPasswordRequest;
import org.example.authservice.auth.requests.ResetPasswordRequest;
import org.example.authservice.auth.responses.AuthenticationResponse;
import org.example.authservice.auth.requests.RegistraitonRequest;
import org.example.authservice.auth.responses.PasswordResetResponse;
import org.example.authservice.auth.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistraitonRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticationService.activateAccount(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws MessagingException {
        authenticationService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> responseEntity(
            @Valid @RequestBody ResetPasswordRequest request,
            @RequestParam String token
            ) throws MessagingException {
        return ResponseEntity.ok(authenticationService.resetPassword(request, token));
    }

}
