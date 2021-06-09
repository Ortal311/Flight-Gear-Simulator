package algo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import viewModel.TimeSeries;

import java.util.*;

import static algo.StatLib.*;

public class ZScoreAlgorithm implements AnomalyDetector{

	Vector<Float> tx;
	HashMap<Integer, LinkedList<Float>> ZScoreMap;
	HashMap<String, ArrayList<Float>> avgMap;

	public ZScoreAlgorithm() {
		this.tx = new Vector<>();
		this.ZScoreMap = new HashMap<>();
		avgMap = new HashMap<>();
	}

	public float[] ListToArr(List<Float> lst)
	{
		float[] res= new float[lst.size()];

		for(int i=0; i<res.length; i++) {
			res[i]= lst.get(i);
		}
		return res;
	}

	public float calcZScore(List<Float> col, String attribute)
	{
		float avg, sigma;
		float[] arrFloat;
		int colSize = col.size();
		float x;

		if(colSize == 0) {
			return 0;
		}

		x = col.get(colSize - 1);
		if(colSize == 1) {
			arrFloat = ListToArr(col);
			avgMap.get(attribute).add(StatLib.avg(arrFloat));
			return Math.abs((x - StatLib.avg(arrFloat))) / StatLib.var(arrFloat);
		}

		arrFloat = ListToArr(col.subList(0, col.size() - 1));
		avg = (avgMap.get(attribute).get(colSize - 2) * (colSize) + x) / (colSize + 1);
		avgMap.get(attribute).add(avg);
		sigma = (float)Math.sqrt(StatLib.var(arrFloat));

		return Math.abs((x - avg)) / sigma;
	}

	public float argMax(LinkedList<Float> z)
	{
		float max=0;
		for(int i=0; i < z.size(); i++) {
			if(max<z.get(i))
				max=z.get(i);
		}
		return max;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		int index = 0;
		LinkedList<Float> zScored = new LinkedList<>();
		String attribute;

		for(ArrayList<Float> col: ts.tsNum.values()) {
			attribute = ts.atts.get(index);
			avgMap.put(attribute, new ArrayList<>());

			for(int j = 0; j < col.size(); j++) {
				zScored.add(calcZScore(col.subList(0, j), attribute));
			}

			tx.add(argMax(zScored));
			this.ZScoreMap.put(index++, zScored);
		}
	}

	public List<AnomalyReport> detect(TimeSeries data) {
		List<AnomalyReport> lst = new LinkedList<>();
		String attribute;

		for(int indexCol = 0; indexCol < data.atts.size(); indexCol++) {
			ArrayList<Float> col = data.tsNum.get(indexCol);
			attribute = data.atts.get(indexCol);
			for(int indexTime = 0; indexTime < col.size(); indexTime++) {
				if (calcZScore(col.subList(0, indexTime), attribute) > tx.get(indexCol)) {
					lst.add(new AnomalyReport(attribute, indexTime));
				}
			}
		}
		return lst;
	}

	public AnchorPane paint() {
		AnchorPane ap=new AnchorPane();

		return ap;
	}


}
