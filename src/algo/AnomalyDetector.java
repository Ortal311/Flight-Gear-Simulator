package algo;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import viewModel.TimeSeries;
import algo.AnomalyReport;

import java.util.List;


public interface AnomalyDetector
{
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	public AnchorPane paint();
}
