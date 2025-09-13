package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ProyectoLogica;
import model.Encargado;
import model.Tarea;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditarTareaController implements Initializable {

    @FXML private TextField txtDescripcion;
    @FXML private DatePicker dtpFecha;
    @FXML private ChoiceBox<Tarea.Prioridad> cbPrioridad;
    @FXML private ChoiceBox<Tarea.Estado> cbEstado;
    @FXML private ChoiceBox<Encargado> cbResponsable;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Tarea tareaOriginal;
    private List<Encargado> usuarios;
    private ProyectoLogica proyectoLogica;
    private int codigoProyecto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbPrioridad.setItems(FXCollections.observableArrayList(Tarea.Prioridad.values()));
        cbEstado.setItems(FXCollections.observableArrayList(Tarea.Estado.values()));
    }

    public void cargarDatos(Tarea tarea, List<Encargado> usuarios, ProyectoLogica proyectoLogica, int codigoProyecto) {
        this.tareaOriginal = tarea;
        this.usuarios = usuarios;
        this.proyectoLogica = proyectoLogica;
        this.codigoProyecto = codigoProyecto;

        txtDescripcion.setText(tarea.getDescripcion());
        dtpFecha.setValue(tarea.getFechaFinalizacionEsperada());
        cbPrioridad.setValue(tarea.getPrioridad());
        cbEstado.setValue(tarea.getEstado());

        cbResponsable.setItems(FXCollections.observableArrayList(usuarios));
        cbResponsable.setValue(tarea.getResponsable());
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) {
            return;
        }

        try {
            Tarea tareaEditada = new Tarea(
                    tareaOriginal.getNumero(),
                    txtDescripcion.getText().trim(),
                    dtpFecha.getValue(),
                    cbPrioridad.getValue(),
                    cbEstado.getValue(),
                    cbResponsable.getValue()
            );

            proyectoLogica.updateTarea(codigoProyecto, tareaEditada);
            mostrarInfo("Tarea actualizada correctamente");
            cerrarVentana();

        } catch (Exception e) {
            mostrarError("Error al actualizar la tarea: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarError("La descripción es obligatoria");
            return false;
        }

        if (dtpFecha.getValue() == null) {
            mostrarError("La fecha de vencimiento es obligatoria");
            return false;
        }

        if (dtpFecha.getValue().isBefore(LocalDate.now())) {
            mostrarError("La fecha de vencimiento no puede ser anterior a hoy");
            return false;
        }

        if (cbPrioridad.getValue() == null) {
            mostrarError("La prioridad es obligatoria");
            return false;
        }

        if (cbEstado.getValue() == null) {
            mostrarError("El estado es obligatorio");
            return false;
        }

        if (cbResponsable.getValue() == null) {
            mostrarError("El responsable es obligatorio");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}