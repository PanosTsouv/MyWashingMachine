package com.example.mywashingmachine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mywashingmachine.databinding.ProgramInfoDialogBinding;
import com.example.mywashingmachine.databinding.SettingsDialogBinding;

public class SettingsDialogFragment extends DialogFragment {

    SettingsDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        binding = SettingsDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View generalView = binding.getRoot();
        binding.timePicker.setIs24HourView(true);

        binding.chooseTime.setOnClickListener(v -> {
            ((BaseActivity) getActivity()).doPositiveClick();
        });
        binding.cancelTime.setOnClickListener(v -> {
            ((BaseActivity) getActivity()).doNegativeClick();
        });

        builder.setView(generalView);
        return builder.create();
    }

    public SettingsDialogBinding getBinding()
    {
        return this.binding;
    }
}
