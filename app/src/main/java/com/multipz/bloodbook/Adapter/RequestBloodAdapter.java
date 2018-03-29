package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Model.BloodRequestModelClass;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 18-11-2017.
 */

public class RequestBloodAdapter extends RecyclerView.Adapter<RequestBloodAdapter.PlanetViewHolder> {

    ArrayList<BloodRequestModelClass> RequestList;
    private Context con;
    private int row_index = -1;
    private ItemClickListener clickListener;
    private Shared shared;

    public RequestBloodAdapter(Context context, ArrayList<BloodRequestModelClass> planetList) {
        this.con = context;
        this.RequestList = planetList;
        shared = new Shared(con);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_blood, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlanetViewHolder holder, final int position) {

        BloodRequestModelClass modelClass = RequestList.get(position);
        holder.txtusername.setText(modelClass.getUserName());
        holder.city.setText(modelClass.getAddress() + "," + modelClass.getArea() + "," + modelClass.getCity());
        holder.contact_no.setText("+91 " + modelClass.getContactNo());
        holder.txtblood_type.setText(modelClass.getBloodType());
        holder.txtDistance.setText(modelClass.getDistance() + " Away");

        Glide.with(con).load(Config.Img + "" + RequestList.get(position).getUserImage()).into(holder.userimage);

    }


    @Override
    public int getItemCount() {
        return RequestList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtusername, city, contact_no, txtblood_type, txtDistance;
        CircleImageView userimage;

        public PlanetViewHolder(View view) {
            super(view);
            userimage = (CircleImageView) itemView.findViewById(R.id.userimage);

            txtusername = (TextView) itemView.findViewById(R.id.txtusername);
            city = (TextView) itemView.findViewById(R.id.city);
            contact_no = (TextView) itemView.findViewById(R.id.contact_no);
            txtblood_type = (TextView) itemView.findViewById(R.id.txtblood_type);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


}