package edu.cg.wuyufei.card;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by wuyufei on 15/10/17.
 */
public class Cube {
    Square topSquare;
    Square bottomSquare;


    float h;
    float scale;

    int topTexId;
    int bottomTexId;
    int sideTexId;

    static float[] mStack=new float[16];

    private final float[] rotationMatrix = new float[16];
    private final String message = "CUBE";


    public Cube(float h, int topTexId, int bottomTexId, int sideTexId) {
        this.h = h;
        this.topTexId = topTexId;
        this.bottomTexId = bottomTexId;
        this.sideTexId = sideTexId;

        topSquare = new Square();
        bottomSquare = new Square();
        Log.e(message,"create a cube");


    }

    public void draw(float[] scratch){
        MatrixState.scratch = scratch;
        Log.e("Flag------", String.valueOf(MyGLSurfaceView.flag));
        MatrixState.pushMatrix();
//        MatrixState.r(180, 1, 0, 0);
        Log.e("aaaaaa", "-----");
//        MatrixState.print();
        //顶面r
        topSquare.draw(topTexId,bottomTexId);
        Log.e("bbbbb", "-----");
        MatrixState.print();

        MatrixState.popMatrix();

//        bottomSquare.draw(topTexId,bottomTexId);

    }

}
