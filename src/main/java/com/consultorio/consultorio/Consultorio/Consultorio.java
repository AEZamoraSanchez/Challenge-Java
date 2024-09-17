package com.consultorio.consultorio.Consultorio;

import java.util.List;


import com.consultorio.consultorio.Cita.Cita;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;


@Entity
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numeroConsultorio;

    private Long piso;

    @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas;

    public Long getId() {
        return id;
    }

    public Long getNumeroConsultorio() {
        return numeroConsultorio;
    }

    public void setNumeroConsultorio(Long numeroConsultorio) {
        this.numeroConsultorio = numeroConsultorio;
    }

    public Long getPiso() {
        return piso;
    }

    public void setPiso(Long piso) {
        this.piso = piso;
    }

}
