package com.example.materialdesigndemo;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.materialdesigndemo.context.PreferenceEntity;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;

import static huitx.libztframework.utils.LOGUtils.LOG;

public class MainActivity extends AppCompatActivity {

    private Toolbar mtoolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoolBar = findViewById(R.id.toolbar_title);
        setSupportActionBar(mtoolBar);

        //获取actionbar对象，也就是这里的toolbar，设置左侧按钮的图标（默认是返回键）
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        }

    }


    @Override
    public void addOnBackPressedCallback(@NonNull LifecycleOwner owner, @NonNull OnBackPressedCallback onBackPressedCallback) {

        super.addOnBackPressedCallback(owner, onBackPressedCallback);
    }


    private void FragmentMethod(){
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction mTransaction = manager.beginTransaction();

        HomeFragment mHomeFragment = new HomeFragment();
        InfoFragment mInfoFragment = new InfoFragment();

        //当前Fragment设置一个目标Fragment和一个请求码
        mHomeFragment.setTargetFragment(mInfoFragment, 100);

//        mTransaction.add(mHomeFragment, "home");
//        mTransaction.add(mInfoFragment, "info");
        mTransaction.replace(R.id.frame_main, mInfoFragment);

        mTransaction.commitAllowingStateLoss();



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:

                break;
            case android.R.id.home:
                Log.i("test", "加载菜单");


                Snackbar.make(mtoolBar, "snackbar", Snackbar.LENGTH_INDEFINITE)
                        .setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i("test", "click Snackbar");
                                FragmentMethod();
                            }
                        })
                        .show();
                break;
        }
        return true;
    }

    protected boolean setscr = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (setscr) return;

        if (hasFocus) {
            boolean isSave = PreferencesUtils.getBoolean(this, PreferenceEntity.KEY_SCREEN_ISSAVE, false);
            if (!isSave) {
                LOGUtils.LOG("初始化屏幕信息");
                //获取屏幕尺寸，不包括虚拟功能高度<br><br>
                int widowHeight = getWindowManager().getDefaultDisplay().getHeight();

                Resources resources = MainActivity.this.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        int top = rect.top; // 状态栏的高度
                View view = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int top2 = view.getTop(); // 状态栏的高度
                int width = view.getWidth(); // 视图的宽度
                int height = view.getHeight(); // 视图的高度
                int navigationBarHeight = resources.getDimensionPixelSize(resourceId);    //虚拟按键的高度

//                DisplayMetrics metric = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metric);
//                int mewidth = metric.widthPixels;  // 屏幕宽度（像素）
//                int meheight = metric.heightPixels;  // 屏幕高度（像素）
//                float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//                int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
//                LOGUtils.LOG("屏幕密度: " + density + "; 屏幕密度DPI: " + densityDpi);

                float status_bar_height = 0;
                int resourceId_status = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId_status > 0) {
                    status_bar_height = this.getResources().getDimensionPixelSize(resourceId_status);
                    LOGUtils.LOG("状态栏高度: " + status_bar_height);
                }

                PreferenceEntity.ScreenTop = status_bar_height;
                PreferenceEntity.ScreenTitle_navigationBarHeight = navigationBarHeight;
                PreferenceEntity.screenWidth = width;
                PreferenceEntity.screenHeight = height + top2;


                boolean hasNavigationBar = false;
                if ((height + top2) - widowHeight > 0) hasNavigationBar = true;
                else hasNavigationBar = false;
                LOG("  虚拟键盘 hasNavigationBar =  " + (!hasNavigationBar ? "没显示" : "显示了"));
                PreferenceEntity.hasNavigationBar = hasNavigationBar;


                LOGUtils.LOG("状态栏的高度：" + status_bar_height + ",状态栏的高度:" + top2 + ",标题栏与状态栏的高度占比:"
                        + PreferencesUtils.getFloat(this, "ScreenTitle")
                        + ",视图的宽度:" + width + ",视图的高度(不包括状态栏的高度):" + height + "屏幕高度（不包括虚拟功能高度）： " + widowHeight + ",屏幕的宽度:"
                        + PreferenceEntity.screenWidth + ",屏幕的高度:"
                        + PreferenceEntity.screenHeight + "虚拟键盘的高度：" + navigationBarHeight + "手机厂商：" + Build.MANUFACTURER);
                PreferenceEntity.saveScreenView();
                setscr = true;
            } else {
                LOG("屏幕信息已保存，初始化");
                PreferenceEntity.initScreenView();
                setscr = true;
            }

        }

    }
}
