package view;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PlayerButtons extends Pane {

   public Button play, stop, pause, minus, plus, rewind, forward, openCSV, openXML;
   public Slider sliderTime;
   public Text textPlaySpeed, textTime;
   public TextField textField;

    public List<Node> set() {
        List<Node> lst = new ArrayList<>();

        play = new Button("Play");
        play.setLayoutX(20);
        play.setLayoutY(465);
        play.setPrefSize(60, 30);
        lst.add(play);

        this.stop = new Button("Stop");
        stop.setLayoutX(85);
        stop.setLayoutY(465);
        stop.setPrefSize(60, 30);
        lst.add(stop);

        this.pause = new Button("Pause");
        pause.setLayoutX(150);
        pause.setLayoutY(465);
        pause.setPrefSize(60, 30);
        lst.add(pause);

        this.minus = new Button("-15");
        minus.setLayoutX(215);
        minus.setLayoutY(465);
        minus.setPrefSize(60, 30);
        lst.add(minus);

        this.plus = new Button("+15");
        plus.setLayoutX(410);
        plus.setLayoutY(465);
        plus.setPrefSize(60, 30);
        lst.add(plus);

        this.rewind = new Button("<<");
        rewind.setLayoutX(280);
        rewind.setLayoutY(465);
        rewind.setPrefSize(60, 30);
        lst.add(rewind);

        this.forward = new Button(">>");
        forward.setLayoutX(345);
        forward.setLayoutY(465);
        forward.setPrefSize(60, 30);
        lst.add(forward);

        this.openCSV=new Button("ChooseCSVFile");
        openCSV.setLayoutX(20);
        openCSV.setLayoutY(425);
        openCSV.setPrefSize(90, 30);
        lst.add(openCSV);

        this.openXML=new Button("ChooseXMLFile");
        openXML.setLayoutX(120);
        openXML.setLayoutY(425);
        openXML.setPrefSize(90, 30);
        lst.add(openXML);

        this.sliderTime = new Slider();
        sliderTime.setLayoutX(17);
        sliderTime.setLayoutY(515);
        sliderTime.setPrefWidth(760);
        lst.add(sliderTime);

        this.textPlaySpeed = new Text("Play Speed");
        textPlaySpeed.setLayoutX(495);
        textPlaySpeed.setLayoutY(487);
        textPlaySpeed.setStyle("-fx-font-size: 17px");
        lst.add(textPlaySpeed);

        this.textField = new TextField("1.0");
        textField.setLayoutX(580);
        textField.setLayoutY(468);
        textField.setPrefWidth(45);
        lst.add(textField);

        this.textTime = new Text("00 : 00 : 00");
        textTime.setLayoutX(650);
        textTime.setLayoutY(488);
        textTime.setStyle("-fx-font-size: 20px");
        lst.add(textTime);

        return lst;
    }

}


