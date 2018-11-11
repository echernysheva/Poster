package com.waxvas.poster;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class PostingFragment extends Fragment {

    Button send;
    Switch swFB, swVK;
    TextView mes;

    public void Sync(){
        send.setEnabled((swFB.isChecked()||swVK.isChecked()) && mes.length()>0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posting, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        send = (Button) view.findViewById(R.id.buttonSend);
        swFB = (Switch) view.findViewById(R.id.switchFB);
        swVK = (Switch) view.findViewById(R.id.switchVK);
        mes = (TextView) view.findViewById(R.id.messageText);

        // обработка изменения текста в поле
        mes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Sync();
            }
        });
        // обработка нажатия свитча
        swFB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sync();
            }
        });
        // обработка нажатия свитча
        swVK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sync();
            }
        });

        Sync();
    }
}
