package edu.unicentro.trabalho1pooii.service;

import edu.unicentro.trabalho1pooii.dao.INota;
import edu.unicentro.trabalho1pooii.dao.NotaDAO;
import edu.unicentro.trabalho1pooii.model.Nota;
import java.sql.SQLException;
import java.util.List;

public class NotaService {

    private final INota notaDAO = new NotaDAO();

    public void cadastrar(Nota nota) throws ServiceException {
        validar(nota);
        try {
            notaDAO.inserir(nota);
        } catch (SQLException e) {
            throw traduzirFalha("cadastrar", e);
        }
    }

    public void atualizar(Nota nota) throws ServiceException {
        validar(nota);
        if (nota.getNotaID() <= 0) {
            throw new IllegalArgumentException("Nota inválida.");
        }
        try {
            notaDAO.atualizar(nota);
        } catch (SQLException e) {
            throw traduzirFalha("atualizar", e);
        }
    }

    public void excluir(int notaID) throws ServiceException {
        if (notaID <= 0) {
            throw new IllegalArgumentException("Nota inválida.");
        }
        try {
            notaDAO.excluir(notaID);
        } catch (SQLException e) {
            throw traduzirFalha("excluir", e);
        }
    }

    public void fixar(int notaID, boolean fixado) throws ServiceException {
        if (notaID <= 0) {
            throw new IllegalArgumentException("Nota inválida.");
        }
        try {
            notaDAO.definirFixado(notaID, fixado);
        } catch (SQLException e) {
            throw traduzirFalha("fixar", e);
        }
    }

    public List<Nota> listar(int userID) throws ServiceException {
        try {
            return notaDAO.listarPorUsuario(userID);
        } catch (SQLException e) {
            throw traduzirFalha("listar", e);
        }
    }

    private void validar(Nota nota) {
        if (nota == null) {
            throw new IllegalArgumentException("A nota deve ser informada.");
        }
        if (nota.getUserID() <= 0) {
            throw new IllegalArgumentException("A nota precisa pertencer a um usuário.");
        }
        if (nota.getNome() == null || nota.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o título da nota!");
        }
        if (nota.getNome().strip().length() > 255) {
            throw new IllegalArgumentException("O título pode ter no máximo 255 caracteres!");
        }
        nota.setNome(nota.getNome().strip());
    }

    private ServiceException traduzirFalha(String operacao, SQLException causa) {
        // 23503 = violação de chave estrangeira (usuário não existe)
        if ("23503".equals(causa.getSQLState())) {
            return new ServiceException("O usuário da nota não existe.", causa);
        }
        return new ServiceException("Não foi possível " + operacao + " a nota.", causa);
    }
}