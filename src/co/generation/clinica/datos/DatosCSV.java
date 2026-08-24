package co.generation.clinica.datos;

import co.generation.clinica.model.EstadoTurno;
import co.generation.clinica.model.Especialidad;
import co.generation.clinica.model.Medico;
import co.generation.clinica.model.Paciente;
import co.generation.clinica.model.Turno;
import co.generation.clinica.service.ClinicaService;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatosCSV {

    private static final String DIR_PATH = "datos";
    private static final String F_PACIENTES = DIR_PATH + File.separator + "pacientes.csv";
    private static final String F_MEDICOS = DIR_PATH + File.separator + "medicos.csv";
    private static final String F_TURNOS = DIR_PATH + File.separator + "turnos.csv";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static void asegurarDirectorio() {
        try {
            Path path = Paths.get(DIR_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.out.println("Error al crear carpeta datos: " + e.getMessage());
        }
    }

    public static void cargar(ClinicaService servicio) {
        asegurarDirectorio();
        cargarPacientes(servicio);
        cargarMedicos(servicio);
        cargarTurnos(servicio);
    }

    private static void cargarPacientes(ClinicaService servicio) {
        File f = new File(F_PACIENTES);
        if (!f.exists()) return;

        try (BufferedReader br = Files.newBufferedReader(f.toPath())) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 5) continue;

                int id = Integer.parseInt(p[0].trim());
                String cedula = p[1].trim();
                String nombre = p[2].trim();
                String apellido = p[3].trim();
                String telefono = p[4].trim();

                Paciente paciente = new Paciente(cedula, nombre, apellido, telefono);
                paciente.setId(id);
                servicio.getPacientes().add(paciente);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private static void cargarMedicos(ClinicaService servicio) {
        File f = new File(F_MEDICOS);
        if (!f.exists()) return;

        try (BufferedReader br = Files.newBufferedReader(f.toPath())) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 4) continue;

                int id = Integer.parseInt(p[0].trim());
                String nombre = p[1].trim();
                String apellido = p[2].trim();
                Especialidad esp = Especialidad.valueOf(p[3].trim().toUpperCase());

                Medico medico = new Medico(nombre, apellido, esp);
                medico.setId(id);
                servicio.getMedicos().add(medico);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar médicos: " + e.getMessage());
        }
    }

    private static void cargarTurnos(ClinicaService servicio) {
        File f = new File(F_TURNOS);
        if (!f.exists()) return;

        try (BufferedReader br = Files.newBufferedReader(f.toPath())) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 6) continue;

                int id = Integer.parseInt(p[0].trim());
                Paciente paciente = servicio.buscarPorCedula(p[1].trim());
                Medico medico = servicio.buscarPorNombreApellido(p[2].trim(), p[3].trim());

                if (paciente == null || medico == null) continue;

                LocalDateTime fechaHora = LocalDateTime.parse(p[4].trim(), FMT);
                EstadoTurno estado = EstadoTurno.valueOf(p[5].trim().toUpperCase());

                Turno turno = new Turno(paciente, medico, fechaHora);
                turno.setId(id);
                turno.setEstado(estado);
                servicio.getTurnos().add(turno);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar turnos: " + e.getMessage());
        }
    }

    public static void guardar(ClinicaService servicio) {
        asegurarDirectorio();
        guardarPacientes(servicio.getPacientes());
        guardarMedicos(servicio.getMedicos());
        guardarTurnos(servicio.getTurnos());
    }

    private static void guardarPacientes(List<Paciente> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(F_PACIENTES))) {
            for (Paciente paciente : lista) {
                bw.write(paciente.getId() + "," + paciente.getCedula() + "," + paciente.getNombre() + "," + paciente.getApellido() + "," + paciente.getTelefono());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    private static void guardarMedicos(List<Medico> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(F_MEDICOS))) {
            for (Medico medico : lista) {
                bw.write(medico.getId() + "," + medico.getNombre() + "," + medico.getApellido() + "," + medico.getEspecialidad());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar médicos: " + e.getMessage());
        }
    }

    private static void guardarTurnos(List<Turno> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(F_TURNOS))) {
            for (Turno turno : lista) {
                bw.write(turno.getId() + "," +
                        turno.getPaciente().getCedula() + "," +
                        turno.getMedico().getNombre() + "," +
                        turno.getMedico().getApellido() + "," +
                        turno.getFechaHora().format(FMT) + "," +
                        turno.getEstado());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar turnos: " + e.getMessage());
        }
    }
}