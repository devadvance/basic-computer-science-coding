package com.cs1635.mgj7.handwrite;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawingPanel extends View implements OnTouchListener {
	List<Point> pointList = new ArrayList<Point>();
    Paint mPaint = new Paint();
    List<SerialPath> pathList = new ArrayList<SerialPath>();
	
	public DrawingPanel(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
        mPaint.setStrokeWidth(5);
    }
	
	// This constructor is used by LayoutInflater
    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
        mPaint.setStrokeWidth(5);
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for (SerialPath tempSerialPath : pathList) {
			canvas.drawPath(tempSerialPath, mPaint);
		}
	}
	// To allow setting of the color from outside of the class
	public void setPaintColor(int color) {
		mPaint.setColor(color);
	}
	
	public void clearPanel() {
		pathList.clear();
		pointList.clear();
		invalidate();
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		SerialPath tempSerialPath;
		Point pointTemp = new Point();
		final int action = event.getActionMasked();
		 if (action == MotionEvent.ACTION_DOWN) {
			 tempSerialPath = new SerialPath();
			 tempSerialPath.setLastPoint(event.getX(), event.getY());
			 pathList.add(tempSerialPath);
			
			 pointTemp.x = (int) (event.getX() / this.getWidth() * 254);
			 pointTemp.y = (int) (event.getY() / this.getHeight() * 254);
			 pointList.add(pointTemp);
			 
		 } else if (action == MotionEvent.ACTION_MOVE) {
			 tempSerialPath = pathList.get(pathList.size() - 1);
			 tempSerialPath.lineTo(event.getX(), event.getY());
			
			 pointTemp.x = (int) (event.getX() / this.getWidth() * 254);
			 pointTemp.y = (int) (event.getY() / this.getHeight() * 254);
			pointList.add(pointTemp);
			
			invalidate();
			
		 } else if (action == MotionEvent.ACTION_UP) {
			 pointTemp.x = 255;
			 pointTemp.y = 0;
			pointList.add(pointTemp);
		 }
		
		return true;
	}
    
	public boolean isEmpty() {
		return pointList.isEmpty();
	}
	
	public List<Point> getPointList() {
		
		return pointList;
	}
	
	public List<Point> getTerminatedPointList() {
		List<Point> tempList = new ArrayList<Point>(pointList);
		Point tempPoint = new Point();
		tempPoint.x = 255;
		tempPoint.y = 255;
		tempList.add(tempPoint);
		
		return tempList;
	}
	
	public List<SerialPath> getSerialPathList() {
		return new ArrayList<SerialPath>(pathList);
	}
	
	public void setSerialPathList(List<SerialPath> pList) {
		pathList = new ArrayList<SerialPath>(pList);
	}
	
	public void setPointList(List<Point> pList) {
		pointList = new ArrayList<Point>(pList);
	}
	
	public void load(List<Point> pList) {
		pointList = new ArrayList<Point>(pList);
		pathList = new ArrayList<SerialPath>();
		boolean first = true;
		SerialPath tempSerialPath = null;
		float tempX;
		float tempY;
		
		for (int i = 0; i < pointList.size(); i++) {
			if (first) {
				tempSerialPath = new SerialPath();
				tempX = (float)(pointList.get(i).x) / 254 * this.getWidth();
				tempY = (float)(pointList.get(i).y) / 254 * this.getHeight();
				tempSerialPath.setLastPoint(tempX, tempY);
				
				first = false;
			}
			else {
				if (pointList.get(i).x == 255) {
					pathList.add(tempSerialPath);
					first = true;
				}
				else {
					tempX = (float)(pointList.get(i).x) / 254 * this.getWidth();
					tempY = (float)(pointList.get(i).y) / 254 * this.getHeight();
					tempSerialPath.lineTo(tempX, tempY);
				}
			}
		}
		
		invalidate();
	}
}
