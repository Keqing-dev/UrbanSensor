package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.dao.PlanRepository;
import dev.keqing.urbansensor.entity.Plan;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.entity.CommonResponse;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "*")
@Tag(name = "Plan")
@RestController
@RequestMapping(value = "/plan")
public class PlanController {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private Validations validations;

    @PostMapping
    @Operation(summary = "Creación de plan", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> createPlan(@RequestBody Plan plan, HttpServletRequest request) throws CustomException {

        validations.validateUser(request);

        if (plan.getName().trim().equals("")) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        planRepository.save(plan);

        return ResponseEntity.ok(new CommonResponse(true, "Plan creado exitosamente."));
    }

    @GetMapping
    @Operation(summary = "Lista de planes")
    public ResponseEntity<CommonResponse> getAll() {

        List<Plan> planList = planRepository.findAll();

        return ResponseEntity.ok(new CommonResponse(true, planList));
    }

    @DeleteMapping
    @Operation(summary = "Eliminar pan", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> deletePlan(@RequestParam String planId) throws CustomException {

        Optional<Plan> plan = planRepository.findById(planId);

        if (plan.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        planRepository.deleteById(planId);

        return ResponseEntity.ok(new CommonResponse(true, "Plan eliminado exitosamente."));
    }


}
