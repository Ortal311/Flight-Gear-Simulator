package viewModel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {

    Model m;
    public DoubleProperty timeStamp, throttle, rudder, aileron,
            elevators, sliderTime, choiceSpeed, pitch, roll, yaw;
    public TimeSeries ts;

    public StringProperty timeFlight;

    public ObservableList<String> attributeList;
    public Clock clock;
    //from timeBoard
    public StringProperty altimeter, airSpeed, fd;
    public Boolean xmlFile, csvFile;

    public ViewModelController(Model m) {
        this.m = m;
        clock = new Clock();
        this.xmlFile = false;
        this.csvFile = false;
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

        timeFlight = new SimpleStringProperty();
        altimeter = new SimpleStringProperty();
        airSpeed = new SimpleStringProperty();
        fd = new SimpleStringProperty();

        attributeList = FXCollections.observableArrayList();

        choiceSpeed.addListener((o, ov, nv) -> {
            speedPlay();
        });

        sliderTime.addListener((o, ov, nv) -> {
            // updateDisplayVariables(nv.intValue());
            timeStamp.setValue(nv.doubleValue());
            m.setTime(nv.doubleValue());
        });

        timeStamp.addListener((o, ov, nv) -> {
            updateDisplayVariables(nv.intValue());
        });
    }

    public void updateDisplayVariables(int time) {
        sliderTime.setValue(time);
        timeFlight.setValue(String.valueOf(time));
        aileron.setValue(ts.getValueByTime(m.attributeMap.get("aileron").associativeName, time));
        elevators.setValue(ts.getValueByTime(m.attributeMap.get("elevators").associativeName, time));
        rudder.setValue(ts.getValueByTime(m.attributeMap.get("rudder").associativeName, time));
        throttle.setValue(ts.getValueByTime(m.attributeMap.get("throttle").associativeName, time));
        altimeter.setValue(String.valueOf(ts.getValueByTime(m.attributeMap.get("altimeter").associativeName, time)));
        airSpeed.setValue(String.valueOf(ts.getValueByTime(m.attributeMap.get("airSpeed").associativeName, time)));
        fd.setValue(String.valueOf(ts.getValueByTime(m.attributeMap.get("fd").associativeName, time)));
        pitch.setValue(ts.getValueByTime(m.attributeMap.get("pitch").associativeName, time));
        roll.setValue(ts.getValueByTime(m.attributeMap.get("roll").associativeName, time));
        yaw.setValue(ts.getValueByTime(m.attributeMap.get("yaw").associativeName, time));
    }


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
        if(csvFile && xmlFile) {
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
