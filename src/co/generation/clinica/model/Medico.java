package co.generation.clinica.model;

import co.generation.clinica.interfaces.Registrable;

public class Medico implements Registrable {

    private int id;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;

    public Medico(int id, String nombre, String apellido, Especialidad especialidad) {
        this.id = id;
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public Medico(String nombre, String apellido, Especialidad especialidad) {
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if(nombre==null||nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {

        if(apellido==null||apellido.trim().isEmpty()){
            throw new IllegalArgumentException("El apellido no puede ser nulo ni vacío.");
        }
        this.apellido = apellido.trim();
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        if(nombre==null){
            throw new IllegalArgumentException("La especialidad no puede ser nula.");
        }
        this.especialidad = especialidad;
    }

    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if (obj==null||getClass() != obj.getClass()){
            return false;
        }
        Medico medico = (Medico) obj;
        return this.nombre.equalsIgnoreCase(medico.nombre) && this.apellido.equalsIgnoreCase(medico.apellido);
    }

    public String toString(){
        return "Dr. "+nombre+" "+apellido+" - "+especialidad;
    }

    @Override
    public String getDatosRegistro() {
        return this.toString();
    }

    @Override
    public boolean esValido() {
        if(this.nombre==null || this.nombre.trim().isEmpty()){
            return false;
        }
        if(this.apellido==null || this.apellido.trim().isEmpty()){
            return false;
        }
        if(this.especialidad==null){
            return false;
        }
        return true;
    }
}
