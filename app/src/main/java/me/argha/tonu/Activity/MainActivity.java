package me.argha.tonu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.argha.tonu.R;
import me.argha.tonu.helpers.MyPreferenceManager;
import me.argha.tonu.utils.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bitmap bmp;
    @Bind(R.id.mainHelpBtn)
    ImageView mainHelpBtn;
    String filename;
    String username;



    MyPreferenceManager preferenceManager;
    boolean clicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        preferenceManager= new MyPreferenceManager(this);
        Toast.makeText(this,preferenceManager.pref.getBoolean(getResources().getString(R.string.is_user_logged_in),false)+"",Toast.LENGTH_LONG).show();
        Toast.makeText(this,preferenceManager.pref.getString(getResources().getString(R.string.username),"Shoumik"),Toast.LENGTH_LONG).show();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        username = getIntent().getStringExtra("userName");

    //    imageViewpropic.setImageBitmap(bitmap);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        ImageView nav_propic=(ImageView)hView.findViewById(R.id.profilePic);
        nav_user.setText(username);
        nav_propic.setImageBitmap(bitmap);
    //    Picasso.with(this).load("http://graph.facebook.com/" + "10153459358326496" + "/picture?type=small").into(nav_propic);
    //    nav_propic.setVisibility(View.VISIBLE);


    }

    @OnClick({R.id.mainReportBtn,R.id.mainDangerZoneBtn,R.id.mainHelpBtn,R.id.mainExpertHelpBtn})
    public void mainBtnClicks(View view){
        switch (view.getId()){
            case R.id.mainReportBtn:
                startActivity(new Intent(this,SharedLocationActivity.class));
                break;
            case R.id.mainDangerZoneBtn:
                Toast.makeText(MainActivity.this, "Showing all incidents previously occured", Toast
                        .LENGTH_SHORT).show();
                startActivity(new Intent(this,DangerZone.class));
                break;
            case R.id.mainHelpBtn:
                if(!clicked){

                    //TODO get location
                    String name= /*preferenceManager.getUser().getName();*/"Nabil";
                    String messageToSend = name+" is in an emergency and would like your help" +
                            ".\nAddress: Map coordinates\nLocation: 23.803435, 90.378862\n" +
                            "http://maps.google.com/?q=23.803435,90.378862";
//                    String number = "01621209959";
                    Set<String> numbers= preferenceManager.getEmergencyContactNumbers();
                    for(String n: numbers){
//                        SmsManager.getDefault().sendTextMessage(n, null, messageToSend, null,null);
                        Log.e("MainActivity","Sending a message to "+n);
                    }

                  //  showBeeAnimation();
                    mainHelpBtn.setImageResource(R.drawable.bebutton);
                    clicked=true;
                    Util.showToast(this, "Alert has been sent to emergency contacts and nearest " +
                            "police stations");
                    android.os.Handler handler= new android.os.Handler();
                    mainHelpBtn.setImageResource(R.drawable.afterbee);


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //mainHelpBtn.setImageResource(R.drawable.afterbee);
                            mainHelpBtn.setImageResource(R.drawable.bebutton);

                        }
                    }, 200);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainHelpBtn.setImageResource(R.drawable.afterbee);


                        }
                    }, 700);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainHelpBtn.setImageResource(R.drawable.bebutton);


                        }
                    }, 1200);




                }else {
                    clicked=false;
                    mainHelpBtn.setImageResource(R.drawable.bebutton);
                }
              //  showBeeAnimation();
                break;
            case R.id.mainExpertHelpBtn:
                showHelpOptionDialog();
                break;
            /*case R.id.mainForumBtn:
                startActivity(new Intent(this,ForumActivity.class));
                break;*/

        }
    }

    private void showBeeAnimation() {
        mainHelpBtn.setImageResource(R.drawable.bebutton);
        clicked=true;
        Util.showToast(this,"Alert has been sent to emergency contacts and nearest " +
                "police stations");
        android.os.Handler handler= new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHelpBtn.setImageResource(R.drawable.afterbee);
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHelpBtn.setImageResource(R.drawable.bebutton);
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHelpBtn.setImageResource(R.drawable.afterbee);
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHelpBtn.setImageResource(R.drawable.bebutton);
            }
        }, 500);
    }


    private void showHelpOptionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"CHAT","TALK"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                startActivity(new Intent(MainActivity.this,ChatActivity.class));
                                break;
                            case 1:
                                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:1234"));
                                startActivity(callIntent);*/
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setTitle("Please select - ");

        Dialog dialog=builder.create();
        dialog.show();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_help:
                startActivity(new Intent(this,IdentifyActivity.class));
                break;
            case R.id.menu_item_notification:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent=null;
        int id = item.getItemId();
        if (id == R.id.settings) {
            intent= new Intent(MainActivity.this,SettingsActivity.class);

        } else if (id == R.id.emergency_contacts) {
            intent= new Intent(MainActivity.this,EmergencyContactsActivity.class);
        } else if (id == R.id.profile) {
            intent= new Intent(MainActivity.this,ProfileActivity.class);
        } else if (id == R.id.faq) {
            intent= new Intent(MainActivity.this,FAQActivity.class);
        } else if (id == R.id.logout) {
            preferenceManager.clear();
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        else if (id == R.id.laws) {

        } else if (id == R.id.terms_and_conditions) {

        }
        if(intent!=null) startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
