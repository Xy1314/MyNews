package text.bwie.com.mynews;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import text.bwie.com.mynews.bean.Bean;

/**
 * Created by XIn💕 on 2017/9/13.
 */

public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private static final int TYPE_4 = 3;
    List<Bean.DataBean> list;
    private Context context;
    private MyItemClickListener mItemClickListener;

    public RvAdapter(List<Bean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public  void loadMore(List<Bean.DataBean> datalist,boolean flag){
        for (Bean.DataBean dataBean : datalist) {
            if (flag) {
                list.add(0,dataBean);
            }
            else {
                list.add(dataBean);
            }
        }
        notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getVideo_detail_info() == null) {
            if (list.get(position).getImage_list() == null) {
                return TYPE_1;
            } else if (list.get(position).getImage_list().size() >= 3) {
                return TYPE_2;
            } else {
                return TYPE_3;
            }
        } else {
            return TYPE_4;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = View.inflate(context, R.layout.news_item3, null);
        holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        switch (type) {
            case TYPE_1:

                viewHolder.tv1.setTextColor(Color.BLUE);
                viewHolder.tv1.setVisibility(View.VISIBLE);
                viewHolder.tv1.setText(list.get(position).getTitle());

                break;

            case TYPE_2:
                viewHolder.tv1.setVisibility(View.VISIBLE);
                viewHolder.layout1.setVisibility(View.VISIBLE);

                viewHolder.tv1.setText(list.get(position).getTitle());
                Glide.with(context).load(list.get(position).getImage_list().get(0).getUrl()).override(100, 80).fitCenter().into(viewHolder.img1);
                Glide.with(context).load(list.get(position).getImage_list().get(1).getUrl()).override(100, 80).fitCenter().into(viewHolder.img2);
                Glide.with(context).load(list.get(position).getImage_list().get(2).getUrl()).override(100, 80).fitCenter().into(viewHolder.img3);
                break;

            case TYPE_3:

                viewHolder.layout2.setVisibility(View.VISIBLE);
                viewHolder.tv2.setText(list.get(position).getTitle());
                Glide.with(context).load(list.get(position).getMiddle_image().getUrl()).override(100, 80).into(viewHolder.img4);
                break;

            case TYPE_4:
                viewHolder.img5.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getVideo_detail_info().getDetail_video_large_image().getUrl()).into(viewHolder.img5);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout2, layout1;
        TextView tv1, tv2;
        ImageView img1, img2, img3, img4, img5;
        private MyItemClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            layout1 = (LinearLayout) itemView.findViewById(R.id.layout_1);
            layout2 = (LinearLayout) itemView.findViewById(R.id.layout_2);
            tv1 = (TextView) itemView.findViewById(R.id.item3_tv1);
            tv2 = (TextView) itemView.findViewById(R.id.layout_2_tv);
            img1 = (ImageView) itemView.findViewById(R.id.item3_imgage1);
            img2 = (ImageView) itemView.findViewById(R.id.item3_imgage2);
            img3 = (ImageView) itemView.findViewById(R.id.item3_imgage3);
            img4 = (ImageView) itemView.findViewById(R.id.layout_2_img);
            img5 = (ImageView) itemView.findViewById(R.id.item3_image4);
            this.mListener = mItemClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}
