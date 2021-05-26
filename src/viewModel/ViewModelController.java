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
            elevators, sliderTime, choiceSpeed;
    public TimeSeries ts;

    public double rate;
    public StringProperty timeFlight;

    public ObservableList<String> attributeList;
    public Clock clock;
    //from timeBoard
    public StringProperty altimeter, airSpeed, fd, pitch, roll, yaw;

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

        timeFlight = new SimpleStringProperty();
        altimeter = new SimpleStringProperty();
        airSpeed = new SimpleStringProperty();
        fd = new SimpleStringProperty();
        pitch = new SimpleStringProperty();
        roll = new SimpleStringProperty();
        yaw = new SimpleStringProperty();

        attributeList= FXCollections.observableArrayList();

        choiceSpeed.addListener((o, ov, nv) -> {
            rate = nv.doubleValue();
            speedPlay(rate);

        });

        timeStamp.addListener((o, ov, nv) -> {
            updateDisplayVariables(nv.intValue());
            //m.displayFlight(nv.intValue());
        });
//        sliderTime.addListener((o, ov, nv) -> m.setTime((double) nv));
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
        pitch.setValue(String.valueOf(ts.getValueByTime(29, value)));//18
        roll.setValue(String.valueOf(ts.getValueByTime(17, value)));
        yaw.setValue(String.valueOf(ts.getValueByTime(20,value)));
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
        }
    }

    public void openXMLFile() {
        m.openXML();
    }

    public void play() {
        //need to convert it, because in the choice list is come as small numbers

//        new Thread(() -> {
//            for (int i = 1; i < ts.getSize() - 1; i++) {
//                this.timeStamp.setValue(i);
//                clock.increcment();
//
//                try {
//                    if (choiceSpeed.doubleValue() == 0.5) rate = 150;
//                    else if (choiceSpeed.doubleValue() == 1.5) rate = 75;
//                    else if (choiceSpeed.doubleValue() == 2) rate = 50;
//                    else if (choiceSpeed.doubleValue() == 2.5) rate = 20;
//                    else rate = 100;
//                    Thread.sleep((long) rate);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("DONE");
//        }).start();

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

    public void plus15() {
        this.m.plus151File();
    }

    public void minus15() {
        this.m.minus15File();
    }

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
        if(o==m)
        {
        //    System.out.println(1);
        this.timeStamp.setValue(m.getTime());
        clock.increcment();
        }

    }


}
