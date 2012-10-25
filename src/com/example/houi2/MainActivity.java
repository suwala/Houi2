package com.example.houi2;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity{
	
	public SensorManager sensorManager;
	public SensorEventListener sensorEventListener;
	public ImageView iv;
	Handler handler = new Handler();
	public Timer timer;
	public Float mtrix;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.iv = (ImageView)this.findViewById(R.id.imageView1);
    }


	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
		
		TextView tv1 = (TextView)this.findViewById(R.id.textView1);
		TextView tv2= (TextView)this.findViewById(R.id.textView2);
		TextView tv3 = (TextView)this.findViewById(R.id.textView3);
		this.sensorEventListener = new OrientationSensorListener(tv1,tv2,tv3,this);
		
		for(Sensor sensor:sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)){
			sensorManager.registerListener(sensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		for (Sensor sensor : sensorManager
                .getSensorList(Sensor.TYPE_MAGNETIC_FIELD)) {
            sensorManager.registerListener(sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
		
		timer = new Timer();
		
		timer.schedule(new TimerTask(){
						
			public void run(){
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自動生成されたメソッド・スタブ
						Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
						int x = bmp.getWidth();
						int y = bmp.getHeight();
						
						Matrix matrix = new Matrix();
						/*mtrix += 10;
						if(mtrix >360)
							mtrix =0;
						matrix.postRotate(mtrix);
						*/
						if(mtrix!= null)
						matrix.postRotate(mtrix);
						
						bmp = Bitmap.createBitmap(bmp,0,0,x,y,matrix,true);
						iv.setImageBitmap(bmp);
					}
				});
			}
		},0,500);
		}


	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
		
		this.sensorManager.unregisterListener(sensorEventListener);
		this.timer.cancel();
	}

    
}
