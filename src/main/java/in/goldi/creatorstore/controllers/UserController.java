package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.UserResponse;
import in.goldi.creatorstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // GET ALL USERS
    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers();
    }


    // GET ALL CUSTOMERS
    @GetMapping("/customers")
    public List<UserResponse> getAllCustomers() {

        return userService.getAllCustomers();
    }


    // GET USER BY ID
    @GetMapping("/{id}")
    public UserResponse getUserById(
            @PathVariable Long id
    ) {

        return userService.toResponse(
                userService.getUserById(id)
        );
    }


    // DELETE USER
    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);
    }
}