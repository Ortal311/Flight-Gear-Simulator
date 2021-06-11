package view_Graphs;

import algo.Point;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GraphsController {

    @FXML
    private LineChart<String, Number> chosenAttributeGraph, mostCorrelatedAttribute, anomalyDetectionGraph;
    @FXML
    private ScatterChart<String, Number> pointsReg;
    @FXML
    Canvas regPaint;
    @FXML
    Circle circle;


    private ListProperty<Point> pointsOfSelectedAttribute;
    public StringProperty selectedAttribute,correlatedAttribute;
    public DoubleProperty value, graphSpeed, timeStamp, valueCorrelate, x1, y1, x2, y2;
    public IntegerProperty sizeTS;
    private int rowNumber;
    double pointX;
    double pointY;
    Line line;

    public GraphsController() {
        this.selectedAttribute = new SimpleStringProperty();
        this.correlatedAttribute=new SimpleStringProperty();
        this.value = new SimpleDoubleProperty();// also the val of X Axis
        this.valueCorrelate = new SimpleDoubleProperty();// also the val of Y Axis
        this.timeStamp = new SimpleDoubleProperty();

        //4 points to draw the reg_line
        this.x1 = new SimpleDoubleProperty();
        this.x2 = new SimpleDoubleProperty();
        this.y1 = new SimpleDoubleProperty();
        this.y2 = new SimpleDoubleProperty();

        regPaint = new Canvas();
        circle = new Circle();


    }

    public void init() {

        selectedAttribute.setValue("0");
        correlatedAttribute.setValue("0");
        value.setValue(0);
        valueCorrelate.setValue(0);
        x1.setValue(0);
        x2.setValue(0);
        y1.setValue(0);
        y2.setValue(0);
        XYChart.Series series1 = new XYChart.Series();
        chosenAttributeGraph.getData().add(series1);
        series1.getNode().setStyle("-fx-stroke: black;");

        //correlateGraph
        XYChart.Series series2 = new XYChart.Series();
        mostCorrelatedAttribute.getData().add(series2);
        series2.getNode().setStyle("-fx-stroke: black;");

        timeStamp.addListener((o, ov, nv) -> {
            Platform.runLater(() -> {
                series1.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), value.doubleValue()));
                series2.getData().add(new XYChart.Data<>(timeStamp.getValue().toString(), valueCorrelate.doubleValue()));
            });
            // note if we move the slider the condition will be T cuz it'll also go a bit backward
            if (nv.doubleValue() < ov.doubleValue()) {
                series1.getData().clear();
                series2.getData().clear();
            }
        });


        chosenAttributeGraph.setCreateSymbols(false);
        mostCorrelatedAttribute.setCreateSymbols(false);


        value.addListener(v -> paintReg());
        valueCorrelate.addListener(v -> paintReg());


        x1.addListener(v -> paintReg());
        GraphicsContext gc = regPaint.getGraphicsContext2D();


        //gc.clearRect(0, 0, regPaint.getWidth(), regPaint.getHeight());


    }

    public void paintReg() {
        GraphicsContext gc = regPaint.getGraphicsContext2D();
        pointX = regPaint.getWidth() / 2;
        pointY = regPaint.getHeight() / 2;

        gc.clearRect(0, 0, regPaint.getWidth(), regPaint.getHeight());
        gc.setLineWidth(1);
        //  gc.strokeLine(x1.doubleValue(), y1.doubleValue(), x2.doubleValue(), y2.doubleValue());
        gc.strokeLine(10, 150 - 200, 0, 200);

    }

}