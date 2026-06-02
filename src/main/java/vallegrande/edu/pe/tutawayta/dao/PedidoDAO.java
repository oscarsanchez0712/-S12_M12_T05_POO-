package vallegrande.edu.pe.tutawayta.dao;

import vallegrande.edu.pe.tutawayta.model.Pedido;
import vallegrande.edu.pe.tutawayta.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class PedidoDAO {

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return lista;
        String sql = "SELECT id, cliente, producto, cantidad, total, estado, fecha FROM pedidos ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Pedido(rs.getInt("id"), rs.getString("cliente"),
                    rs.getString("producto"), rs.getInt("cantidad"),
                    rs.getDouble("total"), rs.getString("estado"), rs.getString("fecha")));
        } catch (SQLException e) { System.err.println("Error listar pedidos: " + e.getMessage()); }
        return lista;
    }

    public boolean insertar(Pedido p) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "INSERT INTO pedidos (cliente, producto, cantidad, total, estado, fecha) VALUES (?,?,?,?,?,NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCliente()); ps.setString(2, p.getProducto());
            ps.setInt(3, p.getCantidad());   ps.setDouble(4, p.getTotal());
            ps.setString(5, p.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error insertar pedido: " + e.getMessage()); return false; }
    }

    public boolean actualizar(Pedido p) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "UPDATE pedidos SET cliente=?, producto=?, cantidad=?, total=?, estado=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCliente()); ps.setString(2, p.getProducto());
            ps.setInt(3, p.getCantidad());   ps.setDouble(4, p.getTotal());
            ps.setString(5, p.getEstado());  ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error actualizar pedido: " + e.getMessage()); return false; }
    }

    public boolean eliminar(int id) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) return false;
        String sql = "DELETE FROM pedidos WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error eliminar pedido: " + e.getMessage()); return false; }
    }
}
