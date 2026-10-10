package edu.unicentro.trabalho1pooii.controller;

import edu.unicentro.trabalho1pooii.main.Alertas;
import edu.unicentro.trabalho1pooii.main.GerenciadorTelas;
import edu.unicentro.trabalho1pooii.main.Sessao;
import edu.unicentro.trabalho1pooii.model.Nota;
import edu.unicentro.trabalho1pooii.service.NotaService;
import edu.unicentro.trabalho1pooii.service.ServiceException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NotaController {

    @FXML private TextField txtTitulo;
    @FXML private TextArea txtTexto;
    @FXML private MenuItem miExcluir;
    @FXML private MenuItem miFixar;

    private final NotaService notaService = new NotaService();

    // null = nota nova
    private Nota nota;

    // Chamado pelo MenuController logo depois de abrir a tela.
    public void setNota(Nota nota) {
        this.nota = nota;
        if (nota == null) {
            miExcluir.setVisible(false);
            miFixar.setVisible(false);
        } else {
            txtTitulo.setText(nota.getNome());
            txtTexto.setText(nota.getTexto() == null ? "" : nota.getTexto());
            miFixar.setText(nota.isFixado() ? "Desfixar" : "Fixar");
        }
    }

    @FXML
    private void btnSalvarOnAction() {
        try {
            if (nota == null) {
                Nota nova = new Nota(Sessao.getUsuarioLogado().getUserID(),
                        txtTitulo.getText(), txtTexto.getText());
                notaService.cadastrar(nova);
            } else {
                nota.setNome(txtTitulo.getText());
                nota.setTexto(txtTexto.getText());
                notaService.atualizar(nota);
            }
        } catch (IllegalArgumentException e) {
            Alertas.mostrar(Alert.AlertType.WARNING, e.getMessage());
            return;
        } catch (ServiceException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        voltarParaMenu();
    }

    @FXML
    private void btnExcluirOnAction() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir esta nota?", ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText(null);
        confirmacao.initOwner(GerenciadorTelas.getStage());
        if (confirmacao.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            notaService.excluir(nota.getNotaID());
        } catch (IllegalArgumentException e) {
            Alertas.mostrar(Alert.AlertType.WARNING, e.getMessage());
            return;
        } catch (ServiceException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        voltarParaMenu();
    }

    @FXML
    private void btnVoltarOnAction() {
        voltarParaMenu();
    }

    private void voltarParaMenu() {
        try {
            GerenciadorTelas.trocarTela(GerenciadorTelas.pasta_view + "menu.fxml", "Notas");
        } catch (IOException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, "Não foi possível abrir o menu.");
        }
    }

    @FXML
    void miFixarOnAction() {
        if (nota == null) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Salve a nota antes de fixá-la.");
            return;
        }
        try {
            notaService.fixar(nota.getNotaID(), !nota.isFixado());
        } catch (IllegalArgumentException e) {
            Alertas.mostrar(Alert.AlertType.WARNING, e.getMessage());
            return;
        } catch (ServiceException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        voltarParaMenu();
    }
}