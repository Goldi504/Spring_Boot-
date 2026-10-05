package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.entities.User;
import in.goldi.creatorstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ==========================================
    // GET ALL USERS
    // ==========================================

    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    // ==========================================
    // GET ALL CUSTOMERS
    // ==========================================

    @GetMapping("/customers")
    public List<User> getAllCustomers() {

        return userService.getAllCustomers();
    }

    // ==========================================
    // GET USER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id
    ) {

        return userService.getUserById(id);
    }

    // ==========================================
    // DELETE USER
    // ==========================================

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);
    }
}