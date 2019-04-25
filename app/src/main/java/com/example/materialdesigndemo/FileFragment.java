package com.example.materialdesigndemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import huitx.libztframework.context.LibPreferenceEntity;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * 文件读取和写入
 */
public class FileFragment extends Fragment{
    protected View mView; // 当前界面的根
    private TextView mInfoTv;
    private EditText inputtEt;
    private Button fileWriteBtn, fileReadBtn, fileDelBtn, fileRenameBtn, fileListsBtn;

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

        mInfoTv = mView.findViewById(R.id.tv_file_fra);
        inputtEt = mView.findViewById(R.id.et_file_fra);
        fileWriteBtn = mView.findViewById(R.id.btn_write_file_fra);
        fileReadBtn = mView.findViewById(R.id.btn_read_file_fra);
        fileDelBtn = mView.findViewById(R.id.btn_delete_file_fra);
        fileRenameBtn = mView.findViewById(R.id.btn_rename_file_fra);
        fileListsBtn = mView.findViewById(R.id.btn_list_file_fra);

        fileWriteBtn.setOnClickListener(view -> {   //缓存文本内容到文件中
//            setText(writeFile());
            setText(writeToSDCard());
        });

        fileReadBtn.setOnClickListener(view ->{ //读取文本
//            setText(readFile());
            setText(readFileForSD());
        });

        fileDelBtn.setOnClickListener(view ->{ //删除文件
//            File file1 = getActivity().getFilesDir();    //获取APP内部存储地址
            deleteDirectory(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH);
        });

        fileRenameBtn.setOnClickListener(view ->{
            setText(
                    renameFile(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH, "testData.txt", "testDataRename.txt")
            );
        });

        fileListsBtn.setOnClickListener(view ->{
//            setText(
//                    getFiles(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH)
//            );
            getStroageSpace();
        });

        return mView;
    }

    private void setText(Object t){
        mInfoTv.setText(t.toString());
    }

    private void appendText(Object t){
        mInfoTv.append(t.toString() + "\n") ;
    }

    private boolean writeToSDCard(){
        String content = inputtEt.getText().toString();
        if(content.isEmpty()){
            ToastUtils.showToast("请输入内容！");
            return  false;
        }

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtils.showToast("SD卡目录异常！" + Environment.getExternalStorageState());
            return  false;
        }

        String path = Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH;//存放文件的目录！
        File directory = new File(path);

        if (!directory.exists()) {
            //dir.mkdirs();
            if (!directory.mkdirs()) {
                // 创建目录失败: 一般是因为SD卡被拔出了
                ToastUtils.showToast("创建目录失败: 一般是因为SD卡被拔出了");
                return false;
            }
        }

        try{

            FileOutputStream fout = new FileOutputStream(directory + "/" + "testData.txt");
            LOGUtils.LOG(directory + "/" + "testData.txt");
            byte [] bytes = content.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return true;
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

    private String readFileForSD(){
        FileInputStream input;
        BufferedReader reader = null;

        StringBuilder content = new StringBuilder();
        String path = Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH + "/" + "testData.txt";//外部存儲器，存放文件的目录！

        try {
            input = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
        }


        return content.toString();
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

    /**
     * 删除外部存储上的文件
     * @param fileName
     * @return
     */
    private boolean deleteFile(String fileName){
        
        File file = new File(fileName);  //获取外部存储器的目录下的文件地址
//        File file = new File(getActivity().getFilesDir() + "/testData");    //获取APP内部存储地址
        LOGUtils.LOG("缓存文本 " + file.getPath() + "  " + file.getUsableSpace());  //  打印值  /storage/emulated/0/huidf_slimming/testData.txt  5377552384

        if(!file.exists() || !file.isFile()){  //判断文件或者文件件是否存在
            ToastUtils.showToast("文件不存在");
            return  false;
        }
        return file.delete();
    }



    /**
     * 删除外部存储上的文件夹
     * @param fileName
     * @return
     */
    private boolean deleteDirectory(String fileName){

        File file = new File(fileName);  //获取外部存储器的目录下的文件地址
        LOGUtils.LOG("缓存目录地址 " + file.getPath() + "  " + file.getUsableSpace());  //  打印值  /storage/emulated/0/huidf_slimming/testData.txt  5377552384

        if(!file.exists() || !file.isDirectory()){  //判断文件或者文件件是否存在
            ToastUtils.showToast("文件夹不存在");
            return  false;
        }

        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i] .getAbsolutePath());
                if (!flag)
                    break;
            }
        }

        return file.delete();
    }

    /**
     * 重命名文件或者文件夹
     * @param path
     * @param fileName
     * @param newName
     * @return
     */
    private boolean renameFile(String path, String fileName, String newName){

        File mFile = new File(path + "/" + fileName);
        File newFile = new File(path + "/" + newName);
        if(!mFile.exists()){
            ToastUtils.showToast("指定文件名的对象不存在");
//            newFile.mkdirs();
            return  false;
        }
        if(fileName.equals(newName)){
            ToastUtils.showToast("文件名不能和旧文件名重复");
            return  false;
        }
        mFile.renameTo(newFile);

        return true;
    }

    /**
     * 获取文件列表
     * @param path
     * @return
     */
    private boolean getFiles(String path){

        File mFile = new File(path );

        try {
            LOGUtils.LOG("文件全路径名： " + mFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGUtils.LOG("getName： " + mFile.getName());
        LOGUtils.LOG("getAbsolutePath： " + mFile.getAbsolutePath());
        LOGUtils.LOG("getUsableSpace： " + mFile.getUsableSpace());
        LOGUtils.LOG("getFreeSpace： " + mFile.getFreeSpace());
        LOGUtils.LOG("list： " + mFile.list().length);
        LOGUtils.LOG("listFiles： " + mFile.listFiles().length);
        LOGUtils.LOG("listFiles OnlyText2 ： " + mFile.listFiles(new OnlyText2()).length);

        return true;
    }

    //过滤文件列表
    private class OnlyText implements FilenameFilter{

        @Override
        public boolean accept(File dir, String name) {
            if(name.endsWith(".txt")){
                return true;
            }
            return false;
        }
    }

    private class OnlyText2 implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            if(pathname.getName().endsWith(".txt")){
                return true;
            }
            return false;
        }
    }

    private void getStroageSpace(){
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        double bytesTotal = statFs.getBlockSize() * statFs.getBlockCount(); //块的大小 * 块的数量  =  总的存储空间
        double bytesTotal1 = statFs.getBlockSizeLong() * statFs.getBlockCountLong(); //块的大小 * 块的数量  =  总的存储空间

        LOGUtils.LOG("总的存储空间：" + bytesTotal1 );

        BigDecimal bd1 = new BigDecimal(bytesTotal1);
        LOGUtils.LOG("科学计数法，总的存储空间：" + bd1.toPlainString() );
        bytesTotal1 = bd1.doubleValue();
        double megTotal = bytesTotal1/1048576;   //换算成M的存储空间
        double megTotalG = bytesTotal1/1048576/1024;   //换算成M的存储空间

        double avalibleBlocks = statFs.getBlockSize() * statFs.getAvailableBlocks();    //可用存储空间
        double avalibleBlocks1 = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();    //可用存储空间

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        LOGUtils.LOG("总的存储空间：" + bytesTotal1 );
        LOGUtils.LOG("换算成M的存储空间：" + decimalFormat.format(megTotal) );
        LOGUtils.LOG("换算成G的存储空间：" + decimalFormat.format(megTotalG) );
        LOGUtils.LOG("可用存储空间：" + decimalFormat.format(avalibleBlocks1) );
    }

    private boolean createFile(){


        return true;
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
