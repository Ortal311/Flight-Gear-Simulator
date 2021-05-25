package view_AttributesList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import viewModel.ViewModelController;

public class AttributesListController {

    @FXML
    ListView lv;

    DoubleProperty dp;

    public AttributesListController() {
        dp= new SimpleDoubleProperty();
    }
}
