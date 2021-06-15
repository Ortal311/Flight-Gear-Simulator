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

    public TimeSeries tsZscore;
    public TimeSeries tsZscoreAnomal;

    public TimeSeries tsReg;
    public TimeSeries tsRegAnomal;


    ArrayList<CorrelatedFeatures> cfmore95list;

    //for Hybrid
    HashMap<String, CorrelatedFeatures> cfmore95;

    HashMap<String, CorrelatedFeatureForAll> cfless50;

    HashMap<String, CorrelatedFeatureForAll> cfBetween;

    //for ZSore
    public HashMap<String, ArrayList<Float>> ZScoreReg;
    public HashMap<String, ArrayList<Integer>> ZScoreAnomaly;

    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();

    public Line regLineForCorrelateAttribute;


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

        this.cfmore95list = new ArrayList<>();
        this.cfmore95 = new HashMap<>();
        this.cfBetween = new HashMap<>();
        this.cfless50 = new HashMap<>();

        ad = new SimpleAnomalyDetector();
        zScore = new ZScoreAlgorithm();

        tsReg = new TimeSeries();
        tsRegAnomal = new TimeSeries();
        tsZscore = new TimeSeries();
        tsZscoreAnomal = new TimeSeries();
    }

    private List<Point> toListPoints(List<Float> x, List<Float> y) {
        List<Point> lst = new ArrayList<>();
        for (int i = 0; i < x.size(); i++)
            lst.add(new Point(x.get(i), y.get(i)));
        return lst;
    }

    public void learnNormal(TimeSeries ts) {

        findCorrelation(ts);
        // ad.cf=cfmore95list;

        WelzlAlgorithm algorithm = new WelzlAlgorithm();


        int index = 0;
        for (String att : cfless50.keySet()) {
            tsZscore.ts.put(att, ts.ts.get(att));
            tsZscore.tsNum.put(index++, ts.ts.get(att));
            tsZscore.atts.add(att);
        }
        index = 0;
        for (String att : cfmore95.keySet()) {
            String att2 = cfmore95.get(att).feature2;
            tsReg.ts.put(att, ts.ts.get(att));
            Integer index1 = ts.atts.indexOf(att);
            tsReg.tsNum.put(index++, ts.tsNum.get(index1));
            Integer index2 = ts.atts.indexOf(att2);
            tsReg.ts.put(att2, ts.ts.get(att));
            tsReg.tsNum.put(index++, ts.ts.get(index2));

            tsReg.atts.add(att);
            tsReg.atts.add(att2);
        }

        ad.learnNormal(tsReg);
        for(CorrelatedFeatures cf: ad.cf){
            System.out.println("lala"+ cf.feature1);

        }
        zScore.learnNormal(tsZscore);
        ZScoreReg = zScore.getZScoreReg();
        ZScoreAnomaly = zScore.getZscoreAnomal();

        attALG = new HashMap<>();

        System.out.println("Regression");
        for (Map.Entry<String, CorrelatedFeatures> alg : cfmore95.entrySet()) {
            attALG.put(alg.getKey(), "Regression");
           // System.out.println(alg.getValue() + " " + alg.getValue().feature1 + " " + alg.getValue().feature2 + " " + alg.getValue().corrlation);
        }
        System.out.println("Welze");
        for (Map.Entry<String, CorrelatedFeatureForAll> alg : cfBetween.entrySet()) {
            attALG.put(alg.getKey(), alg.getValue().nameALG);
            //System.out.println(alg.getValue().nameALG + " " + alg.getValue().feature1 + " " + alg.getValue().feature2 + " " + alg.getValue().corrlation);
        }
        System.out.println("Zscore");
        for (Map.Entry<String, CorrelatedFeatureForAll> alg : cfless50.entrySet()) {
            attALG.put(alg.getKey(), alg.getValue().nameALG);
            //  System.out.println(alg.getValue().nameALG + " " + alg.getValue().feature1 + " " + alg.getValue().feature2 + " " + alg.getValue().corrlation);

        }
        System.out.println("size of all att ALG: " + attALG.size());
        // System.out.println("size of cf is " + cf.size());

        for (String key : cfBetween.keySet()) {//activate welze on all the attribute with 0.5-0.95 correlation
            circlesMap.put(cfBetween.get(key), algorithm.miniDisk(toListPoints(ts.getAttributeData(cfBetween.get(key).feature1), ts.getAttributeData(cfBetween.get(key).feature2))));
        }
    }


    public void findCorrelation(TimeSeries ts) {

        ArrayList<String> atts = ts.getAttributes();
        int len = ts.getRowSize();

        float vals[][] = new float[atts.size()][len];

        for (int i = 0; i < atts.size(); i++) {
            for (int j = 0; j < ts.getRowSize(); j++) {
                vals[i][j] = ts.getAttributeData(atts.get(i)).get(j);
            }
        }

        for (int i = 0; i < atts.size(); i++) {
            for (int j = i + 1; j < atts.size(); j++) {
                float p = StatLib.pearson(vals[i], vals[j]);//for the pearson
                float threshold;
                if (Math.abs(p) >= 0.95) {

                    if (!cfmore95.containsKey(atts.get(i))) {

                        Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                        Line lin_reg = StatLib.linear_reg(ps);
                        threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase
                        CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);//att1_att2_pearsonCorrelate_null_threshold(the max one)

                        cfmore95.put(atts.get(i), c);



                        if (cfBetween.containsKey(atts.get(i)))//if Wezele had the attribute we'll put it here instead
                            cfBetween.remove(atts.get(i));
                        if (cfless50.containsKey(atts.get(i)))
                            cfless50.remove(atts.get(i));

                    } else // if contain the attribute we'll take the max
                    {
                        if (cfmore95.get(atts.get(i)).corrlation < Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                            Line lin_reg = StatLib.linear_reg(ps);
                            threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase
                            CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);//att1_att2_pearsonCorrelate_null_threshold(the max one)


                            cfmore95.get(atts.get(i)).feature2 = atts.get(j);
                            cfmore95.get(atts.get(i)).corrlation = Math.abs(p);
                        }
                    }
                } else if ((!cfmore95.containsKey(atts.get(i))) && (0.5 <= Math.abs(p)) && (Math.abs(p) < 0.95)) {// 0.5<val<0.95

                    if (!cfBetween.containsKey(atts.get(i))) {
                        CorrelatedFeatureForAll ca = new CorrelatedFeatureForAll(atts.get(i), atts.get(j), "Welzl", Math.abs(p));
                        cfBetween.put(atts.get(i), ca);
                        if (cfless50.containsKey(atts.get(i)))//if ZScore had the attribute we'll put it here instead
                            cfless50.remove(atts.get(i));
                    } else // if contain the attribute we'll take the max
                    {
                        if (cfBetween.get(atts.get(i)).corrlation < Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            cfBetween.get(atts.get(i)).feature2 = atts.get(j);
                            cfBetween.get(atts.get(i)).corrlation = Math.abs(p);
                        }
                    }
                } else if ((Math.abs(p) < 0.5) && (!cfBetween.containsKey(atts.get(i))) && (!cfmore95.containsKey(atts.get(i)))) {
                    if (!cfless50.containsKey(atts.get(i))) {
                        CorrelatedFeatureForAll ca = new CorrelatedFeatureForAll(atts.get(i), atts.get(j), "ZScore", Math.abs(p));
                        cfless50.put(atts.get(i), ca);
                    } else // if contain the attribute we'll take the max
                    {
                        if (cfless50.get(atts.get(i)).corrlation < Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            cfless50.get(atts.get(i)).feature2 = atts.get(j);
                            cfless50.get(atts.get(i)).corrlation = Math.abs(p);
                        }
                    }
                }
            }
        }
    }

    private float findThreshold(Point ps[], Line rl) {// To find Anomal
        float max = 0;
        for (int i = 0; i < ps.length; i++) {
            float d = Math.abs(ps[i].y - rl.f(ps[i].x));
            if (d > max)
                max = d;
        }
        return max;
    }


    public List<AnomalyReport> detect(TimeSeries ts) {

        int index = 0;
        for (String att : cfless50.keySet()) {
            tsZscoreAnomal.ts.put(att, ts.ts.get(att));
            tsZscoreAnomal.tsNum.put(index++, ts.ts.get(att));
            tsZscoreAnomal.atts.add(att);
        }
        index = 0;
        for (String att : cfmore95.keySet()) {
            String att2 = cfmore95.get(att).feature2;

            Integer index1 = ts.atts.indexOf(att);
            tsRegAnomal.tsNum.put(index++, ts.tsNum.get(index1));
            tsRegAnomal.ts.put(att, ts.ts.get(att));
            tsRegAnomal.atts.add(att);

            Integer index2 = ts.atts.indexOf(att2);
            tsRegAnomal.ts.put(att2, ts.ts.get(att2));
            tsRegAnomal.tsNum.put(index++, ts.ts.get(index2));
            tsRegAnomal.atts.add(att2);
        }

        List<AnomalyReport> lst = new ArrayList<>();
        List<AnomalyReport> lstAD = ad.detect(tsRegAnomal);
        lst.addAll(ad.detect(tsRegAnomal));
        lst.addAll(zScore.detect(tsZscoreAnomal));

        for (CorrelatedFeatureForAll c : cfBetween.values()) {
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
        for(AnomalyReport ar : lst){
            System.out.println("ADI"+ar.description+" "+ ar.timeStep);
          //  System.out.println(ar.description+" "+ar.timeStep);
        }

        return lst;
    }

    public Circle getCircle(String att1) {
        for (CorrelatedFeatureForAll cf : circlesMap.keySet()) {
            if (cf.feature1.equals(att1) || cf.feature2.equals(att1))
                return circlesMap.get(cf);
        }
        return null;
    }

    public void initDataForGraphTimeChange() {
        valPointX.setValue(tsRegAnomal.getValueByTime(attribute1.getValue(), timeStep.intValue()));
        if (attribute2.getValue() != null)
            valPointY.setValue(tsRegAnomal.getValueByTime(attribute2.getValue(), timeStep.intValue()));

    }

    public void initDataForGraphAttChange() {
        System.out.println(attribute1.getValue());
        System.out.println(attribute2.getValue());
        regLineForCorrelateAttribute = cfmore95.get(attribute1.getValue()).lin_reg;
        valAtt1X.setValue(tsReg.getMinFromAttribute(attribute1.getValue()));
        valAtt2Y.setValue(regLineForCorrelateAttribute.f(valAtt1X.floatValue()));
        vaAtt1Xend.setValue(tsReg.getMaxFromAttribute(attribute2.getValue()));

        vaAtt2Yend.setValue(regLineForCorrelateAttribute.f(vaAtt1Xend.floatValue()));
    }


    public AnchorPane paint() {
        AnchorPane board = new AnchorPane();

        //data for CircleALG
        BubbleChart<Number, Number> circleGraph = new BubbleChart(new NumberAxis(), new NumberAxis());
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
        sc.setPrefSize(290, 218);
        XYChart.Series line = new XYChart.Series();
        XYChart.Series lineAnomal = new XYChart.Series();
        sc.getData().addAll(line, lineAnomal);
        lineAnomal.getNode().setStyle("-fx-stroke: red;");

        // Data for Reg
        LineChart<Number, Number> regBoard = new LineChart<>(new NumberAxis(), new NumberAxis());
        regBoard.setPrefSize(290, 218);
        XYChart.Series pointsNormal = new XYChart.Series();//points for normal Flight
        XYChart.Series pointsAnomal = new XYChart.Series();//points for Anomaly parts
        XYChart.Series regLine = new XYChart.Series();//line
        regBoard.getData().addAll(pointsNormal, regLine, pointsAnomal);
        pointsAnomal.getNode().setStyle("-fx-stroke: red;");

        //board.getChildren().add(circleGraph);
        board.getStylesheets().add("style.css");
        //circleGraph.getStylesheets().add("style.css");

//        public void mylistener(int ov, int olv, int newvv){
//            //Do....
//        }
//        attribute1.addListener((o, ov, nv)->mylistener());
//        timeStep.add()
        attribute1.addListener((ob, oldV, newV) -> {//to delete the old graph if attribute has changed
            if(oldV != null && !oldV.equals(newV)){
                seriesCircle.getData().clear();
                seriesPoints.getData().clear();
                seriesPointsAnomal.getData().clear();
                pointsNormal.getData().clear();
                pointsAnomal.getData().clear();
                regLine.getData().clear();
                line.getData().clear();
                lineAnomal.getData().clear();
            }
            attribute2.setValue(cfmore95.get(attribute1.getValue()).feature2);

               if (attribute2.getValue() != null) {

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
                    initDataForGraphAttChange();
                    initDataForGraphTimeChange();

                    System.out.println("regression has been activated");
                    sc.setVisible(false);
                    regBoard.setVisible(true);
                    circleGraph.setVisible(false);



                    Platform.runLater(() -> {
                        if (!ad.anomalyAndTimeStep.containsKey(attribute1.getValue())) {
//                        if (nv.doubleValue() > ov.doubleValue() + 30) {
//                            series1.getData().remove(0);
//                        }
                            pointsNormal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points
                            regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                            regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point

                        } else {
                            if (!ad.anomalyAndTimeStep.get(attribute1.getValue()).contains(timeStep.intValue())) {
                                pointsNormal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points

                                regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                                regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point
                            } else {
                                // sc.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

                                pointsAnomal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points of anomaly
                                regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                                regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point

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
                        if ((ZScoreAnomaly.size() != 0) && !ZScoreAnomaly.containsKey(attribute1.getValue())) {// i dont think it's work
                            lineAnomal.getData().add(new XYChart.Data<>(timeStep.getValue(), ZScoreAnomaly.get(attribute1.getValue().toString()).get(timeStep.intValue())));
                        } else {

                            line.getData().add(new XYChart.Data<>(timeStep.getValue(), ZScoreReg.get(attribute1.getValue().toString()).get(timeStep.intValue())));
                        }
                    });

//                    if (!newV.equals(oldV)) {//if change the attribute
//                        line.getData().clear();
//                    }
                }
            });
         }
        });

        board.getChildren().addAll(circleGraph, sc, regBoard);
        return board;
    }

    private Point[] toPoints(ArrayList<Float> x, ArrayList<Float> y) {
        Point[] ps = new Point[x.size()];
        for (int i = 0; i < ps.length; i++)
            ps[i] = new Point(x.get(i), y.get(i));
        return ps;
    }


}
