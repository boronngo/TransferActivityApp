package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;

/**
 * Created by yuuki on 14/11/16.
 */
public class FeatureFragment extends Fragment {

    private LineGraph accelerationGraph;
    private Line xLine = new Line();
    private Line yLine = new Line();
    private Line zLine = new Line();
    private ArrayList<LinePoint> xLinePoints = new ArrayList<LinePoint>();
    private ArrayList<LinePoint> yLinePoints = new ArrayList<LinePoint>();
    private ArrayList<LinePoint> zLinePoints = new ArrayList<LinePoint>();

    private BarGraph featureGraph;
    private ArrayList<Bar> bars = new ArrayList<Bar>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feature, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        accelerationGraph = (LineGraph) getActivity().findViewById(R.id.acceleration_graph);
        featureGraph = (BarGraph) getActivity().findViewById(R.id.feature_graph);

        // accelerationGraphの初期設定
        xLinePoints.add(new LinePoint(0, 0));
        yLinePoints.add(new LinePoint(0, 0));
        zLinePoints.add(new LinePoint(0, 0));
        xLine.setPoints(xLinePoints);
        yLine.setPoints(yLinePoints);
        zLine.setPoints(zLinePoints);

        xLine.setColor(Color.parseColor("#99CC00"));
        yLine.setColor(Color.parseColor("#FFBB33"));
        zLine.setColor(Color.parseColor("#AA66CC"));

        xLine.setShowingPoints(false);
        yLine.setShowingPoints(false);
        zLine.setShowingPoints(false);

        accelerationGraph.addLine(xLine);
        accelerationGraph.addLine(yLine);
        accelerationGraph.addLine(zLine);

        accelerationGraph.setRangeY(-10, 10);
        accelerationGraph.setLineToFill(-1);

        //featureGraphの初期設定
        if(bars.size() != 14) {
            for (int i = 0; i < 14; i++) {
                bars.add(new Bar());
            }
        }
        featureGraph.setBars(bars);
        featureGraph.setShowBarText(false);
    }

    public void updateAcceleration(double[] acceleration) {

        if (xLinePoints.size() > 20) {
            xLinePoints.remove(0);
            yLinePoints.remove(0);
            zLinePoints.remove(0);

            for (int i = 0; i < xLinePoints.size(); i++) {
                LinePoint lp = xLinePoints.get(i);
                lp.setX(lp.getX() - 1);
                xLinePoints.set(i, lp);
                lp = yLinePoints.get(i);
                lp.setX(lp.getX() - 1);
                yLinePoints.set(i, lp);
                lp = zLinePoints.get(i);
                lp.setX(lp.getX() - 1);
                zLinePoints.set(i, lp);
            }

        }

        xLinePoints.add(new LinePoint(xLinePoints.size(), acceleration[0]));
        yLinePoints.add(new LinePoint(yLinePoints.size(), acceleration[1]));
        zLinePoints.add(new LinePoint(zLinePoints.size(), acceleration[2]));

        accelerationGraph.invalidate();
    }

    public void updateFeatureVector(double[] featureVector) {

        System.out.println(bars.toString());
        System.out.println(Arrays.toString(featureVector));

        for (int i = 0; i < bars.size(); i++) {
            bars.get(i).setValue((float)featureVector[i]);
        }
        featureGraph.invalidate();
    }

}
