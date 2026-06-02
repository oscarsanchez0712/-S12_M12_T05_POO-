package vallegrande.edu.pe.tutawayta.model;

import javafx.beans.property.*;

public class Producto {

    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  nombre      = new SimpleStringProperty();
    private final StringProperty  descripcion = new SimpleStringProperty();
    private final DoubleProperty  precio      = new SimpleDoubleProperty();
    private final IntegerProperty stock       = new SimpleIntegerProperty();
    private final StringProperty  estado      = new SimpleStringProperty();

    // ── Constructores ──────────────────────────────────────────────────────────
    public Producto() {}

    public Producto(int id, String nombre, String descripcion, double precio, int stock, String estado) {
        setId(id);
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setStock(stock);
        setEstado(estado);
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────
    public int getId()                        { return id.get(); }
    public IntegerProperty idProperty()       { return id; }
    public void setId(int v)                  { id.set(v); }

    public String getNombre()                 { return nombre.get(); }
    public StringProperty nombreProperty()    { return nombre; }
    public void setNombre(String v)           { nombre.set(v); }

    public String getDescripcion()            { return descripcion.get(); }
    public StringProperty descripcionProperty(){ return descripcion; }
    public void setDescripcion(String v)      { descripcion.set(v); }

    public double getPrecio()                 { return precio.get(); }
    public DoubleProperty precioProperty()    { return precio; }
    public void setPrecio(double v)           { precio.set(v); }

    public int getStock()                     { return stock.get(); }
    public IntegerProperty stockProperty()    { return stock; }
    public void setStock(int v)               { stock.set(v); }

    public String getEstado()                 { return estado.get(); }
    public StringProperty estadoProperty()    { return estado; }
    public void setEstado(String v)           { estado.set(v); }

    @Override
    public String toString() {
        return "Producto{id=" + getId() + ", nombre=" + getNombre() + ", precio=" + getPrecio() + "}";
    }
}
