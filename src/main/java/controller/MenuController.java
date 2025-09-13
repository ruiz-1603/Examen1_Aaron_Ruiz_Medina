package controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logica.ProyectoLogica;
import model.Proyecto;
import model.Tarea;
import model.Usuario;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    // FXML Components - Proyectos
    @FXML private ChoiceBox<Usuario> btnEncargado;
    @FXML private Button btnCrearProyecto;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<ProyectoTableData> tblProyectos;
    @FXML private TableColumn<ProyectoTableData, Integer> colCodigo;
    @FXML private TableColumn<ProyectoTableData, String> colDescripcion;
    @FXML private TableColumn<ProyectoTableData, String> colEncargado;
    @FXML private TableColumn<ProyectoTableData, Integer> colNumeroTareas;

    // FXML Components - Tareas
    @FXML private Button btnCrearTarea;
    @FXML private Button btnEditar;
    @FXML private TextField txtDescripcion1; // txtDescripcionTarea
    @FXML private DatePicker dtpVencimiento;
    @FXML private ChoiceBox<Tarea.Prioridad> btnEncargado21; // btnPrioridad
    @FXML private ChoiceBox<Tarea.Estado> btnEncargado2; // btnEstado
    @FXML private ChoiceBox<Usuario> btnEncargado1; // btnResponsable
    @FXML private TableView<TareaTableData> tblTareas;
    @FXML private TableColumn<TareaTableData, Integer> colCodigo1; // colNumeroTarea
    @FXML private TableColumn<TareaTableData, String> colDescripcion1; // colDescripcionTarea
    @FXML private TableColumn<TareaTableData, String> colEncargado1; // colVencimiento
    @FXML private TableColumn<TareaTableData, String> colNumeroTareas1; // colPrioridad
    @FXML private TableColumn<TareaTableData, String> colEstado;
    @FXML private TableColumn<TareaTableData, String> colEncargadoTarea;

    // Lógica de negocio
    private ProyectoLogica proyectoLogica;
    private ObservableList<ProyectoTableData> proyectos = FXCollections.observableArrayList();
    private ObservableList<TareaTableData> tareas = FXCollections.observableArrayList();
    private List<Usuario> usuarios;
    private ProyectoTableData proyectoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            proyectoLogica = new ProyectoLogica("src/main/resources/datos-prueba.xml");

            // Cargar usuarios
            usuarios = proyectoLogica.findAllUsuarios();

            setupTableColumns();
            setupChoiceBoxes();
            loadData();

            // Configurar listeners
            tblProyectos.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            proyectoSeleccionado = newSelection;
                            loadTareasForProyecto(newSelection.getCodigo());
                        }
                    });

        } catch (Exception e) {
            showAlert("Error", "No se pudo inicializar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<>("encargado"));
        colNumeroTareas.setCellValueFactory(new PropertyValueFactory<>("numeroTareas"));


        colCodigo1.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDescripcion1.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEncargado1.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        colNumeroTareas1.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEncargadoTarea.setCellValueFactory(new PropertyValueFactory<>("responsable"));

        tblProyectos.setItems(proyectos);
        tblTareas.setItems(tareas);
    }

    private void setupChoiceBoxes() {
        btnEncargado.setItems(FXCollections.observableArrayList(usuarios));

        btnEncargado1.setItems(FXCollections.observableArrayList(usuarios));

        btnEncargado21.setItems(FXCollections.observableArrayList(Tarea.Prioridad.values()));

        btnEncargado2.setItems(FXCollections.observableArrayList(Tarea.Estado.values()));
    }

    private void loadData() {
        try {
            List<Proyecto> listaProyectos = proyectoLogica.findAllProyectos();
            proyectos.clear();

            for (Proyecto proyecto : listaProyectos) {
                ProyectoTableData data = new ProyectoTableData(
                        proyecto.getCodigo(),
                        proyecto.getDescripcion(),
                        proyecto.getEncargadoGeneral().getNombre(),
                        proyecto.getTareas().size()
                );
                proyectos.add(data);
            }

        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTareasForProyecto(int codigoProyecto) {
        try {
            Proyecto proyecto = proyectoLogica.findProyectoByCodigo(codigoProyecto).orElse(null);
            if (proyecto != null) {
                tareas.clear();
                for (Tarea tarea : proyecto.getTareas()) {
                    TareaTableData data = new TareaTableData(
                            tarea.getNumero(),
                            tarea.getDescripcion(),
                            tarea.getFechaFinalizacionEsperada().toString(),
                            tarea.getPrioridad().name(),
                            tarea.getEstado().name(),
                            tarea.getResponsable().getNombre()
                    );
                    tareas.add(data);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudieron cargar las tareas: " + e.getMessage());
        }
    }

    @FXML
    private void CrearProyecto() {
        try {
            String descripcion = txtDescripcion.getText();
            Usuario encargado = btnEncargado.getValue();

            if (descripcion == null || descripcion.trim().isEmpty()) {
                showAlert("Error", "La descripción del proyecto es obligatoria");
                return;
            }

            if (encargado == null) {
                showAlert("Error", "Debe seleccionar un encargado");
                return;
            }

            Proyecto nuevoProyecto = new Proyecto(0, descripcion, encargado);
            proyectoLogica.createProyecto(nuevoProyecto);

            txtDescripcion.clear();
            btnEncargado.setValue(null);

            loadData();

            showAlert("Éxito", "Proyecto creado exitosamente");

        } catch (Exception e) {
            showAlert("Error", "No se pudo crear el proyecto: " + e.getMessage());
        }
    }

    @FXML
    private void CrearTarea() {
        try {
            if (proyectoSeleccionado == null) {
                showAlert("Error", "Debe seleccionar un proyecto primero");
                return;
            }

            String descripcion = txtDescripcion1.getText();
            LocalDate fechaVencimiento = dtpVencimiento.getValue();
            Tarea.Prioridad prioridad = btnEncargado21.getValue();
            Tarea.Estado estado = btnEncargado2.getValue();
            Usuario responsable = btnEncargado1.getValue();

            if (descripcion == null || descripcion.trim().isEmpty()) {
                showAlert("Error", "La descripción de la tarea es obligatoria");
                return;
            }

            if (fechaVencimiento == null) {
                showAlert("Error", "La fecha de vencimiento es obligatoria");
                return;
            }

            if (prioridad == null) {
                showAlert("Error", "La prioridad es obligatoria");
                return;
            }

            if (estado == null) {
                showAlert("Error", "El estado es obligatorio");
                return;
            }

            if (responsable == null) {
                showAlert("Error", "El responsable es obligatorio");
                return;
            }

            Tarea nuevaTarea = new Tarea(0, descripcion, fechaVencimiento, prioridad, estado, responsable);
            proyectoLogica.createTarea(proyectoSeleccionado.getCodigo(), nuevaTarea);

            txtDescripcion1.clear();
            dtpVencimiento.setValue(null);
            btnEncargado21.setValue(null);
            btnEncargado2.setValue(null);
            btnEncargado1.setValue(null);

            loadTareasForProyecto(proyectoSeleccionado.getCodigo());
            loadData(); // Para actualizar el número de tareas en la tabla de proyectos

            showAlert("Éxito", "Tarea creada exitosamente");

        } catch (Exception e) {
            showAlert("Error", "No se pudo crear la tarea: " + e.getMessage());
        }
    }

    @FXML
    private void EditarTarea() {
        TareaTableData tareaSeleccionada = tblTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada == null) {
            showAlert("Error", "Debe seleccionar una tarea para editar");
            return;
        }

        try {
            txtDescripcion1.setText(tareaSeleccionada.getDescripcion());
            dtpVencimiento.setValue(LocalDate.parse(tareaSeleccionada.getFechaVencimiento()));
            btnEncargado21.setValue(Tarea.Prioridad.valueOf(tareaSeleccionada.getPrioridad()));
            btnEncargado2.setValue(Tarea.Estado.valueOf(tareaSeleccionada.getEstado()));

            Usuario responsable = usuarios.stream()
                    .filter(u -> u.getNombre().equals(tareaSeleccionada.getResponsable()))
                    .findFirst()
                    .orElse(null);
            btnEncargado1.setValue(responsable);

        } catch (Exception e) {
            showAlert("Error", "No se pudo cargar la tarea para editar: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ProyectoTableData {
        private final SimpleIntegerProperty codigo;
        private final SimpleStringProperty descripcion;
        private final SimpleStringProperty encargado;
        private final SimpleIntegerProperty numeroTareas;

        public ProyectoTableData(int codigo, String descripcion, String encargado, int numeroTareas) {
            this.codigo = new SimpleIntegerProperty(codigo);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.encargado = new SimpleStringProperty(encargado);
            this.numeroTareas = new SimpleIntegerProperty(numeroTareas);
        }

        public int getCodigo() { return codigo.get(); }
        public String getDescripcion() { return descripcion.get(); }
        public String getEncargado() { return encargado.get(); }
        public int getNumeroTareas() { return numeroTareas.get(); }

        public SimpleIntegerProperty codigoProperty() { return codigo; }
        public SimpleStringProperty descripcionProperty() { return descripcion; }
        public SimpleStringProperty encargadoProperty() { return encargado; }
        public SimpleIntegerProperty numeroTareasProperty() { return numeroTareas; }
    }

    public static class TareaTableData {
        private final SimpleIntegerProperty numero;
        private final SimpleStringProperty descripcion;
        private final SimpleStringProperty fechaVencimiento;
        private final SimpleStringProperty prioridad;
        private final SimpleStringProperty estado;
        private final SimpleStringProperty responsable;

        public TareaTableData(int numero, String descripcion, String fechaVencimiento,
                              String prioridad, String estado, String responsable) {
            this.numero = new SimpleIntegerProperty(numero);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
            this.prioridad = new SimpleStringProperty(prioridad);
            this.estado = new SimpleStringProperty(estado);
            this.responsable = new SimpleStringProperty(responsable);
        }

        public int getNumero() { return numero.get(); }
        public String getDescripcion() { return descripcion.get(); }
        public String getFechaVencimiento() { return fechaVencimiento.get(); }
        public String getPrioridad() { return prioridad.get(); }
        public String getEstado() { return estado.get(); }
        public String getResponsable() { return responsable.get(); }

        public SimpleIntegerProperty numeroProperty() { return numero; }
        public SimpleStringProperty descripcionProperty() { return descripcion; }
        public SimpleStringProperty fechaVencimientoProperty() { return fechaVencimiento; }
        public SimpleStringProperty prioridadProperty() { return prioridad; }
        public SimpleStringProperty estadoProperty() { return estado; }
        public SimpleStringProperty responsableProperty() { return responsable; }
    }
}