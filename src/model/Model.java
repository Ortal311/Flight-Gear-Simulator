package model;

import flightSetting.FlightSetting;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import viewModel.TimeSeries;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable implements SimulatorModel {

    public Socket socket;
    public PrintWriter out;
    public TimeSeries ts;// = new TimeSeries("reg_flight.csv");
    double time = 0;
    //int ms=100;//millisecond
    Options op = new Options();
    Thread displaySetting;

    private volatile boolean pause = false;
    private boolean stop = false;
    public static boolean afterPause=false;

    public boolean isStop() {
        return stop;
    }

    public void close() {
        time = 0;
        this.stop = true;
    }

    @Override
     public void ConnectToServer(String ip, double port) {
        //System.out.println("thread worked");
        try {
            socket = new Socket("127.0.0.1", 5402);
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("inside connectedToserver  "+ Thread.currentThread().getName());

            // new Thread(() -> displayFlight()).start();


            if (stop == true) {// needs to be replaced with join!!
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTimeSeries(TimeSeries ts)
    {
        this.ts=ts;
    }
   /* public class PlayDisplay implements Runnable {
        @Override
        synchronized public void run() {
            int i = 0;//= time;
            boolean condition = op.rewind ? i >= 0 : i < ts.rows.size();
            op.setPlaySpeed(op.forward ? op.playSpeed / 2 : 100);
            time = op.plus15 ? time + 15 : time;
            time = op.minus15 ? time - 15 : time;

            for (i = (int) time; condition && !stop; ) {
                while (pause || op.scroll)  //pause needs to be replaced with thread( works only one time now)
                {
//                    System.out.println(displaySetting.getName());
//                    System.out.println(displaySetting.isAlive());
//                    try {
//                        //this.wait();
//                        System.out.println("inside Runnalbe"+ Thread.currentThread().getName());
//                        displaySetting.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(displaySetting.isAlive());

                }
                System.out.println(ts.rows.get(i));
                out.println(ts.rows.get(i));
                out.flush();
                time = i;
                try {
                    Thread.sleep((long) op.playSpeed);//responsible for the speed of the display
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                i = op.rewind ? i - 1 : i + 1;

//            if(pause)
//            {
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {}
//
//            }
            }
        }
    }
*/
        synchronized public void displayFlight() {
        int i = 0;//= time;
        boolean condition = op.rewind ? i >= 0 : i < ts.rows.size();
        op.setPlaySpeed(op.forward ? op.playSpeed / 2 : 100);
        time = op.plus15 ? time + 15 : time;
        time = op.minus15 ? time - 15 : time;

        for (i = (int) time; condition && !stop; ) {
            while (pause || op.scroll)  //pause needs to be replaced with thread( works only one time now)
            {
                System.out.println(displaySetting.getName());
                System.out.println("inside display_whileCon  "+ Thread.currentThread().getName());

                try {
                    System.out.println(Thread.currentThread().getName());
                    //Thread.currentThread().wait();
                    System.out.println(this.toString());
                    System.out.println(this);
                    System.out.println(this.displaySetting.isAlive());

                    this.wait();// it works because it close all the process (Model)!!!
                  //  Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(displaySetting.isAlive());

            }
            System.out.println(ts.rows.get(i));
            System.out.println("inside displaySendData  "+ Thread.currentThread().getName());

            out.println(ts.rows.get(i));
            out.flush();
            time = i;
            try {
                Thread.sleep((long) op.playSpeed);//responsible for the speed of the display
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i = op.rewind ? i - 1 : i + 1;

//            if(pause)
//            {
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {}
//
//            }
        }
    }

    public void setTime(double time) {
        this.time = time;
        new Thread(() -> displayFlight()).start();
    }


    public void openXML() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open XML file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            System.out.println("the name of the file is:" + chosen.getName());
        }
        if (!chosen.getName().contains(".xml"))  //checking the file
        {
            //System.err.println("wrong file, choose xml file");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong file chosen");
            alert.setContentText("please choose a csv file");
            alert.showAndWait();
        }
    }

    synchronized public void playFile() {
        System.out.println("inside playFile function "+ Thread.currentThread().getName());
        if(afterPause){
            this.notify();
            pause=false;
        }
        if (!stop && pause) {
            System.out.println("lkafjldafasjdflasdfal");
            pause = false;
            //this.notify();
            //displayFlight();
        } else {
       //     new Thread(() -> ConnectToServer("127.0.0.1", 5402)).start();
            if(!afterPause)
            {

                ConnectToServer("127.0.0.1", 5402);

                System.out.println("connected to server again after play");
            }
            //here we operate displayFlight with Thread here
            displaySetting=new Thread(()->displayFlight(),"Thread of displaySetting function");
            displaySetting.start();



            //here operate displayFlight that is inside Runnable
           /* PlayDisplay p=new PlayDisplay();
            displaySetting=new Thread(p);
            displaySetting.start();*/
            System.out.println("inside playFile  "+ Thread.currentThread().getName());

        }
    }

     public void pauseFile() {
//        new Thread(() -> pause()).start();
        System.out.println("inside puaseFile  "+ Thread.currentThread().getName());
         System.out.println(this);
        pause = true;

         afterPause=true;

    }

//    public void pause() {
//        pause = true;
//    }

    public void stopFile() {
        new Thread(() -> close()).start();
        //this.close();
    }

    public void rewindFile() {
        op.rewind = true;
        new Thread(() -> displayFlight()).start();
    }

    public void forwardFile() {
        op.forward = true;
        new Thread(() -> displayFlight()).start();
    }

    public void plus151File() {
        op.plus15 = true;
        new Thread(() -> displayFlight()).start();
    }

    public void minus15File() {
        op.minus15 = true;
        new Thread(() -> displayFlight()).start();
    }

    @Override
    public void writeToXML(FlightSetting settings) throws IOException {
        FileOutputStream fos = new FileOutputStream("settings.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :" + e.toString());
            }
        });
        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }

    @Override
    public FlightSetting readFromXML() throws IOException {

        FileInputStream fis = new FileInputStream("settings.xml");
        XMLDecoder decoder = new XMLDecoder(fis);
        FlightSetting decodedSettings = (FlightSetting) decoder.readObject();
        decoder.close();
        fis.close();
        return decodedSettings;
    }

    @Override
    public void openFile() {

    }


    //NOTE:we'll need to add get the result of each functions when needed-
    // and we'll get them from the update of the viewModelController

}