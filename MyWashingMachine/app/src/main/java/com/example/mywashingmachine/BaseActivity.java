package com.example.mywashingmachine;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class BaseActivity extends AppCompatActivity {

    Thread t;
    Task m1;
    Handler handler1;
    ArrayList<WashingProgram> temp;
    boolean isPause = false;
    boolean machineON = false;
    public static volatile boolean isProgramRunning = false;
    SettingsDialogFragment myTimeDialog;
    boolean endRunnableRun = true;
    MessageDialog myMessageDialog;
    static boolean endMessageAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("TAG", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        ImageButton hideMachineState = findViewById(R.id.hide_machine_state);
        ImageButton showMachineState = findViewById(R.id.show_machine_state);
        RelativeLayout machineState = findViewById(R.id.machine_state);

        showMachineState.setVisibility(View.GONE);
        findViewById(R.id.show).setVisibility(View.GONE);

        findViewById(R.id.container).setVisibility(View.INVISIBLE);

        hideMachineState.setOnClickListener(v -> {
            machineState.setVisibility(View.GONE);
            showMachineState.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams divider = (RelativeLayout.LayoutParams) findViewById(R.id.divider2).getLayoutParams();
            divider.addRule(RelativeLayout.BELOW, R.id.show_machine_state);
            findViewById(R.id.divider2).setLayoutParams(divider);
            findViewById(R.id.divider2).requestLayout();
            findViewById(R.id.show).setVisibility(View.VISIBLE);
        });

        showMachineState.setOnClickListener(v -> {
            machineState.setVisibility(View.VISIBLE);
            showMachineState.setVisibility(View.GONE);
            RelativeLayout.LayoutParams divider = (RelativeLayout.LayoutParams) findViewById(R.id.divider2).getLayoutParams();
            divider.addRule(RelativeLayout.BELOW, R.id.machine_state);
            findViewById(R.id.divider2).setLayoutParams(divider);
            findViewById(R.id.divider2).requestLayout();
            findViewById(R.id.show).setVisibility(View.GONE);
        });

        Button washingPrograms = findViewById(R.id.washing_programs);

//        Button stop = findViewById(R.id.cancel_button);
//        stop.setOnClickListener(v -> {
//            m1.terminate();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        findViewById(R.id.accept).setOnClickListener(v -> {
            endMessageAccepted = true;
        });

        findViewById(R.id.pause_button).setOnClickListener(v -> {
            if(isPause)
            {
                ((Button)findViewById(R.id.pause_button)).setText("ΠΑΥΣΗ");
                isPause = false;
            }
            else
            {
                ((Button)findViewById(R.id.pause_button)).setText("ΕΚΚΙΝΗΣΗ");
                isPause = true;
            }
        });

        washingPrograms.setOnClickListener(v -> {
            if(machineON) {
                Intent openWashingProgramsActivity = new Intent(BaseActivity.this, MainActivity.class);
                Bundle args = new Bundle();
                args.putString("profileTemp", ((TextView)findViewById(R.id.profile_temp)).getText().toString());
                args.putString("profileDuration", ((TextView)findViewById(R.id.profile_duration)).getText().toString());
                args.putString("profileSpins", ((TextView)findViewById(R.id.profile_spins)).getText().toString());
                args.putString("profileTimeValue", ((TextView)findViewById(R.id.profile_time_value)).getText().toString());
                if(isProgramRunning) {
                    args.putInt("icon", R.drawable.lock_v2);
                }
                else
                {
                    args.putInt("icon", R.drawable.unlock_24);
                }
                if (temp != null) {
                    args.putParcelableArrayList("Programs", temp);
                }
                openWashingProgramsActivity.putExtras(args);
                startActivity(openWashingProgramsActivity);
            }
        });

        findViewById(R.id.time_settings).setOnClickListener(v -> {
            if(machineON) {
                FragmentManager fm = getSupportFragmentManager();
                myTimeDialog = new SettingsDialogFragment();
                myTimeDialog.show(fm, "fragment_time");
            }
        });

        findViewById(R.id.washing_start).setOnClickListener(v -> {
            if(!machineON) {
                findViewById(R.id.left_info).setVisibility(View.VISIBLE);
                findViewById(R.id.settings).setVisibility(View.VISIBLE);
                findViewById(R.id.container).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.washing_start)).setText("ΑΠΕΝΕΡΓΟΠΟΙΗΣΗ ΠΛΥΝΤΗΡΙΟΥ");
                ((Button)findViewById(R.id.washing_programs)).setEnabled(true);
                ((Button)findViewById(R.id.time_settings)).setEnabled(true);
                ((Button)findViewById(R.id.washing_start)).setTextColor(Color.parseColor("#D2222D"));
                machineON = true;
            }
            else
            {
                if(isProgramRunning) {
                    FragmentManager fm = getSupportFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("Parent", "BaseActivity");
                    myMessageDialog = new MessageDialog();
                    myMessageDialog.setArguments(args);
                    myMessageDialog.show(fm, "disc_message");
                }
                else
                {
                    machineDisconnect();
                }
            }
        });
    }

    public void doNegativeClick()
    {
        myTimeDialog.dismiss();
    }

    public void doPositiveClick()
    {
        String startTime = String.valueOf(myTimeDialog.getBinding().timePicker.getHour());
        String startMin = String.valueOf(myTimeDialog.getBinding().timePicker.getMinute());
        if(startTime.length() == 1) startTime = "0" + startTime;
        if(startMin.length() == 1) startMin = "0" + startMin;
        ((TextView)findViewById(R.id.profile_time_value)).setText(startTime + ":" + startMin);
        myTimeDialog.dismiss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e("TAG", "onNewIntent: " );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG", "start");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("TAG", "resume");
        if (getIntent() != null && getIntent().getParcelableArrayListExtra("Programs") != null) {
            temp = getIntent().getParcelableArrayListExtra("Programs");
            getIntent().removeExtra("Programs");
        }

        if (getIntent() != null && getIntent().getStringExtra("startProgram") != null) {
            if(isProgramRunning)
            {
                endRunnableRun = false;
                findViewById(R.id.pause_button).setVisibility(View.INVISIBLE);
                if(m1 != null && t != null) {
                    m1.terminate(false);
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            isProgramRunning = true;

            int programDuration = getIntent().getExtras().getInt("startProgramDuration");
            boolean hasPreWash = getIntent().getExtras().getBoolean("startProgramPreWash");
            boolean hasSqeezing = getIntent().getExtras().getBoolean("startProgramHasSqeezing");
            long prewash = 0;
            long sqeezing = 0;
            if(hasPreWash)
            {
                prewash = (int)Math.ceil((double)programDuration/4);
                programDuration += prewash;
            }
            if(hasSqeezing)
            {
                sqeezing = (int)Math.ceil((double)programDuration/15);
                programDuration += sqeezing;
            }

            int maxDurationInMilliSec = programDuration*60*1000;
            ((ProgressBar)findViewById(R.id.circularProgressBar)).setMax(maxDurationInMilliSec-1000);
            handler1 = new Handler();
            m1= new Task(0, maxDurationInMilliSec, prewash*60*1000, sqeezing*60*1000);
            t = new Thread(m1);   //call it
            t.start();

            TextView profileTemp = findViewById(R.id.profile_temp);
            String currentTempAsString = "" + getIntent().getExtras().getInt("startProgramTemp");
            profileTemp.setText(currentTempAsString);

            TextView profileSpins = findViewById(R.id.profile_spins);
            String currentSpinsAsString = "" + getIntent().getExtras().getInt("startProgramSpins");
            profileSpins.setText(currentSpinsAsString);

            TextView profileDuration = findViewById(R.id.profile_duration);
            String currentDurationAsString = "" + getIntent().getExtras().getInt("startProgramDuration");
            profileDuration.setText(currentDurationAsString);

            ((TextView)findViewById(R.id.textView2)).setText(getIntent().getExtras().getString("startProgram"));
            ((TextView) findViewById(R.id.time_value)).setText(covertTimeFormat(maxDurationInMilliSec));
        }
    }

    private String covertTimeFormat(long timeLeft)
    {
        int timeleftInSec = (int)(timeLeft/1000);
        int timeleftInMin = (int)(timeleftInSec/60);
        int timeleftInHour = (int)(timeleftInMin/60);

        String timeStringLeftInSec = "" + (timeleftInSec - ((timeleftInMin - timeleftInHour*60)*60) - timeleftInHour*60*60);
        if (timeStringLeftInSec.length() == 1)
            timeStringLeftInSec = "0" + (timeleftInSec - ((timeleftInMin - timeleftInHour*60)*60) - timeleftInHour*60*60);

        String timeStringLeftInMin = "" + (timeleftInMin - timeleftInHour*60);
        if (timeStringLeftInMin.length() == 1)
            timeStringLeftInMin = "0" + (timeleftInMin - timeleftInHour*60);

        String timeStringLeftInHour = "" + timeleftInHour;
        if (timeStringLeftInHour.length() == 1)
            timeStringLeftInHour = "0" + timeleftInHour;

        return timeStringLeftInHour + ":" + timeStringLeftInMin + ":" + timeStringLeftInSec;
    }

    class Task implements Runnable {
        boolean running = true;
        long start,max;
        long pauseStart = 0;
        long pauseEnd = 0;
        long prewash = 0;
        long sqeezing = 0;
        boolean pause = false;

        Task(long a, long b, long prewash , long sqeezing) { start = a; max = b; this.prewash = prewash; this.sqeezing = sqeezing;}

        public void terminate(boolean changeViewsWithEnd) {
            running = false;
            endRunnableRun = changeViewsWithEnd;
        }

        public void pause()
        {
            if(isPause)
            {
                pauseStart = System.currentTimeMillis();
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.door_icon).setBackgroundResource(R.drawable.unlock_24);
                    }
                });
            }
            while(isPause && running){
                pause = true;
            }
            if(pause)
            {
                pauseEnd = System.currentTimeMillis();
                start += pauseEnd - pauseStart;
                pause = false;
                if(!running)
                {
                    isPause = false;
                    BaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((Button)findViewById(R.id.pause_button)).setText("ΠΑΥΣΗ");
                        }
                    });
                }
                else
                {
                    BaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.door_icon).setBackgroundResource(R.drawable.lock_v2);
                        }
                    });
                }
            }

        }

        public void waitStartTime()
        {
            if(((TextView)findViewById(R.id.profile_time_value)).getText().equals("- - : - -"))
            {
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.pause_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.door_icon).setBackgroundResource(R.drawable.lock_v2);
                    }
                });
                return;
            }

            Calendar rightNow = Calendar.getInstance();
            String currentHourIn24Format = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
            String currentMin = String.valueOf(rightNow.get(Calendar.MINUTE));

            if(currentHourIn24Format.length() == 1) currentHourIn24Format = "0" + currentHourIn24Format;
            if(currentMin.length() == 1) currentMin = "0" + currentMin;

            String currentTime = currentHourIn24Format + ":" + currentMin;

            while(!currentTime.equals((String) ((TextView)findViewById(R.id.profile_time_value)).getText()) && running)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rightNow = Calendar.getInstance();
                currentHourIn24Format = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
                currentMin = String.valueOf(rightNow.get(Calendar.MINUTE));
                if(currentHourIn24Format.length() == 1) currentHourIn24Format = "0" + currentHourIn24Format;
                if(currentMin.length() == 1) currentMin = "0" + currentMin;
                currentTime = currentHourIn24Format + ":" + currentMin;
                Log.e("Time", currentTime );
            }
            if(!running && isPause)
            {
                isPause = false;
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button)findViewById(R.id.pause_button)).setText("ΠΑΥΣΗ");
                    }
                });
            }
            if(running) {
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.pause_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.door_icon).setBackgroundResource(R.drawable.lock_v2);
                    }
                });
            }
        }

        @Override
        public void run() {
            waitStartTime();
            start = System.currentTimeMillis();
            endMessageAccepted = false;
            while((System.currentTimeMillis() - start) < max && running){
                pause();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("value", "" + (System.currentTimeMillis() - start));
                Log.e("max", "" + max);
                final long value = (System.currentTimeMillis()  - start);
                long timeleft = max - (System.currentTimeMillis()  - start);
                String timeleftString = covertTimeFormat(timeleft);
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        if(running && (System.currentTimeMillis() - start) < max) {
                            ((TextView)findViewById(R.id.profile_time_value)).setText("- -" + " : " + "- -");
                            ((ProgressBar) findViewById(R.id.circularProgressBar)).setProgress((int) value);
                            ((TextView) findViewById(R.id.time_value)).setText(timeleftString);
                            if( value < prewash ) {
                                ((TextView) findViewById(R.id.stage_value)).setText("Πρόπλυση");
                            }
                            else if( value < max - sqeezing ) {
                                ((TextView) findViewById(R.id.stage_value)).setText("Πλύση");
                            }
                            else if( value < max) {
                                ((TextView) findViewById(R.id.stage_value)).setText("Στύψιμο");
                            }
                        }
                        //Log.e("TAG", "Run " + ((TextView) findViewById(R.id.textView2)).getText());
                    }
                });
            }
            if(running)
                ((ProgressBar)findViewById(R.id.circularProgressBar)).setProgress((int)max);
            Log.e("valueout", "" + (System.currentTimeMillis() - start));
            Log.e("maxout", "" + max);
            if((System.currentTimeMillis() - start) >= max)
            {
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.text).setVisibility(View.VISIBLE);
                        findViewById(R.id.accept).setVisibility(View.VISIBLE);
                        findViewById(R.id.textView2).setVisibility(View.INVISIBLE);
                        findViewById(R.id.stage_container).setVisibility(View.INVISIBLE);
                        findViewById(R.id.time_container).setVisibility(View.INVISIBLE);
                        findViewById(R.id.pause_button).setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("mphka", "" + endMessageAccepted);
                while(!endMessageAccepted){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                        findViewById(R.id.stage_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.time_container).setVisibility(View.VISIBLE);
                        findViewById(R.id.pause_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.text).setVisibility(View.INVISIBLE);
                        findViewById(R.id.accept).setVisibility(View.INVISIBLE);
                    }
                });
                endMessageAccepted = false;
                endRunnableRun = true;
            }
            if(endRunnableRun)
            {
                BaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearRunProgram();
                    }
                });
            }
            ((ProgressBar) findViewById(R.id.circularProgressBar)).setProgress(0);
            ((ProgressBar) findViewById(R.id.circularProgressBar)).setMax(0);
            isProgramRunning = false;
        }
    }

    public void clearRunProgram()
    {
        if(isPause)
        {
            isPause = false;
            ((Button)findViewById(R.id.pause_button)).setText("ΠΑΥΣΗ");
        }
        ((TextView) findViewById(R.id.time_value)).setText("--:--");
        ((TextView) findViewById(R.id.stage_value)).setText("-");
        ((TextView) findViewById(R.id.textView2)).setText("Κανένα Πρόγραμμα");
        findViewById(R.id.pause_button).setVisibility(View.GONE);
        findViewById(R.id.door_icon).setBackgroundResource(R.drawable.unlock_24);
        ((TextView) findViewById(R.id.profile_duration)).setText("0");
        ((TextView) findViewById(R.id.profile_spins)).setText("0");
        ((TextView) findViewById(R.id.profile_temp)).setText("0");
    }

    public void doMessagePositiveClick()
    {
        machineDisconnect();
        myMessageDialog.dismiss();
    }

    public void machineDisconnect()
    {
        if(m1 != null && t != null) {
            m1.terminate(true);
            endMessageAccepted = true;
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        findViewById(R.id.left_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.settings).setVisibility(View.INVISIBLE);
        findViewById(R.id.container).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.washing_start)).setText("ΕΝΕΡΓΟΠΟΙΗΣΗ ΠΛΥΝΤΗΡΙΟΥ");
        ((TextView)findViewById(R.id.profile_time_value)).setText("- - : - -");
        ((Button)findViewById(R.id.washing_programs)).setEnabled(false);
        ((Button)findViewById(R.id.time_settings)).setEnabled(false);
        ((Button)findViewById(R.id.washing_start)).setTextColor(Color.parseColor("#238823"));
        machineON = false;
    }

    public void doMessageNegativeClick()
    {
        myMessageDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "stop");
        if (this.getIntent().getExtras() != null) {
            this.getIntent().removeExtra("startProgram");
            this.getIntent().removeExtra("startProgramSpins");
            this.getIntent().removeExtra("startProgramTemp");
            this.getIntent().removeExtra("startProgramDuration");
            this.getIntent().removeExtra("startProgramPreWash");
            this.getIntent().removeExtra("startProgramHasSqeezing");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "pause");
    }
}
