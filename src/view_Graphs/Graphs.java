package view_Graphs;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Graphs extends AnchorPane {
    public StringProperty selectedAttribute,correlatedAttribute;
    public DoubleProperty value, valueCorrelate, graphSpeed, timeStamp;
    public IntegerProperty sizeTS;
    public ListProperty<Float>DataOfAttUntilIndex;

    GraphsController gc;


    public Graphs() {
        super();
        FXMLLoader fxl = new FXMLLoader();

        selectedAttribute = new SimpleStringProperty();
        correlatedAttribute=new SimpleStringProperty();
        value = new SimpleDoubleProperty();
        valueCorrelate = new SimpleDoubleProperty();
        graphSpeed = new SimpleDoubleProperty();
        timeStamp = new SimpleDoubleProperty();
        sizeTS = new SimpleIntegerProperty();
        DataOfAttUntilIndex=new SimpleListProperty<>();

        try {
            AnchorPane graph = fxl.load(getClass().getResource("Graphs.fxml").openStream());
            gc = fxl.getController();
            gc.init();
            // timeStamp.setValue(0);
            gc.value.bind(value);
            gc.timeStamp.bind(timeStamp);
            gc.selectedAttribute.bind(selectedAttribute);
            gc.correlatedAttribute.bind(correlatedAttribute);
            gc.valueCorrelate.bind(valueCorrelate);

            this.getChildren().add(graph);
        } catch (IOException e) {
        }
    }
}
