package com.consultorio.consultorio.Cita;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.consultorio.consultorio.Consultorio.Consultorio;
import com.consultorio.consultorio.Doctor.Doctor;

public interface CitaRepository extends JpaRepository<Cita, Long>{
    boolean existsByConsultorioAndHorarioConsulta(Consultorio consultorio, LocalDateTime horarioConsulta);

    boolean existsByDoctorAndHorarioConsulta(Doctor doctor, LocalDateTime horarioConsulta);

    boolean existsByNombrePacienteAndHorarioConsultaBetween(String nombrePaciente, LocalDateTime startTime, LocalDateTime endTime);

    long countByDoctorAndHorarioConsultaBetween(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
}
