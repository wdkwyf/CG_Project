package edu.cg.wuyufei.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import edu.cg.wuyufei.box.OpenGLView;
import edu.cg.wuyufei.gallery.R;
public class CardBoxActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_card_box);

        //mOpenGLView=new OpenGLView(this);

        final OpenGLView mOpenGLView = new OpenGLView(this);
        mOpenGLView.requestFocus();
        mOpenGLView.setFocusableInTouchMode(true);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.boxLayout);
        linearLayout.addView(mOpenGLView);


        Button okBt = (Button) findViewById(R.id.okB);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardBoxActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });

    }

}
