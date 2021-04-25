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

    @Override
    public void ConnectToServer(String ip, double port) {

        try {
            socket = new Socket("127.0.0.1", 5402);
            System.out.println("here 1");
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("here 2");
            BufferedReader in = new BufferedReader(new FileReader("reg_flight.csv"));
            System.out.println("here 3");
            String line;

            while ((line = in.readLine()) != null) {

                System.out.println(line);

                out.println(line);
                out.flush();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            out.close();
            in.close();
            socket.close();
//            String command1="set /controls/flight/aileron";
//            String command2="set /controls/flight/elevator";
//            String command3="set /controls/flight/rudder";
//            String command4="set /controls/engines/current-engine/throttle";
//
//            out.println(command1+" "+1);
//            out.println(command2+" "+1);
//            out.println(command3+" "+1);
//            out.println(command4+" "+1);

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open maze file");
        fc.setInitialDirectory(new File("./"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            System.out.println("the name of the file is:" + chosen.getName());
        }
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


