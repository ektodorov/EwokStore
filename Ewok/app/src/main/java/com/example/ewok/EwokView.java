package com.example.ewok;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class EwokView extends View {

    private ArrayList<PointMy> mArrayPoints;
    private RectF mRectArea;
    private RectF mRectAreaStore;
    private Paint mPaintEwok;
    private Paint mPaintArea;
    private Paint mPaintAreaStore;
    private int mSideLength;
    private float mRatioLength;

    public EwokView(Context context) {
        super(context);
        init();
    }

    public EwokView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EwokView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isInEditMode()) {return;}

        canvas.drawColor(Color.GREEN);

        if(mRectArea != null) {
            canvas.drawRect((mRectArea.left * mRatioLength), (mRectArea.top * mRatioLength), (mRectArea.right * mRatioLength),
                    (mRectArea.bottom * mRatioLength), mPaintArea);
        }

        if(mRectAreaStore != null) {
            canvas.drawRect((mRectAreaStore.left * mRatioLength), (mRectAreaStore.top * mRatioLength),
                    (mRectAreaStore.right * mRatioLength), (mRectAreaStore.bottom * mRatioLength), mPaintAreaStore);
        }

        int count = mArrayPoints.size();
        for(int x = 0; x < count; x++) {
            PointMy point = mArrayPoints.get(x);
            canvas.drawCircle((point.getX() * mRatioLength), (point.getY() * mRatioLength), 3.0f, mPaintEwok);
        }
    }

    private void init() {
        mArrayPoints = new ArrayList<PointMy>();
        mPaintEwok = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintEwok.setColor(Color.RED);
        mPaintArea = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArea.setColor(Color.BLUE);
        mPaintArea.setAlpha(125);
        mPaintAreaStore = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAreaStore.setColor(Color.WHITE);
        mPaintAreaStore.setAlpha(170);
    }

    public void setPoints(ArrayList<PointMy> aArrayPoints) {
        mArrayPoints = aArrayPoints;
    }

    public void setArea(RectF aRectArea) {
        mRectArea = aRectArea;
    }

    public void setAreaStore(RectF aRectAreaStore) {
        mRectAreaStore = aRectAreaStore;
    }

    public void setSideLength(int aSideLength) {
        mSideLength = aSideLength;
        mRatioLength = mSideLength / ConstantsE.SCALE_AREA;
    }

    public int getSideLength() {
        return mSideLength;
    }
}
