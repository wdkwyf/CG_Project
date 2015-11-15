package edu.cg.energy;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Camera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

import edu.cg.wuyufei.gallery.R;

/**
 * Created by wuyufei on 15/10/30.
 */
public class Renderer extends RajawaliRenderer {
    public Context context;
    private DirectionalLight directionalLight;
    private Sphere earthSpere;

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);


    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void initScene() {
        directionalLight = new DirectionalLight(1f, 0.2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentScene().addLight(directionalLight);

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Earth", R.drawable.baiyang);
        try {
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException e) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }


        earthSpere = new Sphere(0.5f, 14, 14);
        earthSpere.setMaterial(material);
        getCurrentScene().addChild(earthSpere);
        getCurrentCamera().setZ(4.2);

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        earthSpere.rotate(Vector3.Axis.Y, 1.0);
    }
}
