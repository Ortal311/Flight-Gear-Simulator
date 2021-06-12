package viewModel;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimeSeries {

    public Map<String, ArrayList<Float>> ts;
    public Map<Integer, ArrayList<Float>> tsNum;
    public ArrayList<String> rows;
    public ArrayList<String> atts;
    int dataRowSize;

    public TimeSeries(){
        this.ts=new HashMap<>();
        this.tsNum=new HashMap<>();
        this.rows=new ArrayList<>();
        this.atts=new ArrayList<>();
        this.dataRowSize=0;
    }

    public TimeSeries(String csvFileName) {
        System.out.println("init TS");
        ts = new HashMap<>();
        tsNum = new HashMap<>();
        rows = new ArrayList<>();

        atts = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(csvFileName));
            String line = in.readLine();
            int j = 0;
            for (String att : line.split(",")) {
                atts.add(att);
                ts.put(att, new ArrayList<>());
                tsNum.put(j++, new ArrayList<>());
            }
            while ((line = in.readLine()) != null) {
                int i = 0;
                for (String val : line.split(",")) {
                    ts.get(atts.get(i)).add(Float.parseFloat(val));
                    tsNum.get(i).add(Float.parseFloat(val));
                    i++;
                }
                rows.add(line);
            }
            dataRowSize = ts.get(atts.get(0)).size();

            in.close();
        } catch (IOException e) {
        }
    }

    public ArrayList<Float> getAttributeData(String name) {
        return ts.get(name);
    }

    public ArrayList<String> getAttributes() {
        return atts;
    }

    public int getSize() {// they both doing the exact same thing, but cuz we mixed code we need to change the name we call that function
        return dataRowSize;
    }

    public int getRowSize() {
        return dataRowSize;
    }

    public float getValueByTime(int index, int time) {

        float f = tsNum.get(index).get(time);
        return tsNum.get(index).get(time);
    }

    public float getValueByTime(String index, int time) {

        return ts.get(index).get(time);
    }

    public Map<String, ArrayList<Float>> getTs() {
        return ts;
    }

    public ArrayList<String> getAtts() {
        return atts;
    }

    public float getMinFromAttribute(String val) {
        ArrayList<Float> lst = ts.get(val);

        float minVal = ts.get(val).get(0);//get the val of the first timeStep

        for (float f : lst) {
            if ((f < minVal))
                minVal = f;
        }
        return minVal;
    }

    public float getMaxFromAttribute(String val) {
        ArrayList<Float> lst = ts.get(val);
        float maxVal = 0;
        for (float f : lst) {
            if ((f > maxVal))
                maxVal = f;
        }
        return maxVal;
    }
    public ListProperty<Float> getDataOfAttUntilIndex(String s, int index){
        ListProperty<Float>dataUntilIndex= new SimpleListProperty<>();
        List<Float>lst= ts.get(s).subList(0,index);
        dataUntilIndex.addAll(lst);
        return  dataUntilIndex;
    }


    //    public List<String> getAttributes() {
//        return new LinkedList<>(this.map.keySet());
//    }


}
