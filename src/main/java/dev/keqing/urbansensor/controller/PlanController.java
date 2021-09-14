package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.dao.PlanRepository;
import dev.keqing.urbansensor.response.CommonResponse;
import dev.keqing.urbansensor.entity.Plan;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.response.PlanResponse;
import dev.keqing.urbansensor.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan Creado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
            @ApiResponse(responseCode = "400", description = "Nombre Inválido", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> createPlan(@RequestBody Plan.Create plan, HttpServletRequest request) throws CustomException {

        validations.validateUser(request);

        if (plan.getName().trim().equals("")) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        Plan newPlan = new Plan();
        newPlan.setName(plan.getName());

        planRepository.save(newPlan);

        return ResponseEntity.ok(new CommonResponse(true, "Plan creado exitosamente."));
    }

    @GetMapping
    @Operation(summary = "Lista de planes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Planes", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PlanResponse.PlanData.class))}),
            @ApiResponse(responseCode = "404", description = "Planes no Encontrados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> getAll() throws CustomException {

        List<Plan> planList = planRepository.findAll();

        if (planList.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND, "No existen planes actualmente");

        return ResponseEntity.ok(new CommonResponse(true, planList));
    }

    @DeleteMapping
    @Operation(summary = "Eliminar pan", security = @SecurityRequirement(name = "bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan Eliminado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
            @ApiResponse(responseCode = "404", description = "Plan no Encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.Message.class))}),
    })
    public ResponseEntity<CommonResponse> deletePlan(@RequestParam String planId) throws CustomException {

        Optional<Plan> plan = planRepository.findById(planId);

        if (plan.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }

        planRepository.deleteById(planId);

        return ResponseEntity.ok(new CommonResponse(true, "Plan eliminado exitosamente."));
    }

}
