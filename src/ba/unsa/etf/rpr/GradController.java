package ba.unsa.etf.rpr;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GradController {
    @FXML
    private TextField fieldNaziv;
    @FXML
    private TextField fieldBrojStanovnika;

    public void validiraj() {
        if (fieldNaziv.getText().equals("")) {
            fieldNaziv.getStylesheets().removeAll("poljeIspravno");
            fieldNaziv.getStylesheets().add("poljeNijeIspravno");
        } else {
            fieldNaziv.getStylesheets().removeAll("poljeNijeIspravno");
            fieldNaziv.getStylesheets().add("poljeIspravno");
        }
        try {
            if (Integer.parseInt(fieldBrojStanovnika.getText()) < 0) {
                fieldBrojStanovnika.getStylesheets().removeAll("poljeIspravno");
                fieldBrojStanovnika.getStylesheets().add("poljeNijeIspravno");
            } else {
                fieldBrojStanovnika.getStylesheets().removeAll("poljeNijeIspravno");
                fieldBrojStanovnika.getStylesheets().add("poljeIspravno");
            }
        } catch (NumberFormatException e) {
            System.out.println("Nije unesen broj");
        }
    }

    public void zatvori(javafx.event.ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }
}
