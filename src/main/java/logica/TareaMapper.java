package logica;
import datos.TareaEntity;
import model.Tarea;
import model.Encargado;

public class TareaMapper {

    public static TareaEntity toEntity(Tarea tarea) {
        if (tarea == null) return null;

        TareaEntity entity = new TareaEntity();
        entity.setNumero(tarea.getNumero());
        entity.setDescripcion(tarea.getDescripcion());
        entity.setFechaFinalizacionEsperada(tarea.getFechaFinalizacionEsperada());
        entity.setPrioridad(tarea.getPrioridad() != null ? tarea.getPrioridad().name() : null);
        entity.setEstado(tarea.getEstado() != null ? tarea.getEstado().name() : null);
        entity.setResponsableId(tarea.getResponsable() != null ? tarea.getResponsable().getId() : 0);

        return entity;
    }

    public static Tarea toModel(TareaEntity entity, Encargado responsable) {
        if (entity == null) return null;

        Tarea.Prioridad prioridad = null;
        if (entity.getPrioridad() != null) {
            prioridad = Tarea.Prioridad.valueOf(entity.getPrioridad());
        }

        Tarea.Estado estado = null;
        if (entity.getEstado() != null) {
            estado = Tarea.Estado.valueOf(entity.getEstado());
        }

        return new Tarea(
                entity.getNumero(),
                entity.getDescripcion(),
                entity.getFechaFinalizacionEsperada(),
                prioridad,
                estado,
                responsable
        );
    }
}