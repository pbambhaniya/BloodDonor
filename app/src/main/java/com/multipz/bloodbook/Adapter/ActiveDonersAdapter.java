package com.multipz.bloodbook.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class ActiveDonersAdapter extends RecyclerView.Adapter<ActiveDonersAdapter.PlanetViewHolder> {

    ArrayList<String> planetList;
    private Context con;
    private int row_index = 0;
        private ItemClickListener clickListener;

    public ActiveDonersAdapter(Context context, ArrayList<String> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_active_doner_select, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        String data = planetList.get(position);
        holder.txtbloodgrouptype_active.setText(data);

        if (row_index == position) {
            holder.txtbloodgrouptype_active.setTextColor(Color.parseColor("#d15241"));
        } else {
            holder.txtbloodgrouptype_active.setTextColor(Color.parseColor("#ffffff"));
        }

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtbloodgrouptype_active;

        public PlanetViewHolder(View view) {
            super(view);
            txtbloodgrouptype_active = (TextView) view.findViewById(R.id.txtbloodgrouptype_active);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
                row_index = getAdapterPosition();
                notifyDataSetChanged();

            }
        }
    }
}