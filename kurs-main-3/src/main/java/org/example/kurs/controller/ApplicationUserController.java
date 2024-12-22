package org.example.kurs.controller;

import org.example.kurs.model.ApplicationUser;
import org.example.kurs.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

    @Autowired
    private ApplicationUserRepository userRepository;

    @GetMapping
    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ApplicationUser getUserById(@PathVariable Long id) {
        Optional<ApplicationUser> user = userRepository.findById(id);
        return user.orElse(null);
    }
    @PostMapping
    public ApplicationUser createUser(@RequestBody ApplicationUser user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ApplicationUser updateUser(@PathVariable Long id, @RequestBody ApplicationUser user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
