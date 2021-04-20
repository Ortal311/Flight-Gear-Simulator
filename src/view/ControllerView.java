package view;

import javafx.stage.FileChooser;
import viewModel.ViewModelController;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ControllerView implements Observer {
    ViewModelController vmc;

    public void onOpen(){
        vmc.openFile();
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
