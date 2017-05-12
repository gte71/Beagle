package com.teljstedt.beagle.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import com.teljstedt.beagle.R;
import com.teljstedt.beagle.controller.Movement;
import com.teljstedt.beagle.model.WeatherData;

public class MainActivity extends Activity {
    Movement movement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movement=new Movement( getResources().getInteger(R.integer.XThresholdPct), getResources().getInteger(R.integer.YThresholdPct));
        WeatherData weatherData = new WeatherData(this, getResources().getString(R.string.longitude) , getResources().getString(R.string.latitude) );


    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                movement.InitSwipe(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
                switch (movement.Swipe(event)) {
                    case LEFT:
                        Log.i("MotionEvent", "go Right Activity");
                        goQuiz();
                        break;
                    case RIGHT:
                        Log.i("MotionEvent", "go Left Activity");
                        break;
                    case UNCLEAR:
                        Log.i("MotionEvent", "Unclear movement");
                    default:
                        break;
                }
            }
        }

        return false;
    } // onTouchEvent
*/

}
