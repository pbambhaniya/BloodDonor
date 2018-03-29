package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Model.MyDonateBloodHistoryModel;
import com.multipz.bloodbook.Model.TotalHistoryModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 24-11-2017.
 */

public class TotalHistoryAdapter extends RecyclerView.Adapter<TotalHistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<TotalHistoryModel> list;
    private ItemClickListener clickListener;

    public TotalHistoryAdapter(Context context, ArrayList<TotalHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_total_req_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TotalHistoryModel data = list.get(position);
        holder.txt_blood_type.setText("Requested For " + data.getRequestedBlood() + " Blood Group");
        if (data.getDonetedUserName().contentEquals("")) {
            holder.txt_donated_name.setText("Blood request pending");
            holder.txt_date.setText("Requested On : " + data.getRequestedDate());
        } else {
            holder.txt_donated_name.setText("Donated by " + data.getDonetedUserName());
            holder.txt_date.setText("Donated On : " + data.getRequestedDate());

        }
        holder.txt_location.setText(data.getRequestedAddress());
        Glide.with(context).load(Config.Img + "" + list.get(position).getRequestedImage()).into(holder.img_profile);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_blood_type, txt_date, txt_location, txt_donated_name;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            txt_donated_name = (TextView) view.findViewById(R.id.txt_donated_name);
            txt_blood_type = (TextView) view.findViewById(R.id.txt_blood_type);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
            img_profile = (CircleImageView) view.findViewById(R.id.img_profile);
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

