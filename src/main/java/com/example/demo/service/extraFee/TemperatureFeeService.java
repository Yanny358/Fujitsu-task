package com.example.demo.service.extraFee;

import com.example.demo.model.TemperatureExtraFee;
import com.example.demo.repository.TemperatureExtraFeeRepository;
import com.example.demo.validationError.TemperatureFeeValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureFeeService {
    
    private final TemperatureExtraFeeRepository temperatureExtraFeeRepository;
    
    /**
     * Retrieves all temperature extra fees from the repository.
     *
     * @return a list of all {@link TemperatureExtraFee}.
     */
    public List<TemperatureExtraFee> getAllTemperatureFees() {
        return temperatureExtraFeeRepository.findAll();
    }
    
    /**
     * Creates a new temperature extra fee entry.
     *
     * @param temperatureExtraFee the {@link TemperatureExtraFee} entity to be saved.
     * @return the saved {@link TemperatureExtraFee} entity.
     * @throws TemperatureFeeValidationException if validation fails for min, max values or adjustment amount.
     */
    public TemperatureExtraFee createTemperatureFee(TemperatureExtraFee temperatureExtraFee) {
        validateMinAndMaxValues(temperatureExtraFee);
        return temperatureExtraFeeRepository.save(temperatureExtraFee);
    }

    /**
     * Updates an existing temperature extra fee identified by its ID.
     *
     * @param id the ID of the {@link TemperatureExtraFee} to update.
     * @param temperatureExtraFee the updated {@link TemperatureExtraFee} entity.
     * @return the updated {@link TemperatureExtraFee}, or {@code null} if not found.
     * @throws TemperatureFeeValidationException if validation fails for min, max values or adjustment amount.
     */
    public TemperatureExtraFee updateTemperatureFee(Long id, TemperatureExtraFee temperatureExtraFee){
        return temperatureExtraFeeRepository.findById(id).map(existingFee -> {
            existingFee.setMinValue(temperatureExtraFee.getMinValue());
            existingFee.setMaxValue(temperatureExtraFee.getMaxValue());
            existingFee.setAdjustmentAmount(temperatureExtraFee.getAdjustmentAmount());
            validateMinAndMaxValues(existingFee);
            return temperatureExtraFeeRepository.save(existingFee);
        }).orElse(null);
    }

    /**
     * Deletes a temperature extra fee entry by its ID.
     *
     * @param id the ID of the {@link TemperatureExtraFee} to delete.
     * @return {@code true} if the entry was found and deleted, {@code false} otherwise.
     */
    public boolean deleteTemperatureFee(Long id){
        return temperatureExtraFeeRepository.findById(id).map(temperatureFee -> {
            temperatureExtraFeeRepository.delete(temperatureFee);
            return true;
        }).orElse(false);
    }

    private void validateMinAndMaxValues(TemperatureExtraFee temperatureExtraFee) {
        if (temperatureExtraFee.getMaxValue() > 50) {
            throw new TemperatureFeeValidationException("Max value cannot be more than 50!");
        }
        if (temperatureExtraFee.getMinValue() < -50) {
            throw new TemperatureFeeValidationException("Min value cannot be less than -50!");
        }
        if (temperatureExtraFee.getAdjustmentAmount() < 0){
            throw new TemperatureFeeValidationException("Adjustment amount value cannot be less than 0!");
        }
    }
}
