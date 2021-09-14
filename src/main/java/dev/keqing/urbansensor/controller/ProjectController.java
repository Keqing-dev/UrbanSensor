package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.dao.UserProjectRepository;
import dev.keqing.urbansensor.entity.*;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.projection.ReportFile;
import dev.keqing.urbansensor.response.CommonResponse;
import dev.keqing.urbansensor.response.ProjectResponse;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.FileType;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Validations validations;

    @Autowired
    private Paging paging;

    @PostMapping()
    @Operation(summary = "Creación de proyecto", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creación Exitosa", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectData.class))}),
    })
    public ResponseEntity<CommonResponse> createProject(@RequestBody Project.Create project, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);

        Project newProject = new Project();
        newProject.setName(project.getName());

        Project projectSaved = projectRepository.save(newProject);

        UserProject userProject = new UserProject(user, projectSaved);
        userProjectRepository.save(userProject);

        return ResponseEntity.ok(new CommonResponse(true, projectSaved));
    }

    @PatchMapping
    @Operation(summary = "Actualización de proyecto", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización Exitosa", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectData.class))}),
            @ApiResponse(responseCode = "404", description = "Proyecto no Encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> updateProject(@RequestBody Project.Update project, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);
        Project updateProject = projectRepository.findById(project.getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));
        UserProject userProject = userProjectRepository.findByProject_Id(updateProject.getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validations.validateUserProject(user, userProject);

        updateProject.setName(project.getName());

        Project projectSaved = projectRepository.save(updateProject);

        return ResponseEntity.ok(new CommonResponse(true, projectSaved));
    }

    @DeleteMapping
    @Operation(summary = "Eliminación de proyecto", description = "Elimina el proyecto y todos los reportes que contiene", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eliminación Exitosa", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
            @ApiResponse(responseCode = "404", description = "Proyecto no Encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> deleteProject(@RequestParam String projectId, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);
        UserProject userProject = userProjectRepository.findByProject_Id(projectId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado."));

        validations.validateUserProject(user, userProject);

        List<ReportFile> reportFiles = reportRepository.findAllByProject_Id(projectId);

        for (ReportFile report : reportFiles) {
            fileStorageService.deleteFile(report.getFile(), FileType.FILE);
        }

        reportRepository.deleteAllByProject_Id(projectId);

        userProjectRepository.deleteAllByProject_Id(projectId);

        projectRepository.deleteById(projectId);

        return ResponseEntity.ok(new CommonResponse(true, "Proyecto Eliminado."));
    }

    @GetMapping
    @Operation(summary = "Mis proyectos",
            description = "Trae todos los proyectos de un usuario ingresado en el sistema, ordenados por la fecha de creación en forma descendente",
            security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Proyectos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectContent.class))}),
            @ApiResponse(responseCode = "404", description = "Proyectos no Encontrados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> getMyAllProject(HttpServletRequest request,
                                                          @RequestParam int page,
                                                          @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws CustomException {
        Pageable pageable = generalConfig.pageable(page, limit);
        User user = validations.validateUser(request);
        Page<Project> projectList = projectRepository.findAllProjectByUser_IdAndCountTheirReports(user.getId(), pageable);

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        Paging paginated = paging.toPagination(projectList, page, "project");

        return ResponseEntity.ok(new CommonResponse(true, projectList.getContent(), paginated));
    }

    @GetMapping(value = "/user")
    @Operation(summary = "Lista de proyectos", description = "Trae todos los proyectos de un usuario, ordenados por la fecha de creación en forma descendente", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Proyectos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectContent.class))}),
            @ApiResponse(responseCode = "404", description = "Proyectos no Encontrados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> getAllProjectByUser(@RequestParam String userId,
                                                               @RequestParam int page,
                                                               @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws CustomException {
        Pageable pageable = PageRequest.of(generalConfig.initPage(page), generalConfig.limitPage(limit));

        Page<Project> projectList = projectRepository.findAllProjectByUser_IdAndCountTheirReports(userId, pageable);

        if (projectList.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND);

        Paging paginated = paging.toPagination(projectList, page, "user");

        return ResponseEntity.ok(new CommonResponse(true, projectList.getContent(), paginated));
    }

    @GetMapping(value = "/latest")
    @Operation(summary = "Últimos proyectos", description = "Trae los últimos tres proyectos de un usuario, ordenados por la fecha de creación en forma descendente", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Proyectos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectContent.class))}),
            @ApiResponse(responseCode = "404", description = "Proyectos no Encontrados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> getLatest(HttpServletRequest request) throws CustomException {

        Pageable pageable = generalConfig.pageable(1, 3);
        Page<Project> projectList = projectRepository.findLast3ProjectByUser_IdAndCountTheirReports(request.getRemoteUser(), pageable);

        if (projectList.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new CommonResponse(true, projectList.getContent()));
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Buscar Mis proyectos", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de Búsqueda", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.ProjectContent.class))}),
            @ApiResponse(responseCode = "404", description = "Búsqueda sin Resultados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> searchProjectByUser(@RequestParam String search, @RequestParam int page, HttpServletRequest request) throws CustomException {

        User user = validations.validateUser(request);

        Pageable pageable = generalConfig.pageable(page, 10);
        Page<Project> userProjects = projectRepository.searchAllProjectByUserAndCountTheirReports(user.getId(), search, pageable);

        if(userProjects.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND);

        Paging paginated = paging.toPagination(userProjects, page, "project/search");
        return ResponseEntity.ok(new CommonResponse(true, userProjects.getContent(), paginated));
    }

}



