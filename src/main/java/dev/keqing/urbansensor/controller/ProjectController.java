package dev.keqing.urbansensor.controller;


import dev.keqing.urbansensor.config.GeneralConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(value = "*")
@Tag(name = "Project")
@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    private final GeneralConfig generalConfig = GeneralConfig.INSTANCE;

}
