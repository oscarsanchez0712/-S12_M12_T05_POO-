package vallegrande.edu.pe.tutawayta.controller;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import vallegrande.edu.pe.tutawayta.dao.PedidoDAO;
import vallegrande.edu.pe.tutawayta.model.Pedido;

import java.net.URL;
import java.util.*;

public class PedidoController implements Initializable {

    @FXML private Label lblFormTitulo;
    @FXML private TextField txtCliente, txtProducto, txtCantidad, txtTotal, txtBuscar;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Button btnGuardar;
    @FXML private Label lblMensaje, lblContador;
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido,Integer> colId, colCantidad;
    @FXML private TableColumn<Pedido,String> colCliente, colProducto, colEstado, colFecha, colAcciones;
    @FXML private TableColumn<Pedido,Double> colTotal;

    private final PedidoDAO dao = new PedidoDAO();
    private final ObservableList<Pedido> listaData = FXCollections.observableArrayList();
    private FilteredList<Pedido> listaFiltrada;
    private Pedido editando = null;
    private boolean modoEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbEstado.setItems(FXCollections.observableArrayList("Pendiente","En proceso","Entregado","Cancelado"));
        cmbEstado.setValue("Pendiente");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item,empty); setText(empty||item==null?null:String.format("S/. %.2f",item));
            }
        });
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item,empty); if(empty||item==null){setText(null);setStyle("");return;}
                setText(item);
                setStyle(switch(item){
                    case "Entregado"   -> "-fx-text-fill:#27ae60;-fx-font-weight:bold;";
                    case "En proceso"  -> "-fx-text-fill:#2980b9;-fx-font-weight:bold;";
                    case "Cancelado"   -> "-fx-text-fill:#e74c3c;-fx-font-weight:bold;";
                    default            -> "-fx-text-fill:#e67e22;-fx-font-weight:bold;";
                });
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
        tablaPedidos.setItems(listaFiltrada);
        txtBuscar.textProperty().addListener((obs,o,v) -> {
            String f = v.toLowerCase().trim();
            listaFiltrada.setPredicate(p -> f.isEmpty()||p.getCliente().toLowerCase().contains(f)||p.getProducto().toLowerCase().contains(f));
            lblContador.setText(listaFiltrada.size()+" registro(s)");
        });
        cargarPedidos();
    }

    @FXML public void cargarPedidos() { listaData.setAll(dao.listarTodos()); lblContador.setText(listaFiltrada.size()+" registro(s)"); mostrarMensaje("",false); }

    @FXML private void guardar() {
        if (txtCliente.getText().trim().isEmpty()||txtProducto.getText().trim().isEmpty()) { mostrarMensaje("⚠ Cliente y producto son obligatorios.",true); return; }
        try {
            Pedido p = modoEdicion ? editando : new Pedido();
            p.setCliente(txtCliente.getText().trim()); p.setProducto(txtProducto.getText().trim());
            p.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            p.setTotal(Double.parseDouble(txtTotal.getText().trim()));
            p.setEstado(cmbEstado.getValue());
            boolean ok = modoEdicion ? dao.actualizar(p) : dao.insertar(p);
            if (ok) { mostrarMensaje("✅ Pedido "+(modoEdicion?"actualizado":"registrado")+".",false); limpiarFormulario(); cargarPedidos(); }
            else mostrarMensaje("❌ Error al guardar.",true);
        } catch (NumberFormatException e) { mostrarMensaje("⚠ Cantidad y total deben ser números.",true); }
    }

    private void cargarEnFormulario(Pedido p) {
        modoEdicion=true; editando=p; lblFormTitulo.setText("✏  EDITAR  #"+p.getId());
        txtCliente.setText(p.getCliente()); txtProducto.setText(p.getProducto());
        txtCantidad.setText(String.valueOf(p.getCantidad())); txtTotal.setText(String.valueOf(p.getTotal()));
        cmbEstado.setValue(p.getEstado()); btnGuardar.setText("💾  Actualizar");
        btnGuardar.getStyleClass().add("btn-update"); mostrarMensaje("Editando pedido #"+p.getId(),false);
    }

    private void confirmarEliminar(Pedido p) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmar"); a.setHeaderText("¿Eliminar pedido #"+p.getId()+"?"); a.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> r = a.showAndWait();
        if (r.isPresent()&&r.get()==ButtonType.OK) { if(dao.eliminar(p.getId())){mostrarMensaje("✅ Eliminado.",false);cargarPedidos();}else mostrarMensaje("❌ Error.",true); }
    }

    @FXML public void limpiarFormulario() {
        modoEdicion=false; editando=null; lblFormTitulo.setText("✚  NUEVO PEDIDO");
        txtCliente.clear(); txtProducto.clear(); txtCantidad.clear(); txtTotal.clear();
        cmbEstado.setValue("Pendiente"); btnGuardar.setText("💾  Guardar");
        btnGuardar.getStyleClass().remove("btn-update"); mostrarMensaje("",false);
    }

    @FXML private void volverInicio() { tablaPedidos.getScene().getWindow().hide(); }

    private void mostrarMensaje(String t, boolean err) {
        lblMensaje.setText(t); lblMensaje.getStyleClass().removeAll("msg-ok","msg-error");
        if(!t.isEmpty()) lblMensaje.getStyleClass().add(err?"msg-error":"msg-ok");
    }
}
