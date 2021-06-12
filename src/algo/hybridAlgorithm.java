package algo;

import viewModel.TimeSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

public class hybridAlgorithm {

    HashMap<String,HashSet<CorrelatedFeatures>> cfAlgo;
    HashMap<CorrelatedFeatures,Circle> circlesMap;
    HashMap<String, ArrayList<Integer>> anomalyMapByAtt;
    SimpleAnomalyDetector ad;
    ZScoreAlgorithm zScore;

    public hybridAlgorithm() {
        circlesMap = new HashMap<>();
        anomalyMapByAtt = new HashMap<>();
    }

    private List<Point> toListPoints(List<Float> x, List<Float> y){
        List<Point> lst = new ArrayList<>();
        for(int i=0; i < x.size(); i++)
            lst.add(new Point(x.get(i), y.get(i)));
        return lst;
    }

    public void learnNormal(TimeSeries ts) {
        ad = new SimpleAnomalyDetector();
        ad.learnNormal(ts);
        List<CorrelatedFeatures> cf = ad.getNormalModel();
        WelzlAlgorithm algorithm = new WelzlAlgorithm();
        TimeSeries tsZscore=new TimeSeries();

        cfAlgo = new HashMap<>();
        cfAlgo.put("ZScore", new HashSet<>());
        cfAlgo.put("Regression", new HashSet<>());
        cfAlgo.put("Welzl", new HashSet<>());
        for(CorrelatedFeatures c : cf) {
            if(Math.abs(c.corrlation) >= 0.95)
                cfAlgo.get("Regression").add(c);
            else {
                if(Math.abs(c.corrlation) < 0.5)
                    cfAlgo.get("ZScore").add(c);
                else
                    cfAlgo.get("Welzl").add(c);
            }
        }

        for(CorrelatedFeatures c : cfAlgo.get("Welzl")) {
            circlesMap.put(c,algorithm.miniDisk(toListPoints(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2))));
        }

        for(CorrelatedFeatures c: cfAlgo.get("ZScore")) {
            tsZscore.ts.put(c.feature1, ts.getAttributeData(c.feature1));
            tsZscore.ts.put(c.feature2, ts.getAttributeData(c.feature2));
        }

        zScore = new ZScoreAlgorithm();
        zScore.learnNormal(tsZscore);
        ad.learnNormal(ts);
    }

    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> lst = new ArrayList<>();
        lst.addAll(ad.detect(ts));
        TimeSeries zScoreTest = new TimeSeries();

        for(CorrelatedFeatures c: cfAlgo.get("ZScore")) {
            zScoreTest.ts.put(c.feature1, ts.getAttributeData(c.feature1));
            zScoreTest.ts.put(c.feature2, ts.getAttributeData(c.feature2));
        }

        lst.addAll(zScore.detect(zScoreTest));

        for(CorrelatedFeatures c : cfAlgo.get("Welzl")) {

            ArrayList<Integer> anomalyLst = new ArrayList<>();
            List<Point> ps = toListPoints(ts.getAttributeData(c.feature1), ts.getAttributeData(c.feature2));

            for(int i = 0; i < ps.size(); i++) {
                if(!circlesMap.get(c).isInside(ps.get(i))) {
                    lst.add(new AnomalyReport(c.feature1 + "-" + c.feature2, i));
                    anomalyLst.add(i);
                }
            }
            anomalyMapByAtt.put(c.feature1, anomalyLst);
        }

        return lst;
    }

    public Circle getCircle(String att1, String att2) {
        for(CorrelatedFeatures cf : circlesMap.keySet()) {
            if(cf.feature1.equals(att1) && cf.feature2.equals(att2) || cf.feature1.equals(att2) && cf.feature2.equals(att1))
                return circlesMap.get(cf);
        }
        return null;
    }

//    public AnchorPane paint() {
//        AnchorPane board=new AnchorPane();
//
//        BubbleChart<Number, Number> zscoreGraph=new BubbleChart(new NumberAxis(), new NumberAxis());
//        zscoreGraph.setAnimated(false);
//
//        XYChart.Series<Number, Number> seriesPoints = new XYChart.Series();
//        XYChart.Series<Number, Number> seriesCircle = new XYChart.Series();
//
//        zscoreGraph.getData().addAll(seriesPoints,seriesCircle);
//
//        zscoreGraph.setPrefSize(300, 250);
//
//        seriesCircle.getData().add(new XYChart.Data(4,4,3));
//        seriesPoints.getData().add(new XYChart.Data(3,3,0.2));
//        seriesPoints.getData().add(new XYChart.Data(6,6,0.2));
//        seriesPoints.getData().add(new XYChart.Data(3,6,0.2));
//        seriesPoints.getData().add(new XYChart.Data(4,4,0.2));
//
//        board.getChildren().add(zscoreGraph);
//
//
//        return board;
//    }
}
