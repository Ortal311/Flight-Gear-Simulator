package view_PlayerButtons;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import viewModel.ViewModelController;

import java.io.IOException;

public class PlayerButtons extends AnchorPane {

    PlayerButtonsController pbc;

    public ObservableList speedList;

    public PlayerButtons() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane buttons= fxl.load(getClass().getResource("PlayerButtons.fxml").openStream());
            pbc= fxl.getController();
            this.getChildren().add(buttons);

        } catch (IOException e) {}
    }

    public void setVmc(ViewModelController vmc)
    {
        pbc.init(vmc);

    }

}


