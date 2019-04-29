package com.example.materialdesigndemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.beibei.server.Book;
import com.beibei.server.IBookManager;

import java.util.List;

import androidx.fragment.app.Fragment;
import huitx.libztframework.utils.LOGUtils;

import static huitx.libztframework.utils.LOGUtils.LOG;

/**
 * AIDL
 */
public class AIDLFragment extends Fragment{
    protected View mView; // 当前界面的根
    private TextView mInfoTv;
    private EditText inputtEt;
    private Button fileWriteBtn, fileReadBtn, fileDelBtn, fileRenameBtn, fileListsBtn;

    private static final String ARG_NUMBER = "arg_number";

    private IBookManager bookManager;
    private boolean connected;
    private List<Book> bookList;

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
        });

        fileReadBtn.setOnClickListener(view ->{ //读取文本
//            setText(readFile());
            if (connected) {
                try {
                    bookList = bookManager.getBookList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                log();
            }else LOGUtils.LOG("serviceUnConnected");
        });

        fileDelBtn.setOnClickListener(view ->{ //删除文件
//            File file1 = getActivity().getFilesDir();    //获取APP内部存储地址
        });

        fileRenameBtn.setOnClickListener(view ->{
        });

        fileListsBtn.setOnClickListener(view ->{
//            setText(
//                    getFiles(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH)
//            );
        });



        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        bindService();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.beibei.server");
        intent.setAction("com.beibei.server.action");

//        Intent intent = new Intent(getActivity(), AIDLService.class);
        LOG("bindservice" + getActivity().bindService(intent, serviceConnection, getActivity().BIND_AUTO_CREATE));
    }

    private void log() {
        for (Book book : bookList) {
            LOGUtils.LOG(book.getBookName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connected) {
            getActivity().unbindService(serviceConnection);
        }
    }
}
