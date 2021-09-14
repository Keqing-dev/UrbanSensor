package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ProjectRepository;
import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.entity.*;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.projection.ReportSummary;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.FileType;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @Operation(summary = "Creación de reporte", security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<CommonResponse> createReport(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "latitude") String latitude,
            @RequestParam(value = "longitude") String longitude,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "categories") String categories,
            @RequestParam(value = "observations", required = false) String observations,
            @RequestParam(value = "project") String projectId,
            HttpServletRequest request
    ) throws CustomException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Proyecto no encontrado"));

        User user = validations.validateUser(request);

        String filename = fileStorageService.storeFile(file, FileType.FILE);

        Report report = new Report();

        report.setFile(filename);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setAddress(address);
        report.setCategories(categories);
        report.setProject(project);
        report.setUser(user);
        if (observations != null) report.setObservations(observations);

        reportRepository.save(report);

        return ResponseEntity.ok(new CommonResponse(true, report));
    }

    @GetMapping
    @Operation(summary = "Mis reportes", security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<CommonResponse> getAll(@RequestParam(name = "page") int page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(name = "sortBy", required = false, defaultValue = "timestampDESC") String sortBy,
                                          HttpServletRequest request) throws CustomException {
        final int itemPerPage = generalConfig.getItemPerPage();
        User user = validations.validateUser(request);

        List<String> allowedAttributes = List.of("timestamp");

        String type = sortBy.replaceAll("ASC|DESC", "");

        Sort sort = Sort.by(sortBy.contains("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, allowedAttributes.contains(type) ? type : "timestamp");

        Pageable pageable = PageRequest.of(page - 1, limit > 100 ? itemPerPage : limit, sort);
        Page<ReportSummary> reports = reportRepository.findAllByUser(user, pageable);

        Paging paginated = paging.toPagination(reports, page, "report");

        return ResponseEntity.ok(new CommonResponse(true, reports.getContent(), paginated));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtención de reporte", security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<CommonResponse> get(@PathVariable String id, HttpServletRequest request) throws CustomException {
        User user = validations.validateUser(request);

        Report report = reportRepository.findByIdAndUser(id, user).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new CommonResponse(true, report));
    }

    @GetMapping("/project")
    @Operation(summary = "Reportes de un proyecto", security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<CommonResponse> getReportsByProject(@RequestParam String id, @RequestParam int page) {

        Page<ReportSummary> reportSummaries = reportRepository.findAllByProject_IdOrderByTimestampDesc(id, generalConfig.pageable(page, 10));

        Paging paging = new Paging().toPagination(reportSummaries, page, "/report/project");

        return ResponseEntity.ok(new CommonResponse(true, reportSummaries.getContent(), paging));

    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte", security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<CommonResponse> deleteReport(@PathVariable String id, HttpServletRequest request) throws CustomException {

        validations.validateUser(request);

        Report report = reportRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        fileStorageService.deleteFile(report.getFile(), FileType.FILE);

        reportRepository.delete(report);

        return ResponseEntity.ok(new CommonResponse(true, "Reporte eliminado exitosamente."));

    }


}
