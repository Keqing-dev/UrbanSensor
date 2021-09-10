package dev.keqing.urbansensor.controller;


import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.dao.UserProjectRepository;
import dev.keqing.urbansensor.dao.UserRepository;
import dev.keqing.urbansensor.entity.CommonResponse;
import dev.keqing.urbansensor.entity.Project;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.entity.UserProject;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin(value = "*")
@Tag(name = "Project")
@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @GetMapping(value = "/latest")
    @Operation(summary = "Últimos proyectos", description = "Trae los últimos tres proyectos de un usuario, ordenados por la fecha de creación en forma descendente",security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> getLatest(HttpServletRequest request) throws CustomException {

        List<UserProject> projectList = userProjectRepository.findFirst3ByUser_IdOrderByProject_CreatedAtDesc(request.getRemoteUser());

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(new CommonResponse(true, projectList));
    }

    @PostMapping()
    @Operation(summary = "Creación de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> createProject(@RequestBody Project project, HttpServletRequest request) throws CustomException {

        User user = validateUser(request);

        Project projectSaved = projectRepository.save(project);

        UserProject userProject = new UserProject(user, projectSaved);
        userProjectRepository.save(userProject);

        return ResponseEntity.ok(new CommonResponse(true, userProject));
    }

    @PatchMapping
    @Operation(summary = "Actualización de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody Project project, HttpServletRequest request) throws CustomException {

        User user = validateUser(request);
        UserProject userProject = userProjectRepository.findByProject_Id(project.getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validateUserProject(user, userProject);

        project.setCreatedAt(userProject.getProject().getCreatedAt());
        Project projectSaved = projectRepository.save(project);

        return ResponseEntity.ok(new ProjectResponse(true, projectSaved));
    }

    @DeleteMapping
    @Operation(summary = "Eliminación de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> deleteProject(@RequestParam String projectId, HttpServletRequest request) throws CustomException {

        User user = validateUser(request);
        UserProject userProject = userProjectRepository.findByProject_Id(projectId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validateUserProject(user, userProject);

        userProjectRepository.delete(userProject);

        projectRepository.deleteById(projectId);

        return ResponseEntity.ok(new CommonResponse(true, "Proyecto Eliminado."));
    }

    @GetMapping()
    @Operation(summary = "Lista de proyectos", description = "Trae todos los proyectos de un usuario, ordenados por la fecha de creación en forma descendente")
    public ResponseEntity<CommonResponse> getAllProjectByUser(@RequestParam String userId, @RequestParam int page) throws CustomException {

        Pageable pageable = PageRequest.of(page, generalConfig.getItemPerPage());

        Page<UserProject> projectList = userProjectRepository.findAllByUser_Id(userId, pageable);

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(new CommonResponse(true, projectList.toList()));
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Buscar proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> searchProjectByUser(@RequestParam String search, @RequestParam int page, HttpServletRequest request) throws CustomException {
        User user = validateUser(request);

        Pageable pageable = PageRequest.of(page, generalConfig.getItemPerPage());
        Page<UserProject> userProjects = userProjectRepository.findAllByProject_NameContainsIgnoreCaseAndUser(search, user, pageable);

        return ResponseEntity.ok(new CommonResponse(true, userProjects.toList()));
    }

    private void validateUserProject(User user, UserProject userProject) throws CustomException {
        if (!user.getId().equals(userProject.getUser().getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "Usuario no autorizado.");
        }
    }

    private User validateUser(HttpServletRequest request) throws CustomException {
        return userRepository.findById(request.getRemoteUser()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
    }

    @GetMapping(value ="/test")
    public ResponseEntity<?> test2() throws IOException {


        return ResponseEntity.ok("AAa");
    }



}



