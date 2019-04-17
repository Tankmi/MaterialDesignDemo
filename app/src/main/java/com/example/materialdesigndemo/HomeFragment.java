package com.example.materialdesigndemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //当当前Fragment完成相应的任务后,我们可以这样将返回值送回给我们的目标Fragment通过Intent
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,new Intent());
    }

}
