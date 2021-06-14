package viewModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

public class ViewModelController extends Observable implements Observer {

    Model m;
    public Clock clock;
    public TimeSeries ts_reg, ts_Anomal;    //ts-reg
    public DoubleProperty timeStamp, throttle, rudder, aileron,
            elevators, sliderTime, choiceSpeed, pitch, roll, yaw, timeStampGraph;
    public DoubleProperty valueAxis, valueCorrelate;
    public StringProperty timeFlight, chosenAttribute, correlateFeature, altimeter, airSpeed, fd;
    public IntegerProperty sizeTS;

    public ObservableList<String> attributeList;

    public int numberOfSpecAttribute, numberOfCorrelateAttribute;
    public Boolean xmlFile, csvFile, algoFile,openedCSV=false;

    public ViewModelController(Model m) {
        this.m = m;
        clock = new Clock();
        m.addObserver(this);    //add Model as Observable
        xmlFile = false;
        csvFile = false;
        algoFile = false;

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
        roll = new SimpleDoubleProperty();
        yaw = new SimpleDoubleProperty();

        valueAxis = new SimpleDoubleProperty();
        valueCorrelate = new SimpleDoubleProperty();

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
            if( xmlFile && algoFile && openedCSV);
                m.setVarivablesTOALG();//updating the date for the alg graph

        });

        chosenAttribute.addListener((o, ov, nv) -> {
            m.attribute1.bind(chosenAttribute);
            if(xmlFile && algoFile && openedCSV)
                m.setVarivablesNamesTOALG();
        });
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


        /*
            To update the specific chosen attribute
            getting the number of the chosen attribute
            numberOfSpecAttribute = ts.getIndexOfAttribute(chosenAttribute.getValue());
            updating by binding the value of the chosen attribute
         */
        valueAxis.setValue(ts_Anomal.getValueByTime(chosenAttribute.getValue(), time));

//        //  Init the name of the correlate attribute
//        correlateFeature.setValue(m.ad.getCorrelateFeature(chosenAttribute.getValue()));//need to be according to the ALG
        correlateFeature=getCorrelateFeature();

        //  Getting the col's number of the correlate attribute
        if (correlateFeature.getValue() != null) {
            valueCorrelate.setValue(ts_Anomal.getValueByTime(correlateFeature.getValue(), time));

        } else {
            numberOfCorrelateAttribute = 0;
            valueCorrelate.setValue(0);
        }
    }
    public StringProperty getCorrelateFeature(){
        //  Init the name of the correlate attribute
        correlateFeature.setValue(m.ad.getCorrelateFeature(chosenAttribute.getValue()));//need to be according to the ALG
        return correlateFeature;
    }
    public ListProperty<Float>getDataOfAtt(String attribute){
        return ts_Anomal.getDataOfAttUntilIndex(attribute,timeStamp.intValue());
    }

    //  Basic Functions- Buttons
    public void openFile() {
        //for reg flight
        FileChooser fc = new FileChooser();
        fc.setTitle("open CSV file");
        fc.setInitialDirectory(new File("./"));
        // File chosen = fc.showOpenDialog(null);
        List<File> chosen = fc.showOpenMultipleDialog(null);
        if (chosen != null && chosen.size() == 2) {
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
                else{
                    m.setTimeSeries(ts_Anomal,ts_reg);
                    //m.initData();
                }
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
        openedCSV = true;
    }

    public void openXMLFile() {
        xmlFile = m.openXML();
//        if(xmlFile) {
//            pitchMax.setValue(m.attributeMap.get("pitch").getMax());
//            pitchMin.setValue(m.attributeMap.get("pitch").getMin());
//        }
    }

    public void play() {
        if (csvFile && xmlFile && algoFile) {
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
        algoFile = m.loadAnomalyDetector();

//        if(algoFile)
//        {
//            m.getPainter();
//        }
    }

    public void speedPlay() {
        if (choiceSpeed.doubleValue() == 0.5) m.properties.setPlaySpeed(150);
        else if (choiceSpeed.doubleValue() == 1.5) m.properties.setPlaySpeed(75);
        else if (choiceSpeed.doubleValue() == 2) m.properties.setPlaySpeed(50);
        else if (choiceSpeed.doubleValue() == 2.5) m.properties.setPlaySpeed(20);
        else m.properties.setPlaySpeed(100);
    }

    public Callable<AnchorPane> getPainter(){return m.getPainter();}

    @Override
    public void update(Observable o, Object arg) {
        if (o == m) {
            this.timeStamp.setValue(m.getTime());
        }
    }
}
