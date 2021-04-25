package viewModel;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import model.SimulatorModel;
import view.ControllerView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModelController extends Observable implements Observer {


    SimulatorModel m;
    //ControllerView cv;

    public ViewModelController(Model m) {
        this.m=m;
        m.addObserver(this);
        //  this.cv=cv;
        //cv.addObserver(this);
    }
    public void openFile(){
        m.openFile();
    }


    @Override
    public void update(Observable o, Object arg) {

    }

  
}
