package com.waxvas.poster;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vk.sdk.VKSdk;


public class AccountsFragment extends Fragment {

    Button loginVK;
    MainActivity act;

    public void Sync(){
        if (act.isLoginVK) loginVK.setText("@string/logout");
        else loginVK.setText("@string/login");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posting, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginVK = (Button) view.findViewById(R.id.buttonVK);
        act = (MainActivity) getActivity();

        loginVK.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                VKSdk.login(getActivity());
            }
        });

        Sync();
    }
}
