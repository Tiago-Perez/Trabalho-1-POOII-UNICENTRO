module edu.unicentro.trabalho1pooii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens edu.unicentro.trabalho1pooii.controller to javafx.fxml;
    exports edu.unicentro.trabalho1pooii.main;
}