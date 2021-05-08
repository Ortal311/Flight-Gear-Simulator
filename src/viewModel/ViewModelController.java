package viewModel;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import model.SimulatorModel;
import model.TimeSeries;
import org.w3c.dom.ls.LSOutput;
import view.ControllerView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {


    // SimulatorModel m;
    Model m;
    public DoubleProperty throttle, rudder, aileron, elevators, sliderTime;

    //TimeSeries ts=new TimeSeries("name");

    //ControllerView cv;

    public ViewModelController(Model m) {
        this.m = m;
        m.addObserver(this);
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        sliderTime = new SimpleDoubleProperty();

        sliderTime.addListener((o, ov, nv) -> m.setTime((double) nv));
        //  this.cv=cv;
        //cv.addObserver(this);

    }

//    public void updateDisplayVariables()
//    {
//       while(m.isStop())
//       {
//           System.out.println("im in update !! ------");
//           m.ts.map.get("aileron").forEach(val -> aileron.setValue(val));
//           m.ts.map.get("elevators").forEach(val -> aileron.setValue(val));
//           m.ts.map.get("throttle").forEach(val -> aileron.setValue(val)); // there are 2 throtteles in csv file???
//           m.ts.map.get("rudder").forEach(val -> aileron.setValue(val));
//
//       }
//    }


    //Basic Functions- Buttons
    public void openFile() {
        this.m.openFile();
    }
    public void openXMLFile() {
        this.m.openXML();
    }

    public void play() {
        System.out.printf("arrived 2");

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

    @Override
    public void update(Observable o, Object arg) {

    }


}
