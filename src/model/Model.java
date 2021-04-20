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
    PrintWriter pr;
    @Override
    public void ConnectToServer(String ip, double port) {

        try {
            socket=new Socket("127.0.0.1",5402);
            String command1="set /controls/flight/aileron";
            String command2="set /controls/flight/elevator";
            String command3="set /controls/flight/rudder";
            String command4="set /controls/engines/current-engine/throttle";

            pr=new PrintWriter(socket.getOutputStream());
            pr.println(command1+" "+1);
            pr.println(command2+" "+1);
            pr.println(command3+" "+1);
            pr.println(command4+" "+1);

            pr.flush();
        } catch (IOException e) { e.printStackTrace();}

    }
    public void openFile(){
        FileChooser fc=new FileChooser();
        fc.setTitle("open maze file");
        fc.setInitialDirectory(new File("./"));
        File chosen=fc.showOpenDialog(null);
        if(chosen!=null){
            System.out.println("the name of the file is:"+chosen.getName());
        }
    }

    @Override
    public void writeToXML(FlightSetting settings) throws IOException {
        FileOutputStream fos = new FileOutputStream("settings.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :"+e.toString());
            }
        });
        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }

    @Override
    public FlightSetting readFromXML() throws IOException{

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


