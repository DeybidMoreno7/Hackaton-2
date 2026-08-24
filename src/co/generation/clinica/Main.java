public class Main {

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=============================================");
            System.out.println("■           CLINICAAPP - MENÚ               ■");
            System.out.println("=============================================");
            System.out.println("■  1. Registrar paciente                    ■");
            System.out.println("■  2. Registrar médico                      ■");
            System.out.println("■  3. Asignar turno                         ■");
            System.out.println("■  4. Listar turnos del día                 ■");
            System.out.println("■  5. Cancelar turno                        ■");
            System.out.println("■  6. Ver turnos por médico                 ■");
            System.out.println("■  7. Ver turnos por paciente               ■");
            System.out.println("■  8. Cambiar estado de turno               ■");
            System.out.println("■  9. Listar pacientes                      ■");
            System.out.println("■ 10. Listar médicos                        ■");
            System.out.println("■  0. Salir                                 ■");
            System.out.println("=============================================");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    System.out.println("--> Opción 1: Registrar paciente");
                    break;
                case 2:
                    System.out.println("--> Opción 2: Registrar médico");
                    break;
                case 3:
                    System.out.println("--> Opción 3: Asignar turno");
                    break;
                case 4:
                    System.out.println("-> Opción 4: Listar turnos del día");
                    break;
                case 5:
                    System.out.println("-> Opción 5: Cancelar turno");
                    break;
                case 6:
                    System.out.println("-> Opción 6: Ver turnos por médico");
                    break;
                case 7:
                    System.out.println("-> Opción 7: Ver turnos por paciente");
                    break;
                case 8:
                    System.out.println("-> Opción 8: Cambiar estado de turno");
                    break;
                case 9:
                    System.out.println("-> Opción 9: Listar pacientes");
                    break;
                case 10:
                    System.out.println("-> Opción 10: Listar médicos");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        sc.close();
    }
}