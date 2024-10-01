package com.consultorio.consultorio.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.consultorio.consultorio.ErrorResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/cita")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping("all/{page}")
    public ResponseEntity<Page<Cita>> getCitas(@PathVariable int page) {
            PageRequest paging = PageRequest.of(page, 6);

            Page<Cita> Citas = this.citaRepository.findAll(paging);

            if(Citas.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay Citas registradas");

            }

            return ResponseEntity.ok(Citas);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable Long id) {
        Cita Cita = this.citaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con el id: " + id));

            return ResponseEntity.ok(Cita);
    }

    @PostMapping()
    public ResponseEntity<Cita> postCita(@RequestBody Cita cita) {
        
        this.validarCita(cita);
        Cita savedCita = this.citaRepository.save(cita);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCita(@PathVariable Long id) {
        Cita Cita = this.citaRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrado con el id: " + id));

        this.citaRepository.delete(Cita);

        return ResponseEntity.ok("Consultorio eliminado con el id: " + id);
    }
    
     @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getReason(), ex.getStatusCode().value());    
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Ha ocurrido un problema interno" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(errorResponse);
    }
    
    private void validarCita(Cita cita) {
        // Validar que no haya otra cita en el mismo consultorio a la misma hora
        if (citaRepository.existsByConsultorioAndHorarioConsulta(cita.getConsultorio(), cita.getHorarioConsulta())) {
            throw new IllegalArgumentException("No se puede agendar cita en un mismo consultorio a la misma hora.");
        }

        // Validar que no haya otra cita para el mismo doctor a la misma hora
        if (citaRepository.existsByDoctorAndHorarioConsulta(cita.getDoctor(), cita.getHorarioConsulta())) {
            throw new IllegalArgumentException("No se puede agendar cita para un mismo Dr. a la misma hora.");
        }

        // Validar que el paciente no tenga otra cita a la misma hora ni con menos de 2 horas de diferencia
        LocalDateTime startTime = cita.getHorarioConsulta().minusHours(2);
        LocalDateTime endTime = cita.getHorarioConsulta().plusHours(2);
        if (citaRepository.existsByNombrePacienteAndHorarioConsultaBetween(cita.getNombrePaciente(), startTime, endTime)) {
            throw new IllegalArgumentException("No se puede agendar cita para un paciente a la misma hora ni con menos de 2 horas de diferencia para el mismo día.");
        }

        // Validar que el doctor no tenga más de 8 citas en el día
        LocalDate fechaCita = cita.getHorarioConsulta().toLocalDate();
        LocalDateTime startOfDay = fechaCita.atStartOfDay();
        LocalDateTime endOfDay = fechaCita.plusDays(1).atStartOfDay();
        if (citaRepository.countByDoctorAndHorarioConsultaBetween(cita.getDoctor(), startOfDay, endOfDay) >= 8) {
            throw new IllegalArgumentException("Un mismo doctor no puede tener más de 8 citas en el día.");
        }
    }

}
