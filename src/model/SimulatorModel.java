package model;

import flightSetting.FlightSetting;

import java.io.IOException;

public interface SimulatorModel {
    public boolean ConnectToServer(String ip, double port);
    public void writeToXML (FlightSetting settings) throws IOException;
    public FlightSetting readFromXML() throws IOException;
    public void openFile();

}
