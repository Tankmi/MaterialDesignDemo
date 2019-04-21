package com.example.materialdesigndemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * 文件读取和写入
 */
public class FileFragment extends Fragment{
    protected View mView; // 当前界面的根
    private EditText inputtEt;
    private Button fileWriteBtn, fileReadBtn;

    private static final String ARG_NUMBER = "arg_number";

    public static FileFragment newInstance(int number) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUMBER, number);

        FileFragment detailFragment = new FileFragment();
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = View.inflate(getActivity(), R.layout.fragment_file, null);
//		view = inflater.inflate(layoutId, container, false);

        inputtEt = mView.findViewById(R.id.et_file_fra);
        fileWriteBtn = mView.findViewById(R.id.btn_write_file_fra);
        fileReadBtn = mView.findViewById(R.id.btn_read_file_fra);

        fileWriteBtn.setOnClickListener(view -> {   //缓存文本内容到文件中
            LOGUtils.LOG("缓存文本 " + writeFile());
        });

        fileReadBtn.setOnClickListener(view ->{ //读取文本
            LOGUtils.LOG("读取文本 " + readFile());
        });
        return mView;
    }

    private boolean writeFile(){
        String content = inputtEt.getText().toString();
        if(content.isEmpty()){
            ToastUtils.showToast("请输入内容！");
            return  false;
        }

        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = getActivity().openFileOutput("testData", Context.MODE_APPEND);

//            byte [] bytes = content.getBytes();
//            out.write(bytes);

            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
//            if(out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private String readFile(){
        FileInputStream input;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            input = getActivity().openFileInput("testData");
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return content.toString();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        int drawableId = getArguments().getInt(ARG_NUMBER);
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
