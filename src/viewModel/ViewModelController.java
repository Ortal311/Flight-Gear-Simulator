package viewModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {

    Model m;
    public TimeSeries ts_reg, ts_Anomal;//ts-reg
    public Runnable r;
    public DoubleProperty timeStamp, throttle, rudder, aileron,
            elevators, sliderTime, choiceSpeed, pitch, roll, yaw, timeStampGraph;
    //public SimpleAnomalyDetector simpleAnomalyDetector=new SimpleAnomalyDetector();
    public double rate;
    public StringProperty timeFlight, chosenAttribute, correlateFeature, altimeter, airSpeed, fd;

    public BooleanProperty graphActivate;

    public ObservableList<String> attributeList;
    public int numberOfSpecAttribute, numberOfCorrelateAttribute;
    public DoubleProperty valueAxis, valueCorrelate, x1, x2, y1, y2;
    public Clock clock;
    public IntegerProperty sizeTS;
    public Boolean xmlFile, csvFile;

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
        pitch = new SimpleDoubleProperty();
        roll = new SimpleDoubleProperty();
        yaw = new SimpleDoubleProperty();
        // newnumber=ts.getIndexOfAttribute(newvalueatt);
        valueAxis = new SimpleDoubleProperty();
        valueCorrelate = new SimpleDoubleProperty();
        x1=new SimpleDoubleProperty();
        x2=new SimpleDoubleProperty();
        y1=new SimpleDoubleProperty();
        y2=new SimpleDoubleProperty();
        timeStampGraph = new SimpleDoubleProperty();

        timeFlight = new SimpleStringProperty();
        altimeter = new SimpleStringProperty();
        airSpeed = new SimpleStringProperty();
        fd = new SimpleStringProperty();
        chosenAttribute = new SimpleStringProperty();
        correlateFeature = new SimpleStringProperty();
        chosenAttribute.setValue("0");
        correlateFeature.setValue("0");

        sizeTS = new SimpleIntegerProperty();

        attributeList = FXCollections.observableArrayList();

        choiceSpeed.addListener((o, ov, nv) -> {
            speedPlay();
        });

        sliderTime.addListener((o, ov, nv) -> {
            timeStamp.setValue(nv.doubleValue());
            m.setTime(nv.doubleValue());
            clock.update(nv.intValue() - ov.intValue());
        });

        timeStamp.addListener((o, ov, nv) -> {
            updateDisplayVariables(nv.intValue());
        });
        r = m.getPainter();
    }

    public void updateDisplayVariables(int time) {
        sliderTime.setValue(time);
        timeFlight.setValue(String.valueOf(time));
        aileron.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("aileron").associativeName, time));
        elevators.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("elevators").associativeName, time));
        rudder.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("rudder").associativeName, time));
        throttle.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("throttle").associativeName, time));
        altimeter.setValue(String.valueOf(ts_Anomal.getValueByTime(m.attributeMap.get("altimeter").associativeName, time)));
        airSpeed.setValue(String.valueOf(ts_Anomal.getValueByTime(m.attributeMap.get("airSpeed").associativeName, time)));
        fd.setValue(String.valueOf(ts_Anomal.getValueByTime(m.attributeMap.get("fd").associativeName, time)));
        pitch.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("pitch").associativeName, time));
        roll.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("roll").associativeName, time));
        yaw.setValue(ts_Anomal.getValueByTime(m.attributeMap.get("yaw").associativeName, time));

        //to update the specific chosen attribute
        //getting the number of the chosen attribute
        // numberOfSpecAttribute = ts.getIndexOfAttribute(chosenAttribute.getValue());
        // updating by binding the value of the chosen attribute
        // valueAxis.setValue(ts.getValueByTime(numberOfSpecAttribute, value));
        valueAxis.setValue(ts_Anomal.getValueByTime(chosenAttribute.getValue(), time));
        x1.setValue(ts_reg.getValueByTime(chosenAttribute.getValue(),0));
        x2.setValue(ts_reg.getValueByTime(chosenAttribute.getValue(),1000));

        //init the name of the correlate attribute
        // correlateFeature.setValue(simpleAnomalyDetector.getCorrelateFeature(chosenAttribute.getValue()));
        correlateFeature.setValue(m.ad.getCorrelateFeature(chosenAttribute.getValue()));

        //getting the col's number of the correlate attribute
        if (correlateFeature.getValue() != null) {
            //numberOfCorrelateAttribute=ts.getIndexOfAttribute(correlateFeature.getValue());
            //updating the value of the correlate attribute
            valueCorrelate.setValue(ts_Anomal.getValueByTime(correlateFeature.getValue(), time));
            y1.setValue(ts_reg.getValueByTime(chosenAttribute.getValue(),0));
            y2.setValue(ts_reg.getValueByTime(chosenAttribute.getValue(),1000));

        } else  {
            numberOfCorrelateAttribute = 0;
            valueCorrelate.setValue(0);

            y1.setValue(0);
            y2.setValue(0);
        }

    }
//    public void getSeries(String selectedAttribute, Number nv, XYChart.Series s){
//        Platform.runLater(()->{
//            s.getData().add(new XYChart.Data<>(String.valueOf(nv.intValue()),valueAxis));
//        });
//    }


    //Basic Functions- Buttons
    public void openFile() {
        //for reg flight
        FileChooser fc = new FileChooser();
        fc.setTitle("open CSV file");
        fc.setInitialDirectory(new File("./"));
        // File chosen = fc.showOpenDialog(null);
        List<File> chosen = fc.showOpenMultipleDialog(null);
        if (chosen != null && chosen.size() == 2) {
            // System.out.println("the name of the file is:" + chosen.getName());
            System.out.println("the name of the file is:" + chosen.get(0).getName());
            System.out.println("the name of the file is:" + chosen.get(1).getName());

            // if (chosen.getName().contains(".csv"))  //checking the file
            if (chosen.get(0).getName().contains(".csv"))  //checking the file
            {
                if (chosen.get(0).getName().equals("reg_flight.csv")) {
                    ts_reg = new TimeSeries(chosen.get(0).getName());
                    ts_Anomal = new TimeSeries(chosen.get(1).getName());
                } else {
                    ts_reg = new TimeSeries(chosen.get(1).getName());
                    ts_Anomal = new TimeSeries(chosen.get(0).getName());
                }

                //simpleAnomalyDetector.learnNormal(ts);
                //if (ts.cols.size() != 42)
                if (ts_reg.atts.size() != 42)
                    System.err.println("wrong amount of columns - should be 42");
                else
                    m.setTimeSeries(ts_Anomal);
            } else {
                //System.err.println("wrong file, choose csv file");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong file chosen");
                alert.setContentText("please choose a csv file");
                alert.showAndWait();
            }
            attributeList.addAll(ts_Anomal.getAttributes());
            sizeTS.setValue(ts_Anomal.getSize());
            altimeter.setValue("0");
            airSpeed.setValue("0");
            fd.setValue("0");
            this.csvFile = true;
        }
    }

    public void openXMLFile() {
        xmlFile = m.openXML();
    }

    public void play() {
        if (csvFile && xmlFile) {
            this.m.playFile();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File is missing");
            alert.setContentText("Please upload csv file and xml file");
            alert.showAndWait();
        }
    }

    public void pause() {
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

    public void loadAnomalyDetector() {
        m.loadAnomalyDetector();
    }

    public void speedPlay() {
        if (choiceSpeed.doubleValue() == 0.5) m.properties.setPlaySpeed(150);
        else if (choiceSpeed.doubleValue() == 1.5) m.properties.setPlaySpeed(75);
        else if (choiceSpeed.doubleValue() == 2) m.properties.setPlaySpeed(50);
        else if (choiceSpeed.doubleValue() == 2.5) m.properties.setPlaySpeed(20);
        else m.properties.setPlaySpeed(100);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m) {
            this.timeStamp.setValue(m.getTime());
        }
    }
}
