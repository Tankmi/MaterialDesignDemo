package com.example.materialdesigndemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import huitx.libztframework.context.LibPreferenceEntity;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.ToastUtils;

public class HomeFragment extends Fragment {
    private static ImageView mImg;

    private static final String ARG_NUMBER = "arg_number";

    static HomeFragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUMBER, R.drawable.lianpo);

        HomeFragment detailFragment = new HomeFragment();
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 当前界面的根
        View mView = View.inflate(getActivity(), R.layout.fragment_home, null);
//		view = inflater.inflate(layoutId, container, false);

        Button mBtn = mView.findViewById(R.id.btn_home_fra);
        mImg = mView.findViewById(R.id.img_homt_fra);
        mBtn.setOnClickListener(view -> {
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            FragmentTransaction mTransaction = manager.beginTransaction();
//
//            InfoFragment mInfoFragment = new InfoFragment();
//
//            mInfoFragment.setTargetFragment(this,100);
//            mTransaction.replace(R.id.frame_home_fragment, mInfoFragment);
//            mTransaction.addToBackStack(null);
//
//            mTransaction.commitAllowingStateLoss();

            //执行返回键操作
//            getActivity().getSupportFragmentManager().popBackStack();

            getBitmapData();

        });
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        int drawableId = getArguments().getInt(ARG_NUMBER);
//        mImg.setBackgroundResource(drawableId);
    }

    static MyHandler mHandler;
    static HandlerThread handlerThread;

    private void getBitmapData() {
        if (mHandler == null) mHandler = new MyHandler(getActivity());

        handlerThread = new HandlerThread("img");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper(), new HandlerThreadCallBack());
        handler.sendEmptyMessageAtTime(1, 1000);

    }

    private static class HandlerThreadCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            String url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=574643212,490700156&fm=27&gp=0.jpg";

            Bitmap bitmap = downLoadBitmap(url);
            if (bitmap == null) {
                ToastUtils.showToast("下载失败");
                return false;
            }

            LOGUtils.LOG("保存图片："  + saveBitmapToSD(bitmap) );

            ImageModel imageModel = new ImageModel(bitmap, url);

            Message msg1 = new Message();
            msg1.obj = imageModel;
            msg1.what = 200;
            mHandler.sendMessage(msg1);
            handlerThread.quit();
            return false;
        }
    }


    private static class MyHandler extends Handler {
        private WeakReference<Context> mContext;

        public MyHandler(Context context) {
            mContext = new WeakReference<>(context);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 200:
                    ImageModel model = (ImageModel) msg.obj;
                    mImg.setImageBitmap(model.getBitmap());

                    handlerThread.quit();
                    break;
            }

        }
    }

    /**
     * 保存bitmap文件到本地
     */
    private static boolean saveBitmapToSD(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;

        String path = Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH;
        File file = new File(path, "bitmap.webp");

        try {

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(file);
            LOGUtils.LOG("文件名： " + file.getName());
            LOGUtils.LOG("文件缓存地址： " + file.getAbsolutePath());

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 通过文件名获取bitmap
     */
    private static Bitmap getBitmapForSD(String name) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream = null;

//        StringBuilder stringBuilder = new StringBuilder("file://" + Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH + "/" + name);
        StringBuilder stringBuilder = new StringBuilder(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH + "/" + name);
        LOGUtils.LOG("图片地址： " + stringBuilder.toString());
        try {
            fileInputStream = new FileInputStream(stringBuilder.toString());
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //下载图片
    private static Bitmap downLoadBitmap(String urls) {
//        Bitmap bitmap = getBitmapForSD("bitmap.jpg");
        Bitmap bitmap = decodeBitmap(ApplicationData.context, "bitmap.webp");
        if (bitmap != null) {
            LOGUtils.LOG("加载缓存的图片");
            return bitmap;
        }
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;


        try {
            final URL url = new URL(urls);
            httpURLConnection = (HttpURLConnection) url.openConnection();
//            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream(),1 *1024);
//            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

            //接收输入流
            InputStream inputStream = httpURLConnection.getInputStream();
            //将输入流转换为bitmap赋值
            bitmap = BitmapFactory.decodeStream(inputStream);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }


    //压缩图片
    private static Bitmap decodeBitmap(Context mContext, String name){
        StringBuilder stringBuilder = new StringBuilder(Environment.getExternalStorageDirectory() + LibPreferenceEntity.KEY_CACHE_PATH + "/" + name);

        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds为true，不返回bitmap，只返回这个bitmap的尺寸
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(fileInputStream, null, options);
        BitmapFactory.decodeFile(stringBuilder.toString(),options);

        //利用返回的原图片的宽高，我们就可以计算出缩放比inSampleSize（只能是2的整数次幂）
        options.inSampleSize = 1;
//        options.inSampleSize = calculateSampleSize(options, 320, 320);
        //使用RGB_565减少图片大小
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //释放内存，共享引用（21版本后失效）
        options.inPurgeable = true;
        options.inInputShareable = true;

        //inJustDecodeBounds为false，返回bitmap
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(stringBuilder.toString(),options);
        return bitmap;
    }

    private static int calculateSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
            inSampleSize *= 2;
        }
        return inSampleSize;
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
