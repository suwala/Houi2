package com.example.houi2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class OrientationSensorListener implements SensorEventListener{

	public TextView tv1,tv2,tv3;
	private float[] accelerometerValues; // 加速度センサーの値    // (1)
	private float[] magneticFieldValues; // 磁気センサーの値    // (2)
	private MainActivity m;


	public OrientationSensorListener(TextView tv,TextView tv2,TextView tv3,MainActivity d){
		this.tv1 = tv;
		this.tv2 = tv2;
		this.tv3 = tv3;
		this.m = d;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] orientationValues = new float[3];

		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			this.accelerometerValues = this.clone(event.values);
		}

		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			this.magneticFieldValues = this.clone(event.values);
		}


		if(this.accelerometerValues != null && this.magneticFieldValues != null){

			float[] inR = new float[16];
			float[] outR = new float[16];

			SensorManager.getRotationMatrix(inR, null, accelerometerValues, this.magneticFieldValues);
			SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
			SensorManager.getOrientation(outR, orientationValues);

			Float x = orientationValues[0];
			Float y = orientationValues[1];
			Float z = orientationValues[2];

			//ラジアン
			tv1.setText("X="+orientationValues[0]+" Y="+orientationValues[1]+" Z="+orientationValues[2]);

			
		//度
		tv2.setText("X="+(int)this.toOrientationDegrees(x)+" Y="+this.toOrientationDegrees(y)+" Z="+this.toOrientationDegrees(z));
		this.m.mtrix = this.toOrientationDegrees(x);
		//方位
		tv3.setText(""+(int)this.toOrientationDegrees(x));
		tv3.setTextSize(40); 
		
		

		}
	}

	//配列のコピー
	public float[] clone(float[] source){
		float[] target = new float[source.length];
		for(int i = 0;i < target.length;i++){
			target[i] = source[i];
		}
		return target;
	}

	//度の表示
	public float toOrientationDegrees(float rad){
		return (float)(rad >= 0 ? Math.toDegrees(rad):360+Math.toDegrees(rad));
	}

	/*
	 * 真北、真東、真南、真西がそれぞれ 0, π/2, -π, -π/2 なので、北、東、南、西のそ
れぞれの範囲は、以下のようになる。
北    -π/4 ～ π/4
東     π/4 ～  3π/4
南    -π   ～ -3π/4, 3π/4 ～ π
西    -π/4 ～ π/4
	 */
	//方位の表示
	public String toOrientationsString(float azimuth){
		double[] ORIENTATON_RANGE ={
				-(Math.PI *3 /4),//南
				-(Math.PI *1 /4),
				+(Math.PI *1 /4),
				+(Math.PI *3 /4),
		};

		for(int i=0;i<ORIENTATON_RANGE.length;i++){
			if(azimuth < ORIENTATON_RANGE[i]){
				return String.valueOf(ORIENTATON_RANGE[i]);
			}
		}

		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ

	}


}
