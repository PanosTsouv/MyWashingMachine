package com.example.mywashingmachine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private WashingProgramAdapter mAdapter;
    private WashingProgram currentProgram;
    private ProgramProfileDialog myDialog;
    ProgramProfileDialog myAddDialog;
    private ArrayList<WashingProgram> myList;
    ArrayList<WashingProgram> washingPrograms;
    MessageDialog myMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView washingProgramListView = findViewById(R.id.washing_program_list);
        myList = new ArrayList<>();
        if (getIntent() != null && getIntent().getStringExtra("profileTemp") != null) {
            ((TextView) findViewById(R.id.profile_temp)).setText(getIntent().getStringExtra("profileTemp"));
            getIntent().removeExtra("profileTemp");
            ((TextView) findViewById(R.id.profile_spins)).setText(getIntent().getStringExtra("profileSpins"));
            getIntent().removeExtra("profileSpins");
            ((TextView) findViewById(R.id.profile_duration)).setText(getIntent().getStringExtra("profileDuration"));
            getIntent().removeExtra("profileDuration");
            ((TextView) findViewById(R.id.profile_time_value)).setText(getIntent().getStringExtra("profileTimeValue"));
            getIntent().removeExtra("profileTimeValue");
            ((ImageView)findViewById(R.id.door_icon)).setBackgroundResource(getIntent().getIntExtra("icon",4));
            getIntent().removeExtra("icon");
        }
        if (getIntent() != null && getIntent().getParcelableArrayListExtra("Programs") != null) {
            washingPrograms = new ArrayList<>();
            washingPrograms = getIntent().getParcelableArrayListExtra("Programs");
        }
        else
        {
            washingPrograms = new ArrayList<>();
            washingPrograms.add(new WashingProgram("Perfect 20°", 1000, 20, 121, "Επιτρέπει το πλύσιμο βαμβακερών, συνθετικών και σύμμεικτων ρούχων στους 20°", true, true, true));
            washingPrograms.add(new WashingProgram("Coloured 40°", 1000, 40, 121, "Ενδύκνειται για βαμβακερά και καθαρίζει άριστα στους 40° χρωματιστά ρούχα", true, true, true));
            washingPrograms.add(new WashingProgram("Ηygiene 60°", 1000, 60, 96, "Ενδύκνειται για βαμβακερά και απομακρύνει και τους πιο ανθεκτικούς λεκέδες στους 60°", true, true, true));
            washingPrograms.add(new WashingProgram("Perfect Rapid", 1000, 40, 59, "Διατηρεί υψηλή ποιότητα καθαρισμού με μειωμένη διάρκεια πλύσης , για μειωμένο φορτίο", true, true, true));
            washingPrograms.add(new WashingProgram("Μεταξωτά", 800, 30, 48, "Για ρούχα με ετικέτα ΜΟΝΟ ΠΛΥΣΙΜΟ ΣΤΟ ΧΕΡΙ ή ΠΛΥΣΙΜΟ ΣΤΑ ΜΕΤΑΞΩΤΑ", true, true, true));
            washingPrograms.add(new WashingProgram("Μάλλινα", 800, 40, 48, "Για μάλλινα ρούχα ή ρούχα που πλένονται στο χέρι", true, true, true));
            washingPrograms.add(new WashingProgram("Ξέβγαλμα", 1000, 0, 28, "Τρείς φάσεις ξεβγάλματος και μία στυψίματος, κατάλληλο για όλα τα υφάσματα ή πλυμένα στο χέρι", false, true, true));
            washingPrograms.add(new WashingProgram("Στύψιμο", 1000, 0, 10, "Ολοκληρώνει το άδειασμα νερού με στύψιμο στο μέγιστο αριθμό στροφών, αν δεν τις μειώσετε", false, false, true));
            washingPrograms.add(new WashingProgram("Ευαίσθητα", 400, 40, 54, "Ιδανικό για ευαίσθητα ρούχα.Το πλύσιμο γίνεται με πολύ νερό για άριστο καθαρισμό", true, true, true));
            washingPrograms.add(new WashingProgram("Ανάμεικτα", 1000, 40, 95, "Απαλό στύψιμο για μείωση ζαρώματος των ρούχων", true, true, true));
            washingPrograms.add(new WashingProgram("Βαμβακερά", 1400, 40, 306, "Για όχι πολύ βρώμικα βαμβακερά, συνδιάζει καθαριότητα και κατανάλωση νερού", true, true, true));
            washingPrograms.add(new WashingProgram("Λευκά", 1000, 60, 169, "Σχεδιασμένο για πεντακάθαρα ρούχα", true, true, true));
            washingPrograms.add(new WashingProgram("Εύκολο σιδέρωμα", 1000, 30, 75, "Αποκλειστικό πρόγραμμα με ατμό για να ελλατώσει και να ισιώσει τις ζάρες στα ρούχα", true, true, true));
            washingPrograms.add(new WashingProgram("SmartFi+", 400, 90, 131, "Καθαρισμός του κάδου του πλυντηρίου.Χρησιμοποιείται χωρίς φορτίο", false, false, true));
        }

        ImageButton hideMachineState = findViewById(R.id.hide_machine_state);
        ImageButton showMachineState = findViewById(R.id.show_machine_state);
        RelativeLayout machineState = findViewById(R.id.machine_state);

        showMachineState.setVisibility(View.GONE);
        findViewById(R.id.show).setVisibility(View.GONE);

        hideMachineState.setOnClickListener(v -> {
            machineState.setVisibility(View.GONE);
            showMachineState.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams divider = (RelativeLayout.LayoutParams) findViewById(R.id.divider1).getLayoutParams();
            divider.addRule(RelativeLayout.BELOW, R.id.show_machine_state);
            findViewById(R.id.divider1).setLayoutParams(divider);
            findViewById(R.id.divider1).requestLayout();
            findViewById(R.id.show).setVisibility(View.VISIBLE);
        });

        showMachineState.setOnClickListener(v -> {
            machineState.setVisibility(View.VISIBLE);
            showMachineState.setVisibility(View.GONE);
            RelativeLayout.LayoutParams divider = (RelativeLayout.LayoutParams) findViewById(R.id.divider1).getLayoutParams();
            divider.addRule(RelativeLayout.BELOW, R.id.machine_state);
            findViewById(R.id.divider1).setLayoutParams(divider);
            findViewById(R.id.divider1).requestLayout();
            findViewById(R.id.show).setVisibility(View.GONE);
        });

        mAdapter = new WashingProgramAdapter(this, 0, new ArrayList<>());

        washingProgramListView.setAdapter(mAdapter);

        washingProgramListView.setOnItemClickListener((parent, view, position, id) -> {
            currentProgram = mAdapter.getItem(position);
            assert currentProgram != null;

            FragmentManager fm = getSupportFragmentManager();
            myDialog = new ProgramProfileDialog();
            Bundle args = new Bundle();
            args.putString("program", currentProgram.getTitleName());
            args.putString("spins", "" + currentProgram.getSpins());
            args.putString("temp", "" + currentProgram.getTemp());
            args.putString("duration", "" + currentProgram.getDuration());
            args.putString("description", currentProgram.getDescription());
            args.putBoolean("prewash", currentProgram.getHasPreWash());
            args.putBoolean("squeezing", currentProgram.getHasSqeezing());
            args.putBoolean("default", currentProgram.getIsDefaultProgram());
            myDialog.setArguments(args);
            myDialog.show(fm, "fragment_edit_name");
        });

        mAdapter.addAll(washingPrograms);

//        Button backButton = findViewById(R.id.back_button);

        Button startButton = findViewById(R.id.start_button);

        Button addButton = findViewById(R.id.addButton);

//        backButton.setOnClickListener(v -> {
//            Intent openWashingProgramsActivity = new Intent(MainActivity.this, BaseActivity.class);
//            openWashingProgramsActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openWashingProgramsActivity.putParcelableArrayListExtra("Programs", washingPrograms);
//            if(currentProgram != null)
//            {
//                currentProgram.setSelectedImage(4);
//            }
//            startActivity(openWashingProgramsActivity);
//            finish();
//        });
        startButton.setEnabled(false);
        startButton.setOnClickListener(v -> {
            if(!BaseActivity.isProgramRunning) {
                startNewProgram();
            }
            else
            {
                FragmentManager fm = getSupportFragmentManager();
                myMessageDialog = new MessageDialog();
                myMessageDialog.show(fm, "message");
            }
        });

        addButton.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            myAddDialog = new ProgramProfileDialog();
            myAddDialog.show(fm, "fragment_edit_name");
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent openWashingProgramsActivity = new Intent(MainActivity.this, BaseActivity.class);
                openWashingProgramsActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                openWashingProgramsActivity.putParcelableArrayListExtra("Programs", washingPrograms);
                if (currentProgram != null) {
                    currentProgram.setSelectedImage(4);
                }
                startActivity(openWashingProgramsActivity);
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent openWashingProgramsActivity = new Intent(MainActivity.this, BaseActivity.class);
            openWashingProgramsActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            openWashingProgramsActivity.putParcelableArrayListExtra("Programs", washingPrograms);
            if (currentProgram != null) {
                currentProgram.setSelectedImage(4);
            }
            startActivity(openWashingProgramsActivity);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startNewProgram()
    {
        Log.e("TAG", "startNewProgram: " );
        Intent startPrograms = new Intent(MainActivity.this, BaseActivity.class);
        startPrograms.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (currentProgram != null && currentProgram.getSelectedImage() == 0) {
            currentProgram.setSelectedImage(4);
            startPrograms.putExtra("startProgram", currentProgram.getTitleName());
            startPrograms.putExtra("startProgramSpins", currentProgram.getSpins());
            startPrograms.putExtra("startProgramTemp", currentProgram.getTemp());
            startPrograms.putExtra("startProgramDuration", currentProgram.getDuration());
            startPrograms.putExtra("startProgramPreWash", currentProgram.getHasPreWash());
            startPrograms.putExtra("startProgramHasSqeezing", currentProgram.getHasSqeezing());
        }
        startPrograms.putParcelableArrayListExtra("Programs", washingPrograms);
        startActivity(startPrograms);
        finish();
    }

    public void doPositiveClick(){
        EditText duration = myDialog.getBinding().duration;
        if(duration.getText().toString().length() != 0) {
            if(Integer.parseInt(duration.getText().toString()) == 0) {
                currentProgram.setDuration(1);
            }
            else {
                currentProgram.setDuration(Integer.parseInt(duration.getText().toString()));
            }
        }
        else
        {
            currentProgram.setDuration(1);
        }

        AutoCompleteTextView temp = myDialog.getBinding().defaultTemps;
        if(temp.getText().toString().length() == 2)
            currentProgram.setTemp(Integer.parseInt(temp.getText().toString().substring(0,1)));
        else
        {
            currentProgram.setTemp(Integer.parseInt(temp.getText().toString().substring(0,2)));
        }

        AutoCompleteTextView spins = myDialog.getBinding().defaultSpins;
        currentProgram.setSpins(Integer.parseInt(spins.getText().toString()));

        SwitchCompat prewash = myDialog.getBinding().prewashBox;
        currentProgram.setHasPreWash(prewash.isChecked());

        SwitchCompat squeezing = myDialog.getBinding().squeezingBox;
        currentProgram.setHasSqeezing(squeezing.isChecked());

        TextView profileTemp = findViewById(R.id.profile_temp);
        String currentTempAsString = "" + currentProgram.getTemp();
        profileTemp.setText(currentTempAsString);

        TextView profileDuration = findViewById(R.id.profile_duration);
        String currentDurationAsString = "" + currentProgram.getDuration();
        profileDuration.setText(currentDurationAsString);

        TextView profileSpins = findViewById(R.id.profile_spins);
        String currentSpinsAsString = "" + currentProgram.getSpins();
        profileSpins.setText(currentSpinsAsString);

        if (myList.size() == 0) {
            myList.add(0, currentProgram);
            currentProgram.setSelectedImage(View.VISIBLE);
        }else if (myList.size() == 1) {
            myList.add(1, currentProgram);
            myList.get(0).setSelectedImage(View.INVISIBLE);
            currentProgram.setSelectedImage(View.VISIBLE);
        }else if (myList.size() >= 2) {
            myList.add(0, myList.get(1));
            myList.add(1,currentProgram);
            myList.get(0).setSelectedImage(View.INVISIBLE);
            currentProgram.setSelectedImage(View.VISIBLE);
        }

        EditText programTitle = (EditText) myDialog.getBinding().programTitle;
        if(programTitle.getText().toString().length() != 0) {
            currentProgram.setTitleName(programTitle.getText().toString());
        }
        else
        {
            currentProgram.setTitleName("Κανένα Όνομα");
        }

        EditText description = (EditText) myDialog.getBinding().description;
        if(description.getText().toString().length() != 0) {
            currentProgram.setDescription(description.getText().toString());
        }
        else
        {
            currentProgram.setDescription("Καμιά Πληροφορία");
        }

        mAdapter.notifyDataSetChanged();
        myDialog.dismiss();
        findViewById(R.id.start_button).setEnabled(true);
    }

    public void doAddNegativeClick()
    {
        myAddDialog.dismiss();
    }

    public void doNegativeClick()
    {
        myDialog.dismiss();
    }

    public void doAddPositiveClick()
    {
        if(myAddDialog.getBinding().programTitle.getText().toString().equals(""))
            myAddDialog.getBinding().programTitle.setText("Κανένα όνομα");
        if(myAddDialog.getBinding().defaultSpins.getText().toString().trim().equals(""))
            myAddDialog.getBinding().defaultSpins.setText("0");
        if(myAddDialog.getBinding().defaultTemps.getText().toString().trim().equals(""))
            myAddDialog.getBinding().defaultTemps.setText("00°");
        if(myAddDialog.getBinding().duration.getText().toString().equals(""))
            myAddDialog.getBinding().duration.setText("1");
        if(myAddDialog.getBinding().description.getText().toString().equals(""))
            myAddDialog.getBinding().description.setText("Καμιά Πληροφορία");
        WashingProgram temp = new WashingProgram(myAddDialog.getBinding().programTitle.getText().toString(),
                Integer.parseInt(myAddDialog.getBinding().defaultSpins.getText().toString()),
                Integer.parseInt(myAddDialog.getBinding().defaultTemps.getText().toString().substring(0,2)),
                Integer.parseInt(myAddDialog.getBinding().duration.getText().toString()),
                myAddDialog.getBinding().description.getText().toString(), myAddDialog.getBinding().prewashBox.isChecked(),
                myAddDialog.getBinding().squeezingBox.isChecked(), false);
        washingPrograms.add(0, temp);
        mAdapter.clear();
        mAdapter.addAll(washingPrograms);
        myAddDialog.dismiss();
    }

    public void doMessagePositiveClick()
    {
        startNewProgram();
        BaseActivity.endMessageAccepted = true;
        myMessageDialog.dismiss();
    }

    public void doMessageNegativeClick()
    {
        myMessageDialog.dismiss();
    }


}