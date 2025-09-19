package datos.entity;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ProyectoEntity {
    private int codigo;
    private String descripcion;
    private int encargadoGeneralId;

    @XmlElementWrapper(name = "tareas")
    @XmlElement(name = "tarea")
    private List<TareaEntity> tareas = new ArrayList<>();

    public ProyectoEntity() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getEncargadoGeneralId() { return encargadoGeneralId; }
    public void setEncargadoGeneralId(int encargadoGeneralId) { this.encargadoGeneralId = encargadoGeneralId; }

    public List<TareaEntity> getTareas() { return tareas; }
    public void setTareas(List<TareaEntity> tareas) { this.tareas = tareas; }
}
