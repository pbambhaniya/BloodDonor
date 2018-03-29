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
import com.multipz.bloodbook.Model.RequestAcceptModelClass;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 23-11-2017.
 */

public class RequestAcceptDonerListAdapter extends RecyclerView.Adapter<RequestAcceptDonerListAdapter.MyViewHolder> {
    Context context;
    ArrayList<RequestAcceptModelClass> list;
    private ItemClickListener clickListener;


    public RequestAcceptDonerListAdapter(Context context, ArrayList<RequestAcceptModelClass> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_accept_donner_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RequestAcceptModelClass data = list.get(position);
        holder.txtusername.setText(data.getUserName());
        holder.txt_blood_type.setText(data.getBloodType());
        holder.txtcity.setText(data.getArea()+", "+data.getCity());
        holder.txtcno.setText(data.getContactNo());

        Glide.with(context).load(Config.Img + "" + list.get(position).getUserImage()).into(holder.imguser);

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
        TextView txt_blood_type, txtusername, txtcity, txtcno, btnDonated;
        CircleImageView imguser;

        public MyViewHolder(View view) {
            super(view);
            txt_blood_type = (TextView) view.findViewById(R.id.txt_blood_type);
            txtusername = (TextView) view.findViewById(R.id.txtusername);
            txtcity = (TextView) view.findViewById(R.id.txtcity);
            txtcno = (TextView) view.findViewById(R.id.txtcno);
            btnDonated = (TextView) view.findViewById(R.id.btnDonated);
            imguser = (CircleImageView) view.findViewById(R.id.imguser);
            itemView.setOnClickListener(this);
            btnDonated.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


}

