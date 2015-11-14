package edu.cg.wuyufei.card;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wuyufei on 15/10/1.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    private int mode = 0;
    private float oldDist = 0;
    private float s = 1;
    private float tX = 0, tY = 0, tZ = 0;
    private float oldX = 0, oldY = 0;
    private float newCenterX = 0, newCenterY = 0;
    public static int flag = 0;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = getDist(e);
                mode += 1;
                if (3 == mode) {
                    oldX = getCenterX(e);
                    oldY = getCenterY(e);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("----1---", String.valueOf(mode));
                if (2 == mode) {
                    float newDist = getDist(e);
                    if (newDist > oldDist + 1) {
                        Log.e("----scale + ---", String.valueOf(mode));
                        Log.e("----2---", String.valueOf(newDist / oldDist));
                        s += 0.03;
                        mRenderer.getmCube().topSquare.scale = s;
                        mRenderer.getmCube().bottomSquare.scale = s;
                        oldDist = newDist;
                    }
                    if (newDist < oldDist - 1) {
                        s -= 0.03;
                        Log.e("----scale - ---", String.valueOf(mode));
                        Log.e("----3---", String.valueOf(newDist / oldDist));

                        mRenderer.getmCube().topSquare.scale = s;
                        mRenderer.getmCube().bottomSquare.scale = s;
                        oldDist = newDist;
                    }
                    requestRender();
                } else {
                    if (1 == mode) {
                        Log.e("----rotate---", String.valueOf(mode));

//                    float dx = x - mPreviousX;
//                    float dy = y - mPreviousY;
//
//                    // reverse direction of rotation above the mid-line
//                    if (y > getHeight() / 2) {
//                        dx = dx * -1;
//                    }
//
//                    // reverse direction of rotation to left of the mid-line
//                    if (x < getWidth() / 2) {
//                        dy = dy * -1;
//                    }
//
//                    mRenderer.setAngle(
//                            mRenderer.getAngle() +
//                                    ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
//                    requestRender();
//                    break;
                        float dx = x - mPreviousX;
                        float dy = y - mPreviousY;

//                    if (y > getHeight() / 2) {
//                        dx = dx * -1;
//                    }
//
//                    // reverse direction of rotation to left of the mid-line
//                    if (x < getWidth() / 2) {
//                        dy = dy * -1;
//                    }
                        mRenderer.getmCube().bottomSquare.yAngle += dx * TOUCH_SCALE_FACTOR;
                        mRenderer.getmCube().topSquare.yAngle += dx * TOUCH_SCALE_FACTOR;
                        requestRender();
//                        mPreviousY = y;//记录触控笔位置
//                        mPreviousX = x;//记录触控笔位置
                        break;


                    } else if (3 == mode) {

                        float dx = getCenterX(e) - oldX;
                        float dy = getCenterY(e) - oldY;

                        tX -= dx * 0.00005;
                        tY -= dy * 0.00005;
                        Log.e("translate:Tx", String.valueOf(tX));
                        Log.e("translate:Ty", String.valueOf(tY));
                        mRenderer.getmCube().topSquare.tranX = tX;
                        mRenderer.getmCube().topSquare.tranY = tY;
                        mRenderer.getmCube().bottomSquare.tranX = tX;
                        mRenderer.getmCube().bottomSquare.tranY = tY;
                        requestRender();
                        break;
                    }
                }


        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private float getDist(MotionEvent e) {
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float) Math.sqrt(x * x + y * y);

    }

    private float getCenterX(MotionEvent e) {
        return (e.getX(0) + e.getX(1) + e.getX(2)) / 3;
    }

    private float getCenterY(MotionEvent e) {
        return (e.getY(0) + e.getY(1) + e.getY(2)) / 3;
    }


}
