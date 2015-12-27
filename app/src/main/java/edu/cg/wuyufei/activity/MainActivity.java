package edu.cg.wuyufei.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.File;

import edu.cg.wuyufei.card.MyGLSurfaceView;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.gallery.adapter.ImageAdapter;
import edu.cg.wuyufei.gallery.widget.GalleryFlow;

public class MainActivity extends Activity {
    private GalleryFlow mGalleryFlow;
    public static int init = 0;
    public final static String EXTRA_CARDID = "edu.cg.wuyufei.activity.CARDID";
    private ImageView ivRipple;

    private RelativeLayout relativeLayout;

    private Animation anim;
    private Animation.AnimationListener animListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String root = Environment.getExternalStorageDirectory().toString();
        File imgDirFront = new File(root + "/card/front");
        File imgDirEnd = new File(root + "/card/end");
        imgDirFront.mkdirs();
        imgDirEnd.mkdirs();

        relativeLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        animListener = new AnimListener(relativeLayout);

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int eventAction = event.getAction();

                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN: {
                    /* Get x and y co-ordinates from the touch on screen */
                        float TouchX = event.getX();
                        float TouchY = event.getY();

					/* Create a new ripple image view everytime a touch occurs */
                        ivRipple = new ImageView(MainActivity.this);
                        ivRipple.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        ivRipple.setImageResource(R.drawable.ripplering4);

					/* Set the imageview to the touch co-ordinates */
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                        params.leftMargin = (int) TouchX;
                        params.topMargin = (int) TouchY;
                        relativeLayout.addView(ivRipple, params);


					/* Create new animation object everytime a touch occurs  */
                        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ripplescale);
                        ivRipple.setAnimation(anim);

					/* Animation listener to clear the image view once animation is complete  */
                        anim.setAnimationListener(animListener);
                        break;
                    }
                }
                return true;
            }
        });

        findViews();
        initViews();
        mGalleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, CardActivity.class);
                intent.putExtra(EXTRA_CARDID, i);
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        mGalleryFlow = (GalleryFlow) findViewById(R.id.gf);
    }

    private void initViews() {
        ImageAdapter adapter = new ImageAdapter(this);
        adapter.createReflectedImages();
        mGalleryFlow.setAdapter(adapter);

    }
}

class AnimListener implements Animation.AnimationListener {

    private RelativeLayout relativeLayout;

    public AnimListener(RelativeLayout rl) {
        relativeLayout = rl;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        try {
//            relativeLayout.removeAllViews();

        } catch (Exception e) {
        }
    }

}
