package model;

import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private int codigo;
    private String descripcion;
    private Encargado encargadoGeneral;
    private List<Tarea> tareas;

    public Proyecto() {
        this.tareas = new ArrayList<>();
    }

    public Proyecto(int codigo, String descripcion, Encargado encargadoGeneral) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.encargadoGeneral = encargadoGeneral;
        this.tareas = new ArrayList<>();
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Encargado getEncargadoGeneral() { return encargadoGeneral; }
    public void setEncargadoGeneral(Encargado encargadoGeneral) { this.encargadoGeneral = encargadoGeneral; }

    public List<Tarea> getTareas() { return tareas; }
    public void setTareas(List<Tarea> tareas) { this.tareas = tareas; }

    public void agregarTarea(Tarea tarea) {
        this.tareas.add(tarea);
    }

    public void eliminarTarea(Tarea tarea) {
        this.tareas.remove(tarea);
    }
}