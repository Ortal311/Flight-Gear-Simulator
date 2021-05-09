package view;


import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class AttributesList extends Pane {

    public ListView list;

    public ListView set() {
        list = new ListView();
        list.setLayoutX(20);
        list.setLayoutY(25);
        list.setPrefHeight(240);
        list.setPrefWidth(200);

        return list;
    }
}
