package com.example.materialdesigndemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import huitx.libztframework.utils.LOGUtils;

public class InfoFragment extends Fragment {
    protected View Mview; // 当前界面的根

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Mview = View.inflate(getActivity(),R.layout.fragment_home, null);
//		view = inflater.inflate(layoutId, container, false);
        return Mview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(){}
        LOGUtils.LOG("fragment OnActivityResult resultCode: " + resultCode);
    }
}
