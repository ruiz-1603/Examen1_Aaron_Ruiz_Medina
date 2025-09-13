package datos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class TareaEntity {
    private int numero;
    private String descripcion;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaFinalizacionEsperada;

    private String prioridad;
    private String estado;
    private int responsableId;

    public TareaEntity() {}

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaFinalizacionEsperada() { return fechaFinalizacionEsperada; }
    public void setFechaFinalizacionEsperada(LocalDate fechaFinalizacionEsperada) {
        this.fechaFinalizacionEsperada = fechaFinalizacionEsperada;
    }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getResponsableId() { return responsableId; }
    public void setResponsableId(int responsableId) { this.responsableId = responsableId; }
}