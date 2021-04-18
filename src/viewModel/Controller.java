package viewModel;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import model.SimulatorModel;
import view.ControllerView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {


    SimulatorModel m;
    ControllerView cv;

    public Controller(Model m, ControllerView cv) {
        this.m=m;
        m.addObserver(this);
        this.cv=cv;
        //cv.addObserver(this);
    }



    @Override
    public void update(Observable o, Object arg) {

    }

  
}
