package com.nitsilchar.hp.passwordStorage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String SHARED_PREFS_NAME="MyPrefs";
    static ListView listView;
    TextView emptyText;
    ArrayAdapter<String> adapter;
    List<String> collection;
    ArrayList<String> myList=new ArrayList<String>();
    PasswordDatabase passwordDatabase;
    AdapterView.AdapterContextMenuInfo info;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        passwordDatabase=new PasswordDatabase(getApplicationContext());
        myList=getArray();
        collection=new ArrayList<>();
        listView=(ListView)findViewById(R.id.listViewID);
        emptyText=(TextView)findViewById(R.id.text2);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item,myList);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyText);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addmenu:
                final AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);
                LayoutInflater inflater=this.getLayoutInflater();
                final View dialogView=inflater.inflate(R.layout.custom_dialog,null);
                dialogBuilder.setView(dialogView);
                final EditText acnt=(EditText)dialogView.findViewById(R.id.dialogEditAccID);
                final EditText pass=(EditText)dialogView.findViewById(R.id.dialogEditPassID);
                dialogBuilder.setIcon(R.mipmap.icon);
                dialogBuilder.setTitle(R.string.main_acnt_info);
                dialogBuilder.setPositiveButton(R.string.main_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collection=passwordDatabase.getAcc();
                        if(!isDuplicate(collection,acnt.getText().toString())){
                            if(!TextUtils.isEmpty(acnt.getText().toString())){
                                myList.add(acnt.getText().toString());
                                adapter.notifyDataSetChanged();
                                passwordDatabase.addCredentials(getApplicationContext(),acnt.getText().toString(),pass.getText().toString());
                                Toast.makeText(getApplicationContext(),
                                        "Added "+acnt.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),
                                        R.string.main_empty_field,Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    R.string.main_duplicate,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialogBuilder.setNegativeButton(R.string.main_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                listView.setAdapter(adapter);
                return true;
            case R.id.logout:
                Toast.makeText(getApplicationContext(),
                        R.string.main_logout, Toast.LENGTH_LONG).show();
                SplashActivity.editor.remove("loginTest");
                SplashActivity.editor.commit();
                Intent sendToLoginandRegistration = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(sendToLoginandRegistration);
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public boolean saveArray(){
        SharedPreferences sp=this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> set=new HashSet<String>();
        set.addAll(myList);
        editor.putStringSet("list",set);
        return editor.commit();
    }

    public ArrayList<String> getArray(){
        SharedPreferences sp=this.getSharedPreferences(SHARED_PREFS_NAME,Activity.MODE_PRIVATE);
        Set<String> set=sp.getStringSet("list",new HashSet<String>());
        return new ArrayList<String>(set);
    }

   @Override
    protected void onPause() {
        super.onPause();
        saveArray();
    }

    @Override
    protected void onStop() {
        saveArray();
        super.onStop();
    }
    public boolean isDuplicate(List<String> col,String value){
        boolean isDuplicate=false;
        for(String s:col){
            if(s.equals(value)){
                isDuplicate=true;
                break;
            }
        }
        return isDuplicate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String data=myList.get(position);
        Intent intent=new Intent(this,Details.class);
        intent.putExtra("Site",data);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainActivity.this,
                    SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        s=myList.get(info.position);
        if(item.getItemId()==R.id.deletecontext){
            AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);
            LayoutInflater inflater=this.getLayoutInflater();
            final View dialogView=inflater.inflate(R.layout.confirm_delete,null);
            dialogBuilder.setView(dialogView);
            final EditText pass1=(EditText)dialogView.findViewById(R.id.passDialog);
            dialogBuilder.setTitle("Are you sure you want to delete "+s);
            dialogBuilder.setIcon(R.mipmap.icon);
            dialogBuilder.setPositiveButton(R.string.main_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(pass1.getText().toString().equals(SplashActivity.sh.getString("password",null))){
                        Toast.makeText(getApplicationContext(),
                                "Deleted "+s,Toast.LENGTH_SHORT).show();
                        passwordDatabase.deleteRow(String.valueOf(info.position));
                        myList.remove(info.position);
                        adapter.notifyDataSetChanged();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),R.string.main_wrong_pass,Toast.LENGTH_LONG);
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
        else
        return super.onContextItemSelected(item);
    }


}




