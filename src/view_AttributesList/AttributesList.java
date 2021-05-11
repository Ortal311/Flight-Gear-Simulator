package view_AttributesList;


import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.io.IOException;

public class AttributesList extends AnchorPane {


    public ObservableList lst;
    public DoubleProperty flag;
    AttributesListController alc;

    public AttributesList() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane list = fxl.load(getClass().getResource("AttributesList.fxml").openStream());
            alc = fxl.getController();
           // lst = alc.lv.getItems();

            this.getChildren().add(list);
        } catch (IOException e) {
        }
    }
        public void set(ViewModelController vmc)
        {
            alc.init(vmc);
        }

}