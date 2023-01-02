package com.example.mywashingmachine;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mywashingmachine.databinding.ProgramInfoDialogBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class ProgramProfileDialog extends DialogFragment {

    ProgramInfoDialogBinding binding;
    Toast preWashToast;
    Toast sqeezingToast;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        binding = ProgramInfoDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View generalView = binding.getRoot();
        ArrayList<String> items = new ArrayList<>(Arrays.asList("10°","20°","30°","40°","50°","60°","70°","80°","90°"));
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        Bundle args = getArguments();
        ArrayList<String> spin_items = new ArrayList<>(Arrays.asList("400","800","900","1000","1100","1200","1300","1400"));
        ArrayAdapter<String> spin_adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spin_items);

        binding.editNameButton.setOnClickListener(v -> {
            int count = 0;
            while(count < 2) {
                binding.programTitle.requestFocus();
                binding.programTitle.setFocusable(true);
                binding.programTitle.setFocusableInTouchMode(true);
                count++;
            }
            binding.programTitle.setSingleLine(true);
            binding.programTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.programTitle.setSelection(binding.programTitle.getText().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput( binding.programTitle, InputMethodManager.SHOW_IMPLICIT);
        });

        binding.programTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
//                    binding.programTitle.setFocusable(false);
//                    binding.programTitle.setFocusableInTouchMode(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.programTitle.getWindowToken(), 0);
                }
            }
        });

        binding.editDurationButton.setOnClickListener(v -> {
            int count = 0;
            while(count < 2) {
                binding.duration.requestFocus();
                binding.duration.setFocusable(true);
                binding.duration.setFocusableInTouchMode(true);
                count++;
            }
            binding.duration.setSelection(binding.duration.getText().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput( binding.duration, InputMethodManager.SHOW_IMPLICIT);
        });

        binding.duration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
//                    binding.duration.setFocusable(false);
//                    binding.duration.setFocusableInTouchMode(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.duration.getWindowToken(), 0);
                }
            }
        });

        binding.editDescriptionButton.setOnClickListener(v -> {
            int count = 0;
            while(count < 2) {
                binding.description.requestFocus();
                binding.description.setFocusable(true);
                binding.description.setFocusableInTouchMode(true);
                count++;
            }
            binding.description.setSelection(binding.description.getText().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput( binding.description, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
//                    binding.description.setFocusable(false);
//                    binding.description.setFocusableInTouchMode(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.description.getWindowToken(), 0);
                }
            }
        });

        if(args != null) {
            binding.defaultSpins.setText(args.getString("spins"));

            binding.defaultTemps.setText(args.getString("temp") + "°");

            binding.duration.setText(args.getString("duration"));

            binding.prewashBox.setChecked(args.getBoolean("prewash"));

            binding.squeezingBox.setChecked(args.getBoolean("squeezing"));

            EditText program = generalView.findViewById(R.id.program_title);
            program.setText(args.getString("program"));

            TextView description = generalView.findViewById(R.id.description);
            description.setText(args.getString("description"));

            if(args.getBoolean("default"))
            {
                binding.editNameButton.setVisibility(View.INVISIBLE);
                binding.programTitle.setEnabled(false);
                Toast toast = Toast.makeText(getContext(), "Δεν μπορείτε να ενεργοποιήσετε την πρόπλυση σε αυτή την πλύση", Toast.LENGTH_LONG);
                Toast toast1 = Toast.makeText(getContext(), "Δεν μπορείτε να ενεργοποιήσετε το στύψιμο σε αυτή την πλύση", Toast.LENGTH_LONG);
                setToast(toast, toast1);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(22);
                ViewGroup group1 = (ViewGroup) toast1.getView();
                TextView messageTextView1 = (TextView) group1.getChildAt(0);
                messageTextView1.setTextSize(22);
                if(args.getString("program").equals("Στύψιμο"))
                {
                    binding.prewashBox.setOnClickListener(v -> {
                        toast1.cancel();
                        toast.show();
                        binding.prewashBox.setChecked(false);
                    });
                    binding.squeezingBox.setOnClickListener(v -> {
                        toast.cancel();
                        toast1.show();
                        binding.squeezingBox.setChecked(false);
                    });
                }

                if(args.getString("program").equals("Ξέβγαλμα"))
                {
                    binding.prewashBox.setOnClickListener(v -> {
                        toast.show();
                        binding.prewashBox.setChecked(false);
                    });
                }

                if(args.getString("program").equals("SmartFi+"))
                {
                    binding.prewashBox.setOnClickListener(v -> {
                        toast1.cancel();
                        toast.show();
                        binding.prewashBox.setChecked(false);
                    });
                    binding.squeezingBox.setOnClickListener(v -> {
                        toast.cancel();
                        toast1.show();
                        binding.squeezingBox.setChecked(false);
                    });
                }
            }
        }

        Button choiceButton = generalView.findViewById(R.id.choice);
        Button cancelButton = generalView.findViewById(R.id.cancel);
        if(args == null)
        {
            choiceButton.setText("ΠΡΟΣΘΗΚΗ");
        }

        if(args != null) {
            choiceButton.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doPositiveClick();
            });
            cancelButton.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doNegativeClick();
            });

        }
        else
        {
            binding.defaultSpins.setText(" ");
            binding.defaultTemps.setText(" ");
            choiceButton.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doAddPositiveClick();
            });
            cancelButton.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doAddNegativeClick();
            });
        }
        binding.defaultSpins.setAdapter(spin_adapt);
        binding.defaultTemps.setAdapter(adapt);

        builder.setView(generalView);
        return builder.create();
    }

    public void setToast(Toast toast, Toast toast1)
    {
        preWashToast = toast;
        sqeezingToast = toast1;
    }

    public void canceltoast()
    {
        if(preWashToast != null) {
            preWashToast.cancel();
        }
        if(sqeezingToast != null) {
            sqeezingToast.cancel();
        }
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onResume();
    }

    public ProgramInfoDialogBinding getBinding()
    {
        return this.binding;
    }

    @Override
    public void onDestroyView() {
        canceltoast();
        super.onDestroyView();
        binding = null;
    }
}
