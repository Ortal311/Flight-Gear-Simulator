package algo;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import viewModel.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements AnomalyDetector {

    ArrayList<CorrelatedFeatures> cf;
    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();
    public DoubleProperty valAtt1X = new SimpleDoubleProperty();//static for line
    public DoubleProperty valAtt2Y = new SimpleDoubleProperty();//static for line
    public DoubleProperty vaAtt1Xend = new SimpleDoubleProperty();//static for line
    public DoubleProperty vaAtt2Yend = new SimpleDoubleProperty();//static for line
    public DoubleProperty valPointX = new SimpleDoubleProperty();
    public DoubleProperty valPointY = new SimpleDoubleProperty();
    public DoubleProperty timeStep = new SimpleDoubleProperty();

    public SimpleAnomalyDetector() {
        cf = new ArrayList<>();
    }

    @Override
    public void learnNormal(TimeSeries ts) {
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
                float p = StatLib.pearson(vals[i], vals[j]);
                if (Math.abs(p) > 0.9) {

                    Point ps[] = toPoints(ts.getAttributeData(atts.get(i)), ts.getAttributeData(atts.get(j)));
                    Line lin_reg = StatLib.linear_reg(ps);
                    float threshold = findThreshold(ps, lin_reg) * 1.1f; // 10% increase

                    CorrelatedFeatures c = new CorrelatedFeatures(atts.get(i), atts.get(j), p, lin_reg, threshold);

                    cf.add(c);
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
        ArrayList<AnomalyReport> v = new ArrayList<>();

        for (CorrelatedFeatures c : cf) {
            ArrayList<Float> x = ts.getAttributeData(c.feature1);
            ArrayList<Float> y = ts.getAttributeData(c.feature2);
            for (int i = 0; i < x.size(); i++) {
                if (Math.abs(y.get(i) - c.lin_reg.f(x.get(i))) > c.threshold) {
                    String d = c.feature1 + "-" + c.feature2;
                    v.add(new AnomalyReport(d, (i + 1)));
                }
            }
        }

        return v;
    }

    @Override
    public AnchorPane paint() {
        AnchorPane ap = new AnchorPane();
        LineChart<Number, Number> sc = new LineChart<>(new NumberAxis(), new NumberAxis());
        sc.setPrefHeight(230);
        sc.setPrefWidth(230);
        XYChart.Series series1 = new XYChart.Series();//points
        XYChart.Series series2 = new XYChart.Series();//line
        sc.getData().addAll(series1, series2);


//        series2.getData().add(new XYChart.Data(6.4, 15.6));

      timeStep.addListener((o, ov, nv) -> {
        // System.out.println("first:"+ valPointX.doubleValue()+" "+"second:"+valPointY.doubleValue() );
            Platform.runLater(() -> {
                series2.getData().add(new XYChart.Data(valAtt1X.doubleValue(), valAtt2Y.doubleValue()));//reg first point
                series2.getData().add(new XYChart.Data(vaAtt1Xend.doubleValue(), vaAtt2Yend.doubleValue()));//reg sec point

                series1.getData().add(new XYChart.Data(valPointX.doubleValue(), valPointY.doubleValue()));//points
            });
        });

//        series1.setName("Equities");

//        series1.getData().add(new XYChart.Data(2.8, 33.6));
//        series1.getData().add(new XYChart.Data(6.8, 23.6));


//        series2.setName("Mutual funds");

        sc.setAnimated(false);
        sc.setCreateSymbols(true);
        ap.getChildren().add(sc);
        ap.getStylesheets().add("style.css");

         paintLive(ap);
        return ap;
    }


    public void paintLive(AnchorPane ap) {
//        LineChart<Number, Number> sc = new LineChart<>(new NumberAxis(), new NumberAxis());
//        sc.setPrefHeight(230);
//        sc.setPrefWidth(230);
//        XYChart.Series series1 = new XYChart.Series();//points
//        XYChart.Series series2 = new XYChart.Series();//line
//        sc.getData().addAll(series1, series2);
//
//        series2.getData().add(new XYChart.Data(valAtt1X.getValue(), valAtt2Y.getValue()));
//        series2.getData().add(new XYChart.Data(vaAtt1Xend.getValue(), vaAtt2Yend.getValue()));
////        series2.getData().add(new XYChart.Data(6.4, 15.6));
//
//        timeStep.addListener((o, ov, nv) -> {
//            Platform.runLater(() -> {
//                series1.getData().add(new XYChart.Data(valPointX.getValue(), valPointY.get()));
//            });
//        });
//
//        series1.setName("Equities");
//
//        series1.getData().add(new XYChart.Data(2.8, 33.6));
//        series1.getData().add(new XYChart.Data(6.8, 23.6));
//
//
//        series2.setName("Mutual funds");
//
//        sc.setAnimated(false);
//        sc.setCreateSymbols(true);
//        ap.getChildren().add(sc);
//        ap.getStylesheets().add("style.css");
    }




    public List<CorrelatedFeatures> getNormalModel() {
        return cf;
    }

    public String getCorrelateFeature(String attribute1) {
        for (CorrelatedFeatures c : cf) {
            if (c.feature1.equals(attribute1))
                return c.feature2;
        }
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
