package com.example.demo.repository;

import com.example.demo.model.PhenomenonExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhenomenonExtraFeeRepository extends JpaRepository<PhenomenonExtraFee,Long> {
}
