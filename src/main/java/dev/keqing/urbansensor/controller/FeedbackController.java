package dev.keqing.urbansensor.controller;


import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.FeedbackRepository;
import dev.keqing.urbansensor.entity.Feedback;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(value = "*")
@RestController
@Tag(name = "Feedback")
@RequestMapping(value = "/feedback")
public class FeedbackController {


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private Paging paging;

    @GetMapping()
    @Operation(summary = "Lista de feedbacks", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> getAll(@RequestParam int page) {
        Page<Feedback> feedbacks = feedbackRepository.findAll(generalConfig.pageable(page, 10));
        return ResponseEntity.ok(new CommonResponse(true, feedbacks.getContent(), paging.toPagination(feedbacks, page, "/feedback")));
    }

    @PostMapping()
    @Operation(summary = "Crear feedback", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> create(@RequestBody Feedback feedback) {
        try {
            feedbackRepository.save(feedback);
        } catch (Exception e) {
            return ResponseEntity.ok(new CommonResponse(false, "Error"));
        }
        return ResponseEntity.ok(new CommonResponse(true, feedback));
    }

    @DeleteMapping()
    @Operation(summary = "Eliminar feedback", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> delete(@RequestParam String id) {

        try {
            feedbackRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.ok(new CommonResponse(false, "Error"));
        }

        return ResponseEntity.ok(new CommonResponse(true, "Eliminado con exito"));
    }

}
