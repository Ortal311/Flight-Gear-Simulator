package viewModel;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import model.SimulatorModel;
import org.w3c.dom.ls.LSOutput;
import view.ControllerView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {


    // SimulatorModel m;
    Model m;

    //ControllerView cv;

    public ViewModelController(Model m) {
        this.m = m;
        m.addObserver(this);
        //  this.cv=cv;
        //cv.addObserver(this);
    }

        //Basic Functions- Buttons
    public void openFile() {
        this.m.openFile();
    }

    public void Play(){
        System.out.printf("arrived 2");

        this.m.playFile();
    }
    public void Pause() {
        this.m.PauseFile();
    }
    public void Stop() {
        this.m.StopFile();
    }

    @Override
    public void update(Observable o, Object arg) {

    }



}
