package logica;
import datos.UsuarioEntity;
import model.Usuario;

public class UsuarioMapper {

    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());

        return entity;
    }

    public static Usuario toModel(UsuarioEntity entity) {
        if (entity == null) return null;

        return new Usuario(entity.getId(), entity.getNombre(), entity.getEmail());
    }
}