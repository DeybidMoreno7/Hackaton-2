import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    enum EstadoTurno { PENDIENTE, ATENDIDO, CANCELADO }
    enum Especialidad { GENERAL, PEDIATRIA, CARDIOLOGIA, URGENCIAS }

    interface Registrable {
        String getDatosRegistro();
        boolean esValido();
    }

    interface Consultable {
        List<Turno> listarTurnosDelDia(LocalDate fecha);
        List<Turno> buscarPorMedico(Medico medico);
        List<Turno> buscarPorPaciente(Paciente paciente);
    }

    static class Paciente implements Registrable {
        private int id;
        private String cedula;
        private String nombre;
        private String apellido;
        private String telefono;

        public Paciente(String cedula, String nombre, String apellido, String telefono) {
            setCedula(cedula);
            setNombre(nombre);
            setApellido(apellido);
            setTelefono(telefono);
        }

        public Paciente(int id, String cedula, String nombre, String apellido, String telefono) {
            this(cedula, nombre, apellido, telefono);
            this.id = id;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getCedula() { return cedula; }
        public void setCedula(String cedula) {
            if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía.");
            this.cedula = cedula.trim();
        }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) {
            if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
            this.nombre = nombre.trim();
        }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) {
            if (apellido == null || apellido.trim().isEmpty()) throw new IllegalArgumentException("El apellido no puede estar vacío.");
            this.apellido = apellido.trim();
        }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) {
            if (telefono == null || !telefono.trim().matches("^[0-9]{7,10}$")) {
                throw new IllegalArgumentException("El teléfono debe contener solo números (7 a 10 dígitos).");
            }
            this.telefono = telefono.trim();
        }

        @Override public String getDatosRegistro() { return toString(); }
        @Override public boolean esValido() {
            return cedula != null && !cedula.isEmpty() && nombre != null && !nombre.isEmpty() &&
                    apellido != null && !apellido.isEmpty() && telefono != null && telefono.matches("^[0-9]{7,10}$");
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Paciente paciente = (Paciente) o;
            return Objects.equals(cedula, paciente.cedula);
        }

        @Override public int hashCode() { return Objects.hash(cedula); }
        @Override public String toString() {
            return "ID: " + id + " | " + nombre + " " + apellido + " | CC: " + cedula + " | Tel: " + telefono;
        }
    }

    static class Medico implements Registrable {
        private int id;
        private String nombre;
        private String apellido;
        private Especialidad especialidad;

        public Medico(String nombre, String apellido, Especialidad especialidad) {
            setNombre(nombre);
            setApellido(apellido);
            setEspecialidad(especialidad);
        }

        public Medico(int id, String nombre, String apellido, Especialidad especialidad) {
            this(nombre, apellido, especialidad);
            this.id = id;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) {
            if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
            this.nombre = nombre.trim();
        }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) {
            if (apellido == null || apellido.trim().isEmpty()) throw new IllegalArgumentException("El apellido no puede estar vacío.");
            this.apellido = apellido.trim();
        }
        public Especialidad getEspecialidad() { return especialidad; }
        public void setEspecialidad(Especialidad especialidad) {
            if (especialidad == null) throw new IllegalArgumentException("La especialidad no puede ser nula.");
            this.especialidad = especialidad;
        }

        @Override public String getDatosRegistro() { return toString(); }
        @Override public boolean esValido() {
            return nombre != null && !nombre.isEmpty() && apellido != null && !apellido.isEmpty() && especialidad != null;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Medico medico = (Medico) o;
            return nombre.equalsIgnoreCase(medico.nombre) && apellido.equalsIgnoreCase(medico.apellido);
        }

        @Override public int hashCode() { return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase()); }
        @Override public String toString() {
            return "ID: " + id + " | Dr(a). " + nombre + " " + apellido + " | Esp: " + especialidad;
        }
    }

    static class Turno {
        private int id;
        private Paciente paciente;
        private Medico medico;
        private LocalDateTime fechaHora;
        private EstadoTurno estado;

        public Turno(Paciente paciente, Medico medico, LocalDateTime fechaHora) {
            setPaciente(paciente);
            setMedico(medico);
            setFechaHora(fechaHora);
            this.estado = EstadoTurno.PENDIENTE;
        }

        public Turno(int id, Paciente paciente, Medico medico, LocalDateTime fechaHora, EstadoTurno estado) {
            this(paciente, medico, fechaHora);
            this.id = id;
            this.estado = estado;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public Paciente getPaciente() { return paciente; }
        public void setPaciente(Paciente paciente) {
            if (paciente == null) throw new IllegalArgumentException("El paciente no puede ser nulo.");
            this.paciente = paciente;
        }
        public Medico getMedico() { return medico; }
        public void setMedico(Medico medico) {
            if (medico == null) throw new IllegalArgumentException("El médico no puede ser nulo.");
            this.medico = medico;
        }
        public LocalDateTime getFechaHora() { return fechaHora; }
        public void setFechaHora(LocalDateTime fechaHora) {
            if (fechaHora == null) throw new IllegalArgumentException("La fecha u hora no puede ser nula.");
            this.fechaHora = fechaHora;
        }
        public EstadoTurno getEstado() { return estado; }
        public void setEstado(EstadoTurno estado) {
            if (estado == null) throw new IllegalArgumentException("El estado no puede ser nulo.");
            this.estado = estado;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Turno turno = (Turno) o;
            return Objects.equals(medico, turno.medico) && Objects.equals(fechaHora, turno.fechaHora);
        }

        @Override public int hashCode() { return Objects.hash(medico, fechaHora); }
        @Override public String toString() {
            return "Turno #" + id + " [" + estado + "] " + fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                    " | Paciente: " + paciente.getNombre() + " " + paciente.getApellido() +
                    " | Medico: Dr. " + medico.getNombre() + " " + medico.getApellido();
        }
    }

    static class ClinicaService implements Consultable {
        private final List<Paciente> pacientes = new ArrayList<>();
        private final List<Medico> medicos = new ArrayList<>();
        private final List<Turno> turnos = new ArrayList<>();

        public List<Paciente> getPacientes() { return pacientes; }
        public List<Medico> getMedicos() { return medicos; }
        public List<Turno> getTurnos() { return turnos; }

        public void registrarPaciente(Paciente p) {
            if (!p.esValido()) {
                System.out.println("-> Error: Datos del paciente no válidos.");
                return;
            }
            if (pacientes.contains(p)) {
                System.out.println("-> Error: Ya existe un paciente con la cédula " + p.getCedula());
                return;
            }
            p.setId(pacientes.stream().mapToInt(Paciente::getId).max().orElse(0) + 1);
            pacientes.add(p);
            System.out.println("-> Paciente registrado con éxito.");
        }

        public Paciente buscarPorCedula(String cedula) {
            for (Paciente p : pacientes) {
                if (p.getCedula().equals(cedula)) return p;
            }
            return null;
        }

        public void registrarMedico(Medico m) {
            if (!m.esValido()) {
                System.out.println("-> Error: Datos del médico no válidos.");
                return;
            }
            if (medicos.contains(m)) {
                System.out.println("-> Error: Ya existe un médico registrado con ese nombre y apellido.");
                return;
            }
            m.setId(medicos.stream().mapToInt(Medico::getId).max().orElse(0) + 1);
            medicos.add(m);
            System.out.println("-> Médico registrado con éxito.");
        }

        public Medico buscarPorNombreApellido(String nombre, String apellido) {
            for (Medico m : medicos) {
                if (m.getNombre().equalsIgnoreCase(nombre) && m.getApellido().equalsIgnoreCase(apellido)) return m;
            }
            return null;
        }

        public void asignarTurno(Turno t) {
            if (turnos.contains(t)) {
                System.out.println("-> Error: El médico ya tiene un turno agendado en esa fecha y hora.");
                return;
            }
            t.setId(turnos.stream().mapToInt(Turno::getId).max().orElse(0) + 1);
            turnos.add(t);
            System.out.println("-> Turno asignado con éxito.");
        }

        public void cancelarTurno(int idTurno) {
            Turno t = buscarTurnoPorId(idTurno);
            if (t == null) {
                System.out.println("-> Turno no encontrado.");
                return;
            }
            if (t.getEstado() == EstadoTurno.ATENDIDO || t.getEstado() == EstadoTurno.CANCELADO) {
                System.out.println("-> El turno no se puede cancelar porque ya está " + t.getEstado());
                return;
            }
            t.setEstado(EstadoTurno.CANCELADO);
            System.out.println("-> Turno #" + idTurno + " cancelado.");
        }

        public void cambiarEstadoTurno(int idTurno, EstadoTurno nuevoEstado) {
            Turno t = buscarTurnoPorId(idTurno);
            if (t == null) {
                System.out.println("-> Turno no encontrado.");
                return;
            }
            t.setEstado(nuevoEstado);
            System.out.println("-> Estado del turno cambiado a " + nuevoEstado);
        }

        private Turno buscarTurnoPorId(int id) {
            for (Turno t : turnos) {
                if (t.getId() == id) return t;
            }
            return null;
        }

        @Override public List<Turno> listarTurnosDelDia(LocalDate fecha) {
            List<Turno> resultado = new ArrayList<>();
            for (Turno t : turnos) {
                if (t.getFechaHora().toLocalDate().equals(fecha)) resultado.add(t);
            }
            resultado.sort(Comparator.comparing(Turno::getFechaHora));
            return resultado;
        }

        @Override public List<Turno> buscarPorMedico(Medico medico) {
            List<Turno> resultado = new ArrayList<>();
            for (Turno t : turnos) {
                if (t.getMedico().equals(medico)) resultado.add(t);
            }
            return resultado;
        }

        @Override public List<Turno> buscarPorPaciente(Paciente paciente) {
            List<Turno> resultado = new ArrayList<>();
            for (Turno t : turnos) {
                if (t.getPaciente().equals(paciente)) resultado.add(t);
            }
            return resultado;
        }
    }
    static class DatosCSV {
        private static final String DIR = "datos/";
        private static final String F_PACIENTES = DIR + "pacientes.csv";
        private static final String F_MEDICOS = DIR + "medicos.csv";
        private static final String F_TURNOS = DIR + "turnos.csv";
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public static void cargar(ClinicaService servicio) {
            new File(DIR).mkdirs();
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
                    if (linea.isBlank()) continue;
                    String[] p = linea.split(",", -1);
                    servicio.getPacientes().add(new Paciente(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
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
                    if (linea.isBlank()) continue;
                    String[] p = linea.split(",", -1);
                    servicio.getMedicos().add(new Medico(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), Especialidad.valueOf(p[3].trim())));
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
                    if (linea.isBlank()) continue;
                    String[] p = linea.split(",", -1);
                    Paciente pac = servicio.buscarPorCedula(p[1].trim());
                    Medico med = servicio.buscarPorNombreApellido(p[2].trim(), p[3].trim());
                    if (pac == null || med == null) continue;
                    servicio.getTurnos().add(new Turno(Integer.parseInt(p[0].trim()), pac, med, LocalDateTime.parse(p[4].trim(), FMT), EstadoTurno.valueOf(p[5].trim())));
                }
            } catch (Exception e) {
                System.out.println("Error al cargar turnos: " + e.getMessage());
            }
        }

        public static void guardar(ClinicaService servicio) {
            new File(DIR).mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(F_PACIENTES))) {
                for (Paciente p : servicio.getPacientes()) {
                    pw.println(p.getId() + "," + p.getCedula() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getTelefono());
                }
            } catch (IOException e) { System.out.println("Error al guardar pacientes: " + e.getMessage()); }

            try (PrintWriter pw = new PrintWriter(new FileWriter(F_MEDICOS))) {
                for (Medico m : servicio.getMedicos()) {
                    pw.println(m.getId() + "," + m.getNombre() + "," + m.getApellido() + "," + m.getEspecialidad());
                }
            } catch (IOException e) { System.out.println("Error al guardar médicos: " + e.getMessage()); }

            try (PrintWriter pw = new PrintWriter(new FileWriter(F_TURNOS))) {
                for (Turno t : servicio.getTurnos()) {
                    pw.println(t.getId() + "," + t.getPaciente().getCedula() + "," + t.getMedico().getNombre() + "," +
                            t.getMedico().getApellido() + "," + t.getFechaHora().format(FMT) + "," + t.getEstado());
                }
            } catch (IOException e) { System.out.println("Error al guardar turnos: " + e.getMessage()); }
        }
    }

    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();
        DatosCSV.cargar(servicio);

        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            String opcion = sc.nextLine().trim();
            System.out.println();

            switch (opcion) {
                case "1":
                    registrarPaciente(servicio, sc);
                    break;
                case "2":
                    registrarMedico(servicio, sc);
                    break;
                case "3":
                    asignarTurno(servicio, sc);
                    break;
                case "4":
                    listarTurnosDelDia(servicio, sc);
                    break;
                case "5":
                    cancelarTurno(servicio, sc);
                    break;
                case "6":
                    verTurnosPorMedico(servicio, sc);
                    break;
                case "7":
                    verTurnosPorPaciente(servicio, sc);
                    break;
                case "8":
                    cambiarEstadoTurno(servicio, sc);
                    break;
                case "9":
                    listarPacientes(servicio);
                    break;
                case "10":
                    listarMedicos(servicio);
                    break;
                case "0":
                    DatosCSV.guardar(servicio);
                    System.out.println("¡Datos guardados exitosamente! Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida. Intente nuevamente.");
            }
            System.out.println();
        }
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("==========================================");
        System.out.println("||            CLINICAAPP - MENÚ         ||");
        System.out.println("==========================================");
        System.out.println(" 1. Registrar paciente");
        System.out.println(" 2. Registrar médico");
        System.out.println(" 3. Asignar turno");
        System.out.println(" 4. Listar turnos del día");
        System.out.println(" 5. Cancelar turno");
        System.out.println(" 6. Ver turnos por médico");
        System.out.println(" 7. Ver turnos por paciente");
        System.out.println(" 8. Cambiar estado de turno");
        System.out.println(" 9. Listar pacientes");
        System.out.println("10. Listar médicos");
        System.out.println(" 0. Salir y Guardar");
        System.out.println("==========================================");
    }

    private static void registrarPaciente(ClinicaService servicio, Scanner sc) {
        System.out.println("--- REGISTRAR PACIENTE ---");
        try {
            System.out.print("Cédula: "); String cedula = sc.nextLine();
            System.out.print("Nombre: "); String nombre = sc.nextLine();
            System.out.print("Apellido: "); String apellido = sc.nextLine();
            System.out.print("Teléfono (7 a 10 dígitos): "); String telefono = sc.nextLine();

            servicio.registrarPaciente(new Paciente(cedula, nombre, apellido, telefono));
        } catch (IllegalArgumentException e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    private static void registrarMedico(ClinicaService servicio, Scanner sc) {
        System.out.println("--- REGISTRAR MÉDICO ---");
        try {
            System.out.print("Nombre: "); String nombre = sc.nextLine();
            System.out.print("Apellido: "); String apellido = sc.nextLine();

            System.out.println("Especialidades: 1.GENERAL | 2.PEDIATRIA | 3.CARDIOLOGIA | 4.URGENCIAS");
            System.out.print("Seleccione especialidad (1-4): ");
            int opEsp = Integer.parseInt(sc.nextLine().trim());

            Especialidad[] espValores = Especialidad.values();
            if (opEsp < 1 || opEsp > espValores.length) {
                System.out.println("Opción de especialidad no válida.");
                return;
            }

            servicio.registrarMedico(new Medico(nombre, apellido, espValores[opEsp - 1]));
        } catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    private static void asignarTurno(ClinicaService servicio, Scanner sc) {
        System.out.println("--- ASIGNAR TURNO ---");
        try {
            System.out.print("Cédula del paciente: ");
            Paciente pac = servicio.buscarPorCedula(sc.nextLine().trim());
            if (pac == null) { System.out.println("Error: Paciente no encontrado."); return; }

            System.out.print("Nombre del médico: "); String nombreMed = sc.nextLine().trim();
            System.out.print("Apellido del médico: "); String apellidoMed = sc.nextLine().trim();

            Medico med = servicio.buscarPorNombreApellido(nombreMed, apellidoMed);
            if (med == null) { System.out.println("Error: Médico no encontrado."); return; }

            System.out.print("Año (Ej: 2026): "); int anio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Mes (1-12): "); int mes = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Día (1-31): "); int dia = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Hora (0-23): "); int hora = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Minuto (0-59): "); int minuto = Integer.parseInt(sc.nextLine().trim());

            LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
            servicio.asignarTurno(new Turno(pac, med, fechaHora));
        } catch (DateTimeParseException e) {
            System.out.println("Error: Fecha u hora ingresada no es válida.");
        } catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    private static void listarTurnosDelDia(ClinicaService servicio, Scanner sc) {
        System.out.println("--- LISTAR TURNOS DEL DÍA ---");
        try {
            System.out.print("Año: "); int anio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Mes (1-12): "); int mes = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Día (1-31): "); int dia = Integer.parseInt(sc.nextLine().trim());

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            List<Turno> turnos = servicio.listarTurnosDelDia(fecha);

            if (turnos.isEmpty()) System.out.println("No hay turnos agendados para " + fecha);
            else turnos.forEach(t -> System.out.println(" - " + t));
        } catch (Exception e) {
            System.out.println("Error en la fecha ingresada.");
        }
    }

    private static void cancelarTurno(ClinicaService servicio, Scanner sc) {
        System.out.println("--- CANCELAR TURNO ---");
        try {
            System.out.print("ID del turno: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            servicio.cancelarTurno(id);
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un ID numérico.");
        }
    }

    private static void verTurnosPorMedico(ClinicaService servicio, Scanner sc) {
        System.out.println("--- VER TURNOS POR MÉDICO ---");
        System.out.print("Nombre del médico: "); String nombre = sc.nextLine().trim();
        System.out.print("Apellido del médico: "); String apellido = sc.nextLine().trim();

        Medico med = servicio.buscarPorNombreApellido(nombre, apellido);
        if (med == null) { System.out.println("Médico no encontrado."); return; }

        List<Turno> turnos = servicio.buscarPorMedico(med);
        if (turnos.isEmpty()) System.out.println("No hay turnos asignados a este médico.");
        else turnos.forEach(t -> System.out.println(" - " + t));
    }

    private static void verTurnosPorPaciente(ClinicaService servicio, Scanner sc) {
        System.out.println("--- VER TURNOS POR PACIENTE ---");
        System.out.print("Cédula del paciente: ");
        Paciente pac = servicio.buscarPorCedula(sc.nextLine().trim());
        if (pac == null) { System.out.println("Paciente no encontrado."); return; }

        List<Turno> turnos = servicio.buscarPorPaciente(pac);
        if (turnos.isEmpty()) System.out.println("No hay turnos registrados para este paciente.");
        else turnos.forEach(t -> System.out.println(" - " + t));
    }

    private static void cambiarEstadoTurno(ClinicaService servicio, Scanner sc) {
        System.out.println("--- CAMBIAR ESTADO DE TURNO ---");
        try {
            System.out.print("ID del turno: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.println("Estados: 1.PENDIENTE | 2.ATENDIDO | 3.CANCELADO");
            System.out.print("Seleccione estado (1-3): ");
            int op = Integer.parseInt(sc.nextLine().trim());

            EstadoTurno[] estados = EstadoTurno.values();
            if (op < 1 || op > estados.length) { System.out.println("Opción no válida."); return; }

            servicio.cambiarEstadoTurno(id, estados[op - 1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numérico válido.");
        }
    }

    private static void listarPacientes(ClinicaService servicio) {
        System.out.println("--- PACIENTES REGISTRADOS ---");
        if (servicio.getPacientes().isEmpty()) System.out.println("No hay pacientes en el sistema.");
        else servicio.getPacientes().forEach(p -> System.out.println(" - " + p));
    }

    private static void listarMedicos(ClinicaService servicio) {
        System.out.println("--- MÉDICOS REGISTRADOS ---");
        if (servicio.getMedicos().isEmpty()) System.out.println("No hay médicos en el sistema.");
        else servicio.getMedicos().forEach(m -> System.out.println(" - " + m));
    }
}