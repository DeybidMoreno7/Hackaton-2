package co.generation.clinica.model;

public enum EstadoTurno {
    PENDIENTE(1),
    ATENDIDO(2),
    CANCELADO(3);
    private final int codigo;
    EstadoTurno(int codigo) {
        this.codigo = codigo;
    }
    public int getCodigo() {
        return codigo;
    }
    public static EstadoTurno desdeCodigo(int codigo) {
        for (EstadoTurno e : values()) {
            if (e.getCodigo() == codigo) {
                return e;
            }
        }
        throw new IllegalArgumentException("Número de estado no válido: " + codigo);
    }
}