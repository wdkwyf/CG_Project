package edu.cg.wuyufei.box;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


/**
 * Created by dingpeien on 15/11/16.
 */
public class OpenGLView extends GLSurfaceView {

    private static final int INVALID_POINTER_ID = 0;
    private OpenGLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private int mActivePointerId = INVALID_POINTER_ID;

    public boolean drawWhatFlag = true;

    public int slideTime = 0;

    public int transWhichFlag;

    {
        transWhichFlag = 0;
    }

    static final int flagDRAG = 1;

    static final int flagSCALE = 2;

    float oldDis = 1f;
    int mode = 0;


    public OpenGLView(Context context) {
        super(context);
        mRenderer = new OpenGLRenderer(this);
        setRenderer(mRenderer);

        //this.onTouchEvent(null);
        setAutoRender(false);
        this.requestRender();

        // TODO Auto-generated constructor stub
    }


    public boolean onTouchEvent(MotionEvent event) {

        // int pCount=event.getPointerCount();


        //System.out.println(event.getX());
        //System.out.println(event.getY());
        // float x=event.getX();
        //float y=event.getY();
        //y = -y;


        int action = event.getAction();


        float x = event.getX(0);
        float y = event.getY(0);
        y = -y;
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                slideTime = slideTime + 1;
                //mRenderer.onTouchMoveTrans(dx, dy);
                mRenderer.onTouchMoveRotate(dx, dy);

//                this.mPreviousX = x;
//                this.mPreviousY = y;
//                break;
            case MotionEvent.ACTION_DOWN:
//					Log.i("tg","touch down/" + x + "/" + y);
                this.mPreviousX = x;
                this.mPreviousY = y;
                break;

            case MotionEvent.ACTION_UP:
//					Log.i("tg","touch up/" + x + "/" + y);
                this.mPreviousX = 0;
                this.mPreviousY = 0;
                setAutoRender(true);
                this.mRenderer.startRotate();
                break;

        }


        this.requestRender();
        return true;
    }

    // 计算中点位置
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void setAutoRender(boolean auto) {
        if (auto) {
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        } else {
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }

    public void animBox() {
        mRenderer.change = true;

    }


}
