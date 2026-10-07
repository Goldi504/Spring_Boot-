package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.UserResponse;
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


    // GET USER BY ID
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        )
                );
    }


    // GET ALL USERS
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // GET ALL CUSTOMERS
    public List<UserResponse> getAllCustomers() {

        return userRepository
                .findByRole(Role.CUSTOMER)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // DELETE USER
    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }


    // GET USER BY EMAIL
    public User getUserByEmail(String email) {

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }


    // ENTITY → DTO
    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}