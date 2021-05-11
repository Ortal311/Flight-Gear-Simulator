package view_AttributesList;


import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AttributesList extends AnchorPane {


    public ObservableList lst;
    public DoubleProperty flag;

    public AttributesList() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane list= fxl.load(getClass().getResource("AttributesList.fxml").openStream());
            AttributesListController alc = fxl.getController();
            lst= alc.lv.getItems();

            this.getChildren().add(list);
        } catch (IOException e) {}

    }


/*
    public ListView set() {
        list = new ListView();
        list.setLayoutX(20);
        list.setLayoutY(25);
        list.setPrefHeight(240);
        list.setPrefWidth(200);

        return list;
    }
 */
}
