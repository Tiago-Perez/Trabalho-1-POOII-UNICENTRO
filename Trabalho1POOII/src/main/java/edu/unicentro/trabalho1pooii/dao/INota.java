package edu.unicentro.trabalho1pooii.dao;

import edu.unicentro.trabalho1pooii.model.Nota;
import java.sql.SQLException;
import java.util.List;

public interface INota {

    void inserir(Nota nota) throws SQLException;

    void atualizar(Nota nota) throws SQLException;

    void excluir(int notaID) throws SQLException;

    void definirFixado(int notaID, boolean fixado) throws SQLException;

    // Lista só as notas do usuário informado.
    List<Nota> listarPorUsuario(int userID) throws SQLException;
}