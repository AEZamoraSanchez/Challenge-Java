package com.consultorio.consultorio.Doctor;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;
    

    @GetMapping()
    public ResponseEntity<List<Doctor>> getDoctors() {
            
            List<Doctor> doctors = this.doctorRepository.findAll();

            if(doctors.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay doctores registrados");

            }

            return ResponseEntity.ok(doctors);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = this.doctorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado con el id: " + id));

            return ResponseEntity.ok(doctor);
    }

    @PostMapping()
    public ResponseEntity<Doctor> postDoctor(@RequestBody Doctor doctor) {
        Doctor saveDoctor = this.doctorRepository.save(doctor);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saveDoctor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        Doctor doctor = this.doctorRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado con el id: " + id));

        this.doctorRepository.delete(doctor);

        return ResponseEntity.ok("Doctor eliminado con el id: " + id);
    }

     @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Ha ocurrido un error interno: " + ex.getMessage());
    }
    
}
