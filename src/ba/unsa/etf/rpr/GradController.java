package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GradController {
    @FXML
    private TextField fieldNaziv;
    @FXML
    private TextField fieldBrojStanovnika;
    @FXML
    private ChoiceBox choiceDrzava;
    private ObservableList<Drzava> drzave;
    private Grad grad;

    public GradController(Grad grad, ObservableList<Drzava> drzave) {
        this.drzave = drzave;
        this.grad = grad;
    }

    public void initialize() {
        choiceDrzava.setItems(drzave);
        if (grad != null) {
            fieldNaziv.setText(grad.getNaziv());
            fieldBrojStanovnika.setText(Integer.toString(grad.getBrojStanovnika()));
            choiceDrzava.getSelectionModel().select(grad.getDrzava());
        }

    }

    public void validiraj(ActionEvent actionEvent) {
        if (fieldNaziv.getText().equals("")) {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        }
        try {
            if (Integer.parseInt(fieldBrojStanovnika.getText()) < 0) {
                fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
                fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
                return;
            } else {
                fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
                fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
            }
        } catch (NumberFormatException e) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
            return;
        }
        if (fieldBrojStanovnika.getText().equals("") || fieldNaziv.getText().equals("")  ) {
            return;
        }
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }

    public void zatvori(ActionEvent actionEvent) {
        fieldNaziv.clear();
        fieldBrojStanovnika.clear();
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }

    public Grad getGrad() {
        return new Grad(0,fieldNaziv.getText(),Integer.parseInt(fieldBrojStanovnika.getText()), (Drzava) choiceDrzava.getSelectionModel().getSelectedItem());
    }
}
