package vallegrande.edu.pe.tutawayta.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import vallegrande.edu.pe.tutawayta.dao.ProductoDAO;
import vallegrande.edu.pe.tutawayta.model.Producto;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    @FXML private Label            lblFormTitulo;
    @FXML private TextField        txtNombre;
    @FXML private TextArea         txtDescripcion;
    @FXML private TextField        txtPrecio;
    @FXML private TextField        txtStock;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Button           btnGuardar;
    @FXML private Label            lblMensaje;

    @FXML private TextField               txtBuscar;
    @FXML private TableView<Producto>     tablaProductos;
    @FXML private Label                   lblContador;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String>  colNombre;
    @FXML private TableColumn<Producto, String>  colDescripcion;
    @FXML private TableColumn<Producto, Double>  colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String>  colEstado;
    @FXML private TableColumn<Producto, String>  colAcciones;

    private final ProductoDAO             dao          = new ProductoDAO();
    private final ObservableList<Producto> listaData   = FXCollections.observableArrayList();
    private       FilteredList<Producto>  listaFiltrada;
    private       Producto               productoEditando = null;
    private       boolean                modoEdicion      = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarCombo();
        configurarTabla();
        configurarBusqueda();
        cargarProductos();
    }

    private void configurarCombo() {
        cmbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo", "Agotado"));
        cmbEstado.setValue("Activo");
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Precio formateado
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("S/. %.2f", item));
            }
        });

        // Estado con color
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "Activo"   -> setStyle("-fx-text-fill:#27ae60;-fx-font-weight:bold;");
                    case "Inactivo" -> setStyle("-fx-text-fill:#e67e22;-fx-font-weight:bold;");
                    case "Agotado"  -> setStyle("-fx-text-fill:#e74c3c;-fx-font-weight:bold;");
                    default         -> setStyle("");
                }
            }
        });

        // Botones Editar / Eliminar por fila
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("✏ Editar");
            private final Button btnDel  = new Button("🗑 Eliminar");
            private final HBox   box     = new HBox(6, btnEdit, btnDel);
            {
                box.setAlignment(Pos.CENTER);
                btnEdit.getStyleClass().add("btn-edit-row");
                btnDel.getStyleClass().add("btn-delete-row");
                btnEdit.setOnAction(e -> cargarEnFormulario(getTableView().getItems().get(getIndex())));
                btnDel.setOnAction(e  -> confirmarEliminar(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        listaFiltrada = new FilteredList<>(listaData, p -> true);
        tablaProductos.setItems(listaFiltrada);
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, old, val) -> {
            String f = val.toLowerCase().trim();
            listaFiltrada.setPredicate(p ->
                f.isEmpty() ||
                p.getNombre().toLowerCase().contains(f) ||
                p.getDescripcion().toLowerCase().contains(f)
            );
            actualizarContador();
        });
    }

    @FXML
    public void cargarProductos() {
        listaData.setAll(dao.listarTodos());
        actualizarContador();
        mostrarMensaje("", false);
    }

    private void actualizarContador() {
        lblContador.setText(listaFiltrada.size() + " registro(s)");
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;

        Producto p = modoEdicion ? productoEditando : new Producto();
        p.setNombre(txtNombre.getText().trim());
        p.setDescripcion(txtDescripcion.getText().trim());
        p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
        p.setStock(Integer.parseInt(txtStock.getText().trim()));
        p.setEstado(cmbEstado.getValue());

        boolean ok     = modoEdicion ? dao.actualizar(p) : dao.insertar(p);
        String  accion = modoEdicion ? "actualizado" : "registrado";

        if (ok) {
            mostrarMensaje("✅  Producto " + accion + " correctamente.", false);
            limpiarFormulario();
            cargarProductos();
        } else {
            mostrarMensaje("❌  Error al guardar. Verifique la conexión.", true);
        }
    }

    private void cargarEnFormulario(Producto p) {
        modoEdicion      = true;
        productoEditando = p;
        lblFormTitulo.setText("✏  EDITAR  #" + p.getId());
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        cmbEstado.setValue(p.getEstado());
        btnGuardar.setText("💾  Actualizar");
        btnGuardar.getStyleClass().add("btn-update");
        mostrarMensaje("Editando: " + p.getNombre(), false);
    }

    private void confirmarEliminar(Producto p) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmar eliminación");
        a.setHeaderText("¿Eliminar \"" + p.getNombre() + "\"?");
        a.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> r = a.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.OK) {
            if (dao.eliminar(p.getId())) {
                mostrarMensaje("✅  Producto eliminado.", false);
                cargarProductos();
            } else {
                mostrarMensaje("❌  No se pudo eliminar.", true);
            }
        }
    }

    @FXML
    public void limpiarFormulario() {
        modoEdicion      = false;
        productoEditando = null;
        lblFormTitulo.setText("✚  NUEVO PRODUCTO");
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbEstado.setValue("Activo");
        btnGuardar.setText("💾  Guardar");
        btnGuardar.getStyleClass().remove("btn-update");
        mostrarMensaje("", false);
    }

    @FXML
    private void volverInicio() {
        tablaProductos.getScene().getWindow().hide();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarMensaje("⚠  El nombre es obligatorio.", true);
            txtNombre.requestFocus(); return false;
        }
        try {
            if (Double.parseDouble(txtPrecio.getText().trim()) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarMensaje("⚠  Precio inválido (solo números positivos).", true);
            txtPrecio.requestFocus(); return false;
        }
        try {
            if (Integer.parseInt(txtStock.getText().trim()) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarMensaje("⚠  Stock inválido (solo enteros positivos).", true);
            txtStock.requestFocus(); return false;
        }
        return true;
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().removeAll("msg-ok", "msg-error");
        if (!texto.isEmpty())
            lblMensaje.getStyleClass().add(esError ? "msg-error" : "msg-ok");
    }
}
