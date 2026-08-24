package co.generation.clinica.model;

import java.util.Objects;

public class Paciente {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese una cédula válida, por favor");
        }
        this.cedula = cedula.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido(){
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un apellido válido");
        }
        this.apellido = apellido.trim();
    }

    public String getTelefono() {
        return telefono;
}
public void setTelefono (String telefono){
        if (telefono == null || !telefono.matches ("^[0-9]{7,10}$")){
            throw new IllegalArgumentException ("El número ingresado debe contener solo digitos numericos, max. 10, min . ");
        }
        this.telefono = telefono.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(cedula, paciente.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + cedula + " - " + telefono;
    }
}