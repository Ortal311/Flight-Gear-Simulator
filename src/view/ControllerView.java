package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.Model;
import viewModel.ViewModelController;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
//for implements Initializable
import javafx.fxml.Initializable;



public class ControllerView extends Pane implements Observer, Initializable {

    @FXML
    Pane board;

    @FXML
    PlayerButtons playerButtons;

    @FXML
    Joystick joystick;

//    @FXML
//    Canvas joystick;
//    @FXML
//    Slider throttle;
//    @FXML
//    Slider rudder;
//    @FXML
//    Slider sliderTime;


    ViewModelController vmc;
    DoubleProperty aileron,elevators;

    double jx,jy;
    double mx,my;

    public ControllerView() {
        jx=0;
        jy=0;
        aileron=new SimpleDoubleProperty();
        elevators=new SimpleDoubleProperty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.getChildren().addAll(joystick.set());
        board.getChildren().addAll(playerButtons.set());
    }

    void init(ViewModelController vmc)
    {
        this.vmc=vmc;
//        throttle.valueProperty().bind(vmc.throttle);
//        //throttle.valueProperty().bindBidirectional(vmc.throttle); for the oposite side
//        board.getChildren().add(new PlayerButtons());
//        rudder.valueProperty().bind(vmc.rudder);
//        aileron.bind(vmc.aileron);
//        elevators.bind(vmc.elevators);
//        vmc.sliderTime.bind(sliderTime.valueProperty());

        /*aileron.addListener((o,ov,nv)->{
            System.out.println("aileron update ------ " + nv);
            jx = (double)nv;
            paint();
        });
        elevators.addListener((o,ov,nv)-> {jy = (double)nv; paint();});*/

        //new Thread(() -> vmc.updateDisplayVariables()).start();
    }

    public void paint()
    {
//        GraphicsContext gc= joystick.getGraphicsContext2D();
//        mx=joystick.getWidth()/2;
//        my=joystick.getHeight()/2;
//        gc.clearRect(0,0,joystick.getWidth(),joystick.getHeight());
//        gc.strokeOval(jx-10,jy-10,100,100);
    }

    public void onOpen() {

        System.out.println("1");
        if (this.vmc == null)
            System.out.println("null");
        this.vmc.openFile();
        // vmc.openFile();
    }
    public void onOpenXML() {

        System.out.println("1");
        if (this.vmc == null)
            System.out.println("null");
        this.vmc.openXMLFile();
        // vmc.openFile();
    }


    public void onPlay() {
        if (this.vmc == null)
            System.out.println("null");
        //System.out.printf("arrived 1");
        this.vmc.play();
    }

    public void onPause(){
        System.out.println("pause 1");
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

    @Override
    public void update(Observable o, Object arg) {

    }
}
