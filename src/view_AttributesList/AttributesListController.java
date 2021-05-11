package view_AttributesList;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import viewModel.ViewModelController;

public class AttributesListController {

    @FXML
    ListView lv;

    DoubleProperty dp;
    ViewModelController vmc;

    public AttributesListController() {

    }

    public void init(ViewModelController vmc)
    {
        dp.bind(vmc.flag);
        dp.addListener((u)->{
                    ObservableList<String> lst = FXCollections.observableArrayList();
                    lst.addAll(vmc.ts.getAttributes());
                    lv.setItems(lst);
                }
        );
    }
}
