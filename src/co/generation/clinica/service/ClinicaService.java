package co.generation.clinica.service;

import co.generation.clinica.interfaces.Consultable;
import co.generation.clinica.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClinicaService implements Consultable {

    private final List<Paciente> pacientes;
    private final List<Medico> medicos;
    private final List<Turno> turnos;
    public ClinicaService() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.turnos = new ArrayList<>();
    }
    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }
    public void registrarPaciente(Paciente p) {
        if (p == null || !p.esValido()) {
            System.out.println("Error: El paciente contiene datos no válidos.");
            return;
        }

        if (pacientes.contains(p)) {
            System.out.println("Error: Ya existe un paciente registrado con la cédula " + p.getCedula());
            return;
        }

        int maxId = 0;
        for (Paciente pac : pacientes) {
            if (pac.getId() > maxId) {
                maxId = pac.getId();
            }
        }
        p.setId(maxId + 1);

        pacientes.add(p);
        System.out.println("Paciente registrado con éxito: " + p.getDatosRegistro());
    }
    public Paciente buscarPorCedula(String cedula) {
        if (cedula == null) return null;
        for (Paciente p : pacientes) {
            if (p.getCedula().equalsIgnoreCase(cedula.trim())) {
                return p;
            }
        }
        return null;
    }
    public void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        List<Paciente> copia = new ArrayList<>(pacientes);
        copia.sort(Comparator.comparing(Paciente::getApellido, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Paciente::getNombre, String.CASE_INSENSITIVE_ORDER));

        System.out.println("--- LISTA DE PACIENTES ---");
        for (Paciente p : copia) {
            System.out.println(p);
        }
    }
    public void registrarMedico(Medico m) {
        if (m == null || !m.esValido()) {
            System.out.println("Error: El médico contiene datos no válidos.");
            return;
        }

        if (medicos.contains(m)) {
            System.out.println("Error: Ya existe un médico con ese nombre y apellido.");
            return;
        }

        int maxId = 0;
        for (Medico med : medicos) {
            if (med.getId() > maxId) {
                maxId = med.getId();
            }
        }
        m.setId(maxId + 1);

        medicos.add(m);
        System.out.println("Médico registrado con éxito: " + m.getDatosRegistro());
    }
    public Medico buscarPorNombreApellido(String nombre, String apellido) {
        if (nombre == null || apellido == null) return null;
        for (Medico m : medicos) {
            if (m.getNombre().equalsIgnoreCase(nombre.trim()) && m.getApellido().equalsIgnoreCase(apellido.trim())) {
                return m;
            }
        }
        return null;
    }
    public void listarMedicos() {
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }

        List<Medico> copia = new ArrayList<>(medicos);
        copia.sort(Comparator.comparing(Medico::getEspecialidad)
                .thenComparing(Medico::getApellido, String.CASE_INSENSITIVE_ORDER));

        System.out.println("--- LISTA DE MÉDICOS ---");
        for (Medico m : copia) {
            System.out.println(m);
        }
    }
    public void asignarTurno(Turno t) {
        if (t == null || t.getPaciente() == null || t.getMedico() == null || t.getFechaHora() == null) {
            System.out.println("Error: Datos del turno incompletos.");
            return;
        }

        if (buscarPorCedula(t.getPaciente().getCedula()) == null) {
            System.out.println("Error: El paciente no existe en el sistema.");
            return;
        }

        if (buscarPorNombreApellido(t.getMedico().getNombre(), t.getMedico().getApellido()) == null) {
            System.out.println("Error: El médico no existe en el sistema.");
            return;
        }

        if (turnos.contains(t)) {
            System.out.println("Error: El médico ya tiene un turno agendado para esa fecha y hora.");
            return;
        }

        int maxId = 0;
        for (Turno tur : turnos) {
            if (tur.getId() > maxId) {
                maxId = tur.getId();
            }
        }
        t.setId(maxId + 1);

        turnos.add(t);
        System.out.println("Turno asignado con éxito: " + t);
    }
    public void cancelarTurno(int idTurno) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }

        if (turno.getEstado() == EstadoTurno.ATENDIDO || turno.getEstado() == EstadoTurno.CANCELADO) {
            System.out.println("No se puede cancelar el turno porque su estado actual es: " + turno.getEstado());
            return;
        }

        turno.setEstado(EstadoTurno.CANCELADO);
        System.out.println("Turno ID " + idTurno + " cancelado correctamente.");
    }
    public void cambiarEstadoTurno(int idTurno, EstadoTurno nuevoEstado) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }

        turno.setEstado(nuevoEstado);
        System.out.println("Estado del turno ID " + idTurno + " actualizado a: " + nuevoEstado);
    }
    private Turno buscarTurnoPorId(int idTurno) {
        for (Turno t : turnos) {
            if (t.getId() == idTurno) {
                return t;
            }
        }
        return null;
    }
    @Override
    public List<Turno> listarTurnosDelDia(LocalDate fecha) {
        List<Turno> resultado = new ArrayList<>();
        if (fecha == null) return resultado;

        for (Turno t : turnos) {
            if (t.getFechaHora().toLocalDate().equals(fecha)) {
                resultado.add(t);
            }
        }

        resultado.sort(Comparator.comparing(Turno::getFechaHora));
        return resultado;
    }
    @Override
    public List<Turno> buscarPorMedico(Medico medico) {
        List<Turno> resultado = new ArrayList<>();
        if (medico == null) return resultado;

        for (Turno t : turnos) {
            if (t.getMedico().equals(medico)) {
                resultado.add(t);
            }
        }
        return resultado;
    }
    @Override
    public List<Turno> buscarPorPaciente(Paciente paciente) {
        List<Turno> resultado = new ArrayList<>();
        if (paciente == null) return resultado;

        for (Turno t : turnos) {
            if (t.getPaciente().equals(paciente)) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}