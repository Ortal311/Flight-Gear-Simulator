package view;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.Model;
import model.SimulatorModel;
import viewModel.Controller;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ControllerView implements Observer {

    public void openFile(){
        FileChooser fc=new FileChooser();
        fc.setTitle("open maze file");
        fc.setInitialDirectory(new File("./"));
        File chosen=fc.showOpenDialog(null);
        if(chosen!=null){
            System.out.println("the name of the file is:"+chosen.getName());
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
