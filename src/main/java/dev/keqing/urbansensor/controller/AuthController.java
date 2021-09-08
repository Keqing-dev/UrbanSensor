package dev.keqing.urbansensor.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.keqing.urbansensor.dao.UsersRepository;
import dev.keqing.urbansensor.entity.Users;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.MessageResponse;
import dev.keqing.urbansensor.response.UserResponse;
import dev.keqing.urbansensor.utils.RSAKeys;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;


@CrossOrigin(value = "*")
@Tag(name = "Auth")
@RestController
@RequestMapping(value = "/authenticate")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping(value = "/login")
    ResponseEntity<UserResponse> login(@RequestBody Users user) throws CustomException, IOException {
        String email = user.getEmail();
        String password = user.getPassword();

        if (password == null || email == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Datos Invalidos, Intenta Nuevamente");
        }

        Users userExists =
                usersRepository.findFirstByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Datos Invalidos, Intenta Nuevamente"));

        String passwordEncoded = userExists.getPassword();

        if (!bCryptPasswordEncoder.matches(password, passwordEncoded)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Datos Invalidos, Intenta Nuevamente");
        }

        Algorithm algorithm = RSAKeys.getAlgorithm();

        String token = JWT.create().withSubject(userExists.getId()).withIssuedAt(new Date()).sign(algorithm);

        userExists.setToken(token);

        return ResponseEntity.ok(new UserResponse(true, userExists));
    }

    @PostMapping(value = "/register")
    ResponseEntity<MessageResponse> createUser(@RequestBody Users user) throws CustomException {

        Optional<Users> userExists = usersRepository.findFirstByEmail(user.getEmail());

        if (userExists.isPresent())
            throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con este email");

        String hash = bCryptPasswordEncoder.encode(user.getPassword());

        Users newUser = new Users();

        newUser.setEmail(user.getEmail());
        newUser.setPassword(hash);
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setProfession(user.getProfession());
        newUser.setPlan(user.getPlan());

        usersRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse(true, "Usuario Creado Exitosamente"));
    }
}
