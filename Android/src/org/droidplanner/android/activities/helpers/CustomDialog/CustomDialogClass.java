package org.droidplanner.android.activities.helpers.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.droidplanner.android.R;
import org.droidplanner.android.activities.interfaces.ControlCallBackListener;

/**
 * Created by Borhan Uddin on 5/3/2018.
 */

public class CustomDialogClass extends Dialog implements  android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button yes;
    public Spinner spinner;
    private static final String[]paths = {"LEFT", "RIGHT"};
    private ControlCallBackListener controlCallBackListener;
    private     int leftorright=0;
    private EditText pwm, time;
    public CustomDialogClass(Activity a, ControlCallBackListener controlCallBackListener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.controlCallBackListener = controlCallBackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.control_dialog_view);
        spinner = (Spinner) findViewById(R.id.leftorright);
        pwm=(EditText)findViewById(R.id.pwm_value);
        time=(EditText)findViewById(R.id.time_value);
        yes = (Button) findViewById(R.id.btn);
        yes.setOnClickListener(this);
        spinner = (Spinner)findViewById(R.id.leftorright);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(c,android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        leftorright = 1;
                        break;
                    case 1:
                        leftorright = 2;
                        break;

                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
}