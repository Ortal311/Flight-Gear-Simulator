package view;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Joystick extends Pane {

    public Slider throttle, rudder;
    public Canvas canvas;
//    public Circle outerCircle, innerCircle;

    public List<Node> set() {
        List<Node> lst = new ArrayList<>();

        this.throttle = new Slider();
        throttle.setOrientation(Orientation.VERTICAL);
        throttle.setLayoutX(530);
        throttle.setLayoutY(25);
        throttle.setPrefHeight(230);
        lst.add(throttle);

        this.rudder = new Slider();
        rudder.setLayoutX(545);
        rudder.setLayoutY(255);
        rudder.setPrefWidth(230);
        lst.add(rudder);

        this.canvas = new Canvas();
        canvas.setLayoutX(555);
        canvas.setLayoutY(25);
        canvas.setHeight(230);
        canvas.setWidth(215);
        lst.add(canvas);

        return lst;
    }
}
