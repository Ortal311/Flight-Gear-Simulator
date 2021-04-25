package view;

import javafx.stage.FileChooser;
import model.Model;
import viewModel.ViewModelController;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ControllerView implements Observer {

    //ViewModelController vmc;
    Model m = new Model();
    ViewModelController vmc = new ViewModelController(m);

    public void onOpen() {

        System.out.println("1");
        if (this.vmc == null) System.out.println("null");
        this.vmc.openFile();
        // vmc.openFile();
    }

    public void click() {
        System.out.println("new button");
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
