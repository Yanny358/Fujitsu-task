package com.example.demo.service.extraFee;

import com.example.demo.model.PhenomenonExtraFee;
import com.example.demo.repository.PhenomenonExtraFeeRepository;
import com.example.demo.validationError.PhenomenonFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhenomenonService {
    
    private final PhenomenonExtraFeeRepository phenomenonExtraFeeRepository;

    /**
     * Retrieves all {@link PhenomenonExtraFee} entries from the repository.
     *
     * @return A list of {@link PhenomenonExtraFee} representing all weather phenomenon fees stored in the database.
     */
    public List<PhenomenonExtraFee> getAllPhenomenonFees() {
        return phenomenonExtraFeeRepository.findAll();
    }
    /**
     * Updates the adjustment amount for a specific weather phenomenon identified by its ID.
     *
     * @param id The ID of the {@link PhenomenonExtraFee} to be updated.
     * @param newFee The new adjustment amount to be set for the phenomenon.
     * @return The updated {@link PhenomenonExtraFee} object if found and updated successfully, otherwise returns {@code null}.
     * @throws PhenomenonFeeValidationException if the new fee is less than 0.
     */

    public PhenomenonExtraFee updateFee(Long id, double newFee) {
        return phenomenonExtraFeeRepository.findById(id).map(phenomenon -> {
            phenomenon.setAdjustmentAmount(newFee);
            validateFee(newFee);
            return phenomenonExtraFeeRepository.save(phenomenon);
        }).orElse(null);
    }

    private void validateFee(double fee) {
        if (fee < 0) {
            throw new PhenomenonFeeValidationException("Adjustment amount value cannot be less than 0!");
        }
    }

}
