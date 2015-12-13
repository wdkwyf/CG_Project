package edu.cg.wuyufei.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.cg.wuyufei.activity.MainActivity;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.gallery.widget.GalleryFlow;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mImageIds;
    private Vector<ImageView> mImages;
    public static int picIndex = 0;
    private List<Integer> drawablesId = new ArrayList<>();
    private File[] fileList;

    public ImageAdapter(Context context) {
        this.mContext = context;
        mImages = new Vector<>();
        String root = Environment.getExternalStorageDirectory().toString();
        String imagePath = root + "/card/front";
        File dir = new File(imagePath);
        fileList = dir.listFiles();
        if (fileList.length == 0) {
            MainActivity.init = 1;
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(mContext.getDrawable(R.drawable.example));
            imageView.setId(picIndex);
            picIndex++;
            drawablesId.add(imageView.getId());
            mImages.add(imageView);

        } else {
            for (File f : fileList) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageDrawable(Drawable.createFromPath(f.getAbsolutePath()));
                imageView.setId(picIndex);
                picIndex++;
                drawablesId.add(imageView.getId());
                mImages.add(imageView);
            }
        }
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mImages.get(position);
    }

    // 生成带有倒影效果的图片
    public boolean createReflectedImages() {
        // 原图片与倒影之间的距离
        final int reflectionGap = 4;
        int index = 0;
        for (File image : fileList) {
            // 原图片
            Bitmap originalImage = BitmapFactory.decodeFile(image.getAbsolutePath());

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // 创建矩阵对象
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            // 将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图
            Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                    height / 2, width, height / 2, matrix, false);

            // 创建一个宽度不变，高度为原图+倒影图高度的位图
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height
                    + height / 2, Config.ARGB_8888);
            // 创建画布
            Canvas canvas = new Canvas(bitmapWithReflection);
            // 绘制原图片
            canvas.drawBitmap(originalImage, 0, 0, null);
            // 绘制原图片与倒影之间的距离
            Paint defaultpaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap,
                    defaultpaint);
            // 绘制倒影图片
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            LinearGradient shader = new LinearGradient(0, height, 0,
                    bitmapWithReflection.getHeight() + reflectionGap,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            // 着色器 用来绘制颜色 上色的
            paint.setShader(shader);
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                    + reflectionGap, paint);

            // 创建一个ImageView用来显示已经画好的bitmapWithReflection
            ImageView imageView = new ImageView(mContext);
            BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);
            // 消除图片锯齿效果
            bd.setAntiAlias(true);
            imageView.setImageDrawable(bd);
            // 设置图片的大小
            imageView.setLayoutParams(new GalleryFlow.LayoutParams(300, 500));
            mImages.set(index++, imageView);
        }
        return true;
    }
}
