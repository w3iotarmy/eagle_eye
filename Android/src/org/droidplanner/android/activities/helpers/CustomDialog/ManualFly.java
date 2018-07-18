package org.droidplanner.android.activities.helpers.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.droidplanner.android.R;
import org.droidplanner.android.activities.interfaces.ControlCallBackListener;

/**
 * Created by Borhan Uddin on 7/18/2018.
 */
public class ManualFly  extends Dialog implements  android.view.View.OnClickListener {
    public Activity c;
    private String TAG = "borhan ManualFly";
    public Dialog d;
    public Button yes;
    public Spinner spinner;
    private static final String[]paths = {"LEFT", "RIGHT"};
    private ControlCallBackListener controlCallBackListener;
    private     int leftorright=0;
    private EditText pwm, time;
    private int pitchBar=0,pitchMax=85,pitchMin=35;
    private int rollBar=0,rollhMax=85,rollMin=35;
    private int throttleBar=0,throttleMax=85,throttleMin=35;
    private int yawBar=0,yawMax=85,yawMin=35;
    private int radio5Bar=0,radio5Max=85,radio5Min=35;
    private int radio6Bar=0,radio6Max=85,radio6Min=35;
    private int radio7Bar=0,radio7Max=85,radio7Min=35;
    private int radio8Bar=0,radio8Max=85,radio8Min=35;
    private int middleValue =60;

    private Button stabilize, loiter, rtl, land,armed,altHold,failsave,auto;
    private int flightModeStabilize=35, flightModeLoiyter=44,flightModeRtl=52,flightModeLand=60,flightModeAltHold=68,flightModeAuto=77,flightModeFailsSave=30;
    DiscreteSeekBar discreteSeekBarPitch, discreteSeekBarRoll, discreteSeekBarThrottle,discreteSeekBarYaw,discreteSeekBarRadio5,discreteSeekBarRadio6,discreteSeekBarRadio7,discreteSeekBarRadio8;
    public ManualFly(Activity a, ControlCallBackListener controlCallBackListener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.controlCallBackListener = controlCallBackListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.manual_oparate_dialog);
        discreteSeekBarPitch = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar);
        discreteSeekBarRoll = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar2);
        discreteSeekBarThrottle = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar3);
        discreteSeekBarYaw = (DiscreteSeekBar)findViewById(R.id.simpleSeekBar4);
        discreteSeekBarRadio5  = (DiscreteSeekBar)findViewById(R.id.simpleSeekBarRadio5);
        discreteSeekBarRadio6  = (DiscreteSeekBar)findViewById(R.id.simpleSeekBarRadio6);
        discreteSeekBarRadio7 = (DiscreteSeekBar)findViewById(R.id.simpleSeekBarRadio7);
        discreteSeekBarRadio8 = (DiscreteSeekBar)findViewById(R.id.simpleSeekBarRadio8);

        discreteSeekBarPitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        discreteSeekBarPitch.setProgress(middleValue);
                        break;
                }
                return false;
            }
        });
        discreteSeekBarRoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        discreteSeekBarRoll.setProgress(middleValue);
                        break;
                }
                return false;
            }
        });
        discreteSeekBarThrottle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        //discreteSeekBarThrottle.setProgress(middleValue);
                        break;
                }
                return false;
            }
        });
        discreteSeekBarYaw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        discreteSeekBarYaw.setProgress(middleValue);
                        break;
                }
                return false;
            }
        });
        discreteSeekBarPitch.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarRoll.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarThrottle.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarYaw.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG, "Drone "+value);

                return value;
            }
        });
        discreteSeekBarRadio5.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarRadio6.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarRadio7.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        discreteSeekBarRadio8.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d(TAG,String.valueOf(value));

                return value;
            }
        });
        initComponents();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                int pwm_value=Integer.parseInt(pwm.getText().toString());
                int time_value=Integer.parseInt(time.getText().toString());
                controlCallBackListener.back(leftorright,pwm_value,time_value);
                break;

            default:
                break;
        }
        dismiss();
    }
    public void initComponents()
    {
        //Max Min value
        discreteSeekBarPitch.setMax(pitchMax);
        discreteSeekBarPitch.setMin(pitchMin);
        discreteSeekBarRoll.setMax(rollhMax);
        discreteSeekBarRoll.setMin(rollMin);
        discreteSeekBarThrottle.setMax(throttleMax);
        discreteSeekBarThrottle.setMin(throttleMin);
        discreteSeekBarYaw.setMax(yawMax);
        discreteSeekBarYaw.setMin(yawMin);
        discreteSeekBarRadio5.setMax(yawMax);
        discreteSeekBarRadio5.setMin(yawMin);
        discreteSeekBarRadio6.setMax(yawMax);
        discreteSeekBarRadio6.setMin(yawMin);
        discreteSeekBarRadio7.setMax(yawMax);
        discreteSeekBarRadio7.setMin(yawMin);
        discreteSeekBarRadio8.setMax(yawMax);
        discreteSeekBarRadio8.setMin(yawMin);
        discreteSeekBarYaw.setMin(yawMin);
        discreteSeekBarYaw.setMax(yawMax);
        discreteSeekBarYaw.setMin(yawMin);
        //set Default value progress
        discreteSeekBarPitch.setProgress(middleValue);
        discreteSeekBarRoll.setProgress(middleValue);
        discreteSeekBarThrottle.setProgress(throttleMin);
        discreteSeekBarYaw.setProgress(middleValue);
    }
}
