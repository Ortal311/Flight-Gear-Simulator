package view_AttributesList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AttributesList extends AnchorPane {

    public ObservableList lst;
    AttributesListController alc;

    public AttributesList() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        lst= FXCollections.observableArrayList();
        try {
            AnchorPane list = fxl.load(getClass().getResource("AttributesList.fxml").openStream());
            alc = fxl.getController();

            lst.addListener((ListChangeListener) change -> alc.lv.setItems(change.getList()));

            this.getChildren().add(list);
        } catch (IOException e) {
        }
    }

}