package com.example.demo.controller;

import com.example.demo.model.PhenomenonExtraFee;
import com.example.demo.model.TemperatureExtraFee;
import com.example.demo.service.extraFee.PhenomenonService;
import com.example.demo.validationError.PhenomenonFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/phenomenonFees")
@RequiredArgsConstructor
public class PhenomenonFeeController {
    
    private final PhenomenonService phenomenonService;

    /**
     * Retrieves all weather phenomenon fees from the database.
     *
     * @return A {@link ResponseEntity} containing a list of {@link PhenomenonExtraFee}.
     */
    @GetMapping()
    public ResponseEntity<List<PhenomenonExtraFee>> getAllPhenomenonFees(){
        List<PhenomenonExtraFee> tempExtraFees = phenomenonService.getAllPhenomenonFees();
        return ResponseEntity.ok(tempExtraFees);
    }
    
    /**
     * Updates the adjustment amount for a specific weather phenomenon identified by its ID.
     *
     * @param id The ID of the {@link PhenomenonExtraFee} to update.
     * @param updateRequest A map containing the new adjustment amount under the key "adjustmentAmount".
     * @return A {@link ResponseEntity} with the updated {@link PhenomenonExtraFee} if the update is successful,
     *         a not found response if the {@link PhenomenonExtraFee} with the specified ID does not exist,
     *         or a bad request response if the provided adjustment amount is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhenomenonFee(@PathVariable Long id, @RequestBody Map<String, Double> updateRequest){
        try {
            double newAdjustmentAmount = updateRequest.get("adjustmentAmount");
            PhenomenonExtraFee updatedPhExtraFee = phenomenonService.updateFee(id, newAdjustmentAmount);
            if (updatedPhExtraFee == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedPhExtraFee);
        } catch (PhenomenonFeeValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
