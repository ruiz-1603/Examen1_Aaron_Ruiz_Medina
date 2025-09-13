package datos;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ProyectoDatos {

    private final Path xmlPath;
    private JAXBContext ctx;
    private ProyectoConector cache;

    public ProyectoDatos(String filePath) {
        try {
            this.xmlPath = Path.of(Objects.requireNonNull(filePath));
            System.out.println("Ruta del archivo XML: " + xmlPath.toAbsolutePath());

            this.ctx = JAXBContext.newInstance(
                    ProyectoConector.class,
                    ProyectoEntity.class,
                    TareaEntity.class,
                    UsuarioEntity.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Error inicializando: " + e.getMessage(), e);
        }
    }

    public synchronized ProyectoConector load() {
        try {
            if (cache != null) return cache;

            System.out.println("Cargando datos desde: " + xmlPath.toAbsolutePath());

            Unmarshaller u = ctx.createUnmarshaller();
            cache = (ProyectoConector) u.unmarshal(xmlPath.toFile());

            if (cache.getUsuarios() == null) cache.setUsuarios(new java.util.ArrayList<>());
            if (cache.getProyectos() == null) cache.setProyectos(new java.util.ArrayList<>());

            return cache;
        } catch (Exception e) {
            throw new RuntimeException("Error cargando XML: " + xmlPath + " - " + e.getMessage(), e);
        }
    }

    public synchronized void save(ProyectoConector data) {
        try {
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            File out = xmlPath.toFile();
            File parent = out.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean created = parent.mkdirs();
                System.out.println("Directorio padre creado: " + created);
            }

            m.marshal(data, out);
            cache = data;
            System.out.println("Datos guardados exitosamente en: " + xmlPath.toAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Error guardando XML: " + xmlPath + " - " + e.getMessage(), e);
        }
    }

    public Path getXmlPath() { return xmlPath; }
}