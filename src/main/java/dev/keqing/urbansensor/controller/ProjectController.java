package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.dao.UserProjectRepository;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Project;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.entity.UserProject;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.MessageResponse;
import dev.keqing.urbansensor.response.ProjectResponse;
import dev.keqing.urbansensor.response.StatusResponse;
import dev.keqing.urbansensor.response.UserProjectResponse;
import dev.keqing.urbansensor.utils.Validations;
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
import java.util.List;

@CrossOrigin(value = "*")
@Tag(name = "Project")
@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    private final GeneralConfig generalConfig = GeneralConfig.INSTANCE;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private Validations validations;

    @GetMapping(value = "/latest")
    @Operation(summary = "Últimos proyectos", description = "Trae los últimos tres proyectos de un usuario, ordenados por la fecha de creación en forma descendente", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<UserProjectResponse> getLatest(HttpServletRequest request) throws CustomException {

        List<UserProject> projectList = userProjectRepository.findFirst3ByUser_IdOrderByProject_CreatedAtDesc(request.getRemoteUser());

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(new UserProjectResponse(true, projectList));
    }

    @PostMapping()
    @Operation(summary = "Creación de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<UserProjectResponse> createProject(@RequestBody Project project, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);

        Project projectSaved = projectRepository.save(project);

        UserProject userProject = new UserProject(user, projectSaved);
        userProjectRepository.save(userProject);

        return ResponseEntity.ok(new UserProjectResponse(true, userProject));
    }

    @PatchMapping
    @Operation(summary = "Actualización de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody Project project, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);
        UserProject userProject = userProjectRepository.findByProject_Id(project.getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validations.validateUserProject(user, userProject);


        project.setCreatedAt(userProject.getProject().getCreatedAt());
        Project projectSaved = projectRepository.save(project);

        return ResponseEntity.ok(new ProjectResponse(true, projectSaved));
    }

    @DeleteMapping
    @Operation(summary = "Eliminación de proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<StatusResponse> deleteProject(@RequestParam String projectId, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);
        UserProject userProject = userProjectRepository.findByProject_Id(projectId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validations.validateUserProject(user, userProject);

        userProjectRepository.delete(userProject);

        projectRepository.deleteById(projectId);

        return ResponseEntity.ok(new MessageResponse(true, "Proyecto Eliminado."));
    }

    @GetMapping(value = "/user")
    @Operation(summary = "Lista de proyectos", description = "Trae todos los proyectos de un usuario, ordenados por la fecha de creación en forma descendente")
    public ResponseEntity<UserProjectResponse> getAllProjectByUser(@RequestParam String userId,
                                                                   @RequestParam int page,
                                                                   @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws CustomException {
        Pageable pageable = PageRequest.of(generalConfig.initPage(page), generalConfig.limitPage(limit));

        Page<UserProject> projectList = userProjectRepository.findAllByUser_IdOrderByProject_CreatedAtDesc(userId, pageable);

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        Paging paging = Paging.toPagination(projectList, page, "user");

        return ResponseEntity.ok(new UserProjectResponse(true, projectList.getContent(), paging));
    }

    @GetMapping
    @Operation(summary = "Mis proyectos",
            description = "Trae todos los proyectos de un usuario ingresado en el sistema, ordenados por la fecha de creación en forma descendente",
            security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<UserProjectResponse> getMyAllProject(HttpServletRequest request,
                                                               @RequestParam int page,
                                                               @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws CustomException {
        Pageable pageable = generalConfig.pageable(page, limit);
        User user = validations.validateUser(request);
        Page<UserProject> projectList = userProjectRepository.findAllByUser_IdOrderByProject_CreatedAtDesc(user.getId(), pageable);

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        Paging paging = Paging.toPagination(projectList, page, "project");

        return ResponseEntity.ok(new UserProjectResponse(true, projectList.getContent(), paging));
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Buscar proyecto", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<UserProjectResponse> searchProjectByUser(@RequestParam String search, @RequestParam int page, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);

        Pageable pageable = PageRequest.of(page, generalConfig.getItemPerPage());
        Page<UserProject> userProjects = userProjectRepository.findAllByProject_NameContainsIgnoreCaseAndUser(search, user, pageable);


        Paging paging = Paging.toPagination(userProjects, page, "search");
        return ResponseEntity.ok(new UserProjectResponse(true, userProjects.getContent(), paging));
    }

}



