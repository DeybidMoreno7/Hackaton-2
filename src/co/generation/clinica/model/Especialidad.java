package co.generation.clinica.model;

public enum Especialidad {
    GENERAL(1),
    PEDIATRIA(2),
    CARDIOLOGIA(3),
    URGENCIAS(4);
    private final int codigo;
    Especialidad(int codigo) {
        this.codigo = codigo;
    }
    public int getCodigo() {
        return codigo;
    }
    public static Especialidad desdeCodigo(int codigo) {
        for (Especialidad e : values()) {
            if (e.getCodigo() == codigo) {
                return e;
            }
        }
        throw new IllegalArgumentException("Número de especialidad no válido: " + codigo);
    }
}