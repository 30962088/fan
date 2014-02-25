package com.yoka.fan.wiget;

import java.io.ByteArrayOutputStream;

import com.yoka.fan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;


public class DragRectView extends ImageView {

    public int startX, startY, endX, endY;
    public float startDragX, dragX, startDragY, dragY;

    Paint paint = new Paint();

    public static int DRAG;
    public static int DRAW ;
    public static int EXPAND ;
    public static int EXPAND_TYPE;
    public static int BORDER;
    public static int CORNER;
    public static int CURRENT_MODE = DRAG;
    public static int MINIMUM_DISTANCE;
    public static int EXPAND_BORDER;
    public static int EXPAND_CORNER;
    public static int CIRCLE_RADIUS;
    public static boolean SELECTION_COMPLETE;


    public DragRectView(Context context, int startX) {
        super(context);
        this.startX = startX;
        init();
    }

    public DragRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DragRectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public int getStartX() {
        return startX;
    }


    public int getEndX() {
        return endX;
    }


    public int getStartY() {
        return startY;
    }


    public int getEndY() {
        return endY;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        try{
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    float [] coordinates = new float[]{event.getX(), event.getY()};
                    Matrix matrix = new Matrix();
                    this.getImageMatrix().invert(matrix);
                    matrix.postTranslate(this.getScrollX(), this.getScrollY());
                    matrix.mapPoints(coordinates);

                    SELECTION_COMPLETE = false;
                    if(onTouchCorner(event.getX(), event.getY())) {
                        CURRENT_MODE = EXPAND;
                        EXPAND_TYPE = CORNER;
                        break;
                    }

                    if(touchedInside(event.getX(), event.getY())) {
                        CURRENT_MODE = DRAG;
                        startDragX = (int) event.getX();
                        startDragY = (int) event.getY();
                        break;
                    }
                    CURRENT_MODE = DRAW;
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    endX = (int) event.getX();
                    endY = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(CURRENT_MODE != EXPAND && ((distance(event.getX(), event.getY(), startX, startY) < MINIMUM_DISTANCE) ||
                            (Math.abs(startX - event.getX()) < MINIMUM_DISTANCE) ||
                            (Math.abs(startY- event.getY()) < MINIMUM_DISTANCE)
                    )) {
                        SELECTION_COMPLETE = false;
                        invalidate();
                        break;
                    }
                    SELECTION_COMPLETE = true;
                    if(CURRENT_MODE == DRAG) {
                        dragX = (int) event.getX()-startDragX;
                        dragY = (int) event.getY()-startDragY;
                        startDragX = (int) event.getX();
                        startDragY = (int) event.getY();
                        endX += dragX;
                        startX += dragX;
                        startY += dragY;
                        endY += dragY;
                    }
                    else if(CURRENT_MODE == DRAW) {
                        endX = (int) event.getX();
                        endY = (int) event.getY();
                    }
                    else if(CURRENT_MODE == EXPAND) {
                        if(EXPAND_TYPE == CORNER) {
                            if(EXPAND_CORNER == 0) {
                                if(startX < endX) startX = (int)event.getX();
                                else endX = (int)event.getX();
                                if(startY < endY) startY = (int)event.getY();
                                else endY = (int)event.getY();
                            }
                            else if(EXPAND_CORNER == 1) {
                                if(startX > endX) startX = (int)event.getX();
                                else endX = (int) event.getX();
                                if(startY < endY) startY = (int) event.getY();
                                else endY = (int) event.getY();
                            }
                            else if(EXPAND_CORNER == 2) {
                                if(startX > endX) startX = (int) event.getX();
                                else endX = (int) event.getX();
                                if(startY > endY) startY = (int) event.getY();
                                else endY = (int)event.getY();
                            }
                            else if(EXPAND_CORNER == 3) {
                                if(startX < endX) startX = (int)event.getX();
                                else endX =(int) event.getX();
                                if(startY > endY) startY = (int)event.getY();
                                else endY = (int)event.getY();
                            }
                        }
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    SELECTION_COMPLETE = true;

                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    private boolean touchedInside(float x, float y) {
        return x < Math.max(startX, endX) && x > Math.min(startX, endX) && y < Math.max(startY, endY) && y > Math.min(startY, endY);
    }


    private boolean onTouchCorner(float x, float y) {
        if(detectCorner(x, y, Math.min(startX, endX), Math.min(startY, endY))) { // Top left corner
            EXPAND_CORNER = 0;
            return true;
        }
        if(detectCorner(x, y, Math.max(startX, endX), Math.min(startY, endY))) { //Top right corner
            EXPAND_CORNER = 1;
            return true;
        }
        if(detectCorner(x, y, Math.max(startX, endX), Math.max(startY, endY))) {//Bottom right corner
            EXPAND_CORNER = 2;
            return true;
        }
        if(detectCorner(x, y, Math.min(startX, endX), Math.max(startY, endY))) { //Bottom left corner
            EXPAND_CORNER = 3;
            return true;
        }
        return false;
    }

    private boolean detectCorner(float x1, float y1, float x2, float y2) {
        return distance(x1, y1, x2, y2) <= MINIMUM_DISTANCE;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
    }



    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        if(SELECTION_COMPLETE) {
        	if(maskBitmap == null){
        		maskBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        		maskCanvas = new Canvas(maskBitmap);
        	}
        	maskCanvas.drawRect(0,0,getWidth(),getHeight() , paint);
        	drawSquare();
        	canvas.drawBitmap(maskBitmap, 0f,0f, paint);
        }
    }
    
    private void drawRect(){
    	maskCanvas.drawRect(Math.min(startX, endX),Math.min(startY, endY) , Math.max(startX, endX), Math.max(startY, endY),clearPaint);
    }
    
    private void drawCircle(){
    	maskCanvas.drawOval(new RectF( Math.min(startX, endX),Math.min(startY, endY) , Math.max(startX, endX), Math.max(startY, endY)),clearPaint);
    	
    }
    
    private void drawSquare(){
    	
//    	if(CURRENT_MODE == DRAW){
    		
    		int delta = Math.max(Math.abs(endX-startX),Math.abs(endY-startY)) ;
        	if(startX > endX){
        		Log.d("zzm", "endX："+endX+",startX："+startX+",delta:"+delta);
        		endX = startX-delta;
        	}else{
        		endX = startX+delta;
        	}
        	if(startY >  endY){
        		endY = startY-delta;
        	}else{
        		endY = startY+delta;
        	}
//    	}
        	drawCircle();
    	
    	
    }
    
    
    private Paint clearPaint = new Paint();
    
    private Bitmap maskBitmap;
    
    private Canvas maskCanvas;
    
    
    private void init(){
        DRAW = 1;
        DRAG = 0;
        EXPAND = 2;
        EXPAND_TYPE = 0;
        BORDER = 0;
        CORNER = 1;
        CURRENT_MODE = DRAG;
        MINIMUM_DISTANCE = 20;
        EXPAND_BORDER = 0;
        EXPAND_CORNER = 0;
        CIRCLE_RADIUS = 5;
        SELECTION_COMPLETE = false;
        paint.setColor(getContext().getResources().getColor(R.color.holo_green_light));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); 
        clearPaint.setColor(getContext().getResources().getColor(R.color.transparent));
        clearPaint.setFilterBitmap(true);
    }
    
    
    @Override
    protected void drawableStateChanged() {
    	// TODO Auto-generated method stub
    	super.drawableStateChanged();
    	buildDrawingCache();
    }
    
    public byte[] getSelection(){
    	
        Bitmap drawable = getDrawingCache();
    	Bitmap bitmap = Bitmap.createScaledBitmap(drawable, getWidth(), getHeight(), false);
    	int x1 = Math.min(startX, endX);
    	int y1 = Math.min(startY, endY);
    	int x2 = Math.max(startX, endX);
    	int y2 = Math.max(startY, endY);
    	Bitmap photoBitmap = Bitmap.createBitmap(bitmap,x1 ,y1 ,x2-x1 ,y2-y1 );
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    	byte[] pixs = stream.toByteArray();
    	photoBitmap.recycle();
    	return pixs;
    }
    
  
    
    
}
// Source GIT HUB Libraries