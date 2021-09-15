package dev.keqing.urbansensor.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.keqing.urbansensor.dao.PlanRepository;
import dev.keqing.urbansensor.dao.UserRepository;
import dev.keqing.urbansensor.entity.Plan;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.CommonResponse;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.FileType;
import dev.keqing.urbansensor.utils.RSAKeys;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


@CrossOrigin(value = "*")
@Tag(name = "Auth")
@RestController
@RequestMapping(value = "/authenticate")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;


    @Autowired
    private Validations validations;

    @Autowired
    private FileStorageService fileStorageService;


    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping(value = "/login")
    @Operation(summary = "Autenticación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Exitoso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Datos Inválidos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    ResponseEntity<CommonResponse> login(@RequestBody User.Login user) throws CustomException, IOException {

        String email = user.getEmail();
        String password = user.getPassword();

        if (password == null || email == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Datos Inválidos, Intenta Nuevamente");
        }

        User userExists =
                userRepository.findFirstByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Datos Inválidos, Intenta Nuevamente"));

        String passwordEncoded = userExists.getPassword();

        if (!bCryptPasswordEncoder.matches(password, passwordEncoded)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Datos Inválidos, Intenta Nuevamente");
        }

        Algorithm algorithm = RSAKeys.getAlgorithm();

        String token = JWT.create().withSubject(userExists.getId()).withIssuedAt(new Date()).sign(algorithm);

        userExists.setToken(token);

        return ResponseEntity.ok(new CommonResponse(true, userExists));
    }


    @PatchMapping(value = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Subir/Actualizar Avatar", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subida Exitosa", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Datos Inválidos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    ResponseEntity<CommonResponse> uploadAvatar(@RequestPart MultipartFile file, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);

        String filename = fileStorageService.storeFile(file, FileType.AVATAR, user.getAvatar());

        user.setAvatar(filename);
        User userSaved = userRepository.save(user);

        return ResponseEntity.ok(new CommonResponse(true, userSaved));
    }


    @PostMapping(value = "/register")
    @Operation(summary = "Creación de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro Exitoso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
            @ApiResponse(responseCode = "400", description = "Email Duplicado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
            @ApiResponse(responseCode = "404", description = "Plan no Encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    ResponseEntity<CommonResponse> createUser(@RequestBody User.Register user) throws CustomException {

        Optional<User> userExists = userRepository.findFirstByEmail(user.getEmail());
        Plan plan = planRepository.findById(user.getPlanId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "El plan seleccionado no existe."));
        if (userExists.isPresent())
            throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con este email");

        String hash = bCryptPasswordEncoder.encode(user.getPassword());

        User newUser = new User();

        newUser.setEmail(user.getEmail());
        newUser.setPassword(hash);
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setProfession(user.getProfession());
        newUser.setPlan(plan);
        newUser.setGoogleId(user.getGoogleId());

        userRepository.save(newUser);

        return ResponseEntity.ok(new CommonResponse(true, "Usuario Creado Exitosamente"));
    }
}
