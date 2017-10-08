package com.nitsilchar.hp.passwordStorage.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nitsilchar.hp.passwordStorage.PasswordDatabase;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.SplashActivity;
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
    PasswordDatabase passwordDatabase;


    public PasswordListReyclerView(Context context, ArrayList<String> passwordList) {
        this.context = context;
        this.passwordList = passwordList;
        Log.d(TAG,"myList.size()="+passwordList.size());
        passwordDatabase=new PasswordDatabase(context);


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

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
                    LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
                    final View dialogView=inflater.inflate(R.layout.confirm_delete,null);
                    dialogBuilder.setView(dialogView);
                    final EditText pass1=(EditText)dialogView.findViewById(R.id.passDialog);
                    dialogBuilder.setTitle("Are you sure you want to delete "+passwordList.get(getAdapterPosition()));
                    dialogBuilder.setIcon(R.mipmap.icon);
                    dialogBuilder.setPositiveButton(R.string.main_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(pass1.getText().toString().equals(SplashActivity.sh.getString("password",null))){
                                Toast.makeText(context,
                                        "Deleted "+passwordList.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                                passwordDatabase.deleteRow(passwordList.get(getAdapterPosition()));
                                passwordList.remove(getAdapterPosition());
                                notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(context,R.string.main_wrong_pass,Toast.LENGTH_LONG);
                            }
                        }
                    });

                    dialogBuilder.setNegativeButton(R.string.main_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog b=dialogBuilder.create();
                    b.show();
                    return true;
                }

            });

        }
    }
}
