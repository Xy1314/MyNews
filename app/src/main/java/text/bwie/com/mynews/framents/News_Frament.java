package text.bwie.com.mynews.framents;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import text.bwie.com.mynews.Main2Activity;
import text.bwie.com.mynews.R;
import text.bwie.com.mynews.RvAdapter;
import text.bwie.com.mynews.Utils.RecycleViewDivider;
import text.bwie.com.mynews.bean.Bean;

/**
 * Created by XIn💕 on 2017/9/10.
 */

public class News_Frament extends Fragment {

    private View view;
    private SpringView springView;
    private String url;
    private RecyclerView rv;
    private RvAdapter rvAdapter;
    private List<Bean.DataBean> data;
    private boolean flag;
    private ImageView qq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.news_frament, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        springView = (SpringView) view.findViewById(R.id.news_spring);
        rv = (RecyclerView) view.findViewById(R.id.news_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        url = getArguments().getString("url");
        getXutils(url);
        shangxiala();
        super.onActivityCreated(savedInstanceState);
    }

    private void shangxiala() {
        rv.addItemDecoration( new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));

        //springview,上下拉刷新监听
        springView.setListener(new SpringView.OnFreshListener() {
            //上拉刷新
            @Override
            public void onRefresh() {
                flag=true;
                getXutils(url);
                finishFreshAndLoad();
            }
            //下拉加载
            @Override
            public void onLoadmore() {
                flag=false;
                getXutils(url);
                finishFreshAndLoad();
            }
        });
    }

    //刷新后完成加载
    private void finishFreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               springView.onFinishFreshAndLoad();
            }
        }, 1000);
    }
    //xutils网络请求数据
    public void getXutils(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                Bean bean = gson.fromJson(result, Bean.class);
                data = bean.getData();
                if (rvAdapter==null){
                    rvAdapter = new RvAdapter(data, getActivity());
                    rv.setAdapter(rvAdapter);
                }else {
                    rvAdapter.loadMore(data,flag);
                }


                rvAdapter.setItemClickListener(new RvAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), Main2Activity.class);
                        intent.putExtra("url", data.get(position).getUrl());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}
