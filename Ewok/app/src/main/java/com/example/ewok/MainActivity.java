package com.example.ewok;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";

    private EditText mEditTextX;
    private EditText mEditTextY;
    private Button mButtonAdd;
    private EditText mEditTextCoverageWidth;
    private EditText mEditTextCoverageHeight;
    private Button mButtonClear;
    private Button mButtonShowArea;
    private EwokView mEwokView;

    private ArrayList<PointMy> mArrayPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextX = (EditText)findViewById(R.id.editTextXMain);
        mEditTextY = (EditText)findViewById(R.id.editTextYMain);
        mButtonAdd = (Button)findViewById(R.id.buttonAddPointMain);
        mEditTextCoverageWidth = (EditText)findViewById(R.id.editTextCoverageWidth);
        mEditTextCoverageHeight = (EditText)findViewById(R.id.editTextCoverageHeight);
        mButtonClear = (Button)findViewById(R.id.buttonClearMain);
        mButtonShowArea = (Button)findViewById(R.id.buttonShowAreaMain);
        mEwokView = (EwokView)findViewById(R.id.ewokViewMain);

        mEwokView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mEwokView.getLayoutParams();
                int width = mEwokView.getWidth();
                params.height = width;
                mEwokView.setLayoutParams(params);
                mEwokView.setSideLength(width);
            }
        });

        mArrayPoints = new ArrayList<PointMy>();

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strX = mEditTextX.getText().toString();
                String strY = mEditTextY.getText().toString();
                try {
                    int x = Integer.parseInt(strX);
                    int y = Integer.parseInt(strY);
                    if(x < 0 || x > ConstantsE.SCALE_AREA) {
                        Toast.makeText(MainActivity.this, R.string.toast_supply_value_x, Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(y < 0 || y > ConstantsE.SCALE_AREA) {
                        Toast.makeText(MainActivity.this, R.string.toast_supply_value_y, Toast.LENGTH_LONG).show();
                        return;
                    }

                    PointMy point = new PointMy(x, y);
                    mArrayPoints.add(point);
                    mEwokView.setPoints(mArrayPoints);
                    mEwokView.invalidate();
                } catch(NumberFormatException ex) {
                    Toast.makeText(MainActivity.this, R.string.toast_supply_value_xy, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArrayPoints.clear();
                mEwokView.setPoints(mArrayPoints);
                mEwokView.setArea(null);
                mEwokView.setAreaStore(null);
                mEwokView.invalidate();
            }
        });

        mButtonShowArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<PointMy> points = new ArrayList<PointMy>();
//                PointMy point1 = new PointMy(1.0f, 1.0f);
//                PointMy point2 = new PointMy(1.5f, 2.0f);
//                PointMy point3 = new PointMy(3.0f, 4.0f);
//                PointMy point4 = new PointMy(5.0f, 7.0f);
//                PointMy point5 = new PointMy(3.5f, 5.0f);
//                PointMy point6 = new PointMy(4.5f, 5.0f);
//                PointMy point7 = new PointMy(3.5f, 4.5f);
//                points.add(point1);
//                points.add(point2);
//                points.add(point3);
//                points.add(point4);
//                points.add(point5);
//                points.add(point6);
//                points.add(point7);

                ClusterMy cluster1 = new ClusterMy();
                ClusterMy cluster2 = new ClusterMy();
                ClusterMy cluster3 = new ClusterMy();
                ArrayList<ClusterMy> clusters = new ArrayList<ClusterMy>();
                clusters.add(cluster1);
                clusters.add(cluster2);
                clusters.add(cluster3);

                createClusters(clusters, mArrayPoints);
            }
        });
    }

    /* Utils */
    private void createClusters(ArrayList<ClusterMy> aClusters, ArrayList<PointMy> aPoints) {
        Collections.sort(aPoints, new Comparator<PointMy>() {
            @Override
            public int compare(PointMy lhs, PointMy rhs) {
                double distanceLhs = Math.sqrt((lhs.getX() * lhs.getX()) + (lhs.getY() * lhs.getY()));
                double distanceRhs = Math.sqrt((rhs.getX() * rhs.getX()) + (rhs.getY() * rhs.getY()));
                if(distanceLhs < distanceRhs) {
                    return -1;
                } else if(distanceLhs > distanceRhs) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        while(aPoints.size() < 3) {
            aPoints.add(new PointMy((float)(Math.random() * ConstantsE.SCALE_AREA), (float)(Math.random() * ConstantsE.SCALE_AREA)));
        }

        ClusterMy cluster1 = aClusters.get(0);
        ClusterMy cluster2 = aClusters.get(1);
        ClusterMy cluster3 = aClusters.get(2);

        ArrayList<PointMy> points = new ArrayList<PointMy>(aPoints);
        cluster1.addPoint(points.remove(0));
        cluster2.addPoint(points.remove(points.size() / 2));
        cluster3.addPoint(points.remove(points.size() - 1));

        cluster1.setCenter(cluster1.getPoints().get(0));
        cluster2.setCenter(cluster2.getPoints().get(0));
        cluster3.setCenter(cluster3.getPoints().get(0));

        //add to clusters
        int count = points.size();
        for(int x = 0; x < count; x++) {
            addPoint(aClusters, points.get(x));
        }

        //move points between clusters if they are closer to another cluster's center
        count = aClusters.size();
        for(int x = 0; x < count; x++) {
            ClusterMy cluster = aClusters.get(x);
            for(int i = 0; i < cluster.getPoints().size(); i++) {
                updatePoint(aClusters, cluster, cluster.getPoints().get(i));
            }
        }

        //print clusters
        count = aClusters.size();
        for(int x = 0; x < count; x++) {
            ClusterMy cluster = aClusters.get(x);
            Log.i(LOG, "cluster=" + x);
            int countPoints = cluster.getPoints().size();
            for(int i = 0; i < countPoints; i++) {
                Log.i(LOG, "i=" + i + ", point=" + cluster.getPoints().get(i).toString());
            }
        }

        ClusterMy clusterBiggest = null;
        int sizeBiggest = 0;
        count = aClusters.size();
        for(int x = 0; x < count; x++) {
            ClusterMy cluster = aClusters.get(x);
            if(cluster.size() > sizeBiggest) {
                sizeBiggest = cluster.size();
                clusterBiggest = cluster;
            }
        }

        PointMy pointMin = getPointMin(clusterBiggest.getPoints());
        PointMy pointMax = getPointMax(clusterBiggest.getPoints());
        RectF rectArea = new RectF(pointMin.getX(), pointMin.getY(), pointMax.getX(), pointMax.getY());
        Log.i(LOG, "pointMin=" + pointMin);
        Log.i(LOG, "pointMax=" + pointMax);
        Log.i(LOG, "rectArea=" + rectArea);

        int width = 0;
        int height = 0;
        String strWidth = mEditTextCoverageWidth.getText().toString();
        String strHeight = mEditTextCoverageHeight.getText().toString();
        try {
            width = Integer.parseInt(strWidth);
            height = Integer.parseInt(strHeight);
            if(width < 0 || width > ConstantsE.SCALE_AREA) {
                Toast.makeText(MainActivity.this, R.string.toast_supply_value_width, Toast.LENGTH_LONG).show();
                return;
            }

            if(height < 0 || height > ConstantsE.SCALE_AREA) {
                Toast.makeText(MainActivity.this, R.string.toast_supply_value_height, Toast.LENGTH_LONG).show();
                return;
            }
        } catch(NumberFormatException ex) {
            Toast.makeText(MainActivity.this, R.string.toast_supply_value_width_height, Toast.LENGTH_LONG).show();
            return;
        }

        float left = rectArea.left;
        float top = rectArea.top;
        float right = rectArea.left + width;
        float bottom = rectArea.top + height;
        if(right > 100) {
            int substract = (int)(right - 100);
            left = left - substract;
            right = right - substract;
        }
        if(bottom > 100) {
            int substract = (int)(bottom - 100);
            top = top - substract;
            bottom = bottom - substract;
        }
        RectF rectStore = new RectF(left, top, right, bottom);
        Log.i(LOG, "rectStore=" + rectStore);

        mEwokView.setArea(rectArea);
        mEwokView.setAreaStore(rectStore);
        mEwokView.invalidate();
    }

    private void addPoint(ArrayList<ClusterMy> aClusters, PointMy aPoint) {
        double distanceClosest = Double.MAX_VALUE;
        ClusterMy clusterClosest = null;
        int count = aClusters.size();
        for(int x = 0; x < count; x++) {
            ClusterMy cluster = aClusters.get(x);
            double distance = aPoint.getDistance(cluster.getCenter());
            if(distance < distanceClosest) {
                distanceClosest = distance;
                clusterClosest = cluster;
            }
        }
        clusterClosest.addPoint(aPoint);
        clusterClosest.updateCenter();
    }

    private void updatePoint(ArrayList<ClusterMy> aClusters, ClusterMy aClusterOrigin, PointMy aPoint) {
        double distanceClosest = Double.MAX_VALUE;
        ClusterMy clusterClosest = null;
        int count = aClusters.size();
        for(int x = 0; x < count; x++) {
            ClusterMy cluster = aClusters.get(x);
            double distance = aPoint.getDistance(cluster.getCenter());
            if(distance < distanceClosest) {
                distanceClosest = distance;
                clusterClosest = cluster;
            }
        }
        aClusterOrigin.removePoint(aPoint);
        clusterClosest.addPoint(aPoint);
        clusterClosest.updateCenter();
    }

    private PointMy getPointMin(ArrayList<PointMy> aPoints) {
        PointMy retVal = null;
        float x = Float.MAX_VALUE;
        float y = Float.MAX_VALUE;
        int count = aPoints.size();
        for(int i = 0; i < count; i++) {
            PointMy point = aPoints.get(i);
            if(point.getX() < x) {
                x = point.getX();
            }
            if(point.getY() < y) {
                y = point.getY();
            }
        }
        retVal = new PointMy(x, y);
        return retVal;
    }

    private PointMy getPointMax(ArrayList<PointMy> aPoints) {
        PointMy retVal = null;
        float x = 0.0f;
        float y = 0.0f;
        int count = aPoints.size();
        for(int i = 0; i < count; i++) {
            PointMy point = aPoints.get(i);
            if(point.getX() > x) {
                x = point.getX();
            }
            if(point.getY() > y) {
                y = point.getY();
            }
        }
        retVal = new PointMy(x, y);
        return retVal;
    }
}
