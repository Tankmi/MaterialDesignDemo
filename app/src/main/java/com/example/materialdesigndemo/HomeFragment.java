package com.example.materialdesigndemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import huitx.libztframework.utils.LOGUtils;

public class HomeFragment extends Fragment{
    protected View mView; // 当前界面的根
    private Button mBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = View.inflate(getActivity(),R.layout.fragment_home, null);
//		view = inflater.inflate(layoutId, container, false);

        mBtn = mView.findViewById(R.id.btn_home_fra);
        mBtn.setOnClickListener(view -> {
            LOGUtils.LOG("启动info");

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction mTransaction = manager.beginTransaction();

            InfoFragment mInfoFragment = new InfoFragment();

            mInfoFragment.setTargetFragment(this,100);
            mTransaction.replace(R.id.frame_home_fragment, mInfoFragment);
            mTransaction.addToBackStack(null);

            mTransaction.commitAllowingStateLoss();

        });
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LOGUtils.LOG("fragment is hidden " + hidden);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LOGUtils.LOG("home fragment OnActivityResult requestCode:" + requestCode
                + "\n resultCode: " + resultCode
                + "\n data: " + data.getStringExtra("info_data"));
    }


}
