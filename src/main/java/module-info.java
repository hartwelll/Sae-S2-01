module universite_paris8.iut.ylecoguic.saeterrarialike {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens universite_paris8.iut.ylecoguic.saeterrarialike to javafx.fxml;
    exports universite_paris8.iut.ylecoguic.saeterrarialike;
    exports universite_paris8.iut.ylecoguic.saeterrarialike.controller;
    opens universite_paris8.iut.ylecoguic.saeterrarialike.controller to javafx.fxml;
}