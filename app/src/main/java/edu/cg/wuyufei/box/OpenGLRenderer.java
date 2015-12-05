package edu.cg.wuyufei.box;

/**
 * Created by dingpeien on 15/11/16.
 */

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenGLRenderer implements Renderer {

    //Square square = new Square();
    //float angle;
    Mesh root;
    Group group;
    float xTrans = 0;
    float yTrans = 0;
    float xCons = 0;
    float yCons = 0;
    private Handler handler = null;
    private float rotateV = 0;
    private float rotated = 0;
    private float scaleRate = 1;
    private float scaleCons = 0;
    private static final float NORMALS_COS = (float) Math.cos(Math.PI / 2);
    private static final float NORMALS_SIN = (float) Math.sin(Math.PI / 2);
    private RotateTask rTask = null;
    private static final int MSG_ROTATE_STOP = 1;
    private OpenGLView surface = null;
    boolean change = false;

    private int autoAngle = 0;
    Box nCube = new Box(0.5f, 0.5f, 0.5f);
//    Box nCube2=new Box(0.5f, 0.5f, 0.5f);
//    Box nCube3=new Box(0.5f, 0.5f, 0.5f);
//    Box nCube4=new Box(0.5f, 0.5f, 0.5f);
    //*****************************************bird init

    public static int birdNum = 20;
    private Bird birds;

    private Vector<Bird> birdVector;

    {
        birdVector = new Vector<>(birdNum);
    }

    boolean twink = true;
    boolean key;
    float zoom = -10.0f;
    float tilt = 90.0f;
    float spin;
    int one = 0x10000;
    Random random = new Random();


    public OpenGLRenderer(OpenGLView surface) {
        // TODO Auto-generated constructor stub

        this.surface = surface;

        group = new Group();


        nCube.rx = 75;
        nCube.ry = 45;


        group.add(nCube);
//        group.add(nCube2);
//        group.add(nCube3);
//        group.add(nCube4);
        for (int i = 0; i < birdNum; i++) {
            birds = new Bird();
            birdVector.add(birds);
        }
        //root=group;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == MSG_ROTATE_STOP) {
                    OpenGLRenderer.this.surface.setAutoRender(false);
                }
            }
        };
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // MatrixState.pushMatrix();
        gl.glTranslatef(0, 0, -6f);
/////////////////////////////////////////////
        gl.glPushMatrix();
        //gl.glRotatef(autoAngle, 0, 0, 0);
        gl.glTranslatef(0, 0, 2f);
        if (rotated != 0) {
            RotateOnTouch(gl);
        }
        //if (change){
        drawAniBox(gl);
        //}
//        if (xCons!=0||yCons!=0) {
//            TransOnTouch(gl);
//        }
        //if (scaleCons!=0) {
        //ScaleOnTouch(gl);
        //}
        // gl.glRotatef(75, 1, 1, 0);
        gl.glColor4f(1, 1, 1, 1);
        group.draw(gl, 0);
        gl.glPopMatrix();
/////////////////////////////////////////////draw bird

        for (int i = 0; i < birdNum; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, -30f);
            gl.glRotatef(tilt, 1, 0, 0);
            gl.glRotatef(birdVector.get(i).angle, 0, 1, 0);
            gl.glTranslatef(birdVector.get(i).dist, 0, 0);
            gl.glRotatef(-birdVector.get(i).angle, 0, 1, 0);
            gl.glRotatef(-tilt, 1, 0, 0);
            if (twink) {
                gl.glColor4f((float) birdVector.get(i).r / 255.0f, (float) birdVector.get(i).g / 255.0f, (float) birdVector.get(i).b / 255.0f, 1);
                birdVector.get(i).draw(gl);
            }
            gl.glRotatef(spin, 0, 0, 1);
            gl.glColor4f((float) birdVector.get(i).r / 255.0f, (float) birdVector.get(i).g / 255.0f, (float) birdVector.get(i).b / 255.0f, 1);
            birdVector.get(i).draw(gl);
            spin += 0.04f;
            birdVector.get(i).angle += (float) (i) / (float) (birdNum);
            birdVector.get(i).dist -= 0.01f;
            if (birdVector.get(i).dist < 4.0f) {
                birdVector.get(i).dist += 8;
                birdVector.get(i).r = random.nextInt(256);
                birdVector.get(i).g = random.nextInt(256);
                birdVector.get(i).b = random.nextInt(256);

            }

            gl.glPopMatrix();
        }


        autoAngle++;
    }

    private void ScaleOnTouch(GL10 gl) {
        gl.glScalef(scaleRate, scaleRate, 0);
    }

    private void RotateOnTouch(GL10 gl) {
        this.rotated += rotateV;//如果加上，则会自动转动
        gl.glRotatef(rotated, xTrans, yTrans, 0);
        if (rotateV > 0) {
//			Log.i("tg","GL rotateV/" + rotateV);
//			Log.i("tg","GL rotated/" + rotated + "/" + rotateV);
        }
    }

    private void TransOnTouch(GL10 gl) {
        gl.glTranslatef(xTrans, yTrans, 0);

    }

    public void onTouchMoveScale(float s) {
        scaleCons = s;
        scaleRate += scaleCons;

    }

    public void onTouchMoveRotate(float dx, float dy) {
        rotateV = Math.abs(dx) / 10 + Math.abs(dy) / 10;
//		Log.i("tg","GL rotateV/" + rotateV);
        rotated += rotateV;
        setAxisXY(dx, dy);
    }

    public void onTouchMoveTrans(float dx, float dy) {
        xCons = dx * 0.005f;
        yCons = dy * 0.005f;
        xTrans += xCons;
        yTrans += yCons;
    }

    public void setAxisXY(float dx, float dy) {
        this.xTrans = dx * NORMALS_COS - dy * NORMALS_SIN;
        this.yTrans = dx * NORMALS_SIN + dy * NORMALS_COS;
    }

    public void startRotate() {
        if (rTask != null) {
            rTask.running = false;
        }
        rTask = new RotateTask();
        rTask.start();
    }

    class RotateTask extends Thread {
        boolean running = true;

        @Override
        public void run() {
            while (running && rotateV > 0) {
                if (rotateV > 50) {
                    rotateV -= 7;
                } else if (rotateV > 20) {
                    rotateV -= 3;
                } else {
                    rotateV--;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (rotateV <= 0) {
                    handler.sendEmptyMessage(MSG_ROTATE_STOP);
                }
            }
        }
    }


    public void drawAniBox(GL10 gl) {
        gl.glRotatef(autoAngle, 0, 0, 0);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
                100.0f);

    }


    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
        // TODO Auto-generated method stub
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glClearColor(0f, 0f, 0f, 0.5f);

        gl.glClearDepthf(1.0f);

        gl.glEnable(GL10.GL_DEPTH_TEST);

        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        for (int i = 0; i < birdNum; i++)    // 循环设置全部方块
        {
            Bird b = new Bird();
            b.angle = 0.0f;
            b.dist = ((float) (i) / (float) birdNum) * 5.0f;
            b.r = random.nextInt(256);
            b.g = random.nextInt(256);
            b.b = random.nextInt(256);
            birdVector.add(b);

        }


    }


}