package controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import logica.ProyectoLogica;
import model.Proyecto;
import model.Tarea;
import model.Encargado;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private ChoiceBox<Encargado> btnEncargado;
    @FXML private Button btnCrearProyecto;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<Proyecto> tblProyectos;
    @FXML private TableColumn<Proyecto, Integer> colCodigo;
    @FXML private TableColumn<Proyecto, String> colDescripcion;
    @FXML private TableColumn<Proyecto, String> colEncargado;
    @FXML private TableColumn<Proyecto, Integer> colNumeroTareas;

    @FXML private Button btnCrearTarea;
    @FXML private Button btnEditar;
    @FXML private TextField txtDescripcionTarea;
    @FXML private DatePicker dtpVencimiento;
    @FXML private ChoiceBox<Tarea.Prioridad> btnPrioridad;
    @FXML private ChoiceBox<Tarea.Estado> btnEstado;
    @FXML private ChoiceBox<Encargado> btnResponsable;
    @FXML private TableView<Tarea> tblTareas;
    @FXML private TableColumn<Tarea, Integer> colNumero;
    @FXML private TableColumn<Tarea, String> colDescripcionTarea;
    @FXML private TableColumn<Tarea, String> colVencimiento;
    @FXML private TableColumn<Tarea, String> colPrioridad;
    @FXML private TableColumn<Tarea, String> colEstado;
    @FXML private TableColumn<Tarea, String> colAsignado;

    private ProyectoLogica proyectoLogica;
    private ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();
    private ObservableList<Tarea> tareas = FXCollections.observableArrayList();
    private List<Encargado> usuarios;
    private Proyecto proyectoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            proyectoLogica = new ProyectoLogica("src/main/resources/datos-prueba.xml");
            usuarios = proyectoLogica.findAllUsuarios();

            configurarTablas();
            cargarChoiceBoxes();
            cargarProyectos();

            tblProyectos.getSelectionModel().selectedItemProperty().addListener(
                    (obs, old, nuevo) -> {
                        if (nuevo != null) {
                            proyectoSeleccionado = nuevo;
                            cargarTareas(nuevo.getCodigo());
                        }
                    }
            );
        }catch(Exception e){
            mostrarError("Error al iniciar");
        }
    }

    private void configurarTablas() {
        try {
            // Configuración de columnas de Proyectos
            colCodigo.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getCodigo()).asObject());
            colDescripcion.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDescripcion()));
            colEncargado.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getEncargadoGeneral().getNombre()));
            colNumeroTareas.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTareas().size()).asObject());

            // Configuración de columnas de Tareas
            colNumero.setCellValueFactory(t -> new SimpleIntegerProperty(t.getValue().getNumero()).asObject());
            colDescripcionTarea.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getDescripcion()));
            colVencimiento.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getFechaFinalizacionEsperada().toString()));
            colPrioridad.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getPrioridad().name()));
            colEstado.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getEstado().name()));
            colAsignado.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getResponsable().getNombre()));

            tblProyectos.setItems(proyectos);
            tblTareas.setItems(tareas);
        }catch(Exception e){
            mostrarError("Error al configurar las tablas");
        }
    }

    private void cargarChoiceBoxes() {
        try{
            btnEncargado.setItems(FXCollections.observableArrayList(usuarios));
            btnResponsable.setItems(FXCollections.observableArrayList(usuarios));
            btnPrioridad.setItems(FXCollections.observableArrayList(Tarea.Prioridad.values()));
            btnEstado.setItems(FXCollections.observableArrayList(Tarea.Estado.values()));
        }catch(Exception e){
            mostrarError("Error al cargar botones");
        }
    }

    private void cargarProyectos() {
        try{
            proyectos.setAll(proyectoLogica.findAllProyectos());
        }catch(Exception e){
            mostrarError("Error al cargar los proyectos");
        }
    }

    private void cargarTareas(int codigoProyecto) {
        try{
            Proyecto proyecto = proyectoLogica.findProyectoByCodigo(codigoProyecto).orElse(null);
            if (proyecto != null) {
                tareas.setAll(proyecto.getTareas());
            }
        }catch(Exception e){
            mostrarError("Error al cargar las tareas");
        }
    }

    @FXML
    private void CrearProyecto() {
        String desc = txtDescripcion.getText();
        Encargado enc = btnEncargado.getValue();

        if (desc.isEmpty() || enc == null) {
            mostrarError("Faltan datos");
            return;
        }

        try {
            Proyecto p = new Proyecto(0, desc, enc);
            proyectoLogica.createProyecto(p);

            txtDescripcion.clear();
            btnEncargado.setValue(null);
            cargarProyectos();
            mostrarInfo("Proyecto creado");
        }catch(Exception e){
            mostrarError("Error al crear el proyecto");
        }
    }

    @FXML
    private void CrearTarea() {
        if (proyectoSeleccionado == null) {
            mostrarError("Selecciona un proyecto");
            return;
        }

        String desc = txtDescripcionTarea.getText();
        LocalDate fecha = dtpVencimiento.getValue();
        Tarea.Prioridad prioridad = btnPrioridad.getValue();
        Tarea.Estado estado = btnEstado.getValue();
        Encargado responsable = btnResponsable.getValue();

        if (desc.isEmpty() || fecha == null || prioridad == null || estado == null || responsable == null) {
            mostrarError("Faltan datos");
            return;
        }

        try {
            Tarea t = new Tarea(0, desc, fecha, prioridad, estado, responsable);
            proyectoLogica.createTarea(proyectoSeleccionado.getCodigo(), t);

            limpiarFormularioTarea();
            cargarTareas(proyectoSeleccionado.getCodigo());
            cargarProyectos();
            mostrarInfo("Tarea creada");
        }catch(Exception e){
            mostrarError("Error al crear la tarea");
        }
    }

    @FXML
    private void EditarTarea() {
        Tarea seleccionada = tblTareas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una tarea");
            return;
        }

        try{
            Dialog<Tarea> dialog = crearDialogEditarTarea(seleccionada);
            dialog.showAndWait().ifPresent(tareaEditada -> {
                try {
                    proyectoLogica.updateTarea(proyectoSeleccionado.getCodigo(), tareaEditada);
                    cargarTareas(proyectoSeleccionado.getCodigo());
                    cargarProyectos();
                    mostrarInfo("Tarea actualizada");
                } catch (Exception e) {
                    mostrarError("Error al actualizar la tarea");
                }
            });
        }catch(Exception e){
            mostrarError("Error al editar la tarea");
        }
    }

    private Dialog<Tarea> crearDialogEditarTarea(Tarea tarea) {
        Dialog<Tarea> dialog = new Dialog<>();
        dialog.setTitle("Editar Tarea");
        dialog.setHeaderText("Modificar datos de la tarea");

        ButtonType guardarButton = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButton, ButtonType.CANCEL);

        // Crear campos del formulario
        TextField txtDesc = new TextField(tarea.getDescripcion());
        DatePicker dtpFecha = new DatePicker(tarea.getFechaFinalizacionEsperada());
        ChoiceBox<Tarea.Prioridad> cbPrioridad = new ChoiceBox<>(FXCollections.observableArrayList(Tarea.Prioridad.values()));
        cbPrioridad.setValue(tarea.getPrioridad());
        ChoiceBox<Tarea.Estado> cbEstado = new ChoiceBox<>(FXCollections.observableArrayList(Tarea.Estado.values()));
        cbEstado.setValue(tarea.getEstado());
        ChoiceBox<Encargado> cbResponsable = new ChoiceBox<>(FXCollections.observableArrayList(usuarios));
        cbResponsable.setValue(tarea.getResponsable());

        // Layout del formulario
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Descripción:"), 0, 0);
        grid.add(txtDesc, 1, 0);
        grid.add(new Label("Fecha vencimiento:"), 0, 1);
        grid.add(dtpFecha, 1, 1);
        grid.add(new Label("Prioridad:"), 0, 2);
        grid.add(cbPrioridad, 1, 2);
        grid.add(new Label("Estado:"), 0, 3);
        grid.add(cbEstado, 1, 3);
        grid.add(new Label("Responsable:"), 0, 4);
        grid.add(cbResponsable, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado a Tarea
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButton) {
                if (txtDesc.getText().isEmpty() || dtpFecha.getValue() == null ||
                        cbPrioridad.getValue() == null || cbEstado.getValue() == null || cbResponsable.getValue() == null) {
                    mostrarError("Todos los campos son obligatorios");
                    return null;
                }

                Tarea tareaEditada = new Tarea(
                        tarea.getNumero(), // Mantener el mismo número
                        txtDesc.getText(),
                        dtpFecha.getValue(),
                        cbPrioridad.getValue(),
                        cbEstado.getValue(),
                        cbResponsable.getValue()
                );
                return tareaEditada;
            }
            return null;
        });

        return dialog;
    }

    private void limpiarFormularioTarea() {
        txtDescripcionTarea.clear();
        dtpVencimiento.setValue(null);
        btnPrioridad.setValue(null);
        btnEstado.setValue(null);
        btnResponsable.setValue(null);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
    }
}