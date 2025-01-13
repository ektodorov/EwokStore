package com.example.ewok;


import java.util.ArrayList;

public class ClusterMy {

    private PointMy mCenter;
    private ArrayList<PointMy> mPoints;

    public ClusterMy() {
        super();
        mPoints = new ArrayList<PointMy>();
    }

    public ClusterMy(ArrayList<PointMy> aPoints) {
        super();
        mPoints = aPoints;
    }

    public void updateCenter() {
        mCenter = findCenter(mPoints);
    }

    public PointMy findCenter(ArrayList<PointMy> aPoints) {
        float x = 0;
        float y = 0;
        int count = aPoints.size();
        for(int i = 0; i < count; i++) {
            PointMy point = aPoints.get(i);
            x = x + point.getX();
            y = y + point.getY();
        }
        x = x / count;
        y = y / count;
        return new PointMy(x, y);
    }

    /**
     * Finds the center point of the cluster
     * @param aCluster - cluster
     * @return - center point of the cluster
     */
    public static PointMy getCenter(ArrayList<PointMy> aCluster) {
        float x = 0;
        float y = 0;
        int count = aCluster.size();
        for(int i = 0; i < count; i++) {
            PointMy point = aCluster.get(i);
            x = x + point.getX();
            y = y + point.getY();
        }
        x = x / count;
        y = y / count;
        return new PointMy(x, y);
    }

    public void addPoint(PointMy aPoint) {
        mPoints.add(aPoint);
    }

    public void removePoint(PointMy aPoint) {
        mPoints.remove(aPoint);
    }

    public int size() {
        return mPoints.size();
    }

    public PointMy getCenter() {
        return mCenter;
    }

    public void setCenter(PointMy aCenter) {
        mCenter = aCenter;
    }

    public ArrayList<PointMy> getPoints() {
        return mPoints;
    }

    public void setPoints(ArrayList<PointMy> aPoints) {
        mPoints = aPoints;
    }
}
