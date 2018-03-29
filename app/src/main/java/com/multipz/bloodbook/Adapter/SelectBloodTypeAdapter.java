package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.bloodbook.Model.BloodType;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class SelectBloodTypeAdapter extends RecyclerView.Adapter<SelectBloodTypeAdapter.PlanetViewHolder> {

    ArrayList<BloodType> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;
    private Shared shared;

    public SelectBloodTypeAdapter(Context context, ArrayList<BloodType> planetList) {
        this.con = context;
        this.planetList = planetList;
        shared = new Shared(con);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blood_group_type, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlanetViewHolder holder, final int position) {
        int data = planetList.get(position).getId();
        String type = planetList.get(position).getBloodtype();
        holder.txtbloodgroup.setText(type);
        // holder.txtbloodgroup_id.setText(data);
        holder.layoutbloodgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });


        if (row_index == position) {
            holder.layoutbloodgroup.setBackgroundColor(Color.parseColor("#d15241"));
            holder.txtbloodgroup.setTextColor(Color.parseColor("#ffffff"));
            shared.setBlood_type_id(planetList.get(position).getId());

        } else {
            holder.layoutbloodgroup.setBackgroundResource(R.drawable.backborderbloodtype);
            holder.txtbloodgroup.setTextColor(Color.parseColor("#000000"));
        }

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layoutbloodgroup;
        TextView txtbloodgroup, txtbloodgroup_id;

        public PlanetViewHolder(View view) {
            super(view);
            layoutbloodgroup = (LinearLayout) itemView.findViewById(R.id.layoutbloodgroup);
            txtbloodgroup = (TextView) itemView.findViewById(R.id.txtbloodgroup);
            txtbloodgroup_id = (TextView) itemView.findViewById(R.id.txtbloodgroup_id);

            itemView.setOnClickListener(this);
            layoutbloodgroup.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


}