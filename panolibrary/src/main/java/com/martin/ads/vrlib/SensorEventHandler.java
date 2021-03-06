package com.martin.ads.vrlib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.martin.ads.vrlib.constant.Constants;
import com.martin.ads.vrlib.utils.SensorUtils;
import com.martin.ads.vrlib.utils.StatusHelper;


/**
 * Project: Pano360
 * Package: com.martin.ads.pano360
 * Created by Ads on 2016/5/2.
 */
public class SensorEventHandler implements SensorEventListener {

    public static String TAG = "PanoMediaPlayerWrapper";

    private float[] rotationMatrix = new float[16];

    //private float[] orientationData=new float[3];

    private StatusHelper statusHelper;
    private PanoRender.SensorHandlerCallback sensorHandlerCallback;

    private boolean sensorRegistered;
    private SensorManager sensorManager;

    public void init(){
        sensorRegistered=false;
        sensorManager = (SensorManager) statusHelper.getContext()
                .getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorRot = sensorManager.getDefaultSensor(Constants.SENSOR_ROT);
        if (sensorRot==null) return;
        sensorManager.registerListener(this, sensorRot, SensorManager.SENSOR_DELAY_GAME);
        sensorRegistered=true;
    }

    public void releaseResources(){
        if (!sensorRegistered || sensorManager==null) return;
        sensorManager.unregisterListener(this);
        sensorRegistered = false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy != 0){
            int type = event.sensor.getType();
            switch (type){
                case Constants.SENSOR_ROT:
                    SensorUtils.sensorRotationVectorToMatrix(event,rotationMatrix);
                    sensorHandlerCallback.updateSensorMatrix(rotationMatrix);
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setSensorHandlerCallback(PanoRender.SensorHandlerCallback sensorHandlerCallback){
        this.sensorHandlerCallback=sensorHandlerCallback;
    }

    public void setStatusHelper(StatusHelper statusHelper){
        this.statusHelper=statusHelper;
    }

}
