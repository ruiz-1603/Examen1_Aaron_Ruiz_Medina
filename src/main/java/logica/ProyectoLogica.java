package logica;
import datos.ProyectoConector;
import datos.ProyectoDatos;
import datos.ProyectoEntity;
import datos.UsuarioEntity;
import model.Proyecto;
import model.Tarea;
import model.Usuario;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProyectoLogica {

    private final ProyectoDatos store;

    public ProyectoLogica(String rutaArchivo) {
        this.store = new ProyectoDatos(rutaArchivo);
    }


    public List<Usuario> findAllUsuarios() {
        ProyectoConector data = store.load();
        return data.getUsuarios().stream()
                .map(UsuarioMapper::toModel)
                .collect(Collectors.toList());
    }

    public Optional<Usuario> findUsuarioById(int id) {
        ProyectoConector data = store.load();
        return data.getUsuarios().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .map(UsuarioMapper::toModel);
    }


    public List<Proyecto> findAllProyectos() {
        ProyectoConector data = store.load();
        Map<Integer, Usuario> usuariosMap = createUsuariosMap(data);

        return data.getProyectos().stream()
                .map(proyectoEntity -> {
                    Usuario encargado = usuariosMap.get(proyectoEntity.getEncargadoGeneralId());
                    return ProyectoMapper.toModel(proyectoEntity, encargado, usuariosMap);
                })
                .collect(Collectors.toList());
    }

    public Optional<Proyecto> findProyectoByCodigo(int codigo) {
        ProyectoConector data = store.load();
        Map<Integer, Usuario> usuariosMap = createUsuariosMap(data);

        return data.getProyectos().stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .map(proyectoEntity -> {
                    Usuario encargado = usuariosMap.get(proyectoEntity.getEncargadoGeneralId());
                    return ProyectoMapper.toModel(proyectoEntity, encargado, usuariosMap);
                });
    }

    public Proyecto createProyecto(Proyecto nuevo) {
        validarProyecto(nuevo);

        ProyectoConector data = store.load();

        if (nuevo.getCodigo() <= 0) {
            nuevo.setCodigo(generarSiguienteCodigoProyecto(data));
        } else {
            boolean codigoTomado = data.getProyectos().stream()
                    .anyMatch(p -> p.getCodigo() == nuevo.getCodigo());
            if (codigoTomado) {
                throw new IllegalArgumentException("Ya existe un proyecto con código: " + nuevo.getCodigo());
            }
        }

        ProyectoEntity entity = ProyectoMapper.toEntity(nuevo);
        data.getProyectos().add(entity);
        store.save(data);

        return nuevo;
    }

    public Proyecto updateProyecto(Proyecto proyecto) {
        if (proyecto == null || proyecto.getCodigo() <= 0) {
            throw new IllegalArgumentException("El proyecto a actualizar requiere un código válido.");
        }
        validarProyecto(proyecto);

        ProyectoConector data = store.load();

        for (int i = 0; i < data.getProyectos().size(); i++) {
            ProyectoEntity actual = data.getProyectos().get(i);
            if (actual.getCodigo() == proyecto.getCodigo()) {
                data.getProyectos().set(i, ProyectoMapper.toEntity(proyecto));
                store.save(data);
                return proyecto;
            }
        }

        throw new NoSuchElementException("No existe proyecto con código: " + proyecto.getCodigo());
    }


    public Tarea createTarea(int codigoProyecto, Tarea nuevaTarea) {
        validarTarea(nuevaTarea);

        ProyectoConector data = store.load();

        ProyectoEntity proyectoEntity = data.getProyectos().stream()
                .filter(p -> p.getCodigo() == codigoProyecto)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No existe proyecto con código: " + codigoProyecto));

        if (nuevaTarea.getNumero() <= 0) {
            int siguienteNumero = proyectoEntity.getTareas().stream()
                    .mapToInt(t -> t.getNumero())
                    .max()
                    .orElse(0) + 1;
            nuevaTarea.setNumero(siguienteNumero);
        } else {
            boolean numeroTomado = proyectoEntity.getTareas().stream()
                    .anyMatch(t -> t.getNumero() == nuevaTarea.getNumero());
            if (numeroTomado) {
                throw new IllegalArgumentException("Ya existe una tarea con número: " + nuevaTarea.getNumero());
            }
        }

        proyectoEntity.getTareas().add(TareaMapper.toEntity(nuevaTarea));
        store.save(data);

        return nuevaTarea;
    }

    public Tarea updateTarea(int codigoProyecto, Tarea tarea) {
        if (tarea == null || tarea.getNumero() <= 0) {
            throw new IllegalArgumentException("La tarea a actualizar requiere un número válido.");
        }
        validarTarea(tarea);

        ProyectoConector data = store.load();

        // Buscar el proyecto
        ProyectoEntity proyectoEntity = data.getProyectos().stream()
                .filter(p -> p.getCodigo() == codigoProyecto)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No existe proyecto con código: " + codigoProyecto));

        // Actualizar la tarea
        for (int i = 0; i < proyectoEntity.getTareas().size(); i++) {
            if (proyectoEntity.getTareas().get(i).getNumero() == tarea.getNumero()) {
                proyectoEntity.getTareas().set(i, TareaMapper.toEntity(tarea));
                store.save(data);
                return tarea;
            }
        }

        throw new NoSuchElementException("No existe tarea con número: " + tarea.getNumero() +
                " en el proyecto: " + codigoProyecto);
    }


    private Map<Integer, Usuario> createUsuariosMap(ProyectoConector data) {
        return data.getUsuarios().stream()
                .collect(Collectors.toMap(
                        UsuarioEntity::getId,
                        UsuarioMapper::toModel
                ));
    }

    private void validarProyecto(Proyecto proyecto) {
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto nulo.");
        }
        if (proyecto.getDescripcion() == null || proyecto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del proyecto es obligatoria.");
        }
        if (proyecto.getEncargadoGeneral() == null) {
            throw new IllegalArgumentException("El encargado general es obligatorio.");
        }
    }

    private void validarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("Tarea nula.");
        }
        if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea es obligatoria.");
        }
        if (tarea.getFechaFinalizacionEsperada() == null) {
            throw new IllegalArgumentException("La fecha de finalización esperada es obligatoria.");
        }
        if (tarea.getFechaFinalizacionEsperada().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser pasada.");
        }
        if (tarea.getPrioridad() == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }
        if (tarea.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        if (tarea.getResponsable() == null) {
            throw new IllegalArgumentException("El responsable es obligatorio.");
        }
    }

    private int generarSiguienteCodigoProyecto(ProyectoConector data) {
        return data.getProyectos().stream()
                .mapToInt(ProyectoEntity::getCodigo)
                .max()
                .orElse(0) + 1;
    }
}