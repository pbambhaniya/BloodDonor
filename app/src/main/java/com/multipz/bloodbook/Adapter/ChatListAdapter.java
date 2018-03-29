package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.multipz.bloodbook.Model.ChatListModelClass;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<ChatListModelClass> list = new ArrayList<>();
    Context mcontext;
    private ItemClickListener clickListener;
    private int pos = 0;

    public ChatListAdapter(Context context, ArrayList<ChatListModelClass> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        ChatListModelClass m = list.get(position);
        holder.txtUserName.setText(m.getName());
        Glide.with(mcontext).load(m.getImg())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userimg);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtUserName, txtdate, txtshowmsg, txtCountMsg;
        ImageView userimg;

        public MyViewHolder(View view) {
            super(view);
            txtUserName = (TextView) view.findViewById(R.id.txtUserName);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            txtshowmsg = (TextView) view.findViewById(R.id.txtshowmsg);
            txtCountMsg = (TextView) view.findViewById(R.id.txtCountMsg);
            userimg = (ImageView) view.findViewById(R.id.userimg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }

}
