package model;

import algo.SimpleAnomalyDetector;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import viewModel.TimeSeries;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Model extends Observable implements SimulatorModel {

    public Socket socket;
    public PrintWriter out;
    public TimeSeries ts_Anomal;
    public Options op = new Options();
    Thread displaySetting;
    public Map<String, Attribute> attributeMap;
    public FlightSetting properties;
    // static double time = 0;
//    private double playSpeed = 100;
    private double time = 0;
    private volatile boolean pause = false;
    private boolean stop = false;
    public static boolean afterPause = false;
    public static boolean afterStop = false;
    public static boolean afterRewind = false;
    public static boolean afterForward = false;
    public boolean isConnect;
    public SimpleAnomalyDetector ad;

    public Model() {
        this.properties = new FlightSetting();
    }

    public boolean isStop() {
        return stop;
    }

    public void close() {
        time = 0;
        this.stop = true;
    }

    public double getTime() {
        return this.time;
    }


//    public double getPlaySpeed() {
//        return playSpeed;
//    }
//
//    public void setPlaySpeed(double playSpeed) {
//        this.playSpeed = playSpeed;
//    }
//    public void setTime(double time) {
//        this.time = time;
//    }


    @Override
    public boolean ConnectToServer(String ip, double port) {
        try {
            socket = new Socket("127.0.0.1", 5402);
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("connected to server");
            return true;

        } catch (IOException e) {
            System.out.println("didnt connect");
            return false;
        }
    }

    public void setTimeSeries(TimeSeries ts) {
        this.ts_Anomal = ts;
    }

    synchronized public void displayFlight(boolean conncetServer) {
        int i = 0;
        int sizeTS = ts_Anomal.getSize();
     //   boolean condition = op.rewind ? i >= 0 : i < ts.rows.size();//if rewind go while>0 else (regula) go while <ts.size

        for (i = (int) time; i<sizeTS;i++ ) {
            while (pause || op.scroll || afterStop || op.forward|| op.rewind)  //pause needs to be replaced with thread( works only one time now)
            {
                try {
                    // System.out.println(Thread.currentThread().getName());
                    //System.out.println(this);

                    if (afterStop) {
                        displaySetting.stop();
                    }

                    if (afterPause) {
                        this.wait();
                    }

                    if (op.forward) {
                        System.out.println("blabla after forward");
                        if (i < sizeTS - 151)
                            i += 150;
                        else
                            i = sizeTS;
                        op.forward = false;
                    }
                    if (op.rewind) {
                        System.out.println("blabla after rewind");
                        if ((i - 150) > 0)
                            i -= 150;
                         else
                            i = 1;
                        op.rewind = false;
                    }
                    if(op.scroll){
                        op.scroll=false;
                        i=(int)time;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           // System.out.println(ts.getAtts().get(i));
            System.out.println(ts_Anomal.rows.get(i));
            if (conncetServer) {
                out.println(ts_Anomal.rows.get(i));
                //out.println(ts.getAtts().get(i));
                out.flush();
            }
            time = i;
            setChanged();
            notifyObservers();
            try {
                Thread.sleep(properties.getPlaySpeed());//responsible for the speed of the display
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          //  i++;
        }
    }

    public void setTime(double time) {
        this.time = time;
        op.scroll=true;
       // new Thread(() -> displayFlight(true)).start();
    }

    public boolean openXML() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open XML file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (!chosen.getName().contains(".xml"))  //checking the file
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong file chosen");
            alert.setContentText("Please choose a xml file");
            alert.showAndWait();
        } else {
            try {
                this.properties = readFromXML(chosen.getName());
                if(this.properties != null) {
                    createMapAttribute();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    synchronized public void playFile() {
        //to delete
        String attribute="";
        System.out.println("the attrucute is "+ attribute);

        if (afterForward) {//somehow it does not responded to it and cannot go back to normal rate
            afterForward = false;
            properties.setPlaySpeed(100);
        } else if (afterRewind) {
            op.rewind = false;
        } else if (afterPause) {
            //  System.out.println("afterPause in display");
            this.notify();
            pause = false;
            afterPause = false;
        } else if (afterStop) {//creating a new thread to run displayFlight()
            if (isConnect) {
                displaySetting = new Thread(() -> displayFlight(true), "Thread of displaySetting function");
                displaySetting.start();
                afterStop = false;
            } else {
                displaySetting = new Thread(() -> displayFlight(false), "Thread of displaySetting function");
                displaySetting.start();
                afterStop = false;
            }

        } else {//first time of Play

            isConnect = ConnectToServer(properties.getIp(), properties.getPort());
            if (isConnect) {
                displaySetting = new Thread(() -> displayFlight(true), "Thread of displaySetting function");
                displaySetting.start();
            } else {//if not connectToFG
                displaySetting = new Thread(() -> displayFlight(false), "Thread of displaySetting function");
                displaySetting.start();
            }
        }
    }

    public void pauseFile() {
        pause = true;
        afterPause = true;
    }


    public void stopFile() {
        afterStop = true;
        this.time = 0;

    }

    public void rewindFile() {
        op.rewind = true;
    }

    public void forwardFile() {
        op.forward = true;
        // new Thread(() -> displayFlight()).start();
    }

    @Override
    public void openFile() {

    }
    //class loader for Anomaly Detector's files

    public void loadAnomalyDetector() {//String input
//        URLClassLoader urlClassLoader= URLClassLoader.newInstance(new URL[]{new URL("file://"+input)});
//        Class<?>c=urlClassLoader.loadClass(urlClassLoader.getName());
//        SimpleAnomalyDetector ad=(SimpleAnomalyDetector) c.newInstance();

        ad = new SimpleAnomalyDetector();
        ad.learnNormal(ts_Anomal);

    }
    @Override
    public Runnable getPainter() {
        return ()->ad.paintALGgraph();
    }


    public void writeToXML(FlightSetting settings) throws IOException {
        FileOutputStream fos = new FileOutputStream("settings.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :" + e.toString());
            }
        });

        List<Attribute> lst = new ArrayList<>();
        lst.add(createAtrribute("aileron", 0, -1, 1));
        lst.add(createAtrribute("elevators", 1, -1, 1));
        lst.add(createAtrribute("rudder", 2, 0, 1));
        lst.add(createAtrribute("throttle", 6, 0, 1));
        lst.add(createAtrribute("altimeter", 25, null, null));
        lst.add(createAtrribute("airSpeed", 24, null, null));
        lst.add(createAtrribute("fd", 36, 0, 360));
        lst.add(createAtrribute("pitch", 29, -10, 17));
        lst.add(createAtrribute("roll", 17, -38, 43));
        lst.add(createAtrribute("yaw", 20, -29, 91));
        settings.setAttributes(lst);
        settings.setPort((double) 5402);
        settings.setIp("127.0.0.1");
        settings.setPlaySpeed(100);

        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }

    public Attribute createAtrribute(String name, Integer associativeName, Integer min, Integer max) {
        Attribute res = new Attribute();
        res.setName(name);
        res.setAssociativeName(associativeName);
        res.setMax(max);
        res.setMin(min);

        return res;
    }

    public FlightSetting readFromXML(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        XMLDecoder decoder = new XMLDecoder(fis);
        FlightSetting decodedSettings = (FlightSetting) decoder.readObject();
        decoder.close();
        fis.close();
        return decodedSettings;
    }

    public void createMapAttribute() {
        this.attributeMap = new HashMap<>();

        for(Attribute attribute: properties.getAttributes()){
            attributeMap.put(attribute.name, attribute);
        }
    }


    //NOTE:we'll need to add get the result of each functions when needed-
    // and we'll get them from the update of the viewModelController

}