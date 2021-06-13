package algo;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
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
    HashMap<String, String> attALG;
    HashMap<CorrelatedFeatureForAll, Circle> circlesMap;
    HashMap<String, ArrayList<Integer>> anomalyMapByAtt;
    SimpleAnomalyDetector ad;
    ZScoreAlgorithm zScore;

    List<CorrelatedFeatures> cfmore95list;

    HashMap<String, CorrelatedFeatureForAll> cfmore95;
    HashMap<String, CorrelatedFeatureForAll> cfless50;
    HashMap<String, CorrelatedFeatureForAll> cfBetween;

    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();

    public DoubleProperty valAtt1X = new SimpleDoubleProperty();//static for line- minValX
    public DoubleProperty valAtt2Y = new SimpleDoubleProperty();//static for line- minValY
    public DoubleProperty vaAtt1Xend = new SimpleDoubleProperty();//static for line -maxValX
    public DoubleProperty vaAtt2Yend = new SimpleDoubleProperty();//static for line -maxValY

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
        this.cfmore95list = ad.getCorrelateMore95lst();
        this.cfmore95 = ad.getCfmore95();
        this.cfBetween = ad.getCfBetween();
        this.cfless50 = ad.getCorrelateless50();

        WelzlAlgorithm algorithm = new WelzlAlgorithm();
        TimeSeries tsZscore = new TimeSeries();

        cfAlgo = new HashMap<>();
        attALG = new HashMap<>();

        cfAlgo.put("ZScore", new HashSet<>());
        cfAlgo.put("Regression", new HashSet<>());
        cfAlgo.put("Welzl", new HashSet<>());

        System.out.println("Reggretion");
        for (Map.Entry<String, CorrelatedFeatureForAll> alg : cfmore95.entrySet()) {
            attALG.put(alg.getKey(), alg.getValue().nameALG);

             System.out.println(alg.getValue().nameALG+" "+alg.getValue().feature1+" "+alg.getValue().feature2+" "+alg.getValue().corrlation);
        }
        System.out.println("Welze");
        for (Map.Entry<String, CorrelatedFeatureForAll> alg : cfBetween.entrySet()) {
            attALG.put(alg.getKey(), alg.getValue().nameALG);
             System.out.println(alg.getValue().nameALG+" "+alg.getValue().feature1+" "+alg.getValue().feature2+" "+alg.getValue().corrlation);
        }
        System.out.println("Zscore");
        for (Map.Entry<String, CorrelatedFeatureForAll> alg : cfless50.entrySet()) {
            attALG.put(alg.getKey(), alg.getValue().nameALG);
            System.out.println(alg.getValue().nameALG + " " + alg.getValue().feature1 + " " + alg.getValue().feature2 + " " + alg.getValue().corrlation);

        }
        System.out.println("size of all att ALG: " + attALG.size());
        System.out.println("size of cf is " + cf.size());

//        for (CorrelatedFeatures c : cfAlgo.get("Welzl")) {//activate welze on all the attribute with 0.5-0.95 correlation
//            circlesMap.put(c, algorithm.miniDisk(toListPoints(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2))));
//        }
        for (String key : cfBetween.keySet()) {//activate welze on all the attribute with 0.5-0.95 correlation
            // CorrelatedFeatureForAll c= new CorrelatedFeatureForAll(cfBetween.get(i).feature1,cfBetween.get(i).feature2,cfBetween.get(i).nameALG,cfBetween.get(i).corrlation);
            circlesMap.put(cfBetween.get(key), algorithm.miniDisk(toListPoints(ts.getAttributeData(cfBetween.get(key).feature1), ts.getAttributeData(cfBetween.get(key).feature2))));
        }
        System.out.println("the size of welzl var is: " + circlesMap.size());


        //check the ZScore for a
        for (String key : cfless50.keySet()) {
            tsZscore.ts.put(cfless50.get(key).feature1, ts.getAttributeData(cfless50.get(key).feature1));
            tsZscore.ts.put(cfless50.get(key).feature2, ts.getAttributeData(cfless50.get(key).feature2));
        }
        System.out.println("the size of tszscore is : " + tsZscore.ts.size());
        zScore = new ZScoreAlgorithm();
        zScore.learnNormal(tsZscore);

        ad.learnNormal(ts);


        for (CorrelatedFeatureForAll key : circlesMap.keySet()) {
            System.out.println(key.feature1 + " " + key.feature2 + " " + key.nameALG);
        }
        System.out.println("the size of circle map is:  " + circlesMap.size());

    }

    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> lst = new ArrayList<>();
        lst.addAll(ad.detect(ts));
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

        return lst;
    }

    public Circle getCircle(String att1) {
        // System.out.println("f1: "+att1);

        for (CorrelatedFeatureForAll cf : circlesMap.keySet()) {
            if (cf.feature1.equals(att1) || cf.feature2.equals(att1))
                return circlesMap.get(cf);
//            System.out.println(cf.feature1 + " " + cf.feature2);
        }
        return null;
    }

    public AnchorPane paint() {
        AnchorPane board = new AnchorPane();

        //data for CircleALG
        BubbleChart<Number, Number> circleGraph = new BubbleChart(new NumberAxis(), new NumberAxis());
        circleGraph.setAnimated(false);
        circleGraph.setAnimated(false);
        circleGraph.setPrefSize(250, 250);
        XYChart.Series<Number, Number> seriesPoints = new XYChart.Series();
        XYChart.Series<Number, Number> seriesPointsAnomal = new XYChart.Series();
        XYChart.Series<Number, Number> seriesCircle = new XYChart.Series();
        circleGraph.getData().addAll(seriesPoints, seriesCircle);
        //seriesPoints.getNode().setStyle("-fx-background-color: blue;");


        //Data for Zscor
        LineChart<Number, Number> sc = new LineChart<>(new NumberAxis(), new NumberAxis());
        sc.setAnimated(false);
        sc.setCreateSymbols(false);
        sc.setPrefSize(350, 250);
        XYChart.Series line = new XYChart.Series();
        XYChart.Series lineAnomal = new XYChart.Series();
        sc.getData().addAll(line, lineAnomal);
        lineAnomal.getNode().setStyle("-fx-stroke: red;");

        // Data for Reg
        LineChart<Number, Number> regBoard = new LineChart<>(new NumberAxis(), new NumberAxis());
        regBoard.setPrefSize(350, 250);
        XYChart.Series pointsNormal = new XYChart.Series();//points for normal Flight
        XYChart.Series pointsAnomal = new XYChart.Series();//points for Anomaly parts
        XYChart.Series regLine = new XYChart.Series();//line
        regBoard.getData().addAll(pointsNormal, regLine, pointsAnomal);


        //board.getChildren().add(circleGraph);
        regBoard.getStylesheets().add("style.css");
        //circleGraph.getStylesheets().add("style.css");

        attribute1.addListener((ob, oldV, newV) -> {//to delete the old graph if attribute has changed

            timeStep.addListener((o, ov, nv) -> {

                if (attALG.get(attribute1.getValue().toString()).equals("Welzl")) {
                    System.out.println("welze has been activated");
                    sc.setVisible(false);
                    regBoard.setVisible(false);
                    circleGraph.setVisible(true);

                    pointsAnomal.getData().clear();
                    pointsAnomal.getData().clear();
                    regLine.getData().clear();
                    line.getData().clear();
                    lineAnomal.getData().clear();

           //         if (getCircle(attribute1.getValue()) != null) {//if it's null there is no circle for them, and we'll need to activate a different ALG
                        double radius = getCircle(attribute1.getValue()).radius;
                        float x = getCircle(attribute1.getValue()).center.x;
                        float y = getCircle(attribute1.getValue()).center.y;

                        Platform.runLater(() -> {
                            seriesCircle.getData().add(new XYChart.Data(x, y, radius));

                            if (!anomalyMapByAtt.containsKey(attribute1.getValue())) {

                                seriesPoints.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), (radius / 10)));//points
                            } else {// if there are anomalies
                                if (!anomalyMapByAtt.get(attribute1.getValue()).contains(timeStep.intValue())) {

                                    seriesPoints.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), (radius / 10)));//points

                                } else {

                                    seriesPointsAnomal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue(), 0.2));//points of anomaly

                                }
                            }
                        });

                        if (!newV.equals(oldV)) {//if change the attribute
                            seriesPoints.getData().clear();
                            seriesPointsAnomal.getData().clear();
                            seriesCircle.getData().clear();
                        }
//                    }
                } else if (attALG.get(attribute1.getValue().toString()).equals("Regression")) {
                    System.out.println("regression has been activated");
                    sc.setVisible(false);
                    regBoard.setVisible(true);
                    circleGraph.setVisible(false);

                    seriesCircle.getData().clear();
                    seriesPoints.getData().clear();
                    seriesPointsAnomal.getData().clear();
                    pointsAnomal.getData().clear();
                    pointsAnomal.getData().clear();
                    regLine.getData().clear();
                    Platform.runLater(() -> {
                        if (!this.ad.anomalyAndTimeStep.containsKey(attribute1.getValue())) {
//                        if (nv.doubleValue() > ov.doubleValue() + 30) {
//                            series1.getData().remove(0);
//                        }
                            pointsNormal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points
                            regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                            regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point

                        } else {
                            if (!this.ad.anomalyAndTimeStep.get(attribute1.getValue()).contains(timeStep.intValue())) {
                                pointsNormal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points

                                regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                                regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point
                            } else {
                                // sc.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

                                pointsAnomal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points of anomaly
                                regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                                regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(),vaAtt2Yend.doubleValue()));//reg sec point

                                //  sc.setBackground(null);
                            }
                        }
                    });
                } else if (attALG.get(attribute1.getValue().toString()).equals("ZScore")) {
                    System.out.println("ZScore has been activated");
                    sc.setVisible(true);
                    regBoard.setVisible(false);
                    circleGraph.setVisible(false);

                    seriesCircle.getData().clear();
                    seriesPoints.getData().clear();
                    seriesPointsAnomal.getData().clear();

                    Platform.runLater(() -> {
                        if ((zScore.ZScoreAnomaly.size() != 0) && !zScore.ZScoreAnomaly.containsKey(zScore.Attribute.getValue())) {// i dont think it's work
                            lineAnomal.getData().add(new XYChart.Data<>(timeStep.getValue(), zScore.ZScoreAnomaly.get(attribute1.getValue().toString()).get(timeStep.intValue())));
                        } else {
                            line.getData().add(new XYChart.Data<>(timeStep.getValue(), zScore.ZScoreReg.get(attribute1.getValue().toString()).get(timeStep.intValue())));
                        }
                    });

                    if (!newV.equals(oldV)) {//if change the attribute
                        line.getData().clear();
                    }
                }
            });
        });

        board.getChildren().addAll(circleGraph, sc, regBoard);
        return board;
    }
//    public AnchorPane paintCircle(AnchorPane board){
//
//    }
}
