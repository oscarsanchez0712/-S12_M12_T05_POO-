package vallegrande.edu.pe.tutawayta.model;

import javafx.beans.property.*;

public class Pedido {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  cliente     = new SimpleStringProperty();
    private final StringProperty  producto    = new SimpleStringProperty();
    private final IntegerProperty cantidad    = new SimpleIntegerProperty();
    private final DoubleProperty  total       = new SimpleDoubleProperty();
    private final StringProperty  estado      = new SimpleStringProperty();
    private final StringProperty  fecha       = new SimpleStringProperty();

    public Pedido() {}
    public Pedido(int id, String cliente, String producto, int cantidad, double total, String estado, String fecha) {
        setId(id); setCliente(cliente); setProducto(producto);
        setCantidad(cantidad); setTotal(total); setEstado(estado); setFecha(fecha);
    }

    public int getId()                        { return id.get(); }
    public IntegerProperty idProperty()       { return id; }
    public void setId(int v)                  { id.set(v); }

    public String getCliente()                { return cliente.get(); }
    public StringProperty clienteProperty()   { return cliente; }
    public void setCliente(String v)          { cliente.set(v); }

    public String getProducto()               { return producto.get(); }
    public StringProperty productoProperty()  { return producto; }
    public void setProducto(String v)         { producto.set(v); }

    public int getCantidad()                  { return cantidad.get(); }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public void setCantidad(int v)            { cantidad.set(v); }

    public double getTotal()                  { return total.get(); }
    public DoubleProperty totalProperty()     { return total; }
    public void setTotal(double v)            { total.set(v); }

    public String getEstado()                 { return estado.get(); }
    public StringProperty estadoProperty()    { return estado; }
    public void setEstado(String v)           { estado.set(v); }

    public String getFecha()                  { return fecha.get(); }
    public StringProperty fechaProperty()     { return fecha; }
    public void setFecha(String v)            { fecha.set(v); }
}
