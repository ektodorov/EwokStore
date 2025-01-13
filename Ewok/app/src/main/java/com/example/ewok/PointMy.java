package com.example.ewok;


public class PointMy {

    private float mX;
    private float mY;

    public PointMy(float aX, float aY) {
        super();
        mX = aX;
        mY = aY;
    }

    /**
     * Finds the distance between this point and another point (Euclidean distance in 2D space).
     * @param aPoint
     * @return distance between the two points.
     */
    public double getDistance(PointMy aPoint) {
        float x = mX - aPoint.getX();
        float y = mY - aPoint.getY();
        double distance = Math.sqrt((x * x) + (y * y));
        return distance;
    }

    /**
     * Finds the distance between two points (Euclidean distance in 2D space).
     * @param pointOne
     * @param pointTwo
     * @return distance between the two points.
     */
    public static double getDistance(PointMy pointOne, PointMy pointTwo) {
        float x = pointOne.getX() - pointTwo.getX();
        float y = pointOne.getY() - pointTwo.getY();
        double distance = Math.sqrt((x * x) + (y * y));
        return distance;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    @Override
    public String toString() {
        return "PointMy{" +
                "x=" + mX +
                ", y=" + mY +
                '}';
    }
}
