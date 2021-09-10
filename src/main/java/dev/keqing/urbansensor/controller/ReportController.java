package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.projection.ReportSummary;
import dev.keqing.urbansensor.entity.CommonResponse;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.Validations;
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
import java.util.Collections;
import java.util.List;

@Tag(name = "Report")
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private Validations validations;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GeneralConfig generalConfig;


    @PostMapping
    ResponseEntity<CommonResponse> createFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(new CommonResponse(false, "Pa mañana queda esto"));
    }

    @GetMapping
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

        Paging paging = null;

        if (reports.hasPrevious() || reports.hasNext())
            paging = new Paging();

        if (reports.hasNext() && paging != null)
            paging.setNext("post?page=" + (page + 1));

        if (reports.hasPrevious() && paging != null)
            paging.setPrevious("post?page=" + (page - 1));

        return ResponseEntity.ok(new CommonResponse(true, Collections.singletonList(reports.getContent()), paging));
    }

    @GetMapping("/{id}")
    ResponseEntity<CommonResponse> get(@PathVariable String id, HttpServletRequest request) throws CustomException {
        User user = validations.validateUser(request);

        Report report = reportRepository.findByIdAndUser(id, user).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new CommonResponse(true, report));
    }
}
