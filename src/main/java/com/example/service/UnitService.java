package com.example.service;

import com.example.model.Unit;
import com.example.repo.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    // Constructor Injection
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    // =========================
    // ADD UNIT
    // =========================
    public Unit addUnit(Unit unit) {

        if (unit.getUnitNumber() == null) {
            throw new RuntimeException(
                    "Unit number is required."
            );
        }

        if (unit.getName() == null ||
                unit.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Unit name is required."
            );
        }

        return unitRepository.save(unit);
    }


    // =========================
    // GET ALL UNITS
    // =========================
    public List<Unit> getAllUnits() {

        return unitRepository.findAll();
    }


    // =========================
    // GET UNIT BY ID
    // =========================
    public Unit getUnitById(Long id) {

        return unitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: " + id
                        ));
    }


    // =========================
    // UPDATE UNIT
    // =========================
    public Unit updateUnit(Long id, Unit updatedUnit) {

        Unit existingUnit = unitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: " + id
                        ));

        if (updatedUnit.getUnitNumber() == null) {
            throw new RuntimeException(
                    "Unit number is required."
            );
        }

        if (updatedUnit.getName() == null ||
                updatedUnit.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Unit name is required."
            );
        }

        existingUnit.setUnitNumber(
                updatedUnit.getUnitNumber()
        );

        existingUnit.setName(
                updatedUnit.getName()
        );

        existingUnit.setDescription(
                updatedUnit.getDescription()
        );

        return unitRepository.save(existingUnit);
    }


    // =========================
    // DELETE UNIT
    // =========================
    public void deleteUnit(Long id) {

        if (!unitRepository.existsById(id)) {

            throw new RuntimeException(
                    "Cannot delete. Unit not found with ID: " + id
            );
        }

        unitRepository.deleteById(id);
    }
}