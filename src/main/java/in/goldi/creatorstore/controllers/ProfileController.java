package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.entities.User;
import in.goldi.creatorstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public User getMyProfile(
            Authentication authentication
    ) {

        return userService
                .getUserByEmail(authentication.getName());
    }
}