package view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.w3c.dom.Node;

import java.awt.*;
import java.util.ArrayList;

public class PlayerButtons extends Pane {

   public Button play, stop, pause, minus, plus, rewind, forward,openCSV,openXML;

    public  PlayerButtons() {

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

    public void set() {
        List<Node> lst = new ArrayList<>();
    }

}


