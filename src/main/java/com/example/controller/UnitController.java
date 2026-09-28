package com.example.controller;

import com.example.model.Unit;
import com.example.service.UnitService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
@CrossOrigin(origins = "*")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    // ADD
    @PostMapping("/add")
    public Unit addUnit(@RequestBody Unit unit) {
        return unitService.addUnit(unit);
    }

    // GET ALL
    @GetMapping("/get")
    public List<Unit> getAllUnits() {
        return unitService.getAllUnits();
    }

    // GET ONE
    @GetMapping("/get/{id}")
    public Unit getUnit(@PathVariable Long id) {
        return unitService.getUnitById(id);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public Unit updateUnit(
            @PathVariable Long id,
            @RequestBody Unit unit) {

        return unitService.updateUnit(id, unit);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteUnit(@PathVariable Long id) {

        unitService.deleteUnit(id);

        return "Unit deleted successfully";
    }
}