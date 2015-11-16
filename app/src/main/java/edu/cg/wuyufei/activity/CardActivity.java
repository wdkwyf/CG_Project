package edu.cg.wuyufei.activity;


import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.rajawali3d.surface.RajawaliSurfaceView;

import edu.cg.energy.MyRSurfaceView;
import edu.cg.energy.Renderer;
import edu.cg.wuyufei.card.MyGLSurfaceView;
import edu.cg.wuyufei.gallery.R;


public class CardActivity extends AppCompatActivity {
    private GLSurfaceView mGLView;
    Renderer rRenderer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("My Title");
        toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        int message = intent
                .getIntExtra(MainActivity.EXTRA_CARDID, -1);
        Log.d("----message-----", String.valueOf(message));
        mGLView = new MyGLSurfaceView(this);
        RajawaliSurfaceView surface = MyRSurfaceView.getRSurfaceView(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.cardLayout);
//        ll.addView(surface);
        ll.addView(mGLView);

//        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));


        rRenderer = new Renderer(this);
        surface.setSurfaceRenderer(rRenderer);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                new MaterialDialog.Builder(this).title("我们来玩一个有趣的♂游戏吧，旋转手机控制小球,进入漩涡区域才能删掉哦").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        Intent intent = new Intent(CardActivity.this, DeleteActivity.class);
                        startActivity(intent);
                    }
                }).positiveText("好的")
                        .negativeText("才不要")
                        .show();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(CardActivity.this, AddActivity.class);
                startActivity(intent);
                break;


        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
