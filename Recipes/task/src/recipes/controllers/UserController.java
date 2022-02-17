package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.models.User;
import recipes.repository.UserRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {
        System.out.println("GOT USER WITH USERNAME: " + user.getUsername() + " " + user.getPassword());
        if (userRepository.findUserByUsername(user.getUsername()) != null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("GOT USER WITH USERNAME: " + user.getUsername() + " " + user.getPassword());
        user.setRole("ROLE_USER");
        userRepository.save(user);
        System.out.println("ACCEPTED USERNAME: " + user.getUsername());

        return new ResponseEntity(HttpStatus.OK);
    }
}
