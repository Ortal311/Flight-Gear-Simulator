package viewModel;

//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.io.*;
import java.util.*;
//import java.util.Iterator;

public class TimeSeries {
    public Map<String, Vector<Float>> map = new HashMap<String, Vector<Float>>();
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
                //colsFloat.add(colF);
                map.put(cols.elementAt(i).elementAt(0), colF); //add the converted col to the map
            }
            reader.close(); //close the reading file
        } catch (IOException e) //in case of exception
        {
            e.printStackTrace();
        }
    }

    public List<String> getAttributes() {
        return new LinkedList<>(this.map.keySet());
    }

}
