package viewModel;

import algo.Point;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class TimeSeries {
    public Map<String, Vector<Float>> map = new HashMap<String, Vector<Float>>();
    public Map<String,Integer>mapIndex=new HashMap<>();
    public Vector<Vector<String>> cols = new Vector<Vector<String>>(); //include fields in the first line
    public Vector<Vector<Float>> colsFloat = new Vector<Vector<Float>>(); //include only the float data
    public Vector<String> rows = new Vector<String>();

    public TimeSeries(String csvFileName) //throws IOException
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFileName)); //open to read the result file
            String line;
            Vector<Vector<String>> csvRows = new Vector<Vector<String>>();
            String[] str;
            while ((line = reader.readLine()) != null) //run on rows of the file and read each line separately
            {
                //String word; //name of each feature
                Vector<String> row = new Vector<String>(); //initialize the row vector
                str = line.split(","); //array of the string numbers in each row
                rows.add(line);

                for (int i = 0; i < str.length; i++) //runs on 4 data columns of the table
                {
                    row.add(str[i]); //add each string number to the vector
                }
                csvRows.add(row); //add the updated row to the vector of vectors
            }


            for (int i = 0; i < csvRows.elementAt(0).size(); i++) //runs on each feature (column)
            {
                Vector<String> col = new Vector<String>(); //initialize the column vector
                for (int j = 0; j < csvRows.size(); j++)
                    col.add(csvRows.elementAt(j).elementAt(i));
                cols.add(col);
            }

            for (int i = 0; i < cols.size(); i++) //runs on each feature and convert the data to float from string numbers
            {
                Vector<Float> colF = new Vector<Float>();
                for (int j = 1; j < cols.firstElement().size(); j++) //length of column
                    colF.add(Float.parseFloat(cols.elementAt(i).elementAt(j))); //convert each string to float
                colsFloat.add(colF);
                map.put(cols.elementAt(i).elementAt(0), colF); //add the converted col to the map
                mapIndex.put(cols.elementAt(i).elementAt(0),i-1);
            }
            reader.close(); //close the reading file
        } catch (IOException e) //in case of exception
        {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return this.rows.size();
    }

    public List<String> getAttributes() {
        return new LinkedList<>(this.map.keySet());
    }

    public float getValueByTime(int index, int time) {
        return this.colsFloat.get(index).get(time);
    }
    public int getIndexOfAttribute(String attribute){
        int s=this.mapIndex.get(attribute);// for checking

        return this.mapIndex.get(attribute);
    }
//    public ListProperty<Point> getListOfPoints(String attributeName,int rowNumber){
//        ListProperty<Point> listProperty=new SimpleListProperty<>();
//        for(int i=0;i<getSize();i++){
//
//        }
//
//    }


}
