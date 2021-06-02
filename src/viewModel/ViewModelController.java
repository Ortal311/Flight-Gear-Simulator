package viewModel;

import algo.SimpleAnomalyDetector;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {

    Model m;
    public DoubleProperty timeStamp, throttle, rudder, aileron,
            elevators, sliderTime, choiceSpeed, pitch1, roll1, yaw1, timeStampGraph;
    public TimeSeries ts;
    public SimpleAnomalyDetector simpleAnomalyDetector=new SimpleAnomalyDetector();


    public double rate;
    public StringProperty timeFlight, chosenAttribute,correlateFeature;

    public BooleanProperty graphActivate;

    public ObservableList<String> attributeList;


    public int numberOfSpecAttribute,numberOfCorrelateAttribute;
    public DoubleProperty valueAxis, valueCorrelate;
    public Clock clock;
    //from timeBoard
    public StringProperty altimeter, airSpeed, fd, pitch, roll, yaw;
    public IntegerProperty sizeTS;

    public ViewModelController(Model m) {
        this.m = m;
        clock = new Clock();
        rate = 100;
        m.addObserver(this);//add Model as Observable

        timeStamp = new SimpleDoubleProperty();
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        sliderTime = new SimpleDoubleProperty();
        choiceSpeed = new SimpleDoubleProperty();

        chosenAttribute = new SimpleStringProperty();
        correlateFeature=new SimpleStringProperty();
        chosenAttribute.setValue("0");
        correlateFeature.setValue("0");
        timeStampGraph = new SimpleDoubleProperty();

        sizeTS = new SimpleIntegerProperty();


        // newnumber=ts.getIndexOfAttribute(newvalueatt);
        valueAxis = new SimpleDoubleProperty();
        valueCorrelate = new SimpleDoubleProperty();

        pitch1 = new SimpleDoubleProperty();
        roll1 = new SimpleDoubleProperty();
        yaw1 = new SimpleDoubleProperty();

        timeFlight = new SimpleStringProperty();
        altimeter = new SimpleStringProperty();
        airSpeed = new SimpleStringProperty();
        fd = new SimpleStringProperty();


//        pitch = new SimpleStringProperty();
//        roll = new SimpleStringProperty();
//        yaw = new SimpleStringProperty();

        attributeList = FXCollections.observableArrayList();


        choiceSpeed.addListener((o, ov, nv) -> {
            rate = nv.doubleValue();
            speedPlay(rate);

        });
        sliderTime.addListener((o, ov, nv) -> {
            // updateDisplayVariables(nv.intValue());
            timeStamp.setValue(nv.doubleValue());

            m.setTime(nv.doubleValue());
        });

        timeStamp.addListener((o, ov, nv) -> {
            updateDisplayVariables(nv.intValue());
            //m.displayFlight(nv.intValue());
        });

    }

    public void updateDisplayVariables(int value) {
        aileron.setValue(ts.getValueByTime(0, value));
        elevators.setValue(ts.getValueByTime(1, value));
        rudder.setValue(ts.getValueByTime(2, value));
        throttle.setValue(ts.getValueByTime(6, value));
        sliderTime.setValue(value);
        timeFlight.setValue(String.valueOf(value));

        altimeter.setValue(String.valueOf(ts.getValueByTime(25, value)));
        airSpeed.setValue(String.valueOf(ts.getValueByTime(24, value)));
        fd.setValue(String.valueOf(ts.getValueByTime(36, value)));

                 //to update the specific chosen attribute

        //getting the number of the chosen attribute
        numberOfSpecAttribute = ts.getIndexOfAttribute(chosenAttribute.getValue());
            //updating by binding the value of the chosen attribute
        valueAxis.setValue(ts.getValueByTime(numberOfSpecAttribute, value));


            //init the name of the correlate attribute
        correlateFeature.setValue(simpleAnomalyDetector.getCorrelateFeature(chosenAttribute.getValue()));
            //getting the col's number of the correlate attribute
        if(correlateFeature.getValue()!=null)
        {
            numberOfCorrelateAttribute=ts.getIndexOfAttribute(correlateFeature.getValue());
            //updating the value of the correlate attribute
            valueCorrelate.setValue(ts.getValueByTime(numberOfCorrelateAttribute,value));
        }
        else{
            numberOfCorrelateAttribute=0;
            valueCorrelate.setValue(0);
        }



//        pitch.setValue(String.valueOf(ts.getValueByTime(29, value)));//18
//        roll.setValue(String.valueOf(ts.getValueByTime(17, value)));
//        yaw.setValue(String.valueOf(ts.getValueByTime(20, value)));


        pitch1.setValue(ts.getValueByTime(29, value));
        roll1.setValue(ts.getValueByTime(17, value));
        yaw1.setValue(ts.getValueByTime(20, value));
    }
//    public void getSeries(String selectedAttribute, Number nv, XYChart.Series s){
//        Platform.runLater(()->{
//            s.getData().add(new XYChart.Data<>(String.valueOf(nv.intValue()),valueAxis));
//        });
//    }


    //Basic Functions- Buttons
    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open CSV file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            System.out.println("the name of the file is:" + chosen.getName());

            if (chosen.getName().contains(".csv"))  //checking the file
            {
                ts = new TimeSeries(chosen.getName());
                simpleAnomalyDetector.learnNormal(ts);
                if (ts.cols.size() != 42)
                    System.err.println("wrong amount of columns - should be 42");
                else
                    m.setTimeSeries(ts);
            } else {
                //System.err.println("wrong file, choose csv file");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong file chosen");
                alert.setContentText("please choose a csv file");
                alert.showAndWait();
            }
            attributeList.addAll(ts.getAttributes());
            sizeTS.setValue(ts.getSize());
            altimeter.setValue("0");
            airSpeed.setValue("0");
            fd.setValue("0");
        }
    }

    public void openXMLFile() {
        m.openXML();
    }

    public void play() {

        this.m.playFile();
    }

    public void pause() {
        System.out.println("pause 2");
        this.m.pauseFile();
    }

    public void stop() {
        this.m.stopFile();
    }

    public void rewind() {
        this.m.rewindFile();
    }

    public void forward() {
        this.m.forwardFile();
    }

//

    public void speedPlay(double rate) {
        //rate.addListener((o, ov, nv) -> m.op.setPlaySpeed((double) nv));
        // m.op.setPlaySpeed(rate);
        if (choiceSpeed.doubleValue() == 0.5) rate = 150;
        else if (choiceSpeed.doubleValue() == 1.5) rate = 75;
        else if (choiceSpeed.doubleValue() == 2) rate = 50;
        else if (choiceSpeed.doubleValue() == 2.5) rate = 20;
        else rate = 100;

        m.setPlaySpeed(rate);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m) {
            //    System.out.println(1);
            this.timeStamp.setValue(m.getTime());

            clock.increcment();

            String p = (String) arg;
//        if(p!=null){
//            if(p.equals("+150")) {
//                clock.miliSec.set(clock.miliSec.get() + 150);
//                if(clock.miliSec.get() == 100) {
//                    clock.seconds.set(clock.seconds.get() + 1);
//                    clock.miliSec.set(0);
//                }
//                if(clock.seconds.get() == 60) {
//                    clock.minutes.set(clock.minutes.get() + 1);
//                    clock.seconds.set(0);
//                }
//            }
//            else if(p.equals("-150")){
//                clock.miliSec.set(clock.miliSec.get() - 150);
//                if(clock.miliSec.get() == 100) {
//                    clock.seconds.set(clock.seconds.get() + 1);
//                    clock.miliSec.set(0);
//                }
//                if(clock.seconds.get() == 60) {
//                    clock.minutes.set(clock.minutes.get() + 1);
//                    clock.seconds.set(0);
//                }
//            }
//
//        }


        }

    }


}
