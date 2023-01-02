package com.example.mywashingmachine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WashingProgramAdapter extends ArrayAdapter<WashingProgram> {
    public WashingProgramAdapter(@NonNull Context context, int resource, ArrayList<WashingProgram> washingPrograms) {
        super(context, resource, washingPrograms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(convertView == null)
        {
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }

        WashingProgram current = getItem(position);

        TextView washingName = listItemView.findViewById(R.id.washing_program_name);
        assert current != null;
        washingName.setText(current.getTitleName());

        TextView description = listItemView.findViewById(R.id.washing_program_info);
        assert current != null;
        description.setText(current.getDescription());

        ImageView selectedItem = listItemView.findViewById(R.id.selected_image);
        selectedItem.setVisibility(current.getSelectedImage());

        return listItemView;
    }
}
