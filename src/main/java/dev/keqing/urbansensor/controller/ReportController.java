package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.ReportResponse;
import dev.keqing.urbansensor.response.ReportsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    private final int itemPerPage = GeneralConfig.INSTANCE.getItemPerPage();

    @GetMapping
    @Operation(summary = "Lista de reportes",security = @SecurityRequirement(name = "bearer"))
    ResponseEntity<ReportsResponse> getAll(@RequestParam(name = "page") int page,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(name = "sortBy", required = false, defaultValue = "timestampDESC") String sortBy) {

        List<String> allowedAttributes = List.of("timestamp");

        String type = sortBy.replaceAll("ASC|DESC", "");

        Sort sort = Sort.by(sortBy.contains("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, allowedAttributes.contains(type) ? type : "timestamp");

        Pageable pageable = PageRequest.of(page - 1, limit > 100 ? itemPerPage : limit, sort);
        Page<Report> reports = reportRepository.findAll(pageable);

        Paging paging = Paging.toPagination(reports,page,"report");

        return ResponseEntity.ok(new ReportsResponse(true, reports.getContent(), paging));
    }

    @GetMapping("/{id}")
    ResponseEntity<ReportResponse> get(@PathVariable String id) throws CustomException {
        Report report = reportRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new ReportResponse(true, report));
    }
}
