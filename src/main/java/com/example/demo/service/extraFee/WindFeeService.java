package com.example.demo.service.extraFee;

import com.example.demo.model.WindExtraFee;
import com.example.demo.repository.WindExtraFeeRepository;
import com.example.demo.validationError.WindFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WindFeeService {
    
    private final WindExtraFeeRepository windExtraFeeRepository;

    /**
     * Retrieves all wind extra fees from the repository.
     *
     * @return a list of all {@link WindExtraFee}.
     */
    public List<WindExtraFee> getAllWindFees() {
        return windExtraFeeRepository.findAll();
    }

    /**
     * Creates a new wind extra fee entry.
     *
     * @param windExtraFee the {@link WindExtraFee} entity to be saved.
     * @return the saved {@link WindExtraFee} entity.
     * @throws WindFeeValidationException if validation fails for min, max values or adjustment amount.
     */
    public WindExtraFee createWindFee(WindExtraFee windExtraFee) {
        validateMinAndMaxValues(windExtraFee);
        return windExtraFeeRepository.save(windExtraFee);
    }

    /**
     * Updates an existing wind extra fee identified by its ID.
     *
     * @param id the ID of the {@link WindExtraFee} to update.
     * @param windExtraFee the updated {@link WindExtraFee} entity.
     * @return the updated {@link WindExtraFee}, or {@code null} if not found.
     * @throws WindFeeValidationException if validation fails for min, max values or adjustment amount.
     */
    public WindExtraFee updateWindFee(Long id, WindExtraFee windExtraFee){
        return windExtraFeeRepository.findById(id).map(existingFee -> {
            existingFee.setMinValue(windExtraFee.getMinValue());
            existingFee.setMaxValue(windExtraFee.getMaxValue());
            existingFee.setAdjustmentAmount(windExtraFee.getAdjustmentAmount());
            validateMinAndMaxValues(existingFee);
            return windExtraFeeRepository.save(existingFee);
        }).orElse(null);
    }

    /**
     * Deletes a wind extra fee entry by its ID.
     *
     * @param id the ID of the {@link WindExtraFee} to delete.
     * @return {@code true} if the entry was found and deleted, {@code false} otherwise.
     */
    public boolean deleteWindFee(Long id){
        return windExtraFeeRepository.findById(id).map(windFee -> {
            windExtraFeeRepository.delete(windFee);
            return true;
        }).orElse(false);
    }

    private void validateMinAndMaxValues(WindExtraFee windExtraFee) {
        if (windExtraFee.getMaxValue() > 20) {
            throw new WindFeeValidationException("Max value cannot be more than 20!");
        }
        if (windExtraFee.getMinValue() < 0) {
            throw new WindFeeValidationException("Min value cannot be less than 0!");
        }
        if (windExtraFee.getAdjustmentAmount() < 0) {
            throw new WindFeeValidationException("Adjustment amount value cannot be less than 0!");
        }
    }
}
