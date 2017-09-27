package text.bwie.com.mynews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    private ViewPager vp;
    private List<View> list;
    private RadioGroup rg;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        vp = (ViewPager) findViewById(R.id.navigation_vp);
        rg = (RadioGroup) findViewById(R.id.navigation_rg);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            sp.edit().putBoolean("isFirst",false).commit();
        }
        else{
            startActivity(new Intent(NavigationActivity.this, autoActivity.class));
            finish();
        }
        initPager();

    }


    private void initPager() {
        list = new ArrayList<View>();
        list.add(View.inflate(this, R.layout.pager1, null));
        list.add(View.inflate(this, R.layout.pager2, null));
        list.add(View.inflate(this, R.layout.pager3, null));
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rg.check(R.id.radio1);
                        break;
                    case 1:
                        rg.check(R.id.radio2);
                        break;
                    case 2:
                        rg.check(R.id.radio3);
                        break;


                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.radio2:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.radio3:
                        vp.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    public void Btn(View view) {
        startActivity(new Intent(NavigationActivity.this, MainActivity.class));
        finish();

    }
}
