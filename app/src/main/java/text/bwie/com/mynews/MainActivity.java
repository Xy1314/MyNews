package text.bwie.com.mynews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import text.bwie.com.mynews.framents.News_Frament;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp;
    private List<String> url_list;
    private FragmentManager fm;
    private TabLayout tab;
    private String[] titleArray;
    private Button night;
    private Button day;
    private ImageView phone;
    private int mode;
    private News_Frament news_frament;
    private Bundle bundle;
    private ImageView qq;
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private View view;
    private ImageView user;
    private ListView listView;
    private String[] left_array;
    private ArrayAdapter<String> adapter;
    private ImageView sj;
    private ImageView guanli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTencent = Tencent.createInstance(APP_ID, MainActivity.this.getApplicationContext());
        mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        view = View.inflate(MainActivity.this, R.layout.fragment_left, null);
        user = (ImageView) findViewById(R.id.action1_user);
        vp = (ViewPager) findViewById(R.id.action1_vp);
        qq = (ImageView) view.findViewById(R.id.left_qq);
        tab = (TabLayout) findViewById(R.id.action1_tab);
        sj = (ImageView) view.findViewById(R.id.left_sj);
        guanli = (ImageView) findViewById(R.id.action1_guanli);
        initList();
        pager();
        slidingMenu();
        qqLogin();
        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PindaoActivity.class);
                intent.putExtra("aaa",titleArray);
                startActivity(intent);
            }
        });
        sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sj== v){
                    //打开注册界面
                    RegisterPage registerPage = new RegisterPage();
                    registerPage.setRegisterCallback(new EventHandler() {// 事件调用监听类

                        @Override
                        public void afterEvent(int event, int result, Object data) {//短信SDK操作回调
                            super.afterEvent(event, result, data);
                            //解析注册结果
                            if (result == SMSSDK.RESULT_COMPLETE) {// 如果状态为完成状态,表示注册成功
                                // 获取Data中的数据
                                HashMap<String, Object> dataMaps = (HashMap<String, Object>) data;
                                // 获取手机号所在国家信息
                                String country = (String) dataMaps.get("country");
                                // 获取收到验证码的手机号码
                                String phone = (String) dataMaps.get("phone");
                                // 提交信息到mob注册
                                submitInfo(country, phone);
                            }
                        }
                    });
                    // 第三步：显示注册界面
                    registerPage.show(MainActivity.this);
                }
            }
        });
    }
    private void qqLogin() {
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(MainActivity.this,"all", mIUiListener);
            }
        });
    }
    private void initList() {
        url_list = new ArrayList<String>();
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?count=20&min_behot_time=1455521444&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_hot&count=20&min_behot_time=1455521166&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_nme=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_local&count=20&min_behot_time=1455521226&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&user_city=%E5%8C%97%E4%BA%AC&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=video&count=20&min_behot_time=1455521349&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_society&count=20&min_behot_time=1455521720&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_entertainment&count=20&min_behot_time=1455522338&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_tech&count=20&min_behot_time=1455522427&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_car&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_sports&count=20&min_behot_time=1455522629&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_finance&count=20&min_behot_time=1455522899&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_military&count=20&min_behot_time=1455522991&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_world&count=20&min_behot_time=1455523059&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=essay_joke&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=image_funny&count=20&min_behot_time=1455524031&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=news_health&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        url_list.add("http://ic.snssdk.com/2/article/v25/stream/?category=image_ppmm&count=20&min_behot_time=1455524172&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
        titleArray = new String[]{"推荐", "热点", "本地", "视频", "社会", "娱乐", "科技", "汽车", "体育", "财经", "军事", "国际", "段子", "趣图", "健康", "美女"};
    }

    private void slidingMenu() {
        final SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置滑动的屏幕范围，该设置为全屏区域都可以滑动
        menu.setBehindOffset(70);//SlidingMenu划出时主页面显示的剩余宽度
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(view);
        listView = (ListView) findViewById(R.id.left_lv);
        left_array = new String[]{"关注", "通知", "本地缓存", "设置"};
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, left_array);
        listView.setAdapter(adapter);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        day = (Button) findViewById(R.id.day);
        night = (Button) findViewById(R.id.left_night);
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            night.setVisibility(view.GONE);
            day.setVisibility(View.VISIBLE);
        } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
            day.setVisibility(view.GONE);
            night.setVisibility(View.VISIBLE);

        }
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });

        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
            }
        });


    }
    private void submitInfo(String country, String phone) {
        Random r = new Random();
        String uid = Math.abs(r.nextInt()) + "";
        String nickName = "设置个昵称";
        SMSSDK.submitUserInfo(uid, nickName, null, country, phone);// 提交用户信息，在监听中返回
    }


    private void pager() {
        final List<Fragment> list = new ArrayList<>();
        fm = getSupportFragmentManager();

        for (int i = 0; i < titleArray.length; i++) {
            news_frament = new News_Frament();
            bundle = new Bundle();
            bundle.putString("url", url_list.get(i));
            news_frament.setArguments(bundle);
            list.add(news_frament);


        }
        vp.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleArray[position];
            }
        });
        tab.setupWithViewPager(vp);

    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());

                        if (response == null) {
                            return;
                        }
                        try {
                            JSONObject jo = (JSONObject) response;
                            int ret = jo.getInt("ret");
                            String nickName = jo.getString("nickname");
                            String gender = jo.getString("gender");
                            String QQimg = jo.getString("figureurl");

                            Glide.with(MainActivity.this).load(QQimg).into(user);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

