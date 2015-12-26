package edu.cg.wuyufei.addForm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.util.MyApplication;

/**
 * Created by wuyufei on 15/11/27.
 */
public class CustomPaint extends View {
    private Paint paint;
    private final String TAG = "CustomPaint";
    private Bitmap cardBg;
    private SharedPreferences pref;
    final float SCALE = getContext().getResources().getDisplayMetrics().density;


    public CustomPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(45);
        cardBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.card_bg);
        pref = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void getDrawnMessage() throws FileNotFoundException {
        Bitmap bmOverlay = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bmOverlay);

        mCanvas.drawBitmap(cardBg, 0, 0, paint);
        String name = pref.getString("name", "NullName");
        String phone = pref.getString("phone", "NullPhone");
        String email = pref.getString("email", "NullEmail");
        String address = pref.getString("address", "NullAddress");
        mCanvas.drawText(name, 100 * SCALE + 0.5f, 58 * SCALE + 0.5f, paint);
        mCanvas.drawText(phone, 114 * SCALE + 0.5f, 86 * SCALE + 0.5f, paint);
        mCanvas.drawText(email, 100 * SCALE + 0.5f, 113 * SCALE + 0.5f, paint);
        mCanvas.drawText(address, 114 * SCALE + 0.5f, 143 * SCALE + 0.5f, paint);
        setDrawingCacheEnabled(true);
        String root = Environment.getExternalStorageDirectory().toString();
//        Toast.makeText(getContext(), root + "save file", Toast.LENGTH_SHORT).show();
        File imgDirFront = new File(root + "/card/front");
        File imgDirEnd = new File(root + "/card/end");
        String imgNameFront, imgNameEnd;
        imgDirFront.mkdirs();
        imgDirEnd.mkdirs();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        imgNameFront = "img" + currentDateandTime + ".jpg";
        imgNameEnd = "img" + currentDateandTime + ".jpg";

        File fileFront = new File(imgDirFront, imgNameFront);
        File fileEnd = new File(imgDirEnd, imgNameEnd);
        if (fileFront.exists()) fileFront.delete();
        if (fileEnd.exists()) fileEnd.delete();
        FileOutputStream outImg = new FileOutputStream(fileFront);
        FileOutputStream outImg2 = new FileOutputStream(fileEnd);
        bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
        PageFragment.bitmap.compress(Bitmap.CompressFormat.JPEG,10,outImg2);

        setDrawingCacheEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cardBg, 0, 0, paint);
        String name = pref.getString("name", "NullName");
        String phone = pref.getString("phone", "NullPhone");
        String email = pref.getString("email", "NullEmail");
        String address = pref.getString("address", "NullAddress");
        canvas.drawText(name, 100 * SCALE + 0.5f, 58 * SCALE + 0.5f, paint);
        canvas.drawText(phone, 114 * SCALE + 0.5f, 86 * SCALE + 0.5f, paint);
        canvas.drawText(email, 100 * SCALE + 0.5f, 113 * SCALE + 0.5f, paint);
        canvas.drawText(address, 114 * SCALE + 0.5f, 143 * SCALE + 0.5f, paint);

        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                if(PageFragment.bitmap != null){
                    getDrawnMessage();
                    Toast.makeText(getContext(),"名片增加成功",Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
