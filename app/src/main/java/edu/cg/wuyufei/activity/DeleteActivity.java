 package edu.cg.wuyufei.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import org.rajawali3d.surface.RajawaliSurfaceView;

import edu.cg.energy.MyRSurfaceView;
import edu.cg.energy.Renderer;
import edu.cg.wuyufei.gallery.R;

public class DeleteActivity extends AppCompatActivity {

    Renderer rRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.delete_rl);

        RajawaliSurfaceView surface = MyRSurfaceView.getRSurfaceView(this);
        rl.addView(surface);
        rRenderer = new Renderer(this);
        surface.setSurfaceRenderer(rRenderer);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
