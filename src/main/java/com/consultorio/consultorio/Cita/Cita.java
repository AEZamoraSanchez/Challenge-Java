package com.consultorio.consultorio.Cita;

import java.time.LocalDateTime;

import com.consultorio.consultorio.Consultorio.Consultorio;
import com.consultorio.consultorio.Doctor.Doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime horarioConsulta;

    private String nombrePaciente;

    @ManyToOne()
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne()
    @JoinColumn(name = "consultorio_id")
    private Consultorio consultorio;

    public Long getId() {
        return id;
    }

    public LocalDateTime getHorarioConsulta() {
        return horarioConsulta;
    }

    public void setHorarioConsulta(LocalDateTime horarioConsulta) {
        this.horarioConsulta = horarioConsulta;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }
}
