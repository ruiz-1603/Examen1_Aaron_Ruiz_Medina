package datos;

import datos.entity.ProyectoEntity;
import datos.entity.UsuarioEntity;
import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "proyectosData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProyectoConector {

    @XmlElementWrapper(name = "usuarios")
    @XmlElement(name = "usuario")
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    @XmlElementWrapper(name = "proyectos")
    @XmlElement(name = "proyecto")
    private List<ProyectoEntity> proyectos = new ArrayList<>();

    public ProyectoConector() {}

    public List<UsuarioEntity> getUsuarios() { return usuarios; }
    public void setUsuarios(List<UsuarioEntity> usuarios) { this.usuarios = usuarios; }

    public List<ProyectoEntity> getProyectos() { return proyectos; }
    public void setProyectos(List<ProyectoEntity> proyectos) { this.proyectos = proyectos; }
}
