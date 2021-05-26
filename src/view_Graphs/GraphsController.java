package view_Graphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;

public class GraphsController {
 /*   @FXML
    CategoryAxis x;
    @FXML
    NumberAxis y;*/
    @FXML
    LineChart<?, ?> lineChart;

    public void init() {
        Axis xAxis = new NumberAxis();
        Axis yAxis = new NumberAxis();
        XYChart.Series series = new XYChart.Series();

        series.getData().add(new XYChart.Data("1", 23));
        series.getData().add(new XYChart.Data("2", 55));
        series.getData().add(new XYChart.Data("3", 30));
        series.getData().add(new XYChart.Data("4", 35));
        series.getData().add(new XYChart.Data("5", 23));
        series.getData().add(new XYChart.Data("6", 55));
        series.getData().add(new XYChart.Data("7", 30));
        series.getData().add(new XYChart.Data("8", 35));
        series.getData().add(new XYChart.Data("9", 23));
        series.getData().add(new XYChart.Data("10", 55));
        series.getData().add(new XYChart.Data("11", 30));
        series.getData().add(new XYChart.Data("12", 35));
        series.getData().add(new XYChart.Data("13", 23));
        series.getData().add(new XYChart.Data("14", 55));
        series.getData().add(new XYChart.Data("15", 30));
        series.getData().add(new XYChart.Data("16", 35));

        ObservableList<XYChart.Series> seriesList = FXCollections.observableArrayList();


        lineChart.getData().addAll(series);
        lineChart.setCreateSymbols(false);//to remove the points from the graph


    }
}
