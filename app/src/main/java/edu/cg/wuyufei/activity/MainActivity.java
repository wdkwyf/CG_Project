package edu.cg.wuyufei.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import edu.cg.wuyufei.card.MyGLSurfaceView;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.gallery.adapter.ImageAdapter;
import edu.cg.wuyufei.gallery.widget.GalleryFlow;

public class MainActivity extends Activity {
	private GalleryFlow mGalleryFlow;
    public final static  String EXTRA_CARDID = "edu.cg.wuyufei.activity.CARDID";


	private int[] imageIds = new int[]{ R.drawable.baiyang, R.drawable.chunv,
			R.drawable.jinniu, R.drawable.juxie, R.drawable.mojie,
			R.drawable.sheshou, R.drawable.shizi, R.drawable.shuangyu,
			R.drawable.shuangzi, R.drawable.shuiping, R.drawable.tianping,
			R.drawable.tianxie};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		initViews();

		mGalleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("-------itemClick-------", "you click" + i);
                Intent intent = new Intent(MainActivity.this, CardActivity.class);

                intent.putExtra(EXTRA_CARDID, i);
                startActivity(intent);
//				finish();
                // no finish(),So we will return initial activity

            }
        });

	}

	private void findViews() {
		mGalleryFlow = (GalleryFlow) findViewById(R.id.gf);
	}

	private void initViews() {
		ImageAdapter adapter = new ImageAdapter(this, imageIds);
		// 生成带有倒影效果的图片
		adapter.createReflectedImages();
		mGalleryFlow.setAdapter(adapter);

	}

}
