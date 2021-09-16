package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.dao.ReportRepository;
import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Tag(name = "Csv")
@CrossOrigin(value = "*")
@RequestMapping(value = "/csv")
public class CsvController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/reports")
    @Operation(summary = "Exportar mis reportes", description = "Genera un reporte con extension .csv delimitado por coma (,), con un tamaño por pagina de 1M, el limite por pagina se puede cambiar por parámetro.", security = @SecurityRequirement(name = "bearer"))
    public void reportsToCsv(HttpServletResponse response, @RequestParam String projectId,@RequestParam(defaultValue = "1") int page ,@RequestParam(defaultValue = "1000000") int limit) throws IOException, CustomException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=reports_" + currentDateTime + ".csv";
        response.setContentType("text/csv");
        response.setHeader(headerKey, headerValue);

        Pageable pageable = PageRequest.of(page, limit);

        Page<Report> listUsers = reportRepository.findAllByProject_IdOrderByTimestampDesc(projectId, pageable, Report.class);

        System.out.println(listUsers.getContent().get(0).getId());
        String[] csvHeader = {"Report id", "Latitude", "Longitude", "Address", "Categories", "Observations", "Report File", "Project Id", "Project Name", "CreationAt"};
        String[] nameMapping = {"id", "latitude", "longitude", "address", "categories", "observations", "file", "projectId", "projectName", "timestamp"};

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(csvHeader);

        for (Report report : listUsers.getContent()) {
            csvWriter.write(report, nameMapping);
        }

        csvWriter.close();

    }

}
