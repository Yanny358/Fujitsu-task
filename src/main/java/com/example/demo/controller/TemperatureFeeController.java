package com.example.demo.controller;

import com.example.demo.model.TemperatureExtraFee;
import com.example.demo.service.extraFee.TemperatureFeeService;
import com.example.demo.validationError.TemperatureFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/temperatureFees")
@RequiredArgsConstructor
public class TemperatureFeeController {

    private final TemperatureFeeService temperatureFeeService;

    /**
     * Retrieves all temperature extra fees.
     *
     * @return a ResponseEntity containing a list of all {@link TemperatureExtraFee} objects.
     */
    @GetMapping()
    public ResponseEntity<List<TemperatureExtraFee>> getAllTemperatureFees(){
        List<TemperatureExtraFee> tempExtraFees = temperatureFeeService.getAllTemperatureFees();
        return ResponseEntity.ok(tempExtraFees);
    }

    /**
     * Creates a new temperature extra fee.
     *
     * @param newTempFee the temperature extra fee to create.
     * @return a ResponseEntity containing the created {@link TemperatureExtraFee}, or a bad request error if validation fails.
     */
    @PostMapping()
    public ResponseEntity<?> createTemperatureFee(@RequestBody TemperatureExtraFee newTempFee){
        try {
            TemperatureExtraFee tempExtraFee = temperatureFeeService.createTemperatureFee(newTempFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(tempExtraFee);
        } catch (TemperatureFeeValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing temperature extra fee by ID.
     *
     * @param id the ID of the temperature extra fee to update.
     * @param tempFee the updated temperature extra fee details.
     * @return a ResponseEntity containing the updated {@link TemperatureExtraFee}, or a not found error if the ID does not exist, or a bad request error if validation fails.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemperatureFee(@PathVariable Long id, @RequestBody TemperatureExtraFee tempFee){
        try {
            TemperatureExtraFee updatedTempExtraFee = temperatureFeeService.updateTemperatureFee(id, tempFee);
            if (updatedTempExtraFee == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedTempExtraFee);
        } catch (TemperatureFeeValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Deletes a temperature extra fee by ID.
     *
     * @param id the ID of the temperature extra fee to delete.
     * @return a ResponseEntity indicating success (200 OK) or not found (404 Not Found) if the ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemperatureFee(@PathVariable Long id){
        boolean toDeleteTempExtraFee = temperatureFeeService.deleteTemperatureFee(id);
        if (!toDeleteTempExtraFee) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
