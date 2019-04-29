package com.example.materialdesigndemo;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.materialdesigndemo.context.PreferenceEntity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeScroll;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.TransitionSet;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;

import static huitx.libztframework.utils.LOGUtils.LOG;

public class MainActivity extends AppCompatActivity {

    private Toolbar mtoolBar;
    private ImageView mImg;
    private Button mFileBtn, mBookBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) FragmentMethod();

        mtoolBar = findViewById(R.id.toolbar_title);
        mImg = findViewById(R.id.img_main);
        mFileBtn = findViewById(R.id.btn_main_01);
        mBookBtn = findViewById(R.id.btn_main_02);
        mFileBtn.setOnClickListener(view ->{
            goFileFragment();
        });
        mBookBtn.setOnClickListener(view ->{
            goAIDLFragment();
        });

        ViewCompat.setTransitionName(mImg, getResources().getString(R.string.home_sharedelement));

        setSupportActionBar(mtoolBar);
        //获取actionbar对象，也就是这里的toolbar，设置左侧按钮的图标（默认是返回键）
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        }

    }

    private void goFileFragment(){
        if (manager == null) manager = getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.frame_main, new FileFragment())
                .commitAllowingStateLoss();
    }



    FragmentManager manager;
    FragmentTransaction mTransaction;
    HomeFragment mHomeFragment;

    private void FragmentMethod() {
        if (manager == null) manager = getSupportFragmentManager();
        mTransaction = manager.beginTransaction();
        if (mHomeFragment == null){
//            mHomeFragment = new HomeFragment();
            mHomeFragment = HomeFragment.newInstance();

            //Fade 淡入淡出
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //使用androidx版本库，可以兼容低版本

                //设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
                mHomeFragment.setEnterTransition(new Fade());
                mHomeFragment.setExitTransition(new Fade());
                //设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
                mHomeFragment.setSharedElementEnterTransition(new DetailTransition());
                mHomeFragment.setSharedElementReturnTransition(new DetailTransition());
//            }
        }

        if (mHomeFragment.isAdded()) {
            mTransaction.show(mHomeFragment);
        } else {
            mTransaction.addSharedElement(mImg, getResources().getString(R.string.home_sharedelement));
            mTransaction.addToBackStack(null);  //监听回退栈
//            mTransaction.setCustomAnimations(R.anim.act_enter_anim, R.anim.act_exit_anim, R.anim.act_enter_anim, R.anim.act_exit_anim);
            mTransaction.add(R.id.frame_main, mHomeFragment);
            mTransaction.commitAllowingStateLoss();
        }



        LOG("getFragments().size()   " + manager.getFragments().size() + "\n"
                + "getFragments().toString()   " + manager.getFragments().toString());


    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        LOG("onAttachFragment : " + fragment.getId());
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
                FragmentMethod();

//                Snackbar.make(mtoolBar, "snackbar", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Log.i("test", "click Snackbar");
//
//                            }
//                        })
//                        .show();
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

    public class DetailTransition extends TransitionSet {
        public DetailTransition() {
            init();
        }

        // 允许资源文件使用
        public DetailTransition(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds())   //View的大小与位置动画
                    . addTransition(new ChangeTransform()) //View的缩放与旋转动画
                    .addTransition(new ChangeScroll())  //处理View的scrollX与scrollY属性
                    . addTransition(new ChangeImageTransform());    //处理ImageView的ScaleType属性
        }
    }



    private void goAIDLFragment(){
        if (manager == null) manager = getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.frame_main, new AIDLFragment())
                .commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
