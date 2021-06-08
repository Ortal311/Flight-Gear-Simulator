package algo;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import viewModel.TimeSeries;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import static algo.StatLib.*;
import java.util.ArrayList;

public class ZScoreAlgorithm implements AnomalyDetector{
	//TimeSeries ts;
	
	Vector<Float> tx;

	public ZScoreAlgorithm()
	{
		tx = new Vector<>();
	}

	
	public float[] ListToArr(List<Float> lst)
	{
		float[] res= new float[lst.size()];
		
		for(int i=0; i<res.length; i++) {
			res[i]= lst.get(i);
		}
		return res;
	}
	
	
	public float calcZScore(List<Float> col)
	{
		float res=0;
		float arrAvg;
		float[] arrFloat;
		float arrSigma;
		
		for(int x = 0; x < col.size(); x++)
		{
			if(x == 0) {
				return 0;
			}
			arrFloat = ListToArr(col.subList(0, x - 1));
			arrAvg = avg(arrFloat);
			arrSigma=(float)Math.sqrt(var(arrFloat));
			res = Math.abs((col.get(x)-arrAvg))/arrSigma;
		}
		return res;
	}
	
	
	public float argMax(LinkedList<Float> z)
	{
		float max=0;
		for(int i=0; i < z.size(); i++)
		{
			if(max<z.get(i))
				max=z.get(i);
		}
		return max;
	}
	
	
	public void learnNormal(TimeSeries ts)
	{
		LinkedList<Float> zScored = new LinkedList<>();

//		for(ArrayList<Float> col: ts.values())
//		{
//			for(int j = 0; j < col.size(); j++) {
//				zScored.add(calcZScore(col.subList(0, j - 1)));
//			}
//
//			tx.add(argMax(zScored));
//		}
	}
	

	public List<AnomalyReport> detect(TimeSeries data)
	{
		int i=0;
		ArrayList<AnomalyReport> v = new ArrayList<>();
		//for(ArrayList<Float> col: data.ts.values()) {
//		for(ArrayList<Float> col: data) {
//			for(int j = 0; j < col.size(); j++) {
//				if(calcZScore(col.subList(0, j - 1)) > tx.get(i++)){
//					return true;
//				}
//			}
//		}
		return v;
	}

	@Override
	public AnchorPane paint() {
		AnchorPane ap=new AnchorPane();

		LineChart<Number,Number> regGraph=new LineChart<>(new NumberAxis(),new NumberAxis());
		XYChart.Series<Number,Number>chosenAttribute=new XYChart.Series<>();
		regGraph.getData().add(chosenAttribute);

		regGraph.setPrefSize(230,230);
		regGraph.setMinSize(230,230);
		regGraph.setMaxSize(230,230);
		ap.getChildren().add(regGraph);
		return ap;
	}

}
