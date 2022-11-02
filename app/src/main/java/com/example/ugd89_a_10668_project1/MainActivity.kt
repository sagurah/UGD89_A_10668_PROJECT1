package com.example.ugd89_a_10668_project1

import android.annotation.SuppressLint
import android.hardware.Camera
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import java.lang.Exception

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import android.os.Build
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if(event.sensor.type == Sensor.TYPE_PROXIMITY){
                if(event.values[0] == 0f){
//                    near
                    mCamera?.stopPreview()
                    mCamera?.release()
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                    mCameraView = CameraView(this@MainActivity, mCamera!!)
                    val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                    camera_view.addView(mCameraView)
                }else{
//                    away
                    mCamera?.stopPreview()
                    mCamera?.release()
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
                    mCameraView = CameraView(this@MainActivity, mCamera!!)
                    val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                    camera_view.addView(mCameraView)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        //Proximity
        if(proximitySensor == null){
            Toast.makeText(this,"No Proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        //Camera
        try{
            mCamera = Camera.open()
        }catch (e: Exception){
            Log.d("Error", "Failed to get Camera" + e.message)
        }
        if(mCamera != null){
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{ view: View? -> System.exit(0) }

        // Accelerometer
    }
}