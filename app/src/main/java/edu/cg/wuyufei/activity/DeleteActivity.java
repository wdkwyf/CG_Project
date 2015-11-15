package edu.cg.wuyufei.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Surface;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import edu.cg.RollBall.AndroidBallView;
import edu.cg.wuyufei.gallery.R;

public class DeleteActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        DisplayMetrics deviceMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(deviceMetrics);

        getAndroidBallView().setDpi(deviceMetrics.xdpi, deviceMetrics.ydpi);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    private AndroidBallView getAndroidBallView() {
        return (AndroidBallView) findViewById(R.id.androidBall_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (getAndroidBallView().confirmed) {
            sensorManager.unregisterListener(this, accelerometer);
            new MaterialDialog.Builder(this).title("进入漩涡区域，就要删除了哦>.<").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    Intent intent = new Intent(DeleteActivity.this, CardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).positiveText("知道了~").show();
            return;

        }
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        switch (getWindowManager().getDefaultDisplay().getOrientation()) {
            case Surface.ROTATION_0:
                getAndroidBallView().setGravity(-event.values[0], event.values[1]);
                break;
            case Surface.ROTATION_90:
                getAndroidBallView().setGravity(event.values[1], event.values[0]);
                break;
            case Surface.ROTATION_180:
                getAndroidBallView().setGravity(event.values[0], -event.values[1]);
                break;
            case Surface.ROTATION_270:
                getAndroidBallView().setGravity(-event.values[1], -event.values[0]);
                break;
        }
    }

}
