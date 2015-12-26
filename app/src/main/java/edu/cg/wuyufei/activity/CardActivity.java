package edu.cg.wuyufei.activity;


import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.rajawali3d.surface.RajawaliSurfaceView;

import java.io.File;

import edu.cg.energy.MyRSurfaceView;
import edu.cg.energy.Renderer;
import edu.cg.wuyufei.card.MyGLSurfaceView;
import edu.cg.wuyufei.gallery.R;


public class CardActivity extends AppCompatActivity {
    public static File curCardFront;
    public static File curCardEnd;
    private GLSurfaceView mGLView;
    private String imageDirFront;
    private String imageDirEnd;
    private LinearLayout ll;
    public final static String CUR_CARDIDFRONT = "edu.cg.wuyufei.activity.CUR_CARDIDFRONT";
    public final static String CUR_CARDIDEND = "edu.cg.wuyufei.activity.CUR_CARDIDEND";
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
        if (MainActivity.init == 0) {
            String root = Environment.getExternalStorageDirectory().toString();
            imageDirFront = root + "/card/front";
            imageDirEnd = root + "/card/end";
            File dir = new File(imageDirFront);
            File dir2 = new File(imageDirEnd);
            File[] fileListFront = dir.listFiles();
            File[] fileListEnd = dir2.listFiles();
            curCardFront = fileListFront[message];
            curCardEnd = fileListEnd[message];
        }

        mGLView = new MyGLSurfaceView(this);
        ll = (LinearLayout) findViewById(R.id.cardLayout);
        ll.addView(mGLView);


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
                        intent.putExtra(CUR_CARDIDFRONT, curCardFront.getAbsolutePath());
                        intent.putExtra(CUR_CARDIDEND, curCardEnd.getAbsolutePath());
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
        if (id == R.id.action_about_us) {
            Intent intent = new Intent(CardActivity.this, AboutUsActivity.class);
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
