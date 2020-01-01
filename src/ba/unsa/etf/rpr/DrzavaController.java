package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DrzavaController {
    @FXML
    private TextField fieldNaziv;

    public void validiraj() {
        if(!fieldNaziv.getText().equals("")) {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
        }
    }

    public void zatvori(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }
}
