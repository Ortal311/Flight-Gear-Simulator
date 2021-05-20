package viewModel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {

    Model m;
    public DoubleProperty timeStamp, throttle, rudder, aileron,
            elevators, sliderTime, flagAttributes;
    public TimeSeries ts;

    public ViewModelController(Model m) {
        this.m = m;
        m.addObserver(this);

        timeStamp = new SimpleDoubleProperty();
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        sliderTime = new SimpleDoubleProperty();
        flagAttributes= new SimpleDoubleProperty();

        timeStamp.addListener((o, ov, nv) -> {
            updateDisplayVariables(nv.intValue());
            //m.displayFlight(nv.intValue());
        });
//        sliderTime.addListener((o, ov, nv) -> m.setTime((double) nv));
    }

    public void updateDisplayVariables(int value) {
//        aileron.setValue(ts.getValueByTime("aileron", value));
//        elevators.setValue(ts.getValueByTime("elevators", value));
        rudder.setValue(ts.getValueByTime(2, value));
        throttle.setValue(ts.getValueByTime(6, value));
        sliderTime.setValue(value);
    }


    //Basic Functions- Buttons
    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open CSV file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            System.out.println("the name of the file is:" + chosen.getName());
        }

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
        flagAttributes.set(1);
    }

    public void openXMLFile()
    {
        m.openXML();
    }

    public void play() {
        new Thread(() -> {
            for(int i = 1; i < ts.getSize() - 1; i++) {
                this.timeStamp.setValue(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("DONE");
        }).start();
        //this.m.playFile();
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

    @Override
    public void update(Observable o, Object arg) {

    }


}
