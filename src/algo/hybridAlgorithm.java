package algo;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import viewModel.TimeSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

public class hybridAlgorithm {

    HashMap<String, HashSet<CorrelatedFeatures>> cfAlgo;
    HashMap<CorrelatedFeatures, Circle> circlesMap;
    HashMap<String, ArrayList<Integer>> anomalyMapByAtt;
    SimpleAnomalyDetector ad;
    ZScoreAlgorithm zScore;

    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();

    public DoubleProperty valPointX = new SimpleDoubleProperty();
    public DoubleProperty valPointY = new SimpleDoubleProperty();

    public DoubleProperty timeStep = new SimpleDoubleProperty();

    public hybridAlgorithm() {
        circlesMap = new HashMap<>();
        anomalyMapByAtt = new HashMap<>();
    }

    private List<Point> toListPoints(List<Float> x, List<Float> y) {
        List<Point> lst = new ArrayList<>();
        for (int i = 0; i < x.size(); i++)
            lst.add(new Point(x.get(i), y.get(i)));
        return lst;
    }

    public void learnNormal(TimeSeries ts) {
        ad = new SimpleAnomalyDetector();
        ad.learnNormal(ts);
        List<CorrelatedFeatures> cf = ad.getNormalModel();

        WelzlAlgorithm algorithm = new WelzlAlgorithm();
        TimeSeries tsZscore = new TimeSeries();

        cfAlgo = new HashMap<>();
        cfAlgo.put("ZScore", new HashSet<>());
        cfAlgo.put("Regression", new HashSet<>());
        cfAlgo.put("Welzl", new HashSet<>());
        for (CorrelatedFeatures c : cf) {
            if (Math.abs(c.corrlation) >= 0.95)
                cfAlgo.get("Regression").add(c);
            else {
                if (Math.abs(c.corrlation) < 0.5)
                    cfAlgo.get("ZScore").add(c);
                else
                    cfAlgo.get("Welzl").add(c);
            }
        }

        for (CorrelatedFeatures c : cfAlgo.get("Welzl")) {
            circlesMap.put(c, algorithm.miniDisk(toListPoints(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2))));
        }

        for (CorrelatedFeatures c : cfAlgo.get("ZScore")) {
            tsZscore.ts.put(c.feature1, ts.getAttributeData(c.feature1));
            tsZscore.ts.put(c.feature2, ts.getAttributeData(c.feature2));
        }

        zScore = new ZScoreAlgorithm();
        zScore.learnNormal(tsZscore);
        ad.learnNormal(ts);


        for (CorrelatedFeatures key: circlesMap.keySet()) {
            System.out.println(key.feature1+" "+key.feature2);
            System.out.println("the size of circle map is:  "+circlesMap.size());

        }
    }

    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> lst = new ArrayList<>();
        // lst.addAll(ad.detect(ts));
        TimeSeries zScoreTest = new TimeSeries();

        for (CorrelatedFeatures c : cfAlgo.get("ZScore")) {
            zScoreTest.ts.put(c.feature1, ts.getAttributeData(c.feature1));
            zScoreTest.ts.put(c.feature2, ts.getAttributeData(c.feature2));
        }

        lst.addAll(zScore.detect(zScoreTest));

        for (CorrelatedFeatures c : cfAlgo.get("Welzl")) {

            ArrayList<Integer> anomalyLst = new ArrayList<>();
            List<Point> ps = toListPoints(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2));

            for (int i = 0; i < ps.size(); i++) {
                if (!circlesMap.get(c).isInside(ps.get(i))) {
                    lst.add(new AnomalyReport(c.feature1 + "-" + c.feature2, i));
                    anomalyLst.add(i);
                }
            }
            anomalyMapByAtt.put(c.feature1, anomalyLst);
        }
//        System.out.println("all the anomaly in welzl");
//
//        for(int i=0;i<lst.size();i++){
//            System.out.println(lst.get(i).description+ "  "+ lst.get(i).timeStep);
//        }
        return lst;
    }

    public Circle getCircle(String att1, String att2) {
        System.out.println("f1: "+att1);
        System.out.println("f2: "+att2);
        for (CorrelatedFeatures cf : circlesMap.keySet()) {
            if (cf.feature1.equals(att1) && cf.feature2.equals(att2) || cf.feature1.equals(att2) && cf.feature2.equals(att1))
                return circlesMap.get(cf);
            System.out.println(cf.feature1+" "+cf.feature2);
        }
        return null;
    }

    public AnchorPane paint() {
        AnchorPane board = new AnchorPane();

        BubbleChart<Number, Number> zscoreGraph = new BubbleChart(new NumberAxis(), new NumberAxis());
        zscoreGraph.setAnimated(false);

        XYChart.Series<Number, Number> seriesPoints = new XYChart.Series();
        XYChart.Series<Number, Number> seriesPointsAnomal = new XYChart.Series();
        XYChart.Series<Number, Number> seriesCircle = new XYChart.Series();

        zscoreGraph.getData().addAll(seriesPoints, seriesCircle);
        //seriesPoints.getNode().setStyle("-fx-background-color: blue;");



        zscoreGraph.setPrefSize(250, 250);

       attribute2.addListener((ob, oldV, newV) -> {//to delete the old graph if attribute has changed
            System.out.println("before if hyper");
            getCircle(attribute1.getValue(),attribute2.getValue());
            if (getCircle(attribute1.getValue(), attribute2.getValue()) != null) {//if it's null there is no circle for them, and we'll need to activate a different ALG
                double radius = getCircle(attribute1.getValue(), attribute2.getValue()).radius;
                float x = getCircle(attribute1.getValue(), attribute2.getValue()).center.x;
                float y = getCircle(attribute1.getValue(), attribute2.getValue()).center.y;
                System.out.println("inside hyper");
                timeStep.addListener((o, ov, nv) -> {

                    Platform.runLater(() -> {
                        seriesCircle.getData().add(new XYChart.Data(x, y, radius));
                               //seriesCircle.getData().add(new XYChart.Data(4, 4, 3));
                        if (!anomalyMapByAtt.containsKey(attribute1.getValue())) {
//                        if (nv.doubleValue() > ov.doubleValue() + 30) {
//                            series1.getData().remove(0);
//                        }
                            seriesPoints.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), (radius/10)));//points
                        } else {// if there are anomalies
                            if (!anomalyMapByAtt.get(attribute1.getValue()).contains(timeStep.intValue())) {

                                seriesPoints.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), (radius/10)));//points

                           } else {
                                // sc.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

                                seriesPointsAnomal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), 0.2));//points of anomaly

                                //  sc.setBackground(null);
                            }
                        }
                    });
                });

//            if(attribute2.getValue() == null){
//                series3.getData().clear();
//                series1.getData().clear();
//                series2.getData().clear();
//            }
            }
           if (!newV.equals(oldV)) {//if change the attribute
               seriesPoints.getData().clear();
               seriesPointsAnomal.getData().clear();
               seriesCircle.getData().clear();
           }
        });


//        seriesCircle.getData().add(new XYChart.Data(4, 4, 3));
//        seriesPoints.getData().add(new XYChart.Data(3, 3, 0.2));
//        seriesPoints.getData().add(new XYChart.Data(6, 6, 0.2));
//        seriesPoints.getData().add(new XYChart.Data(3, 6, 0.2));
//        seriesPoints.getData().add(new XYChart.Data(4, 4, 0.2));

        board.getChildren().add(zscoreGraph);
        zscoreGraph.getStylesheets().add("style.css");

        return board;
    }
}
