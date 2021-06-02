package view_Graphs;

import algo.Point;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import viewModel.ViewModelController;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GraphsController  {

    @FXML
    private LineChart<String, Number> chosenAttributeGraph, mostCorrelatedAttribute, anomalyDetectionGraph;
    private ListProperty<Point>pointsOfSelectedAttribute;
    public StringProperty selectedAttribute;
    public DoubleProperty value, graphSpeed, timeStamp,valueCorrelate;
    public IntegerProperty sizeTS;
    private int rowNumber;

    public GraphsController() {
        this.selectedAttribute = new SimpleStringProperty();
        this.value = new SimpleDoubleProperty();
        this.valueCorrelate=new SimpleDoubleProperty();
        this.timeStamp=new SimpleDoubleProperty();
    }

    public void init() {

        selectedAttribute.setValue("0");
        value.setValue(0);
        valueCorrelate.setValue(0);
        XYChart.Series series1=new XYChart.Series();
        chosenAttributeGraph.getData().add(series1);
        //correlateGraph
        XYChart.Series series2=new XYChart.Series();
        mostCorrelatedAttribute.getData().add(series2);



        timeStamp.addListener((o,ov,nv)->{
            Platform.runLater(() -> {

                //System.out.println(selectedAttribute.getValue().toString());
                // System.out.println(timeStamp.getValue());
                series1.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), value.doubleValue()));
                series2.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(),valueCorrelate.doubleValue()));
              /*  if(nv.doubleValue()<ov.doubleValue())
                {
                    series1.getData().remove(series1);
                    //need to create a new graph with all the points of the new attribute until now

                   // series1.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), value.doubleValue()));
                }*/

                //chosenAttributeGraph.setTitle(selectedAttribute.getValue().toString());
            });
        });

        chosenAttributeGraph.setCreateSymbols(false);
        mostCorrelatedAttribute.setCreateSymbols(false);


    }


}