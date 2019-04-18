package com.example.materialdesigndemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import huitx.libztframework.utils.LOGUtils;

public class InfoFragment extends Fragment {
    protected View mView; // 当前界面的根
    private Button mBtn;

    /**
     * 可通过参数savedInstanceState获取之前保存的值,记得一定要调用父类的super.onCreate。
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = View.inflate(getActivity(),R.layout.fragment_info, null);
        mBtn = mView.findViewById(R.id.btn_info_fra);
        mBtn.setOnClickListener(view -> {
            LOGUtils.LOG("发送事件");

            Intent intentData = new Intent();
            intentData.putExtra("info_data","发送事件");

            //当当前Fragment完成相应的任务后,我们可以这样将返回值送回给我们的目标Fragment通过Intent
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intentData);

            getChildFragmentManager().beginTransaction().hide(this);

        });
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(){}
        LOGUtils.LOG("fragment OnActivityResult resultCode: " + resultCode);
    }
}
