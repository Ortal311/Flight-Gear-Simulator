package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class ControllerView extends Pane implements Observer, Initializable {

    @FXML
    Pane board;

    @FXML
    PlayerButtons playerButtons;

    @FXML
    Joystick joystick;

    @FXML
    AttributesList list;

    ViewModelController vmc;
    DoubleProperty aileron,elevators;

    double jx,jy;
    double mx,my;

    public ControllerView() {
        jx=70;
        jy=80;
        aileron=new SimpleDoubleProperty();
        elevators=new SimpleDoubleProperty();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.getChildren().addAll(joystick.set());
        paint();
        board.getChildren().addAll(playerButtons.set());
        board.getChildren().add(list.set());

        playerButtons.openCSV.setOnAction((e) -> {
            onOpen();
            ObservableList<String> lst = FXCollections.observableArrayList();
            lst.addAll(vmc.ts.getAttributes());
            list.list.setItems(lst);
        });
        playerButtons.play.setOnAction((e) -> onPlay());
        playerButtons.pause.setOnAction((e) -> onPause());
        playerButtons.stop.setOnAction((e) -> onStop());
        playerButtons.forward.setOnAction((e) -> onForward());
        playerButtons.rewind.setOnAction((e) -> onRewind());
        playerButtons.minus.setOnAction((e) -> onMinus15());
        playerButtons.plus.setOnAction((e) -> onPlus15());
    }

    public void paint()
    {
        GraphicsContext gc= joystick.canvas.getGraphicsContext2D();
        mx=joystick.getWidth()/2;
        my=joystick.getHeight()/2;
        gc.clearRect(0,0,joystick.getWidth(),joystick.getHeight());
        gc.strokeOval(jx-10,jy-10,80,80);
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

    @Override
    public void update(Observable o, Object arg) {

    }
}
