package com.nitsilchar.hp.passwordStorage.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.adapter.PasswordRecyclerViewAdapter;
import com.nitsilchar.hp.passwordStorage.database.PasswordDatabase;
import com.nitsilchar.hp.passwordStorage.model.Accounts;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PasswordRecyclerViewAdapter.AccountsAdapterListener {

    private static final String SHARED_PREFS_NAME="MyPrefs";
    private RecyclerView recyclerView;
    TextView emptyText, email;
    ImageView profile;
    PasswordRecyclerViewAdapter adapter;
    List<String> collection;
    List<String> myList=new ArrayList<String>();
    List<Accounts> accountsList;
    PasswordDatabase passwordDatabase;
    private SearchView searchView;
    int flag=1;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        passwordDatabase = new PasswordDatabase(getApplicationContext());
        myList = getArray();
        collection = new ArrayList<>();
        accountsList = getAccounts();
        recyclerView = (RecyclerView) findViewById(R.id.listViewID);
        emptyText = (TextView)findViewById(R.id.text2);
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        email = (TextView) header.findViewById(R.id.edt_profile_email);
        profile = (ImageView) header.findViewById(R.id.img_profile_picture);
        email.setText(getIntent().getStringExtra("email"));
        adapter = new PasswordRecyclerViewAdapter(this, accountsList,  this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        try{profile.setImageBitmap(passwordDatabase.getPic());
            RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(passwordDatabase.getPic());
            profile.setImageDrawable(roundedImageDrawable);}
        catch(Exception e){flag=0; profile.setImageResource(R.mipmap.ic_launcher_round);}

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFloatingAdditionButton();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void onClickFloatingAdditionButton() {
        final AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.custom_dialog,null);
        dialogBuilder.setView(dialogView);
        final EditText acnt=(EditText)dialogView.findViewById(R.id.dialogEditAccID);
        final EditText pass=(EditText)dialogView.findViewById(R.id.dialogEditPassID);
        final EditText description=(EditText)dialogView.findViewById(R.id.dialogEditDescriptionID);
        //added by Arti
        final EditText link=(EditText)dialogView.findViewById(R.id.dialogEditLink);
        dialogBuilder.setIcon(R.mipmap.icon);
        dialogBuilder.setTitle(R.string.main_acnt_info);
        dialogBuilder.setPositiveButton(R.string.main_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                collection=passwordDatabase.getAcc();
                if(!isDuplicate(collection,acnt.getText().toString())){
                    if(!TextUtils.isEmpty(acnt.getText().toString())){
                        passwordDatabase.addCredentials(getApplicationContext(),acnt.getText().toString(),
                                pass.getText().toString(), description.getText().toString(), link.getText().toString());
                        Accounts account = new Accounts(acnt.getText().toString(), pass.getText().toString(), description.getText().toString(), link.getText().toString());
                        accountsList.add(account);
                        adapter.notifyDataSetChanged();
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
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_button)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean saveArray(){
        SharedPreferences sp=this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> set=new HashSet<String>();
        set.addAll(myList);
        editor.putStringSet("list",set);
        return editor.commit();
    }

    public List<String> getArray(){
        /*SharedPreferences sp=this.getSharedPreferences(SHARED_PREFS_NAME,Activity.MODE_PRIVATE);
        Set<String> set=sp.getStringSet("list",new HashSet<String>());
        return new ArrayList<String>(set);*/
        List accounts=passwordDatabase.getAcc();
        return accounts;
    }

    public List<Accounts> getAccounts(){
        List accountsData = passwordDatabase.getAccData();
        return accountsData;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(),
                        R.string.main_logout, Toast.LENGTH_LONG).show();
                SplashActivity.editor.remove("loginTest");
                SplashActivity.editor.commit();
                Intent sendToLoginAndRegistration = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(sendToLoginAndRegistration);
                break;
            case R.id.nav_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                settings.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(settings);
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Are you frustrated with remembering numerous passwords, and want a secure way of saving them paperlessly? Download the app now and memorize only one master password! https://goo.gl/X1yJj2";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Password Storage");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                break;
            case R.id.nav_about_us:
                Intent sendToAboutUs = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(sendToAboutUs);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAccountSelected(Accounts accounts) {
        Toast.makeText(getApplicationContext(), "Selected: " +accounts.getmAccountName() , Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if(flag==1) {passwordDatabase.updatePic(bitmap); }
                else if(flag==0) {passwordDatabase.setPic(bitmap); flag=1; }

                profile.setImageBitmap(bitmap);
                //round image
                RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(bitmap);
                profile.setImageDrawable(roundedImageDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Picture");
        builder.setMessage("Proceed to Edit profile picture?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private RoundedBitmapDrawable createRoundedBitmapImageDrawableWithBorder(Bitmap bitmap){
        int bitmapWidthImage = bitmap.getWidth();
        int bitmapHeightImage = bitmap.getHeight();
        int borderWidthHalfImage = 4;

        int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
        int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
        int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;

        Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
        Canvas mcanvas = new Canvas(roundedImageBitmap);
        mcanvas.drawColor(Color.RED);
        int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
        int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;

        mcanvas.drawBitmap(bitmap, i, j, null);

        Paint borderImagePaint = new Paint();
        borderImagePaint.setStyle(Paint.Style.STROKE);
        borderImagePaint.setStrokeWidth(borderWidthHalfImage*2);
        borderImagePaint.setColor(Color.GRAY);
        mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),roundedImageBitmap);
        roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }

}




