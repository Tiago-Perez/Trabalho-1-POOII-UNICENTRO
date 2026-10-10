package edu.unicentro.trabalho1pooii.controller;

import edu.unicentro.trabalho1pooii.main.Alertas;
import edu.unicentro.trabalho1pooii.main.GerenciadorTelas;
import edu.unicentro.trabalho1pooii.main.Sessao;
import edu.unicentro.trabalho1pooii.model.Nota;
import edu.unicentro.trabalho1pooii.model.Usuario;
import edu.unicentro.trabalho1pooii.service.NotaService;
import edu.unicentro.trabalho1pooii.service.ServiceException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML private Label lblBemVindo;
    @FXML private FlowPane flowNotas;

    private final NotaService notaService = new NotaService();

    @FXML
    private void initialize() {
        Usuario u = Sessao.getUsuarioLogado();
        if (u == null) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Nenhum usuário logado.");
            return;
        }

        lblBemVindo.setText("Bem vindo, " + u.getNome());

        try {
            for (Nota n : notaService.listar(u.getUserID())) {
                flowNotas.getChildren().add(criarCartao(n));
            }
        } catch (ServiceException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // Monta o cartão de preview de uma nota.
    private VBox criarCartao(Nota nota) {
        Label titulo = new Label((nota.isFixado() ? "Fixado: " : "") + nota.getNome());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        titulo.setWrapText(true);

        Label preview = new Label(resumir(nota.getTexto(), 120));
        preview.setWrapText(true);

        VBox cartao = new VBox(6, titulo, preview);
        cartao.setPrefWidth(190);
        cartao.setMaxHeight(160);
        cartao.setStyle("-fx-border-color: gray; -fx-border-radius: 8; "
                + "-fx-background-radius: 8; -fx-padding: 10; -fx-cursor: hand;");

        // Clicar no cartão abre a nota em tela cheia.
        cartao.setOnMouseClicked(e -> abrirNota(nota));
        return cartao;
    }

    // Corta o texto longo para caber no cartão.
    private String resumir(String texto, int max) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= max) {
            return texto;
        }
        return texto.substring(0, max).strip() + "...";
    }

    // nota == null significa "criar uma nota nova".
    private void abrirNota(Nota nota) {
        try {
            NotaController c = GerenciadorTelas.trocarTela(
                    GerenciadorTelas.pasta_view + "nota.fxml", "Nota");
            c.setNota(nota);
        } catch (IOException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, "Não foi possível abrir a nota.");
        }
    }

    @FXML
    private void btnNovaNotaOnAction() {
        abrirNota(null);
    }

    @FXML
    private void btnSairOnAction() {
        Sessao.encerrar();
        try {
            GerenciadorTelas.trocarTela(GerenciadorTelas.pasta_view + "login.fxml", "Login");
        } catch (IOException e) {
            Alertas.mostrar(Alert.AlertType.ERROR, "Não foi possível abrir a tela de login.");
        }
    }
}