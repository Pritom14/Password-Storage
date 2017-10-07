package com.nitsilchar.hp.passwordStorage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.details;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aditya on 8/10/17.
 */

public class PasswordListReyclerView extends RecyclerView.Adapter<PasswordListReyclerView.PasswordListViewHolder> {

    Context context;
    ArrayList<String> passwordList;
    String TAG="MainActivity";

    public PasswordListReyclerView(Context context, ArrayList<String> passwordList) {
        this.context = context;
        this.passwordList = passwordList;
        Log.d(TAG,"myList.size()="+passwordList.size());

    }

    @Override
    public PasswordListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_list_layout, parent, false);

        return new PasswordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PasswordListViewHolder holder, int position) {
        holder.accountName.setText(passwordList.get(position));

    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public class PasswordListViewHolder extends RecyclerView.ViewHolder {
        TextView accountName;
        RelativeLayout layout;
        View line;
        public PasswordListViewHolder(View itemView) {

            super(itemView);
            accountName=(TextView)itemView.findViewById(R.id.account_name);
            layout=(RelativeLayout)itemView.findViewById(R.id.password_layout);
            line=itemView.findViewById(R.id.thin_line);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data=passwordList.get(getAdapterPosition());
                    Intent intent=new Intent(context,details.class);
                    intent.putExtra("Site",data);
                    context.startActivity(intent);
                }
            });

        }
    }
}
