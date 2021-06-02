package view_Graphs;

import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Graphs extends AnchorPane {
    public DoubleProperty lineChart;//categoryAxis,numberAxis,
    public StringProperty selectedAttribute;
    public DoubleProperty value,valueCorrelate,graphSpeed,timeStamp;
    public IntegerProperty sizeTS;

    GraphsController gc;


    public Graphs() {
        super();
        FXMLLoader fxl = new FXMLLoader();

        selectedAttribute = new SimpleStringProperty();
        value=new SimpleDoubleProperty();
        valueCorrelate=new SimpleDoubleProperty();
        graphSpeed=new SimpleDoubleProperty();
        timeStamp=new SimpleDoubleProperty();
        sizeTS=new SimpleIntegerProperty();
//this.graphicsContext=new
        try {
            AnchorPane graph = fxl.load(getClass().getResource("Graphs.fxml").openStream());
            gc = fxl.getController();
            gc.init();
            // timeStamp.setValue(0);
            gc.value.bind(value);
            gc.timeStamp.bind(timeStamp);
            gc.selectedAttribute.bind(selectedAttribute);
            gc.valueCorrelate.bind(valueCorrelate);

            
            this.getChildren().add(graph);
        } catch (IOException e) {
        }
    }
}
