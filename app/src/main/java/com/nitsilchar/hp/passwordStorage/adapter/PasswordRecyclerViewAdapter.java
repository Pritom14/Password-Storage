package com.nitsilchar.hp.passwordStorage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.activity.DetailsActivity;

import java.util.List;

public class PasswordRecyclerViewAdapter extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<String> accounts;

    public PasswordRecyclerViewAdapter(Context context, List<String> accounts) {
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.linear_layout_simple_text, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(accounts.get(position));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = accounts.get(position);
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra("Site",data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textview_title);
        }
    }
}
