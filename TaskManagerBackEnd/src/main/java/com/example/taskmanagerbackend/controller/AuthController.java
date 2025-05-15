package com.example.taskmanagerbackend.controller;

import com.example.taskmanagerbackend.model.User;
import com.example.taskmanagerbackend.repository.UsersRepository;
import com.example.taskmanagerbackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Đăng ký người dùng mới (bỏ mã hoá)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Kiểm tra các trường bắt buộc
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return new ResponseEntity<>("Username is required", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }

        if (usersRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        if (usersRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Lưu thẳng password plain text (KHÔNG MÃ HOÁ)
        usersRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginDetails) {
        System.out.println("ABC");
        if (loginDetails.getUsername() == null || loginDetails.getUsername().isBlank() ||
                loginDetails.getPassword() == null || loginDetails.getPassword().isBlank()) {
            return new ResponseEntity<>("Username and password are required", HttpStatus.BAD_REQUEST);
        }

        // Tìm người dùng theo username
        Optional<User> userOpt = usersRepository.findByUsername(loginDetails.getUsername());

        // Kiểm tra thông tin đăng nhập
        if (userOpt.isPresent() && loginDetails.getPassword().equals(userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getUsername());

            // Trả về thông báo, token và userId
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token,
                    "userId", user.getId()  // Trả về userId
            ));
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }


    // Cập nhật thông tin người dùng (bỏ mã hoá password)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOptional = usersRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            if (updatedUser.getEmail().isBlank()) {
                return new ResponseEntity<>("Email cannot be blank", HttpStatus.BAD_REQUEST);
            }
            if (usersRepository.existsByEmail(updatedUser.getEmail())) {
                return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);
            }
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getImageUrl() != null) user.setImageUrl(updatedUser.getImageUrl());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            if (updatedUser.getPassword().length() < 6) {
                return new ResponseEntity<>("Password must be at least 6 characters", HttpStatus.BAD_REQUEST);
            }
            user.setPassword(updatedUser.getPassword());
        }

        usersRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }
}
