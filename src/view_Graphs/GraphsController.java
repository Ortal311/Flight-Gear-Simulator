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
    public StringProperty selectedAttribute;
    public DoubleProperty value, graphSpeed, timeStamp, valueCorrelate, x1, y1, x2, y2;
    public IntegerProperty sizeTS;
    private int rowNumber;
    double pointX;
    double pointY;
    Line line;

    public GraphsController() {
        this.selectedAttribute = new SimpleStringProperty();
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
        value.setValue(0);
        valueCorrelate.setValue(0);
        x1.setValue(0);
        x2.setValue(0);
        y1.setValue(0);
        y2.setValue(0);
        XYChart.Series series1 = new XYChart.Series();
        chosenAttributeGraph.getData().add(series1);
        //correlateGraph
        XYChart.Series series2 = new XYChart.Series();
        mostCorrelatedAttribute.getData().add(series2);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.YELLOW);

        //circle.centerXProperty().setValue(10);
        //circle.setFill(Color.TRANSPARENT);

        timeStamp.addListener((o, ov, nv) -> {
            Platform.runLater(() -> {
                    circle.setCenterX(timeStamp.getValue());
                    circle.setCenterY(timeStamp.getValue());
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

        value.addListener(v -> paintReg());
        valueCorrelate.addListener(v -> paintReg());


          x1.addListener(v->paintReg());
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
        gc.strokeLine(10,150-200,0,200);

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
//    private void updateLine() {
//        LinearGradient linearGradient = new LinearGradient(x1.get(), y1.get(), x2.get(), y2.get(), false, CycleMethod.REFLECT, new Stop(0,Color.RED),new Stop(1,Color.GREEN));
//        line.setStroke(linearGradient);
//    }
}