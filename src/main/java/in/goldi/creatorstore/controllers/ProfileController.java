package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.UserResponse;
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
    public UserResponse getMyProfile(
            Authentication authentication
    ) {

        return userService.toResponse(
                userService.getUserByEmail(
                        authentication.getName()
                )
        );
    }
}