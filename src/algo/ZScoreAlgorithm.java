package algo;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import viewModel.TimeSeries;

import java.util.*;

public class ZScoreAlgorithm implements AnomalyDetector {
    Vector<Float> tx;
    //HashMap<Integer, LinkedList<Float>> ZScoreMap;//original
    public HashMap<Integer, ArrayList<Float>> ZScoreMap;

    //HashMap<String,LinkedList<Float>>ZScoreReg=new HashMap<>();
    public HashMap<String, ArrayList<Float>> ZScoreReg = new HashMap<>();
    //HashMap<String,LinkedList<Integer>>ZScoreAnomaly=new HashMap<>();
    public HashMap<String, ArrayList<Integer>> ZScoreAnomaly = new HashMap<>();

    public HashMap<String, ArrayList<Float>> avgMap;

    public StringProperty Attribute = new SimpleStringProperty();
    public DoubleProperty timeStep = new SimpleDoubleProperty();

    public Line regLineForCorrelateAttribute;

    public ZScoreAlgorithm() {
        this.tx = new Vector<>();
        this.ZScoreMap = new HashMap<>();
        avgMap = new HashMap<>();
    }

    public float[] ListToArr(List<Float> lst) {
        float[] res = new float[lst.size()];

        for (int i = 0; i < res.length; i++) {
            res[i] = lst.get(i);
        }
        return res;
    }

    public float calcZScore(List<Float> col, String attribute) {
        float avg, sigma, zScore, var;
        float[] arrFloat;
        int colSize = col.size();
        float x;

        if (colSize == 0) {
            return 0;
        }

        x = col.get(colSize - 1);
        if (colSize == 1) {
            arrFloat = ListToArr(col);
            if(StatLib.var(arrFloat) != 0)
                return Math.abs((x - StatLib.avg(arrFloat))) / StatLib.var(arrFloat);
            return 0;
        }

        arrFloat = ListToArr(col.subList(0, col.size() - 1));
        avg = StatLib.avg(arrFloat);
        var = StatLib.var(arrFloat);

        if (var < 0)
            return 0;
        sigma = (float) Math.sqrt(var);

        if (sigma == 0)
            return 0;
        zScore = Math.abs(x - avg) / sigma;

      //  System.out.println("zScore: " + zScore + " " + "attribute: " + attribute);
        return zScore;
    }


    public float argMax(LinkedList<Float> z) {
        float max = 0;
        for (int i = 0; i < z.size(); i++) {
            if (max < z.get(i))
                max = z.get(i);
        }
        return max;
    }

    public float argMax(ArrayList<Float> z) {
        float max = 0;
        for (int i = 0; i < z.size(); i++) {
            if (max < z.get(i))
                max = z.get(i);
        }
        return max;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        int index = 0;
        //LinkedList<Float> zScored = new LinkedList<>();
        ArrayList<Float> zScored = new ArrayList<>();
        String attribute;
        int colSize = ts.atts.size();

        for (int i = 0; i < colSize; i++) {
            ArrayList<Float> col = ts.tsNum.get(i);
            attribute = ts.atts.get(index);
            avgMap.put(attribute, new ArrayList<>());
          //  System.out.println(col.size());
            for (int j = 0; j < col.size(); j++) {

                zScored.add(calcZScore(col.subList(0, j), attribute));
            }
            tx.add(argMax(zScored));
            this.ZScoreReg.put(attribute, zScored);
            this.ZScoreMap.put(index++, zScored);

        }
    }

    public List<AnomalyReport> detect(TimeSeries data) {
        System.out.println("inside begging of detect Zscore 1");
        List<AnomalyReport> lst = new LinkedList<>();
        String attribute;

        for (int indexCol = 0; indexCol < data.atts.size(); indexCol++) {
            ArrayList<Float> col = data.tsNum.get(indexCol);
            attribute = data.atts.get(indexCol);
            for (int indexTime = 0; indexTime < col.size(); indexTime++) {
                if (calcZScore(col.subList(0, indexTime), attribute) > tx.get(indexCol)) {
                    lst.add(new AnomalyReport(attribute, indexTime));
//                    System.out.println("was inside detect ZScore");
//                    System.out.println(attribute + "  " + indexCol);

                    if (!ZScoreAnomaly.containsKey(attribute)) {
                        ZScoreAnomaly.put(attribute, new ArrayList<>());
                        ZScoreAnomaly.get(attribute).add(indexTime);
                    } else
                        ZScoreAnomaly.get(attribute).add(indexTime);
                }
            }
        }
        return lst;
    }

    @Override
    public AnchorPane paint() {
        AnchorPane ap = new AnchorPane();
        //line Chart, child of Anchor
        LineChart<Number, Number> sc = new LineChart<>(new NumberAxis(), new NumberAxis());
        sc.setPrefHeight(210);
        sc.setPrefWidth(290);
        XYChart.Series line = new XYChart.Series();
        XYChart.Series lineAnomal = new XYChart.Series();
        sc.getData().addAll(line,lineAnomal);
        lineAnomal.getNode().setStyle("-fx-stroke: red;");

        Attribute.addListener((ob, oldV, newV) -> {//to delete the old graph if attribute has changed
            timeStep.addListener((o, ov, nv) -> {
                Platform.runLater(() -> {
                    if ((ZScoreAnomaly.size() != 0) && !ZScoreAnomaly.containsKey(Attribute.getValue())) {// i dont think it's work
                        lineAnomal.getData().add(new XYChart.Data<>(timeStep.getValue(), ZScoreReg.get(Attribute.getValue().toString()).get(timeStep.intValue())));
                    } else {
                        line.getData().add(new XYChart.Data<>(timeStep.getValue(), ZScoreReg.get(Attribute.getValue().toString()).get(timeStep.intValue())));
                    }
                });
            });
            if (!newV.equals(oldV)) {//if change the attribute
                line.getData().clear();
            }
        });
        sc.setAnimated(false);
        sc.setCreateSymbols(false);
        ap.getChildren().add(sc);
        return ap;
    }
    public HashMap<String, ArrayList<Float>>getZScoreReg(){
        return this.ZScoreReg;
    }
    public HashMap<String, ArrayList<Integer>>getZscoreAnomal(){
        return this.ZScoreAnomaly;
    }
}
