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
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 24-11-2017.
 */

public class DonateHistoryAdapter extends RecyclerView.Adapter<DonateHistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<MyDonateBloodHistoryModel> list;
    private ItemClickListener clickListener;

    public DonateHistoryAdapter(Context context, ArrayList<MyDonateBloodHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_donation_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyDonateBloodHistoryModel data = list.get(position);
        holder.txt_user_name.setText("Donated to " + data.getUserName());
        holder.txt_date.setText(data.getActionDate());
        holder.txt_hospital_name.setText(data.getArea() + "," + data.getAddress() + "," + data.getCity() + "," + data.getPincode());
        Glide.with(context).load(Config.Img +  "" + list.get(position).getUserImage()).into(holder.img_profile);
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
        TextView txt_user_name, txt_date, txt_hospital_name;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_hospital_name = (TextView) view.findViewById(R.id.txt_hospital_name);
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

