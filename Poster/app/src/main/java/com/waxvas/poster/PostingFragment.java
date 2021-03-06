package com.waxvas.poster;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKWallPostResult;

import com.facebook.AccessToken;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class PostingFragment extends Fragment {
    void makeToast(String mes){
        Toast toast = Toast.makeText(getActivity(), mes, Toast.LENGTH_LONG);
        toast.show();
    }

    int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }

    void makePost(String msg, final int ownerId) {
        makeToast(ownerId + " " + msg);
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.OWNER_ID, String.valueOf(ownerId));
        parameters.put(VKApiConst.MESSAGE, msg);
        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                makeToast("Пост размещен ВК");
            }
            @Override
            public void onError(VKError error) {
                makeToast(error.toString());
            }
        });
    }

    void makePostFB() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.waxvas.poster")).setContentDescription(mes.getText().toString())
                .build();
        ShareDialog shareDialog = new ShareDialog(getActivity());
        shareDialog.show(content);
    }

    String getIdFB() {
        AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
        return fbAccessToken.getUserId();
    }

    Button send;
    SwitchCompat swFB, swVK;
    TextView mes;

    public void Sync(){
        AccessToken fbAccessToken = null;
        try {
            fbAccessToken = AccessToken.getCurrentAccessToken();
        }
        catch (Exception e) {}
        swFB.setEnabled(fbAccessToken != null);
        swVK.setEnabled(VKSdk.isLoggedIn());
        if (!swFB.isEnabled())
            swFB.setChecked(false);
        if (!swVK.isEnabled())
            swVK.setChecked(false);
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
        swFB = (SwitchCompat) view.findViewById(R.id.switchFB);
        swVK = (SwitchCompat) view.findViewById(R.id.switchVK);
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

        // обработка нажатия кнопки отправки
        send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (swVK.isChecked()){
                    //отправка в ВК
                    makePost(mes.getText().toString(), getMyId());
                }
                if (swFB.isChecked()){
                    //отправка в ФБ
                    makePostFB();
                }
            }
        });
        Sync();
    }
}
