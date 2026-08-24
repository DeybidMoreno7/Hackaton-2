package co.generation.clinica.datos;

import co.generation.clinica.model.*;
import co.generation.clinica.service.ClinicaService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatosCSV {

    private static final String DIR = "datos/";
    private static final String F_PACIENTES = DIR + "pacientes.csv";
    private static final String F_MEDICOS = DIR + "medicos.csv";
    private static final String F_TURNOS = DIR + "turnos.csv";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void cargar(ClinicaService servicio) {
        File directorio = new File(DIR);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        cargarPacientes(servicio);
        cargarMedicos(servicio);
        cargarTurnos(servicio);
    }

    private static void cargarPacientes(ClinicaService servicio) {
        File f = new File(F_PACIENTES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
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

                Paciente pac = new Paciente(id, cedula, nombre, apellido, telefono);
                servicio.getPacientes().add(pac);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private static void cargarMedicos(ClinicaService servicio) {
        File f = new File(F_MEDICOS);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 4) continue;

                int id = Integer.parseInt(p[0].trim());
                String nombre = p[1].trim();
                String apellido = p[2].trim();
                Especialidad esp = Especialidad.valueOf(p[3].trim().toUpperCase());

                Medico med = new Medico(id, nombre, apellido, esp);
                servicio.getMedicos().add(med);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar médicos: " + e.getMessage());
        }
    }

    private static void cargarTurnos(ClinicaService servicio) {
        File f = new File(F_TURNOS);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 6) continue;

                int id = Integer.parseInt(p[0].trim());
                Paciente pac = servicio.buscarPorCedula(p[1].trim());
                Medico med = servicio.buscarPorNombreApellido(p[2].trim(), p[3].trim());

                if (pac == null || med == null) continue;

                LocalDateTime fechaHora = LocalDateTime.parse(p[4].trim(), FMT);
                EstadoTurno estado = EstadoTurno.valueOf(p[5].trim().toUpperCase());

                Turno turno = new Turno(id, pac, med, fechaHora, estado);
                servicio.getTurnos().add(turno);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar turnos: " + e.getMessage());
        }
    }


    public static void guardar(ClinicaService servicio) {
        File directorio = new File(DIR);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        guardarPacientes(servicio.getPacientes());
        guardarMedicos(servicio.getMedicos());
        guardarTurnos(servicio.getTurnos());
    }

    private static void guardarPacientes(List<Paciente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(F_PACIENTES))) {
            for (Paciente p : lista) {
                pw.println(p.getId() + "," + p.getCedula() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getTelefono());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    private static void guardarMedicos(List<Medico> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(F_MEDICOS))) {
            for (Medico m : lista) {
                pw.println(m.getId() + "," + m.getNombre() + "," + m.getApellido() + "," + m.getEspecialidad());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar médicos: " + e.getMessage());
        }
    }

    private static void guardarTurnos(List<Turno> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(F_TURNOS))) {
            for (Turno t : lista) {
                pw.println(t.getId() + "," +
                        t.getPaciente().getCedula() + "," +
                        t.getMedico().getNombre() + "," +
                        t.getMedico().getApellido() + "," +
                        t.getFechaHora().format(FMT) + "," +
                        t.getEstado());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar turnos: " + e.getMessage());
        }
    }
}