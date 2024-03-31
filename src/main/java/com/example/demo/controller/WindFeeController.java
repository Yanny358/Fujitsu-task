package com.example.demo.controller;

import com.example.demo.model.WindExtraFee;
import com.example.demo.service.extraFee.WindFeeService;
import com.example.demo.validationError.WindFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/windFees")
@RequiredArgsConstructor
public class WindFeeController {
    
    private final WindFeeService windFeeService;

    /**
     * Retrieves all wind extra fees.
     *
     * @return a ResponseEntity containing a list of all {@link WindExtraFee} objects.
     */
    @GetMapping()
    public ResponseEntity<List<WindExtraFee>> getAllWindFees(){
        List<WindExtraFee> windExtraFees = windFeeService.getAllWindFees();
        return ResponseEntity.ok(windExtraFees);
    }

    /**
     * Creates a new wind extra fee.
     *
     * @param newWindFee the wind extra fee to create.
     * @return a ResponseEntity containing the created {@link WindExtraFee}, or a bad request error if validation fails.
     */
    @PostMapping()
    public ResponseEntity<?> createWindFee(@RequestBody WindExtraFee newWindFee){
        try {
            WindExtraFee windExtraFee = windFeeService.createWindFee(newWindFee);
            return ResponseEntity.status(HttpStatus.CREATED).body(windExtraFee);
        } catch (WindFeeValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing wind extra fee identified by its ID.
     *
     * @param id the ID of the {@link WindExtraFee} to update.
     * @param windFee the updated {@link WindExtraFee} entity.
     * @return a ResponseEntity containing the updated {@link WindExtraFee}, or a not found error if the ID does not exist, or a bad request error if validation fails.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWindFee(@PathVariable Long id, @RequestBody WindExtraFee windFee){
        try {
            WindExtraFee updatedWindExtraFee = windFeeService.updateWindFee(id, windFee);
            if (updatedWindExtraFee == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedWindExtraFee);
        } catch (WindFeeValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Deletes a wind extra fee by its ID.
     *
     * @param id the ID of the {@link WindExtraFee} to delete.
     * @return a ResponseEntity indicating success (200 OK) or not found (404 Not Found) if the ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWindFee(@PathVariable Long id){
        boolean toDeleteWindExtraFee = windFeeService.deleteWindFee(id);
        if (!toDeleteWindExtraFee) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
