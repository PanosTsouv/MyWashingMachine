package com.example.mywashingmachine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mywashingmachine.databinding.MessageDialogBinding;

public class MessageDialog extends DialogFragment {

    MessageDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        binding = MessageDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View generalView = binding.getRoot();
        Bundle args = getArguments();
        if(args == null) {
            binding.confirmMessage.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doMessagePositiveClick();
            });
            binding.declineMessage.setOnClickListener(v -> {
                ((MainActivity) getActivity()).doMessageNegativeClick();
            });
        }
        else
        {
            binding.message.setText("Υπάρχει πλύση σε εκτέλεση. Αν απενεργοποιήσετε το πλυντήριο, η πλύση σας θα σταματήσει. Είστε σίγουροι πως θέλετε να απενεργοποιήσετε το πλυντήριο?");
            binding.confirmMessage.setOnClickListener(v -> {
                ((BaseActivity) getActivity()).doMessagePositiveClick();
            });
            binding.declineMessage.setOnClickListener(v -> {
                ((BaseActivity) getActivity()).doMessageNegativeClick();
            });
        }

        builder.setView(generalView);
        return builder.create();
    }

    public MessageDialogBinding getBinding()
    {
        return this.binding;
    }
}
