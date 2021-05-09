package viewModel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.Model;
import viewModel.TimeSeries;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {


    // SimulatorModel m;
    Model m;
    public DoubleProperty throttle, rudder, aileron, elevators, sliderTime;
    public TimeSeries ts;

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
        System.out.printf("3");
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
            //System.out.println(ts.cols.size());
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
    }

    public void openXMLFile()
    {
        m.openXML();
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
