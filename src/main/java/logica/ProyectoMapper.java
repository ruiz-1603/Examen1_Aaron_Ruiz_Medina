package logica;
import datos.ProyectoEntity;
import datos.TareaEntity;
import model.Proyecto;
import model.Tarea;
import model.Encargado;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProyectoMapper {

    public static ProyectoEntity toEntity(Proyecto proyecto) {
        if (proyecto == null) return null;

        ProyectoEntity entity = new ProyectoEntity();
        entity.setCodigo(proyecto.getCodigo());
        entity.setDescripcion(proyecto.getDescripcion());
        entity.setEncargadoGeneralId(proyecto.getEncargadoGeneral() != null ?
                proyecto.getEncargadoGeneral().getId() : 0);

        List<TareaEntity> tareasEntity = proyecto.getTareas().stream()
                .map(TareaMapper::toEntity)
                .collect(Collectors.toList());
        entity.setTareas(tareasEntity);

        return entity;
    }

    public static Proyecto toModel(ProyectoEntity entity, Encargado encargado, Map<Integer, Encargado> usuariosMap) {
        if (entity == null) return null;

        Proyecto proyecto = new Proyecto(entity.getCodigo(), entity.getDescripcion(), encargado);

        List<Tarea> tareas = entity.getTareas().stream()
                .map(tareaEntity -> {
                    Encargado responsable = usuariosMap.get(tareaEntity.getResponsableId());
                    return TareaMapper.toModel(tareaEntity, responsable);
                })
                .collect(Collectors.toList());

        proyecto.setTareas(tareas);

        return proyecto;
    }
}