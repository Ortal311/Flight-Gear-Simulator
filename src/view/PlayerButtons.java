package view;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerButtons extends Pane {

   public Button play, stop, pause, minus, plus, rewind, forward, openCSV, openXML;
   public Slider sliderTime;

//    public  PlayerButtons() {
//
//        this.play = new Button("Play");
//        this.stop = new Button("Stop");
//        this.pause = new Button("Pause");
//        this.minus = new Button("-15");
//        this.plus = new Button("+15");
//        this.rewind = new Button("<<");
//        this.forward = new Button(">>");
//        this.openCSV=new Button("ChooseCSVFile");
//        this.openXML=new Button("ChooseXMLFile");
//        this.sliderTime = new Slider();
//
//    }

    public List<Node> set() {
        List<Node> lst = new ArrayList<>();

        play = new Button("Play");
        play.setLayoutX(25);
        play.setLayoutY(320);
        play.setPrefSize(60, 30);
        lst.add(play);

        this.stop = new Button("Stop");
        stop.setLayoutX(90);
        stop.setLayoutY(320);
        stop.setPrefSize(60, 30);
        lst.add(stop);

        this.pause = new Button("Pause");
        pause.setLayoutX(155);
        pause.setLayoutY(320);
        pause.setPrefSize(60, 30);
        lst.add(pause);

        this.minus = new Button("-15");
        minus.setLayoutX(220);
        minus.setLayoutY(320);
        minus.setPrefSize(60, 30);
        lst.add(minus);

        this.plus = new Button("+15");
        plus.setLayoutX(415);
        plus.setLayoutY(320);
        plus.setPrefSize(60, 30);
        lst.add(plus);

        this.rewind = new Button("<<");
        rewind.setLayoutX(285);
        rewind.setLayoutY(320);
        rewind.setPrefSize(60, 30);
        lst.add(rewind);

        this.forward = new Button(">>");
        forward.setLayoutX(350);
        forward.setLayoutY(320);
        forward.setPrefSize(60, 30);
        lst.add(forward);

        this.openCSV=new Button("ChooseCSVFile");
        openCSV.setLayoutX(25);
        openCSV.setLayoutY(280);
        openCSV.setPrefSize(90, 30);
        lst.add(openCSV);

        this.openXML=new Button("ChooseXMLFile");
        openXML.setLayoutX(120);
        openXML.setLayoutY(280);
        openXML.setPrefSize(90, 30);
        lst.add(openXML);

        this.sliderTime = new Slider();


        return lst;
    }

}


