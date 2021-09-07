package dev.keqing.urbansensor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(value = "*")
@Tag(name = "Auth")
@RestController
@RequestMapping(value = "/authenticate")
public class AuthController {


    @GetMapping(value = "/test")
    @Operation(security = @SecurityRequirement(name = "bearer-key"))
    public ResponseEntity<?> getTest() {

        return ResponseEntity.ok("OK");
    }


}
