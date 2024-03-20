package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.ResponseObject;
import com.example.demo.models.Vehicle;
import com.example.demo.repositories.VehicleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/Vehicles")
public class VehicleController {
    @Autowired
    private VehicleRepository repository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllVehicles() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Query vehicles successfully", repository.findAll())
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> foundVehicle = repository.findById(id);
        if (foundVehicle.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query vehicle successfully", foundVehicle)
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find vehicle with id = " + id, new ArrayList<>())
            );
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertVehicle(@RequestBody Vehicle newVehicle) {
        List<Vehicle> foundVehicles = repository.findByLicensePlate(newVehicle.getLicensePlate().trim());
        if (foundVehicles.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Vehicle with license plate " + newVehicle.getLicensePlate() + " has been existed.", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Vehicle has been inserted successfully", repository.save(newVehicle))
        );
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateVehicle(@RequestBody Vehicle newVehicle, @PathVariable Long id) {
        Optional<Vehicle> updatedVehicle = repository.findById(id)
                .map(vehicle -> {
                    if (newVehicle.getLicensePlate() != null) {
                        vehicle.setLicensePlate(newVehicle.getLicensePlate());
                    }

                    if (newVehicle.getBrand() != null) {
                        vehicle.setBrand(newVehicle.getBrand());
                    }

                    if (newVehicle.getType() != null) {
                        vehicle.setType(newVehicle.getType());
                    }

                    if (newVehicle.getMaxLoad() != null) {
                        vehicle.setMaxLoad(newVehicle.getMaxLoad());
                    }

                    if (newVehicle.getNumOfSeats() != null) {
                        vehicle.setNumOfSeats(newVehicle.getNumOfSeats());
                    }

                    return repository.save(vehicle);
                });

        if (updatedVehicle.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Vehicle with id = " + id + "updated successfully.", updatedVehicle)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Vehicle with id = " + id + " not found", "")
        );
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<ResponseObject> deleteVehicle(@PathVariable Long id) {
        final Boolean existed = repository.existsById(id);
        repository.deleteById(id);
        if (existed) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Vehicle with id = " + id + " deleted successfully", "")
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Vehicle with id = " + id + " not found", "")
        );
    }
}
