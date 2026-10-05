package in.goldi.creatorstore.services;

import in.goldi.creatorstore.entities.Role;
import in.goldi.creatorstore.entities.User;
import in.goldi.creatorstore.exceptions.ResourceNotFoundException;
import in.goldi.creatorstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ==========================================
    // GET USER BY ID
    // ==========================================

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        )
                );
    }

    // ==========================================
    // GET ALL USERS
    // ==========================================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // ==========================================
    // GET ALL CUSTOMERS
    // ==========================================

    public List<User> getAllCustomers() {

        return userRepository.findByRole(
                Role.CUSTOMER
        );
    }

    // ==========================================
    // DELETE USER
    // ==========================================

    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }

    public User getUserByEmail(String email) {

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }
}