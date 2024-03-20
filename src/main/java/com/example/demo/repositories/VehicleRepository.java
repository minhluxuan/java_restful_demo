package com.example.demo.repositories;

import com.example.demo.models.Vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByLicensePlate(String licensePlate);
}
