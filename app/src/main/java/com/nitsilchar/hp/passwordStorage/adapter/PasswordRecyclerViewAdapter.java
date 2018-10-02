package com.nitsilchar.hp.passwordStorage.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;
import android.widget.Filterable;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.activity.DetailsActivity;
import com.nitsilchar.hp.passwordStorage.activity.SplashActivity;
import com.nitsilchar.hp.passwordStorage.database.PasswordDatabase;
import com.nitsilchar.hp.passwordStorage.model.Accounts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordRecyclerViewAdapter extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> implements Filterable{

    AccountsFilter accountsFilter;
    private Context context;
    private List<Accounts> accounts;
    private List<Accounts> accountsFiltered;
    private AccountsAdapterListener listener;
    PasswordDatabase passwordDatabase;
    public PasswordRecyclerViewAdapter() {
    }

    public PasswordRecyclerViewAdapter(Context context, List<Accounts> accounts, AccountsAdapterListener listener) {
        this.listener = listener;
        this.context = context;
        this.accounts = accounts;
        this.accountsFiltered =  accounts;
        passwordDatabase = new PasswordDatabase(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.linear_layout_simple_text, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.accountName.setText(accountsFiltered.get(position).getmAccountName());
        holder.description.setText(accountsFiltered.get(position).getmDescription());
        holder.iconText.setText(accountsFiltered.get(position).getmAccountName().substring(0,1).toUpperCase());
        holder.iconBg.setImageResource(R.drawable.bg_circle);
        holder.iconBg.setColorFilter(getRandomMaterialColor());
        if (!accountsFiltered.get(position).getmFav().equals("0"))
            holder.iconFav.setImageResource(R.drawable.ic_star_black_24dp);
        else
            holder.iconFav.setImageResource(R.drawable.ic_star_border_black_24dp);
        holder.accountContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = accountsFiltered.get(position).getmAccountName();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("Site", data);
                context.startActivity(intent);
            }
        });
        holder.accountContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final View dialogView=LayoutInflater.from(context).inflate(R.layout.confirm_delete,null);
                dialogBuilder.setView(dialogView);
                final EditText pass1=(EditText)dialogView.findViewById(R.id.passDialog);
                dialogBuilder.setTitle("Confirm deletion?");
                dialogBuilder.setIcon(R.mipmap.icon);
                dialogBuilder.setPositiveButton(R.string.main_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pass1.getText().toString().equals(SplashActivity.sh.getString("password",null))){
                            Toast.makeText(context,
                                    "Deleted!",Toast.LENGTH_SHORT).show();
                            passwordDatabase.deleteRow(accountsFiltered.get(position).getmAccountName());
                            accounts.remove(accountsFiltered.get(position));
                            notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(context,R.string.main_wrong_pass,Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialogBuilder.setNegativeButton(R.string.main_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b= dialogBuilder.create();
                b.show();
                return false;
            }
        });
        holder.iconFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedAccount = accountsFiltered.get(position).getmAccountName();
                if (!accountsFiltered.get(position).getmFav().equals("0")){
                    passwordDatabase.setFavorite(clickedAccount, "0");
                    holder.iconFav.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
                else{
                    passwordDatabase.setFavorite(clickedAccount, "1");
                    holder.iconFav.setImageResource(R.drawable.ic_star_black_24dp);
                }
            }
        });
    }

    private int getRandomMaterialColor() {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_400", "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public int getItemCount() {
        return accountsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        if (accountsFilter == null)
            accountsFilter = new AccountsFilter();

        return accountsFilter;
    }

    private class AccountsFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                accountsFiltered = accounts;
            } else {
                List<Accounts> filteredList = new ArrayList<>();
                for (Accounts row : accounts) {

                    if (row.getmAccountName().toUpperCase().contains(charString.toUpperCase())) {
                        filteredList.add(row);
                    }
                }
                accountsFiltered = filteredList;
            }
            filterResults.values = accountsFiltered;
            filterResults.count= accountsFiltered.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            accountsFiltered = (ArrayList<Accounts>) filterResults.values;
            notifyDataSetChanged();
        }
    }


        class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.account_container)
        LinearLayout accountContainer;
        @BindView(R.id.account_name)
        TextView accountName;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.icon_text)
        TextView iconText;
        @BindView(R.id.icon_bg)
        ImageView iconBg;
        @BindView(R.id.icon_fav)
        private ImageView iconFav;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onAccountSelected(accounts.get(getAdapterPosition()));
                }
            });
        }

    }

    public interface AccountsAdapterListener {
        void onAccountSelected(Accounts accounts);
    }
}
