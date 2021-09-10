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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(value = "*")
@Tag(name = "Plan")
@RestController
@RequestMapping(value = "/plan")
public class PlanController {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private PlanRepository planRepository;

    private final Validations validations = new Validations();

    @PostMapping()
    @Operation(summary = "Creación de plan", security = @SecurityRequirement(name = "bearer"))
    public ResponseEntity<CommonResponse> createPlan(@RequestBody Plan plan, HttpServletRequest request) throws CustomException {

        validations.validateUser(request);

        planRepository.save(plan);

        return ResponseEntity.ok(new CommonResponse(true, "Plan creado exitosamente."));
    }


}
