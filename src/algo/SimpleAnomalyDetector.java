package algo;

import viewModel.TimeSeries;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
//import java.lang.Math.abs;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector
{
	public List<CorrelatedFeatures> ls = new ArrayList<CorrelatedFeatures>();
	//public float threshold;
	public float minCor=(float) 0.9;
	public HashMap<String,String>featureANDCorrelateFeature=new HashMap<>();

	@Override
	public void learnNormal(TimeSeries ts)
	{
		//float minCor=(float) 0.9;
		float max = 0;
		float resP;
		int count = 1;
		String feature1,feature2;

		Map.Entry<String, Vector<Float>> pair1 = null;
		Map.Entry<String, Vector<Float>> pair2 = null;

		Iterator<Map.Entry<String, Vector<Float>>> it1 = ts.map.entrySet().iterator();

		while(it1.hasNext()) //runs on all columns of the map
		{
			max=0;
			Iterator<Map.Entry<String, Vector<Float>>> it2 = it1;

			pair1 = it1.next();
			feature1 = pair1.getKey();
			feature2 = null;

			while(it2.hasNext())
			{
				pair2 = it2.next(); //checks only with the forward columns (not from the beginning)
				if(pair2.getKey().equals(feature1)) //if checks the same columns
					break;

				int size = pair1.getValue().size(); //length of each column of data
				float[] arrA = new float[size];
				float[] arrB = new float[size];

				for(int i=0; i<size; i++)
				{
					arrA[i]=pair1.getValue().elementAt(i);
					arrB[i]=pair2.getValue().elementAt(i);
				}

				resP = Math.abs(StatLib.pearson(arrA,arrB)); //calculate pearson between 2 columns

				if(resP > minCor && resP > max)
				{
					feature2 = pair2.getKey();
					max = resP;
				}
			}

			if(max > 0) //max has been updated for the highest resP
			{
				int sizeCol = pair1.getValue().size(); //size of each column of data
				float[] arr1 = new float[sizeCol];
				float[] arr2 = new float[sizeCol];

				Vector<Float> tmp = ts.map.get(feature2); //change 2nd column to the max column compared to pair1

				Point[] points = new Point[sizeCol]; //create array of points
				for(int i=0; i<sizeCol; i++) //insert to array of points
				{
					arr1[i] = pair1.getValue().elementAt(i);
					arr2[i] = tmp.elementAt(i);
					points[i] = new Point(arr1[i], arr2[i]);
				}
				Line line = StatLib.linear_reg(points); //calculate the linear reg between 2 columns

				float threshold = 0; //initialize the threshold
				//Point p = null;
				float devCalc = 0;

				for(int i=0; i<sizeCol; i++)
				{
					//p=new Point(arr1[i], arr2[i]);
					devCalc = StatLib.dev(points[i], line);
					if(devCalc > threshold) //check if the dev is higher than threshold
						threshold = devCalc; //update to new threshold
				}
				ls.add(new CorrelatedFeatures(feature1, feature2, max, line, threshold));	//add to the list the new element of CorrelatedFeatures
				featureANDCorrelateFeature.put(feature1,feature2);
				System.out.println("featureANDCorrelateFeature was inside");
			}
			it1 = ts.map.entrySet().iterator();
			for(int i=0; i<count;i++)
			{
				pair1 = it1.next();
			}
			count++;
		}
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts)
	{
		Vector<AnomalyReport> result = new Vector<AnomalyReport>();
		int size = ls.size();
		for(int i = 0; i < size; i++)
		{
			String feat1 = ls.get(i).feature1;
			String feat2 = ls.get(i).feature2;
			Line line = ls.get(i).lin_reg;
			float threshold = (float) (ls.get(i).threshold + ((ls.get(i).threshold)*0.1));
			int sizeMapData = ts.map.get(feat1).size(); //size of data lines in each column

			for(int j = 0; j < sizeMapData; j++) //runs on each line of data in both columns
			{
				float x = ts.map.get(feat1).get(j);
				float y = ts.map.get(feat2).get(j);
				Point p = new Point(x,y);
				float devRes = StatLib.dev(p, line);
				if(devRes > threshold)
				{
					AnomalyReport report = new AnomalyReport(feat1 + "-" + feat2, j+1);
					result.add(report);
				}
			}
		}
		return result;
	}
	public String getCorrelateFeature(String feature){
		String f=featureANDCorrelateFeature.get(feature);
		featureANDCorrelateFeature.forEach((key, value) -> System.out.println(key + "                :                " + value));
		featureANDCorrelateFeature.size();

		return f;
	}

	public List<CorrelatedFeatures> getNormalModel()
	{
		return ls;
	}
}
//yuval