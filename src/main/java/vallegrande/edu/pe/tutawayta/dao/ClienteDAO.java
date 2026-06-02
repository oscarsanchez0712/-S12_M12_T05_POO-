package vallegrande.edu.pe.tutawayta.dao;

import vallegrande.edu.pe.tutawayta.model.Cliente;
import vallegrande.edu.pe.tutawayta.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ClienteDAO {

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return lista;
        String sql = "SELECT id, nombre, apellido, email, telefono, estado FROM clientes ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Cliente(rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("apellido"), rs.getString("email"),
                    rs.getString("telefono"), rs.getString("estado")));
        } catch (SQLException e) { System.err.println("Error listar clientes: " + e.getMessage()); }
        return lista;
    }

    public boolean insertar(Cliente c) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "INSERT INTO clientes (nombre, apellido, email, telefono, estado) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre()); ps.setString(2, c.getApellido());
            ps.setString(3, c.getEmail());  ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error insertar cliente: " + e.getMessage()); return false; }
    }

    public boolean actualizar(Cliente c) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "UPDATE clientes SET nombre=?, apellido=?, email=?, telefono=?, estado=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre()); ps.setString(2, c.getApellido());
            ps.setString(3, c.getEmail());  ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEstado()); ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error actualizar cliente: " + e.getMessage()); return false; }
    }

    public boolean eliminar(int id) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "DELETE FROM clientes WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error eliminar cliente: " + e.getMessage()); return false; }
    }
}
