package flightSetting;

import java.io.IOException;
/*
צריך להוסיף:
מכיוון ששמות העמודות יכולים להשתנות בין הקלטות של כלי טיס שונים
אז רצוי שבקובץ הגדרות זה תהיה לכם הגדרה לשמה של כל עמודה.

אולי נוכל ליצור עוד קובץ הגדרות של כל ההגדרות אבל נקצה כל מחרוזרת (הגדרה) ע"י שימוש ב set חיצוני
אפשר גם להוסיף אותם לכאן ולהתחל את השם הספציפי רק ב set עבור כל מה ששונה ואז כשנשלח אובייקט XML שונה נצטרך לעשות את זה ביצירה
 */

public class FlightSetting {
    private String location="location";
    private int minLocation,maxLocation;

    private String altitude="altitude";
    private int minAltitude,maxAltitude;

    private String speed="speed";
    private int minSpeed,maxSpeed;

    private String direction="direction";
    private int minDirection,maxDirection;

    private String yaw="yaw";
    private int minYaw,maxYaw;

    private String pitch="pitch";
    private int minPitch,maxPitch;

    private String roll="roll";
    private int minRoll,maxRoll;

    private String throttle="throttle";
    private int minThrottle,maxThrottle;

    private String X="X";
    private int minX,maxX;

    private String Y="Y";
    private int minY,maxY;


    private String SteeringWheel="SteeringWheel";
    private int minSteeringWheel,maxSteeringWheel;

    private String speedPlay="speedPlay";



    public String getLocation() {
        return location;
    }

    public int getMinLocation() {
        return minLocation;
    }

    public int getMaxLocation() {
        return maxLocation;
    }

    public String getAltitude() {
        return altitude;
    }

    public int getMinAltitude() {
        return minAltitude;
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public String getSpeed() {
        return speed;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public String getDirection() {
        return direction;
    }

    public int getMinDirection() {
        return minDirection;
    }

    public int getMaxDirection() {
        return maxDirection;
    }

    public String getYaw() {
        return yaw;
    }

    public int getMinYaw() {
        return minYaw;
    }

    public int getMaxYaw() {
        return maxYaw;
    }

    public String getPitch() {
        return pitch;
    }

    public int getMinPitch() {
        return minPitch;
    }

    public int getMaxPitch() {
        return maxPitch;
    }

    public String getRoll() {
        return roll;
    }

    public int getMinRoll() {
        return minRoll;
    }

    public int getMaxRoll() {
        return maxRoll;
    }

    public String getThrottle() {
        return throttle;
    }

    public int getMinThrottle() {
        return minThrottle;
    }

    public int getMaxThrottle() {
        return maxThrottle;
    }

    public String getX() {
        return X;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public String getY() {
        return Y;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public String getSteeringWheel() {
        return SteeringWheel;
    }

    public int getMinSteeringWheel() {
        return minSteeringWheel;
    }

    public int getMaxSteeringWheel() {
        return maxSteeringWheel;
    }

    public String getSpeedPlay() {
        return speedPlay;
    }

}

