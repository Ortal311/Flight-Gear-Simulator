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


import javafx.geometry.Insets;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import viewModel.TimeSeries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javafx.geometry.Insets;


public class SimpleAnomalyDetector implements AnomalyDetector {

    TimeSeries tsReg;
    TimeSeries tsAnomal;
    public Line regLineForCorrelateAttribute;

    ArrayList<CorrelatedFeatures> cf;
    ArrayList<CorrelatedFeatures> cfmore95list;

    HashMap<String,CorrelatedFeatureForAll>cfmore95;

    HashMap<String,CorrelatedFeatureForAll>cfless50;

    HashMap<String,CorrelatedFeatureForAll>cfBetween;

    ArrayList<AnomalyReport> ar;
    public Map<String, ArrayList<Integer>> anomalyAndTimeStep = new HashMap<>();

    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();

    public DoubleProperty valAtt1X = new SimpleDoubleProperty();//static for line- minValX
    public DoubleProperty valAtt2Y = new SimpleDoubleProperty();//static for line- minValY
    public DoubleProperty vaAtt1Xend = new SimpleDoubleProperty();//static for line -maxValX
    public DoubleProperty vaAtt2Yend = new SimpleDoubleProperty();//static for line -maxValY

    public DoubleProperty valPointX = new SimpleDoubleProperty();
    public DoubleProperty valPointY = new SimpleDoubleProperty();

    public DoubleProperty timeStep = new SimpleDoubleProperty();

    public List<Double> collectedDataUntilChange=new ArrayList<>();


    public SimpleAnomalyDetector() {
        cf = new ArrayList<>();
        cfmore95list=new ArrayList<>();
        cfmore95=new HashMap<>();
        cfless50=new HashMap<>();
        cfBetween=new HashMap<>();

    }

    @Override
    public void learnNormal(TimeSeries ts) {
        tsReg=ts;
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

                if (Math.abs(p) > 0.9) {//only if above o.
                    Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                    Line lin_reg = StatLib.linear_reg(ps);
                    float threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase
                    CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);
                    cf.add(c);
                }
                if (Math.abs(p) >= 0.95) {

                    if(!cfmore95.containsKey(atts.get(i))){

                    Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                    Line lin_reg = StatLib.linear_reg(ps);
                    float threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase
                    CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);//att1_att2_pearsonCorrelate_null_threshold(the max one)
                    CorrelatedFeatureForAll ca=new CorrelatedFeatureForAll(atts.get(i), atts.get(j),"Regression", Math.abs(p)) ;
                         cfmore95list.add(c);
                        cfmore95.put(atts.get(i),ca);


                        if(cfBetween.containsKey(atts.get(i)))//if ZScore had the attribute we'll put it here instead
                            cfBetween.remove(atts.get(i));
                        if(cfless50.containsKey(atts.get(i)))
                            cfless50.remove(atts.get(i));

                    }
                    else // if contain the attribute we'll take the max
                    {
                        if(cfmore95.get(atts.get(i)).corrlation<Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                            Line lin_reg = StatLib.linear_reg(ps);
                            float threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase
                            CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);//att1_att2_pearsonCorrelate_null_threshold(the max one)

                            cfmore95.get(atts.get(i)).feature2=atts.get(j);
                            cfmore95.get(atts.get(i)).corrlation=Math.abs(p);
                        }
                    }
                }


                else if((!cfmore95.containsKey(atts.get(i)))&&(0.5<=Math.abs(p))&&(Math.abs(p) <0.95)){// 0.5<val<0.95

                    if(!cfBetween.containsKey(atts.get(i))){
                        CorrelatedFeatureForAll ca=new CorrelatedFeatureForAll(atts.get(i), atts.get(j),"Welzl", Math.abs(p)) ;
                        cfBetween.put(atts.get(i),ca);
                        if(cfless50.containsKey(atts.get(i)))//if ZScore had the attribute we'll put it here instead
                            cfless50.remove(atts.get(i));
                    }
                    else // if contain the attribute we'll take the max
                    {
                        if(cfBetween.get(atts.get(i)).corrlation<Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            cfBetween.get(atts.get(i)).feature2=atts.get(j);
                            cfBetween.get(atts.get(i)).corrlation=Math.abs(p);
                        }
                    }
                }


                else if((Math.abs(p) <0.5)&&(!cfBetween.containsKey(atts.get(i)))&&(!cfmore95.containsKey(atts.get(i)))){
                    if(!cfless50.containsKey(atts.get(i))){
                        CorrelatedFeatureForAll ca=new CorrelatedFeatureForAll(atts.get(i), atts.get(j),"ZScore", Math.abs(p)) ;
                        cfless50.put(atts.get(i),ca);
                    }
                    else // if contain the attribute we'll take the max
                    {
                        if(cfless50.get(atts.get(i)).corrlation<Math.abs(p))//if the the val with the different att is higher,we'll tack the other att
                        {
                            cfless50.get(atts.get(i)).feature2=atts.get(j);
                            cfless50.get(atts.get(i)).corrlation=Math.abs(p);
                        }
                    }
                }
            }
        }
    }

    private Point[] toPoints(ArrayList<Float> x, ArrayList<Float> y) {
        Point[] ps = new Point[x.size()];
        for (int i = 0; i < ps.length; i++)
            ps[i] = new Point(x.get(i), y.get(i));
        return ps;
    }

    private float findThreshold(Point ps[], Line rl) {
        float max = 0;
        for (int i = 0; i < ps.length; i++) {
            float d = Math.abs(ps[i].y - rl.f(ps[i].x));
            if (d > max)
                max = d;
        }
        return max;
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        tsAnomal=ts;
        ArrayList<AnomalyReport> v = new ArrayList<>();

        for (CorrelatedFeatures c : cf) {
            ArrayList<Float> x = ts.getAttributeData(c.feature1);
            ArrayList<Float> y = ts.getAttributeData(c.feature2);
            for (int i = 0; i < x.size(); i++) {
                if (Math.abs(y.get(i) - c.lin_reg.f(x.get(i))) > c.threshold) {
                    String d = c.feature1 + "-" + c.feature2;
                    v.add(new AnomalyReport(d, (i + 1)));
                    if (!anomalyAndTimeStep.containsKey(c.feature1)) {
                        anomalyAndTimeStep.put(c.feature1, new ArrayList<>());
                        anomalyAndTimeStep.get(c.feature1).add(i + 1);

                    } else
                        anomalyAndTimeStep.get(c.feature1).add(i + 1);
                }
            }
        }

        return v;
    }
    public void initDataForGraphTimeChange(){
        valPointX.setValue(tsAnomal.getValueByTime(attribute1.getValue(), timeStep.intValue()));
//        if ((attribute2.getValue() == null)) { //find a way to leave it as null and not paint it in that case
//            valPointY.setValue(0);
//        } else {
//            valPointY.setValue(tsAnomal.getValueByTime(attribute2.getValue(), timeStep.intValue()));
//        }
        if(attribute2.getValue()!=null)
        valPointY.setValue(tsAnomal.getValueByTime(attribute2.getValue(), timeStep.intValue()));


    }
    public void initDataForGraphAttChange(){
       // attribute2.setValue(getCorrelateFeature(attribute1.getValue()));
                    //need to update with atta

        regLineForCorrelateAttribute=getRegLine(attribute1.getValue(),attribute2.getValue());
        valAtt2Y.setValue(tsReg.getMinFromAttribute(attribute1.getValue()));
        valAtt2Y.setValue(regLineForCorrelateAttribute.f(valAtt1X.floatValue()));
        vaAtt1Xend.setValue(tsReg.getMaxFromAttribute(attribute2.getValue()));
        vaAtt2Yend.setValue(regLineForCorrelateAttribute.f(vaAtt1Xend.floatValue()));
    }

    @Override
    public AnchorPane paint() {
        AnchorPane ap = new AnchorPane();
        //Data for BubbleChart
        LineChart<Number, Number> sc = new LineChart<>(new NumberAxis(), new NumberAxis());
        sc.setPrefHeight(250);
        sc.setPrefWidth(350);
        XYChart.Series pointsNormal = new XYChart.Series();//points for normal Flight
        XYChart.Series pointsAnomal = new XYChart.Series();//points for Anomaly parts
        XYChart.Series regLine = new XYChart.Series();//line
        sc.getData().addAll(pointsNormal, regLine, pointsAnomal);




        attribute1.addListener((ob, oldV, newV) -> {//to delete the old graph if attribute has changed
            attribute2.setValue(getCorrelateFeature(attribute1.getValue()));
            if (attribute2.getValue() != null)
            {
                initDataForGraphAttChange();
                timeStep.addListener((o, ov, nv) -> {
                    initDataForGraphTimeChange();
                    Platform.runLater(() -> {
                        if (!anomalyAndTimeStep.containsKey(attribute1.getValue())) {
//                        if (nv.doubleValue() > ov.doubleValue() + 30) {
//                            series1.getData().remove(0);
//                        }
                            pointsNormal.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points
                            regLine.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                            regLine.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point

                        } else {
                            if (!anomalyAndTimeStep.get(attribute1.getValue()).contains(timeStep.intValue())) {
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
                });
                if (!newV.equals(oldV)) {//if change the attribute
                    pointsAnomal.getData().clear();
                    pointsNormal.getData().clear();
                    regLine.getData().clear();
                }
//            if(attribute2.getValue() == null){
//                series3.getData().clear();
//                series1.getData().clear();
//                series2.getData().clear();
//            }
            }
        });

        sc.setAnimated(false);
        sc.setCreateSymbols(true);
        ap.getChildren().add(sc);
        ap.getStylesheets().add("style.css");

        return ap;
    }

    public List<CorrelatedFeatures> getNormalModel() {
        return cf;
    }
    public List<CorrelatedFeatures> getCorrelateMore95lst() {
        return cfmore95list;
    }
    public HashMap<String,CorrelatedFeatureForAll>getCorrelateless50(){
        return cfless50;
    }
    public HashMap<String, CorrelatedFeatureForAll> getCfBetween() {
        return cfBetween;
    }

    public HashMap<String, CorrelatedFeatureForAll> getCfmore95() {
        return cfmore95;
    }

    public String getCorrelateFeature(String attribute1) {
        for (CorrelatedFeatures c : cf) {
            if (c.feature1.equals(attribute1))
                return c.feature2;
        }
//        for (CorrelatedFeatures c : cf)
//            System.out.println("f1:  " + c.feature1 + "  f2:  " + c.feature2);
        return null;
    }

    public Line getRegLine(String f1, String f2) {
        for (CorrelatedFeatures c : cf) {
            if (c.feature1.equals(f1) && c.feature2.equals(f2))
                return c.lin_reg;
        }
        return null;
    }

}
