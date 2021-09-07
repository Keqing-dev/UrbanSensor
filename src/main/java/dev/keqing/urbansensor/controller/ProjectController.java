package dev.keqing.urbansensor.controller;


import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.entity.Project;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(value = "*")
@Tag(name = "Project")
@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    private final GeneralConfig generalConfig = GeneralConfig.INSTANCE;

    @GetMapping()
    public ResponseEntity<?> getAll() {

        Pageable pageable = PageRequest.of(0, generalConfig.getItemPerPage());
        Page<Project> projectPage = projectRepository.findAll(pageable);

        return ResponseEntity.ok(projectPage.get());

    }
}
