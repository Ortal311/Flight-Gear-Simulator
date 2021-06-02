package algo;
import java.lang.Math;

public class StatLib {


    // simple average
    public static float avg(float[] x){

        if(x.length == 0)
            return 0;

        float size = x.length;
        float sum = 0;

        for(int j = 0; j < size; j++)
            sum += x[j];

        return sum / size;
    }

    // returns the variance of X and Y
    public static float var(float[] x){

        if(x.length == 0)
            return 0;

        float sum = 0;
        float m = avg(x);

        for(int i = 0; i < x.length; i++){
            sum += x[i] * x[i];
        }

        return (sum / (float)x.length) - (m * m);
    }

    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y){

        if(x.length == 0 && y.length == 0)
            return 0;

        float avgX = avg(x);
        float avgY = avg(y);
        float sum = 0;

        for(int i = 0; i < x.length; i++){
            sum += (x[i] - avgX) * (y[i] - avgY);
        }

        return sum / x.length;
    }


    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y){

        if(x.length == 0 && y.length == 0)
            return 0;

        return (float) (cov(x, y) / (Math.sqrt(var(x)) * Math.sqrt(var(y))));
    }

    // performs a linear regression and returns the line equation
    public static Line linear_reg(Point[] points){

        float arrX[] = new float[points.length];
        float arrY[] = new float[points.length];
        float a;
        float b;

        for(int i = 0; i < arrX.length; i++){
            arrX[i] = points[i].x.floatValue();
        }

        for(int j = 0; j < arrY.length; j++){
            arrY[j] = points[j].y.floatValue();
        }

        if(var(arrX) == 0)
            a = 0;
        else
            a = cov(arrX, arrY) / var(arrX);

        b = avg(arrY) - (a * avg(arrX));

        Line res = new Line(a, b);
        return res;
    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p,Point[] points){

        Line l = linear_reg(points);

        float res = l.f(p.x.floatValue());

        if(res - p.y.floatValue() < 0)
            return (-1) * (res - p.y.floatValue());

        else
            return res - p.y.floatValue();
    }

    // returns the deviation between point p and the line
    public static float dev(Point p,Line l){

        float res = l.f(p.x.floatValue());

        if(res - p.y.floatValue() < 0)
            return (-1) * (res - p.y.floatValue());

        else
            return res - p.y.floatValue();
    }
}