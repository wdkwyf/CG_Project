package edu.cg.energy;

import android.content.Context;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

/**
 * Created by wuyufei on 15/10/30.
 */
public class MyRSurfaceView {
    public static RajawaliSurfaceView getRSurfaceView(Context context){
        final RajawaliSurfaceView rSurface = new RajawaliSurfaceView(context);
        rSurface.setFrameRate(60.0);
        rSurface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        return rSurface;

    }
}
