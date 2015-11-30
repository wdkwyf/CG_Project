package edu.cg.wuyufei.addForm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.util.MyApplication;

/**
 * Created by wuyufei on 15/11/27.
 */
public class CustomPaint extends View {
    private Paint paint;
    private final String TAG = "CustomPaint";
    private Bitmap cardBg;

    public CustomPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        cardBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.baiyang);
    }

    public void getDrawnMessage() throws FileNotFoundException {
        Bitmap bmOverlay = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bmOverlay);

        mCanvas.drawBitmap(cardBg, 0, 0, paint);
//        mCanvas.drawCircle(100,200,100,paint);
        mCanvas.drawText("hello,world", 10, 25, paint);

        setDrawingCacheEnabled(true);
        String root = Environment.getExternalStorageDirectory().toString();
        Toast.makeText(getContext(), root + "save file", Toast.LENGTH_SHORT).show();
        File imgDir = new File(root + "/card/");
        String imgName;
        imgDir.mkdirs();
        int i = 0;
        imgName = "img" + i + ".jpg";
        i++;
        File file = new File(imgDir, imgName);
        if (file.exists()) file.delete();
        FileOutputStream outImg = new FileOutputStream(file);
        bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
        setDrawingCacheEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        invalidate();

        Context context = MyApplication.getContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        canvas.drawBitmap(cardBg, 0, 0, paint);
        canvas.drawCircle(100, 200, 100, paint);

        String name = pref.getString("name", "NullName");
        String phone = pref.getString("phone", "NullPhone");
        String email = pref.getString("email", "NullEmail");
        String address = pref.getString("name", "NullAddress");
        canvas.drawText(name, 10, 25, paint);

        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                getDrawnMessage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
