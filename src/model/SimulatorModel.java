package model;

import viewModel.TimeSeries;

public interface SimulatorModel {
    public boolean ConnectToServer(String ip, double port);
    public void displayFlight(boolean conncetServer);
    public void setTimeSeries(TimeSeries ts,TimeSeries ts2);
    public void openFile();
//    public Runnable getPainter();
}
