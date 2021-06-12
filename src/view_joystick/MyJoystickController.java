package view_joystick;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class MyJoystickController {

    @FXML
    Slider rudder;

    @FXML
    Slider throttle;

    @FXML
    Canvas joystick;

    //private double jx,jy;
    private double mx,my;
    public DoubleProperty jx,jy;
    public MyJoystickController() {
        jx= new SimpleDoubleProperty();
        jy= new SimpleDoubleProperty();
        jx.setValue(0);//70
        jy.setValue(0);//80
        jy.addListener(v->paint());
        jx.addListener(v->paint());
    }


    public void paint()
    {
        GraphicsContext gc= joystick.getGraphicsContext2D();
        GraphicsContext gc2=joystick.getGraphicsContext2D();
        mx= joystick.getWidth()/2;
        my=joystick.getHeight()/2;

        gc.clearRect(0,0,joystick.getWidth(),joystick.getHeight());
        //gc.strokeOval(mx-jx.doubleValue()*50,my-jy.doubleValue()*50,60,60);

        gc.fillOval(mx-30+(jx.doubleValue()*50),my-30+(jy.doubleValue()*50),60,60);
        gc2.strokeOval(mx-50,my-50,100,100);

        gc2.setFill(Color.GRAY);
        gc.setFill(Color.BLACK);


//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(5);
//        gc.strokeLine(40, 10, 10, 40);

//        gc.setFill(Color.BLUE);
//        gc.setStroke(Color.BLACK);
    }
//my
}
