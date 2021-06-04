package algo;

import viewModel.TimeSeries;

import java.util.List;


public interface AnomalyDetector
{
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	void paintALGgraph();
}
