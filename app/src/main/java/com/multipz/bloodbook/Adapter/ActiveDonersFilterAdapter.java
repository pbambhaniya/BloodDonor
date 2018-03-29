package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Model.ActiveDonersModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 21-11-2017.
 */

public class ActiveDonersFilterAdapter extends RecyclerView.Adapter<ActiveDonersFilterAdapter.MyViewHolder> {
    Context context;
    ArrayList<ActiveDonersModel> list;
    private ItemClickListener clickListener;

    public ActiveDonersFilterAdapter(Context context, ArrayList<ActiveDonersModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_doner, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActiveDonersModel data = list.get(position);
        holder.txt_name.setText(data.getUserName());
        holder.txt_location.setText(data.getAddress() + "," + data.getArea() + "," + data.getCity() + "," + data.getPincode());
        holder.txt_contact_no.setText(data.getContactNo());
        holder.txt_blood_type.setText(data.getBloodType());
        Glide.with(context).load(list.get(position).getUserImage()).into(holder.img_profile);

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
        TextView txt_name, txt_location, txt_contact_no, txt_blood_type;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
            txt_contact_no = (TextView) view.findViewById(R.id.txt_contact_no);
            txt_blood_type = (TextView) view.findViewById(R.id.txt_blood_type);
            img_profile = (ImageView) view.findViewById(R.id.img_profile);
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

