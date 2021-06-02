package algo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Point {

    public final FloatProperty x;
    public final FloatProperty y;


    public Point(double x, double y) {
        this.x =new SimpleFloatProperty();
        this.y =new SimpleFloatProperty();
    }

    public float getX() {
        return x.get();
    }

    public FloatProperty xProperty() {
        return x;
    }

    public float getY() {
        return y.get();
    }

    public FloatProperty yProperty() {
        return y;
    }
}
