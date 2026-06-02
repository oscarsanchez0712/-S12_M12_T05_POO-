package vallegrande.edu.pe.tutawayta.controller;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import vallegrande.edu.pe.tutawayta.dao.ClienteDAO;
import vallegrande.edu.pe.tutawayta.model.Cliente;

import java.net.URL;
import java.util.*;

public class ClienteController implements Initializable {

    @FXML private Label lblFormTitulo;
    @FXML private TextField txtNombre, txtApellido, txtEmail, txtTelefono, txtBuscar;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Button btnGuardar;
    @FXML private Label lblMensaje, lblContador;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente,Integer> colId;
    @FXML private TableColumn<Cliente,String> colNombre, colApellido, colEmail, colTelefono, colEstado, colAcciones;

    private final ClienteDAO dao = new ClienteDAO();
    private final ObservableList<Cliente> listaData = FXCollections.observableArrayList();
    private FilteredList<Cliente> listaFiltrada;
    private Cliente editando = null;
    private boolean modoEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        cmbEstado.setValue("Activo");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty); if (empty||item==null){setText(null);setStyle("");return;}
                setText(item);
                setStyle("Activo".equals(item)?"-fx-text-fill:#27ae60;-fx-font-weight:bold;":"-fx-text-fill:#e67e22;-fx-font-weight:bold;");
            }
        });
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button e = new Button("✏ Editar"), d = new Button("🗑 Eliminar");
            private final HBox box = new HBox(6, e, d);
            { box.setAlignment(Pos.CENTER); e.getStyleClass().add("btn-edit-row"); d.getStyleClass().add("btn-delete-row");
              e.setOnAction(ev -> cargarEnFormulario(getTableView().getItems().get(getIndex())));
              d.setOnAction(ev -> confirmarEliminar(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(String item, boolean empty) { super.updateItem(item,empty); setGraphic(empty?null:box); }
        });
        listaFiltrada = new FilteredList<>(listaData, p -> true);
        tablaClientes.setItems(listaFiltrada);
        txtBuscar.textProperty().addListener((obs,o,v) -> {
            String f = v.toLowerCase().trim();
            listaFiltrada.setPredicate(c -> f.isEmpty()||c.getNombre().toLowerCase().contains(f)||c.getApellido().toLowerCase().contains(f)||c.getEmail().toLowerCase().contains(f));
            lblContador.setText(listaFiltrada.size()+" registro(s)");
        });
        cargarClientes();
    }

    @FXML public void cargarClientes() { listaData.setAll(dao.listarTodos()); lblContador.setText(listaFiltrada.size()+" registro(s)"); mostrarMensaje("",false); }

    @FXML private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) { mostrarMensaje("⚠ Nombre obligatorio.",true); return; }
        Cliente c = modoEdicion ? editando : new Cliente();
        c.setNombre(txtNombre.getText().trim()); c.setApellido(txtApellido.getText().trim());
        c.setEmail(txtEmail.getText().trim()); c.setTelefono(txtTelefono.getText().trim());
        c.setEstado(cmbEstado.getValue());
        boolean ok = modoEdicion ? dao.actualizar(c) : dao.insertar(c);
        if (ok) { mostrarMensaje("✅ Cliente "+(modoEdicion?"actualizado":"registrado")+".",false); limpiarFormulario(); cargarClientes(); }
        else mostrarMensaje("❌ Error al guardar.",true);
    }

    private void cargarEnFormulario(Cliente c) {
        modoEdicion=true; editando=c;
        lblFormTitulo.setText("✏  EDITAR  #"+c.getId());
        txtNombre.setText(c.getNombre()); txtApellido.setText(c.getApellido());
        txtEmail.setText(c.getEmail()); txtTelefono.setText(c.getTelefono());
        cmbEstado.setValue(c.getEstado()); btnGuardar.setText("💾  Actualizar");
        btnGuardar.getStyleClass().add("btn-update"); mostrarMensaje("Editando: "+c.getNombre(),false);
    }

    private void confirmarEliminar(Cliente c) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmar"); a.setHeaderText("¿Eliminar a "+c.getNombre()+" "+c.getApellido()+"?"); a.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> r = a.showAndWait();
        if (r.isPresent()&&r.get()==ButtonType.OK) { if(dao.eliminar(c.getId())){mostrarMensaje("✅ Eliminado.",false);cargarClientes();}else mostrarMensaje("❌ Error.",true); }
    }

    @FXML public void limpiarFormulario() {
        modoEdicion=false; editando=null; lblFormTitulo.setText("✚  NUEVO CLIENTE");
        txtNombre.clear(); txtApellido.clear(); txtEmail.clear(); txtTelefono.clear();
        cmbEstado.setValue("Activo"); btnGuardar.setText("💾  Guardar");
        btnGuardar.getStyleClass().remove("btn-update"); mostrarMensaje("",false);
    }

    @FXML private void volverInicio() { tablaClientes.getScene().getWindow().hide(); }

    private void mostrarMensaje(String t, boolean err) {
        lblMensaje.setText(t); lblMensaje.getStyleClass().removeAll("msg-ok","msg-error");
        if(!t.isEmpty()) lblMensaje.getStyleClass().add(err?"msg-error":"msg-ok");
    }
}
