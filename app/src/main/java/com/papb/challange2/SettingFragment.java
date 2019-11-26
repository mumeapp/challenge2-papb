package com.papb.challange2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private EditText interval;
    private EditText jackpotNum;
    private Button saveButton;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        interval = view.findViewById(R.id.interval);
        jackpotNum = view.findViewById(R.id.jackpot_num);
        saveButton = view.findViewById(R.id.save_button);


        saveButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getActivity().getApplication().toString(), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putInt("INTERVAL", Integer.parseInt(interval.getText().toString()));
            editor.putInt("JACKPOT_NUM", Integer.parseInt(jackpotNum.getText().toString()));
            editor.apply();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void initializeUI() {

    }

}
