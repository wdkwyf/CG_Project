package edu.cg.wuyufei.card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.cg.wuyufei.activity.CardActivity;
import edu.cg.wuyufei.activity.MainActivity;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.util.MyApplication;
import edu.cg.wuyufei.util.TextureUtils;

/**
 * Created by wuyufei on 15/10/1.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Square mSquare;
    private Cube mCube;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    public static int texIdFront = -1;
    public static int texIdEnd = -1;
    private Bitmap imageFront,imageEnd;

    private float mAngle;
    private float mScale = 1;
    private float mTransX = 0, mTransY = 0, mTransZ = 0;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color_black
//        GLES20.glClearColor(1f, 0.0f, 0.0f, 0.2f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        if (MainActivity.init == 1) {
            imageFront= BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.example);
            imageEnd = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.example);

        } else {
            imageFront = BitmapFactory.decodeFile(CardActivity.curCardFront.getAbsolutePath());
            imageEnd = BitmapFactory.decodeFile(CardActivity.curCardEnd.getAbsolutePath());
        }

        texIdEnd = TextureUtils.createTexture(imageEnd);
        texIdFront = TextureUtils.createTexture(imageFront);

        mSquare = new Square();
        mCube = new Cube(10f, texIdFront, texIdEnd, texIdFront);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 2, 0, -6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        MatrixState.mMVPMatrix = mMVPMatrix;


        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);


        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mCube.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     * <p/>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setScale(float mScale) {
        this.mScale = mScale;
    }

    public void setmTransY(float mTransY) {
        this.mTransY = mTransY;
    }

    public void setmTransX(float mTransX) {
        this.mTransX = mTransX;
    }

    public void setmTransZ(float mTransZ) {
        this.mTransZ = mTransZ;
    }

    public Cube getmCube() {
        return mCube;
    }
}
