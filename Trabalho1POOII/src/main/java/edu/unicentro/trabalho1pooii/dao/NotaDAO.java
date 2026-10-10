package edu.unicentro.trabalho1pooii.dao;

import edu.unicentro.trabalho1pooii.model.Nota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO implements INota {

    @Override
    public void inserir(Nota nota) throws SQLException {
        String sql = """
                INSERT INTO notas (userid, nome, texto, fixado)
                VALUES (?, ?, ?, FALSE) RETURNING notaid
                """;
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nota.getUserID());
            stmt.setString(2, nota.getNome());
            stmt.setString(3, nota.getTexto());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("O banco não retornou o ID da nota cadastrada.");
                }
                nota.setNotaID(rs.getInt("notaid"));
            }
        }
    }

    @Override
    public void atualizar(Nota nota) throws SQLException {
        String sql = "UPDATE notas SET nome = ?, texto = ? WHERE notaid = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nota.getNome());
            stmt.setString(2, nota.getTexto());
            stmt.setInt(3, nota.getNotaID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int notaID) throws SQLException {
        String sql = "DELETE FROM notas WHERE notaid = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, notaID);
            stmt.executeUpdate();
        }
    }

    @Override
    public void definirFixado(int notaID, boolean fixado) throws SQLException {
        String sql = "UPDATE notas SET fixado = ? WHERE notaid = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBoolean(1, fixado);
            stmt.setInt(2, notaID);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Nota> listarPorUsuario(int userID) throws SQLException {
        List<Nota> lista = new ArrayList<>();
        String sql = "SELECT notaid, userid, nome, texto, fixado FROM notas "
                + "WHERE userid = ? ORDER BY fixado DESC, notaid";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Nota(
                            rs.getInt("notaid"),
                            rs.getInt("userid"),
                            rs.getString("nome"),
                            rs.getString("texto"),
                            rs.getBoolean("fixado")
                    ));
                }
            }
        }
        return lista;
    }
}