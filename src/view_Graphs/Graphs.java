package view_Graphs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Graphs extends AnchorPane {
    public DoubleProperty lineChart;//categoryAxis,numberAxis,

    GraphsController gc;
    public Graphs() {
        super();
        FXMLLoader fxl = new FXMLLoader();
//        categoryAxis=new SimpleDoubleProperty();
//        numberAxis=new SimpleDoubleProperty();


        try {
            AnchorPane graph = fxl.load(getClass().getResource("Graphs.fxml").openStream());
            gc = fxl.getController();
            //gc.lineChart.getData().get().dataProperty().bin
            gc.init();
            this.getChildren().add(graph);
        } catch (IOException e) {
        }
    }
}
