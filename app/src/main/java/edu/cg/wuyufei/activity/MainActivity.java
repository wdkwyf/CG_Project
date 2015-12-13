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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.io.File;

import edu.cg.wuyufei.card.MyGLSurfaceView;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.gallery.adapter.ImageAdapter;
import edu.cg.wuyufei.gallery.widget.GalleryFlow;

public class MainActivity extends Activity {
	private GalleryFlow mGalleryFlow;
    public static int init = 0;
    public final static  String EXTRA_CARDID = "edu.cg.wuyufei.activity.CARDID";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
