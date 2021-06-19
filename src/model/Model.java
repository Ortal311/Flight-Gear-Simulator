package model;

import algo.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import viewModel.TimeSeries;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

public class Model extends Observable implements SimulatorModel {

    public Socket socket;
    public PrintWriter out;
    public TimeSeries ts_Anomal, ts_reg;
    public Options op = new Options();
    Thread displaySetting;
    public Map<String, Attribute> attributeMap;
    public FlightSetting properties;
    private double time = 0;
    private volatile boolean pause = false;
    private boolean stop = false;
    public static boolean afterPause = false;
    public static boolean afterStop = false;
    public static boolean afterRewind = false;
    public static boolean afterForward = false;
    public boolean isConnect, algFile = false;
    public String algName;

    //public AnomalyDetector ad;
    public SimpleAnomalyDetector ad;
    public ZScoreAlgorithm zScore;
    public hybridAlgorithm hyperALG;

    public AnchorPane APref;


    public StringProperty attribute1 = new SimpleStringProperty();
    public StringProperty attribute2 = new SimpleStringProperty();

    public DoubleProperty valAtt1X = new SimpleDoubleProperty();
    public DoubleProperty vaAtt1Xend = new SimpleDoubleProperty();

    public DoubleProperty valAtt2Y = new SimpleDoubleProperty();
    public DoubleProperty vaAtt2Yend = new SimpleDoubleProperty();

    public DoubleProperty timeStep = new SimpleDoubleProperty();

    public DoubleProperty valPointX = new SimpleDoubleProperty();
    public DoubleProperty valPointY = new SimpleDoubleProperty();

    public Line regLineForCorrelateAttribute;


    public Boolean loadAnomalyDetector(String path, String nameALG) throws Exception {//String input

        algName = nameALG.split("\\.")[0];

        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file:\\" + path)});
        Class<?> c = urlClassLoader.loadClass("algo."+algName);


        if (algName.equals("hybridAlgorithm")) {
            hyperALG = (hybridAlgorithm) c.newInstance();
            System.out.println(hyperALG.toString());
            new Thread(() -> initData()).start();//needs if to init data at first time
            hyperALG.learnNormal(ts_reg);
            hyperALG.detect(ts_Anomal);

        } else if (algName.equals("SimpleAnomalyDetector")) {
            ad = (SimpleAnomalyDetector) c.newInstance();
            new Thread(() -> initData()).start();
            ad.learnNormal(ts_reg);
            ad.detect(ts_Anomal);
        } else if (algName.equals("ZScoreAlgorithm")) {
            zScore = (ZScoreAlgorithm) c.newInstance();
            new Thread(() -> initData()).start();
            zScore.learnNormal(ts_reg);
            zScore.detect(ts_Anomal);
        }


//        ad = new SimpleAnomalyDetector();
//        ad.learnNormal(ts_reg);
//        ad.detect(ts_Anomal);
//        getNormal = ad.getNormalModel();

//        zScore=new ZScoreAlgorithm();
//         zScore.learnNormal(ts_reg);
//        zScore.detect(ts_Anomal);


        //  hyperALG = new hybridAlgorithm();
//        new Thread(() -> initData()).start();//needs if to init data at first time
//        hyperALG.learnNormal(ts_reg);
//        hyperALG.detect(ts_Anomal);

        return false;
    }

    public void initData() {
        setVarivablesTOALG();
        setVarivablesNamesTOALG();
    }

    public void setVarivablesTOALG() {//listen to timeStep and init line chart of reg


        if (algName.equals("SimpleAnomalyDetector"))
            ad.timeStep.bind(timeStep);

        else if (algName.equals("ZScoreAlgorithm"))
            zScore.timeStep.bind(timeStep);

        else
            hyperALG.timeStep.bind(timeStep);

    }

    public void setVarivablesNamesTOALG() {//Listen to chosen attribute

        if (algName.equals("SimpleAnomalyDetector"))
            ad.attribute1.bind(attribute1);

        else if (algName.equals("ZScoreAlgorithm"))
            zScore.Attribute.bind(attribute1);
        else
            hyperALG.attribute1.bind(attribute1);

    }

    public Callable<AnchorPane> getPainter() {

             //reg
        if (algName.equals("SimpleAnomalyDetector"))
            return () -> ad.paint();

            //zScore
        else if (algName.equals("ZScoreAlgorithm"))
            return () -> zScore.paint();

            //HyperALG
        else
            return () -> hyperALG.paint();
    }

    public void setTimeSeries(TimeSeries ts, String tsType) {
        if (tsType.equals("Train"))
            this.ts_reg = ts;
        else if (tsType.equals("Test"))
            this.ts_Anomal = ts;
    }

    // public SimpleAnomalyDetector  ad = new SimpleAnomalyDetector();

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


    synchronized public void displayFlight(boolean conncetServer) {
        int i = 0;
        int sizeTS = ts_Anomal.getSize();
        //   boolean condition = op.rewind ? i >= 0 : i < ts.rows.size();//if rewind go while>0 else (regula) go while <ts.size

        for (i = (int) time; i < sizeTS; i++) {
            timeStep.setValue(time);
            while (pause || op.scroll || afterStop || op.forward || op.rewind)  //pause needs to be replaced with thread( works only one time now)
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
                        if (i < sizeTS - 151)
                            i += 150;
                        else
                            i = sizeTS;
                        op.forward = false;
                    }
                    if (op.rewind) {
                        if ((i - 150) > 0)
                            i -= 150;
                        else
                            i = 1;
                        op.rewind = false;
                    }
                    if (op.scroll) {
                        op.scroll = false;
                        i = (int) time;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // System.out.println(ts.getAtts().get(i));
            //   System.out.println(ts_Anomal.rows.get(i));
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

        }
    }

    public void setTime(double time) {
        this.time = time;
        op.scroll = true;
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
                if (this.properties != null) {
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
        String attribute = "";
        System.out.println("the attrucute is " + attribute);

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


    public void createMapAttribute() {
        this.attributeMap = new HashMap<>();

        for (Attribute attribute : properties.getAttributes()) {
            attributeMap.put(attribute.name, attribute);
        }
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


    //NOTE:we'll need to add get the result of each functions when needed-
    // and we'll get them from the update of the viewModelController

}