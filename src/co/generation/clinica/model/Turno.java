package co.generation.clinica.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Turno {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private EstadoTurno estado;

    public Turno(int id, Paciente paciente, Medico medico, LocalDateTime fechaHora, EstadoTurno estado) {
        this.id = id;
        this.setPaciente(paciente);
        this.setMedico(medico);
        this.setFechaHora(fechaHora);
        this.setEstado(estado);
    }

    public Turno(Paciente paciente, Medico medico, LocalDateTime fechaHora) {
        this.setPaciente(paciente);
        this.setMedico(medico);
        this.setFechaHora(fechaHora);
        this.estado = EstadoTurno.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo.");
        }
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El médico no puede ser nulo.");
        }
        this.medico = medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas.");
        }
        this.fechaHora = fechaHora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "[" + estado + "]" + paciente +
                " - " + medico +
                " - " + fechaHora;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Turno)) {
            return false;
        }
        Turno otroTurno = (Turno) obj;
        return Objects.equals(medico, otroTurno.medico)
                && Objects.equals(fechaHora, otroTurno.fechaHora);
    }
}

