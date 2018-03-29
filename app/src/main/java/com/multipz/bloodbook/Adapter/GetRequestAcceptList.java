package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Model.CurrentBloodModel;
import com.multipz.bloodbook.Model.DonateBloodModel;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 23-11-2017.
 */

public class GetRequestAcceptList extends RecyclerView.Adapter<GetRequestAcceptList.MyViewHolder> {
    Context context;
    ArrayList<DonateBloodModel> list;
    private ItemClickListener clickListener;

    public GetRequestAcceptList(Context context, ArrayList<DonateBloodModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_accept_request_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DonateBloodModel data = list.get(position);
        holder.txt_name.setText("Blood requested for " + data.getBloodType() + " by " + data.getUserName());
        holder.txt_Action_date.setText("On : "+data.getActionDate());
        holder.txt_location.setText(data.getAddress() + "," + data.getArea() + "," + data.getCity() + "," + data.getPincode());
        Glide.with(context).load(Config.Img + "" + list.get(position).getUserImage()).into(holder.img_profile);

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
        TextView txt_name, txt_donated_name, txt_Action_date, txt_location;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_donated_name = (TextView) view.findViewById(R.id.txt_donated_name);
            txt_Action_date = (TextView) view.findViewById(R.id.txt_Action_date);
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

