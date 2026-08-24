package co.generation.clinica;

import co.generation.clinica.datos.DatosCSV;
import co.generation.clinica.model.*;
import co.generation.clinica.service.ClinicaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();
        DatosCSV.cargar(servicio);
        Scanner sc = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor, ingrese un número.");
                continue;
            }
            switch (opcion) {
                case 1:
                    registrarPaciente(servicio, sc);
                    break;
                case 2:
                    registrarMedico(servicio, sc);
                    break;
                case 3:
                    asignarTurno(servicio, sc);
                    break;
                case 4:
                    listarTurnosDelDia(servicio, sc);
                    break;
                case 5:
                    cancelarTurno(servicio, sc);
                    break;
                case 6:
                    verTurnosPorMedico(servicio, sc);
                    break;
                case 7:
                    verTurnosPorPaciente(servicio, sc);
                    break;
                case 8:
                    cambiarEstadoTurno(servicio, sc);
                    break;
                case 9:
                    servicio.listarPacientes();
                    break;
                case 10:
                    servicio.listarMedicos();
                    break;
                case 0:
                    DatosCSV.guardar(servicio);
                    System.out.println("Hasta pronto. Datos guardados.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        sc.close();
    }
    private static void mostrarMenu() {
        System.out.println("==========================================");
        System.out.println("||          CLINICAAPP - MENÚ           ||");
        System.out.println("==========================================");
        System.out.println(" 1. Registrar paciente                    ");
        System.out.println(" 2. Registrar médico                      ");
        System.out.println(" 3. Asignar turno                         ");
        System.out.println(" 4. Listar turnos del día                 ");
        System.out.println(" 5. Cancelar turno                        ");
        System.out.println(" 6. Ver turnos por médico                 ");
        System.out.println(" 7. Ver turnos por paciente               ");
        System.out.println(" 8. Cambiar estado de turno               ");
        System.out.println(" 9. Listar pacientes                      ");
        System.out.println(" 10.Listar médicos                       ");
        System.out.println(" 0. Salir                                 ");
        System.out.println("==========================================");
    }
    private static void registrarPaciente(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("Cédula: ");
            String cedula = sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Apellido: ");
            String apellido = sc.nextLine();
            System.out.print("Teléfono: ");
            String telefono = sc.nextLine();

            Paciente p = new Paciente(cedula, nombre, apellido, telefono);
            servicio.registrarPaciente(p);
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }
    private static void registrarMedico(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Apellido: ");
            String apellido = sc.nextLine();
            System.out.println("Especialidades disponibles: GENERAL, PEDIATRIA, CARDIOLOGIA, URGENCIAS");
            System.out.print("Especialidad: ");
            String espStr = sc.nextLine().trim().toUpperCase();
            Especialidad esp = Especialidad.valueOf(espStr);
            Medico m = new Medico(nombre, apellido, esp);
            servicio.registrarMedico(m);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Especialidad no válida o datos incorrectos (" + e.getMessage() + ")");
        }
    }
    private static void asignarTurno(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("Cédula del paciente: ");
            String cedula = sc.nextLine();
            Paciente paciente = servicio.buscarPorCedula(cedula);
            if (paciente == null) {
                System.out.println("Paciente no encontrado.");
                return;
            }

            System.out.print("Nombre del médico: ");
            String nombreMed = sc.nextLine();
            System.out.print("Apellido del médico: ");
            String apellidoMed = sc.nextLine();
            Medico medico = servicio.buscarPorNombreApellido(nombreMed, apellidoMed);
            if (medico == null) {
                System.out.println("Médico no encontrado.");
                return;
            }

            System.out.print("Año (ej. 2026): ");
            int anio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Mes (1-12): ");
            int mes = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Día: ");
            int dia = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Hora (0-23): ");
            int hora = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Minuto (0-59): ");
            int minuto = Integer.parseInt(sc.nextLine().trim());

            LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
            Turno turno = new Turno(paciente, medico, fechaHora);
            servicio.asignarTurno(turno);

        } catch (Exception e) {
            System.out.println("Error al procesar los datos del turno: " + e.getMessage());
        }
    }
    private static void listarTurnosDelDia(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("Año (ej. 2026): ");
            int anio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Mes (1-12): ");
            int mes = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Día: ");
            int dia = Integer.parseInt(sc.nextLine().trim());

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            List<Turno> turnos = servicio.listarTurnosDelDia(fecha);

            if (turnos.isEmpty()) {
                System.out.println("No hay turnos agendados para ese día.");
            } else {
                for (Turno t : turnos) {
                    System.out.println(t);
                }
            }
        } catch (Exception e) {
            System.out.println("Fecha inválida.");
        }
    }
    private static void cancelarTurno(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("ID del turno a cancelar: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            servicio.cancelarTurno(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
    private static void verTurnosPorMedico(ClinicaService servicio, Scanner sc) {
        System.out.print("Nombre del médico: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido del médico: ");
        String apellido = sc.nextLine();

        Medico med = servicio.buscarPorNombreApellido(nombre, apellido);
        if (med == null) {
            System.out.println("Médico no encontrado.");
            return;
        }
        List<Turno> turnos = servicio.buscarPorMedico(med);
        if (turnos.isEmpty()) {
            System.out.println("El médico no tiene turnos asignados.");
        } else {
            for (Turno t : turnos) {
                System.out.println(t);
            }
        }
    }
    private static void verTurnosPorPaciente(ClinicaService servicio, Scanner sc) {
        System.out.print("Cédula del paciente: ");
        String cedula = sc.nextLine();

        Paciente pac = servicio.buscarPorCedula(cedula);
        if (pac == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        List<Turno> turnos = servicio.buscarPorPaciente(pac);
        if (turnos.isEmpty()) {
            System.out.println("El paciente no tiene turnos asignados.");
        } else {
            for (Turno t : turnos) {
                System.out.println(t);
            }
        }
    }
    private static void cambiarEstadoTurno(ClinicaService servicio, Scanner sc) {
        try {
            System.out.print("ID del turno: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.println("Estados disponibles: PENDIENTE, ATENDIDO, CANCELADO");
            System.out.print("Nuevo estado: ");
            String estadoStr = sc.nextLine().trim().toUpperCase();

            EstadoTurno nuevoEstado = EstadoTurno.valueOf(estadoStr);
            servicio.cambiarEstadoTurno(id, nuevoEstado);
        } catch (IllegalArgumentException e) {
            System.out.println("ID o estado ingresado no válido.");
        }
    }
}