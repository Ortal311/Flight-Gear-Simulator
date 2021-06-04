package view_Graphs;

import algo.Point;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.paint.Paint;

public class GraphsController {

    @FXML
    private LineChart<String, Number> chosenAttributeGraph, mostCorrelatedAttribute, anomalyDetectionGraph;
    @FXML
    private ScatterChart<String, Number> pointsReg;
    @FXML
    Canvas regPaint;


    private ListProperty<Point> pointsOfSelectedAttribute;
    public StringProperty selectedAttribute;
    public DoubleProperty value, graphSpeed, timeStamp, valueCorrelate;
    public IntegerProperty sizeTS;
    private int rowNumber;
    double pointX;
    double pointY;

    public GraphsController() {
        this.selectedAttribute = new SimpleStringProperty();
        this.value = new SimpleDoubleProperty();// also the val of X Axis
        this.valueCorrelate = new SimpleDoubleProperty();// also the val of Y Axis
        this.timeStamp = new SimpleDoubleProperty();
        regPaint = new Canvas();

    }

    public void init() {

        selectedAttribute.setValue("0");
        value.setValue(0);
        valueCorrelate.setValue(0);
        XYChart.Series series1 = new XYChart.Series();
        chosenAttributeGraph.getData().add(series1);
        //correlateGraph
        XYChart.Series series2 = new XYChart.Series();
        mostCorrelatedAttribute.getData().add(series2);

//        //reg line
//        XYChart.Series seriesReg = new XYChart.Series();
//
//        seriesReg.getData().add(new XYChart.Data("2.4", 37.6));
//        seriesReg.getData().add(new XYChart.Data("5.2", 229.2));
//        seriesReg.getData().add(new XYChart.Data("6.4", 15.6));
//
//        //reg points
//        XYChart.Series seriesPoints = new XYChart.Series();
//        seriesPoints.getData().add(new XYChart.Data("4.2", 193.2));
//        seriesPoints.getData().add(new XYChart.Data("2.8", 33.6));
//        seriesPoints.getData().add(new XYChart.Data("6.8", 23.6));
//
//
//        anomalyDetectionGraph.setAnimated(false);
//        anomalyDetectionGraph.setCreateSymbols(true);
//
//        anomalyDetectionGraph.getData().add(seriesReg);
//        pointsReg.setAnimated(false);
//
//        pointsReg.getData().add(seriesPoints);
//
//        pointsReg.setOpacity(0.5);

        //anomalyDetectionGraph.setStyle("-fx-background-color: green;");


        timeStamp.addListener((o, ov, nv) -> {
            Platform.runLater(() -> {

                //System.out.println(selectedAttribute.getValue().toString());
                // System.out.println(timeStamp.getValue());
                series1.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), value.doubleValue()));
                series2.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), valueCorrelate.doubleValue()));

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

        value.addListener(v->paintReg());
        valueCorrelate.addListener(v->paintReg());

        //  timeStamp.addListener(v->paintReg());

    }

    public void paintReg() {
        GraphicsContext gc = regPaint.getGraphicsContext2D();
         pointX = regPaint.getWidth() / 2;
         pointY = regPaint.getHeight() / 2;

        gc.clearRect(0,0, regPaint.getWidth(), regPaint.getHeight());
        gc.setLineWidth(1);
        gc.strokeLine(40, 10, 10, 40);


//    }


    }
    public void addPoint(Point p, Paint color) {
//        double displayX=(p.x/(maxValue-minValue))*width+width*(0-minValue)/(maxValue-minValue);
//        double displayY=height-(p.y/(maxValue-minValue))*height-height/2;
//        Circle toDisplay=new Circle();
//        toDisplay.setRadius(1);
//        toDisplay.setCenterX(displayX);
//        toDisplay.setCenterY(displayY);
//        toDisplay.setFill(color);
//        points.add(toDisplay);
//        algoPaint.getChildren().removeAll(points);
//        algoPaint.getChildren().addAll(points);

    }
}