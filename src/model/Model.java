package model;

import flightSetting.FlightSetting;
import javafx.stage.FileChooser;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable implements SimulatorModel {

    Socket socket;
    PrintWriter out;
    TimeSeries ts;// = new TimeSeries("reg_flight.csv");
    private boolean stop = false;

    public void close() {
        this.stop = true;
    }

    @Override
    public void ConnectToServer(String ip, double port) {

        //System.out.println("thread worked");
        try {
            socket = new Socket("127.0.0.1", 5402);
            out = new PrintWriter(socket.getOutputStream());

            for (int i = 0; i < ts.rows.size() && !stop; i++)
            {
                    System.out.println(ts.rows.get(i));
                    out.println(ts.rows.get(i));
                    out.flush();
                    try {
                        Thread.sleep(50);//responsible for the speed of the display
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openFile() {
        System.out.printf("3");
        FileChooser fc = new FileChooser();
        fc.setTitle("open CSV file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            System.out.println("the name of the file is:" + chosen.getName());
        }

        if (chosen.getName().contains(".csv"))  //checking the file
        {
            ts = new TimeSeries(chosen.getName());
            //System.out.println(ts.cols.size());
            if (ts.cols.size() != 42)
                System.err.println("wrong amount of columns - should be 42");
        } else
            System.err.println("wrong file, choose csv file");
    }

    public void playFile() {
        System.out.printf("arrived 3");

        new Thread(() -> ConnectToServer("127.0.0.1", 5402)).start();
        //ConnectToServer("127.0.0.1", 5402);
    }

    public void PauseFile() {
    //need to implements
    }

    public void StopFile() {
        new Thread(() -> close()).start();
        //this.close();
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


    //NOTE:we'll need to add get the result of each functions when needed-
    // and we'll get them from the update of the viewModelController

}


