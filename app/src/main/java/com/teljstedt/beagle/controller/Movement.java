package com.teljstedt.beagle.controller;

import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by sssgne on 2017-04-03.
 */
public class Movement {
    public enum Direction {
        UP,DOWN,LEFT,RIGHT,UNCLEAR
    }

    private float initX;
    private float initY;

    private static float XThresholdPct;
    private static float YThresholdPct;

    public void InitSwipe(MotionEvent event){
        initX =event.getX();
        initY =event.getY();
    }

    public Direction Swipe(MotionEvent event) {
        Direction direction;
        direction=SwipeWhere(event.getX(),event.getY());
        return direction;
    }

    private Direction SwipeWhere(float x, float y) {
        Direction direction=Direction.UNCLEAR;
        float diffXpct = ((initX - x) / initX) * 100;
        float diffYpct = ((initY - y) / initY) * 100;
        float absDiffXpct = Math.abs(diffXpct);
        float absDiffYpct = Math.abs(diffYpct);

        if ( (absDiffXpct > XThresholdPct) && (absDiffYpct<YThresholdPct) ) {
            // Horizontal sweep
            if (diffXpct < (-1 * XThresholdPct ))  { // Left to Right sweep event
                direction=Direction.RIGHT;
            } else if (diffXpct > XThresholdPct ) {  // Right to left sweep event
                direction=Direction.LEFT;
            }
        } else if (absDiffYpct>YThresholdPct && absDiffXpct< XThresholdPct ){
            // vertical sweep
            if (diffYpct > YThresholdPct) {
                // up sweep
                direction=Direction.UP;
            } else {
                // down sweep
                direction=Direction.DOWN;
            }
        } else {
            direction = Direction.UNCLEAR;
        }
        return direction;
    }

    public Movement(int x,int y) {
        XThresholdPct=x;
        YThresholdPct=y;
        Log.d("InitSwipe","XThresholdPct:" + XThresholdPct + ", YThresholdPct:" + YThresholdPct );
    }

}
