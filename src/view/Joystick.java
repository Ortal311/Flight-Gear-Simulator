package view;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Joystick extends Pane {

    Slider throttle, rudder;
    Canvas canvas;

    public List<Node> set() {
        List<Node> lst = new ArrayList<>();



        this.throttle = new Slider();
        throttle.setOrientation(Orientation.VERTICAL);
        throttle.setLayoutX(400);
        throttle.setLayoutY(100);
        throttle.setPrefHeight(170);
        lst.add(throttle);

        this.rudder = new Slider();
        rudder.setLayoutX(350);
        rudder.setLayoutY(50);
        rudder.setPrefWidth(180);
        lst.add(rudder);

        this.canvas = new Canvas();
//        canvas.setLayoutX(300);
//        canvas.setLayoutY(70);
        canvas.setHeight(50);
        canvas.setWidth(50);
        canvas.setStyle("-fx-background-color: black");
        lst.add(canvas);

        return lst;
    }
}
