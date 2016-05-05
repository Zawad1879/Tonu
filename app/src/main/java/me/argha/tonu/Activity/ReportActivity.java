package me.argha.tonu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.argha.tonu.R;
import me.argha.tonu.app.Config;
import me.argha.tonu.gcm.GcmIntentService;
import me.argha.tonu.model.Message;

public class ReportActivity extends AppCompatActivity implements OnClickListener{
    @Bind(R.id.report_name_textview)TextView reportNameTv;
    @Bind(R.id.report_name_edittext)EditText reportNameEt;
    @Bind(R.id.report_description_textview)TextView reportDescTv;
    @Bind(R.id.report_description_edittext)EditText reportDescEt;
    @Bind(R.id.report_voice_btn)TextView reportVoiceBtn;
    @Bind(R.id.report_submit_btn)TextView reportSubmitBtn;
    @Bind(R.id.report_activity_ll)LinearLayout reportActivityLinearLayout;

    private String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        reportSubmitBtn.setOnClickListener(this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");

                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }else{
            finish();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id==R.id.report_submit_btn){
            submitReport();
        }else if(id==R.id.report_voice_btn){

        }
    }

    private void submitReport() {
        String name, desc, nationalId;
        name=reportNameEt.getText().toString();
        desc=reportDescEt.getText().toString();
        Toast.makeText(ReportActivity.this, "Your report has been successfully submitted and will" +
                " be reviewed", Toast
                .LENGTH_LONG).show();
        reportNameEt.setText("");
        reportDescEt.setText("");
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    /**
     * Handles new push notification
     */
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            Message message = (Message) intent.getSerializableExtra("message");
            Toast.makeText(getApplicationContext(), "New push: " + message.getMessage(), Toast.LENGTH_LONG).show();
            TextView receivedMsgTv= new TextView(this);
            receivedMsgTv.setText(message.getMessage());
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity= Gravity.START;
            receivedMsgTv.setLayoutParams(lp);
//            receivedMsgTv.setPadding(10, 5, 50, 5);
            receivedMsgTv.setBackgroundColor(0xfff);
//            receivedMsgTv.setTextColor(0x000);
            reportActivityLinearLayout.addView(receivedMsgTv);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
