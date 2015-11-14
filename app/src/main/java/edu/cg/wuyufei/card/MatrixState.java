package edu.cg.wuyufei.card;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by wuyufei on 15/10/1.
 */
public class MatrixState {
    public static float[] scratch = new float[16];
    public static float[] mMVPMatrix = new float[16];

    static float[][] mStack=new float[10][16];
    static int stackTop=-1;

    public static void  scale(float scale,float[] curMatrix){
        scratch = curMatrix;
        Matrix.scaleM(scratch,0,scale,scale,scale);
    }
    public static void translate(float x,float y,float z,float[] curMatrix){
        scratch = curMatrix;
        Matrix.translateM(scratch, 0, x, y, z);
    }
    public static void pushMatrix()//保护变换矩阵
    {
        stackTop++;
        for(int i=0;i<16;i++)
        {
            mStack[stackTop][i]=scratch[i];
        }
    }

    public static void popMatrix()//恢复变换矩阵
    {
        for(int i=0;i<16;i++)
        {
            scratch[i]=mStack[stackTop][i];
        }
        stackTop--;
    }
    public static void rotate(float angle,float x,float y,float z,float[] curMatrix)
    {
        scratch = curMatrix;
        float[] mRotationMatrix = new float[16];
        Matrix.setRotateM(mRotationMatrix, 0, angle, x, y, z);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

    }
    public static void r(float angle,float x,float y,float z)//设置绕xyz轴移动
    {
        Matrix.rotateM(scratch, 0, angle, x, y, z);
    }
    public static void print(){
        for(int i = 0;i < 16;i++){
            Log.e("printf",scratch[i]+" ");

        }
    }


}
