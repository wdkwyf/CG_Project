package edu.cg.wuyufei.card;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by wuyufei on 15/10/1.
 */
public class Square {
    private final int OFFSET = 20;
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "uniform mat4 uMVMatrix;" +
                    "uniform float uIntensity;" +
                    "attribute vec4 vPosition;" +
                    "varying vec2 vTexCoordinate;" +
                    "varying vec4 vColor;" +
                    "varying float vDiffuse;" +
                    "attribute vec2 aTexCoordinate;" +
                    "attribute vec3 aNormal;" +
                    "uniform vec3 uLightPos;" +
                    "void main() {" +
                    "vTexCoordinate = aTexCoordinate;" +
                    "vec3 modelViewVertex = vec3(uMVMatrix * vPosition);" +
                    "vec3 modelViewNormal = vec3(uMVMatrix * vec4(aNormal,0.0));" +
                    "float distance = length(uLightPos - modelViewVertex);" +
                    "vec3 lightVector = normalize(uLightPos - modelViewVertex);" +
                    "float diffuse = max(dot(modelViewNormal, lightVector),0.1);" +
                    "vDiffuse = uIntensity * float(diffuse * (1.0 / (1.0 + (0.25 * distance * distance))));" +
                    "gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexCoordinate;" +
                    "varying float vDiffuse;" +
                    "void main() {" +
                    " vec4 finalColor=texture2D(uTexture, vTexCoordinate);" +
                    "  gl_FragColor = finalColor * vDiffuse;" +
                    "}";
    public float xAngle, yAngle, zAngle;
    public float scale = 1, tranX = 0, tranY = 0, tranZ = 0;
    public final String TAG = "Square";
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer texCoorBuffer;

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mIntensityHandle;

    private int mTexCoorHandle; // 顶点纹理坐标id

    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.8f, 0.5f, 0.0f,   // top left
            -0.8f, -0.5f, 0.0f,   // bottom left
            0.8f, -0.5f, 0.0f,   // bottom right
            0.8f, 0.5f, 0.0f}; // top right

    float texCoor[] = {
//            0.0f, 1.0f,
//            0.0f, 0.0f,
//            1.0f, 0.0f,
//            1.0f, 1.0f,
            1, 0,
            1, 1,
            0, 1,
            0, 0,

    };
    private final short drawOrder[] = {0, 1, 2, 2, 3,0}; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        //initialize byte buffer for texture
        //纹理空间坐标 S,T


        ByteBuffer cb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cb.order(ByteOrder.nativeOrder());
        texCoorBuffer = cb.asFloatBuffer();
        texCoorBuffer.put(texCoor);
        texCoorBuffer.position(0);


        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        //error check

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(mProgram);
            throw new RuntimeException("Error Program link.");

        }
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     *                  this shape.
     */
    public void draw(int t1, int t2) {
        // Add program to OpenGL environment

//        MatrixState.scratch = mvpMatrix;
//        MatrixState.translate(mTransX, mTransY, mTransZ, scratch);
        int textureId;
        if (yAngle < 0) {
            if (Math.abs(yAngle - 90) % 360 < 180 + OFFSET) {
                textureId = t1;

            } else {
                textureId = t2;
            }

        } else {
            if (Math.abs(yAngle + 90) % 360 < 180 - OFFSET) {
                textureId = t1;

            } else {
                textureId = t2;
            }

        }

        MatrixState.rotate(xAngle, 1, 0, 0, MatrixState.scratch);
        MatrixState.rotate(yAngle, 0, 1, 0, MatrixState.scratch);
//        MatrixState.rotate(zAngle,0,0,1,MatrixState.scratch);
        MatrixState.scale(scale, MatrixState.scratch);
        MatrixState.translate(tranX, tranY, tranZ, MatrixState.scratch);


        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexCoorHandle);


        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(
                mTexCoorHandle, COORDS_PER_VERTEX - 1,
                GLES20.GL_FLOAT, false,
                2 * 4, texCoorBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        mIntensityHandle = GLES20.glGetUniformLocation(mProgram, "uIntensity");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glUniform1f(mIntensityHandle, 10f);


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
//        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.scratch, 0);
//        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);


    }

}
