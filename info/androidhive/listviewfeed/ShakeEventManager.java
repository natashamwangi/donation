package info.androidhive.listviewfeed;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeEventManager implements SensorEventListener {
    private static final float ALPHA = 0.8f;
    private static final int MOV_COUNTS = 2;
    private static final int MOV_THRESHOLD = 4;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500;
    private int counter;
    private long firstMovTime;
    private float[] gravity;
    private ShakeListener listener;
    private Sensor f1s;
    private SensorManager sManager;

    public interface ShakeListener {
        void onShake();
    }

    public ShakeEventManager() {
        this.gravity = new float[3];
    }

    public void setListener(ShakeListener listener) {
        this.listener = listener;
    }

    public void init(Context ctx) {
        this.sManager = (SensorManager) ctx.getSystemService("sensor");
        this.f1s = this.sManager.getDefaultSensor(1);
        register();
    }

    public void register() {
        this.sManager.registerListener(this, this.f1s, 3);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (calcMaxAcceleration(sensorEvent) < 4.0f) {
            return;
        }
        if (this.counter == 0) {
            this.counter++;
            this.firstMovTime = System.currentTimeMillis();
            Log.d("SwA", "First mov..");
        } else if (System.currentTimeMillis() - this.firstMovTime < 500) {
            this.counter++;
            Log.d("SwA", "Mov counter [" + this.counter + "]");
            if (this.counter >= MOV_COUNTS && this.listener != null) {
                this.listener.onShake();
            }
        } else {
            resetAllData();
            this.counter++;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void deregister() {
        this.sManager.unregisterListener(this);
    }

    private float calcMaxAcceleration(SensorEvent event) {
        this.gravity[0] = calcGravityForce(event.values[0], 0);
        this.gravity[1] = calcGravityForce(event.values[1], 1);
        this.gravity[MOV_COUNTS] = calcGravityForce(event.values[MOV_COUNTS], MOV_COUNTS);
        return Math.max(Math.max(event.values[0] - this.gravity[0], event.values[1] - this.gravity[1]), event.values[MOV_COUNTS] - this.gravity[MOV_COUNTS]);
    }

    private float calcGravityForce(float currentVal, int index) {
        return (ALPHA * this.gravity[index]) + (0.19999999f * currentVal);
    }

    private void resetAllData() {
        Log.d("SwA", "Reset all data");
        this.counter = 0;
        this.firstMovTime = System.currentTimeMillis();
    }
}
