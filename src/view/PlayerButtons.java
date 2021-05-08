package view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.awt.*;

public class PlayerButtons extends AnchorPane {

    Button play, stop, pause, minus, plus, rewind, forward,openCSV,openXML;

    public PlayerButtons() {
        this.play = new Button("Play");
        this.stop = new Button("Stop");
        this.pause = new Button("Pause");
        this.minus = new Button("-15");
        this.plus = new Button("+15");
        this.rewind = new Button("<<");
        this.forward = new Button(">>");
        this.openCSV=new Button("ChooseCSVFile");
        this.openXML=new Button("ChooseXMLFile");

    }

}


