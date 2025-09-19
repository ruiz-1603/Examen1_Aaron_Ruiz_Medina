package logica;
import datos.entity.UsuarioEntity;
import model.Encargado;

public class UsuarioMapper {

    public static UsuarioEntity toEntity(Encargado usuario) {
        if (usuario == null) return null;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());

        return entity;
    }

    public static Encargado toModel(UsuarioEntity entity) {
        if (entity == null) return null;

        return new Encargado(entity.getId(), entity.getNombre(), entity.getEmail());
    }
}