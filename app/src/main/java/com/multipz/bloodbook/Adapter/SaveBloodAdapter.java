package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.misc.Utils;
import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Model.ActiveDonersModel;
import com.multipz.bloodbook.Model.SaveBloodModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 23-11-2017.
 */

public class SaveBloodAdapter extends RecyclerView.Adapter<SaveBloodAdapter.MyViewHolder> {
    Context context;
    ArrayList<SaveBloodModel> list;
    private ItemClickListener clickListener;

    public SaveBloodAdapter(Context context, ArrayList<SaveBloodModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save_letter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SaveBloodModel data = list.get(position);
        holder.txt_user_name.setText("Blood requested by " + data.getUserName() + " for \nblood type " + data.getBloodType());
        holder.txt_location.setText(data.getAddress() + "," + data.getArea() + "," + data.getCity() + "," + data.getPincode());
        //holder.txt_blood_type.setText(data.getBloodType());
        Glide.with(context).load(com.multipz.bloodbook.Utils.Config.Img + "" + list.get(position).getUserImage()).into(holder.img_user);

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
        TextView txt_user_name, txt_location, txt_blood_type;
        ImageView img_user;

        public MyViewHolder(View view) {
            super(view);
            txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
            //txt_blood_type = (TextView) view.findViewById(R.id.txt_blood_type);
            img_user = (ImageView) view.findViewById(R.id.img_user);
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

