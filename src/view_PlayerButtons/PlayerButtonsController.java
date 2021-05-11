package view_PlayerButtons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import viewModel.ViewModelController;

import java.awt.*;

public class PlayerButtonsController {

    @FXML
    Slider sliderTime;


    ViewModelController vmc;

    public void init(ViewModelController vmc) {

        this.vmc=vmc;
    }

    public void onOpen() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.openFile();


    }

    public void onOpenXML() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.openXMLFile();
    }

    public void onPlay() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.play();
    }

    public void onPause(){
        this.vmc.pause();
    }

    public void onStop(){
        this.vmc.stop();
    }

    public void onRewind(){
        this.vmc.rewind();
    }

    public void onForward(){
        this.vmc.forward();
    }

    public void onPlus15(){
        this.vmc.plus15();
    }

    public void onMinus15(){
        this.vmc.minus15();
    }

}
