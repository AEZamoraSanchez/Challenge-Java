package com.consultorio.consultorio.Consultorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("consultorio")
public class ConsultorioController {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @GetMapping()
    public ResponseEntity<List<Consultorio>> getConsultorios() {
            
            List<Consultorio> Consultorios = this.consultorioRepository.findAll();

            if(Consultorios.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay Consultorios registrados");

            }

            return ResponseEntity.ok(Consultorios);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultorio> getConsultorioById(@PathVariable Long id) {
        Consultorio Consultorio = this.consultorioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultorio no encontrado con el id: " + id));

            return ResponseEntity.ok(Consultorio);
    }

    @PostMapping()
    public ResponseEntity<Consultorio> postConsultorio(@RequestBody Consultorio consultorio) {
        Consultorio savedConsultorio = this.consultorioRepository.save(consultorio);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConsultorio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsultorio(@PathVariable Long id) {
        Consultorio Consultorio = this.consultorioRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultorio no encontrado con el id: " + id));

        this.consultorioRepository.delete(Consultorio);

        return ResponseEntity.ok("Consultorio eliminado con el id: " + id);
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
