package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.AuthResponse;
import in.goldi.creatorstore.dto.LoginRequest;
import in.goldi.creatorstore.dto.RegisterRequest;
import in.goldi.creatorstore.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }
}