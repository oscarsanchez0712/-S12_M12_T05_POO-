package vallegrande.edu.pe.tutawayta.model;

import javafx.beans.property.*;

public class Cliente {
    private final IntegerProperty id       = new SimpleIntegerProperty();
    private final StringProperty  nombre   = new SimpleStringProperty();
    private final StringProperty  apellido = new SimpleStringProperty();
    private final StringProperty  email    = new SimpleStringProperty();
    private final StringProperty  telefono = new SimpleStringProperty();
    private final StringProperty  estado   = new SimpleStringProperty();

    public Cliente() {}
    public Cliente(int id, String nombre, String apellido, String email, String telefono, String estado) {
        setId(id); setNombre(nombre); setApellido(apellido);
        setEmail(email); setTelefono(telefono); setEstado(estado);
    }

    public int getId()                         { return id.get(); }
    public IntegerProperty idProperty()        { return id; }
    public void setId(int v)                   { id.set(v); }

    public String getNombre()                  { return nombre.get(); }
    public StringProperty nombreProperty()     { return nombre; }
    public void setNombre(String v)            { nombre.set(v); }

    public String getApellido()                { return apellido.get(); }
    public StringProperty apellidoProperty()   { return apellido; }
    public void setApellido(String v)          { apellido.set(v); }

    public String getEmail()                   { return email.get(); }
    public StringProperty emailProperty()      { return email; }
    public void setEmail(String v)             { email.set(v); }

    public String getTelefono()                { return telefono.get(); }
    public StringProperty telefonoProperty()   { return telefono; }
    public void setTelefono(String v)          { telefono.set(v); }

    public String getEstado()                  { return estado.get(); }
    public StringProperty estadoProperty()     { return estado; }
    public void setEstado(String v)            { estado.set(v); }
}
