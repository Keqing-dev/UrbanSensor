package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.entity.*;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.projection.ReportSummary;
import dev.keqing.urbansensor.response.CommonResponse;
import dev.keqing.urbansensor.response.ProjectResponse;
import dev.keqing.urbansensor.response.ReportResponse;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.FileType;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "Report")
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private Validations validations;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private Paging paging;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Creación de reporte", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto Creado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    ResponseEntity<CommonResponse> createReport(
            @RequestPart MultipartFile file,
            @RequestPart String latitude,
            @RequestPart String longitude,
            @RequestPart String address,
            @RequestPart String categories,
            @RequestPart(required = false) String observations,
            @RequestPart String projectId,
            HttpServletRequest request
    ) throws CustomException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        User user = validations.validateUser(request);

        String filename = fileStorageService.storeFile(file, FileType.FILE);

        Report newReport = new Report();

        newReport.setFile(filename);
        newReport.setLatitude(latitude);
        newReport.setLongitude(longitude);
        newReport.setAddress(address);
        newReport.setCategories(categories);
        newReport.setProject(project);
        newReport.setUser(user);
        if (observations != null) newReport.setObservations(observations);

        reportRepository.save(newReport);

        return ResponseEntity.ok(new CommonResponse(true, "Proyecto Creado Exitosamente"));
    }

    @GetMapping
    @Operation(summary = "Mis reportes", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Reportes", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.ReportContent.class))}),
    })
    ResponseEntity<CommonResponse> getReportsByUser(@RequestParam(name = "page") int page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(name = "sortBy", required = false, defaultValue = "timestampDESC") String sortBy,
                                          HttpServletRequest request) throws CustomException {
        final int itemPerPage = generalConfig.getItemPerPage();
        User user = validations.validateUser(request);

        List<String> allowedAttributes = List.of("timestamp");

        String type = sortBy.replaceAll("ASC|DESC", "");

        Sort sort = Sort.by(sortBy.contains("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, allowedAttributes.contains(type) ? type : "timestamp");

        Pageable pageable = PageRequest.of(page - 1, limit > 100 ? itemPerPage : limit, sort);

        Page<ReportSummary> reports = reportRepository.findAllByUser(user, pageable, ReportSummary.class);

        if (reports.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND);

        Paging paginated = paging.toPagination(reports, page, "report");

        return ResponseEntity.ok(new CommonResponse(true, reports.getContent(), paginated));
    }

    @GetMapping("/details")
    @Operation(summary = "Obtención de reporte", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de Reporte", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.ReportData.class))}),
    })
    ResponseEntity<CommonResponse> get(@RequestParam String id, HttpServletRequest request) throws CustomException {
        User user = validations.validateUser(request);

        ReportSummary report = reportRepository.findByIdAndUser(id, user, ReportSummary.class).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new CommonResponse(true, report));
    }

    @GetMapping("/project")
    @Operation(summary = "Reportes de un proyecto", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de Reporte", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.ReportContent.class))}),
    })
    ResponseEntity<CommonResponse> getReportsByProject(@RequestParam String id, @RequestParam int page) throws CustomException {

        Page<ReportSummary> reportSummaries = reportRepository.findAllByProject_IdOrderByTimestampDesc(id, generalConfig.pageable(page, 10), ReportSummary.class);

        if (reportSummaries.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND);

        Paging paging = new Paging().toPagination(reportSummaries, page, "/report/project");

        return ResponseEntity.ok(new CommonResponse(true, reportSummaries.getContent(), paging));
    }


    @DeleteMapping
    @Operation(summary = "Eliminar reporte", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte Eliminado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    ResponseEntity<CommonResponse> deleteReport(@RequestParam String id, HttpServletRequest request) throws CustomException {

        validations.validateUser(request);

        Report report = reportRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        fileStorageService.deleteFile(report.getFile(), FileType.FILE);

        reportRepository.delete(report);

        return ResponseEntity.ok(new CommonResponse(true, "Reporte eliminado exitosamente."));
    }
}
