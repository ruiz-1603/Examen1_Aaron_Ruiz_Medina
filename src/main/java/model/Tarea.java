package model;


import java.time.LocalDate;

public class Tarea {
    public enum Prioridad { ALTA, MEDIA, BAJA }
    public enum Estado { ABIERTA, EN_PROGRESO, EN_REVISION, RESUELTA }

    private int numero;
    private String descripcion;
    private LocalDate fechaFinalizacionEsperada;
    private Prioridad prioridad;
    private Estado estado;
    private Usuario responsable;

    public Tarea() {}

    public Tarea(int numero, String descripcion, LocalDate fechaFinalizacionEsperada,
                 Prioridad prioridad, Estado estado, Usuario responsable) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.fechaFinalizacionEsperada = fechaFinalizacionEsperada;
        this.prioridad = prioridad;
        this.estado = estado;
        this.responsable = responsable;
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaFinalizacionEsperada() { return fechaFinalizacionEsperada; }
    public void setFechaFinalizacionEsperada(LocalDate fechaFinalizacionEsperada) {
        this.fechaFinalizacionEsperada = fechaFinalizacionEsperada;
    }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Usuario getResponsable() { return responsable; }
    public void setResponsable(Usuario responsable) { this.responsable = responsable; }
}