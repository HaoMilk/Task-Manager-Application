package com.example.taskmanagerbackend.controller;

import com.example.taskmanagerbackend.model.User;
import com.example.taskmanagerbackend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    // API đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if (usersRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        // Kiểm tra xem email đã tồn tại chưa
        if (usersRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        // Lưu người dùng mới vào cơ sở dữ liệu
        usersRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // API đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginDetails) {
        Optional<User> user = usersRepository.findByUsername(loginDetails.getUsername());

        if (user.isPresent() && user.get().getPassword().equals(loginDetails.getPassword())) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}
