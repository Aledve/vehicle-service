package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Vehicle;
import yps.systems.ai.repository.IVehicleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicleService") // <--- Ruta actualizada
public class VehicleController {

    private final IVehicleRepository vehicleRepository;

    @Autowired
    public VehicleController(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    ResponseEntity<List<Vehicle>> getAll() {
        return ResponseEntity.ok(vehicleRepository.findAll());
    }

    @GetMapping("/{elementId}")
    ResponseEntity<Vehicle> getByElementId(@PathVariable String elementId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(elementId);
        return optionalVehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byOwner/{ownerId}")
    ResponseEntity<List<Vehicle>> getByOwnerId(@PathVariable String ownerId) {
        List<Vehicle> vehicles = vehicleRepository.findAllByOwnerId(ownerId);
        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> save(@RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return new ResponseEntity<>("Vehicle saved with ID: " + savedVehicle.getElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{elementId}")
    ResponseEntity<String> delete(@PathVariable String elementId) {
        if (vehicleRepository.existsById(elementId)) {
            vehicleRepository.deleteById(elementId);
            return new ResponseEntity<>("Vehicle deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{elementId}")
    ResponseEntity<String> update(@PathVariable String elementId, @RequestBody Vehicle vehicle) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(elementId);
        if (optionalVehicle.isPresent()) {
            vehicle.setElementId(optionalVehicle.get().getElementId());
            vehicleRepository.save(vehicle);
            return new ResponseEntity<>("Vehicle updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
    }
}
