package me.argha.tonu;

/**
 * Created by user pc on 5/4/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbEndpoint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.argha.tonu.app.EndPoints;
import me.argha.tonu.helpers.MyPreferenceManager;

public class RegularRegistrationActivity extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;
    @Bind(R.id.registerButton)Button registerButton;
    //   private Button loginButton;
   // private CheckBox showPassword;
    private EditText username;
    private EditText id;
    private EditText email;
    private EditText phonenumber;
    MyPreferenceManager preferenceManager;

   // RequestQueue requestQueue;
    private String insertUrl;
   // JsonObjectRequest request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferenceManager= new MyPreferenceManager(this);

        ButterKnife.bind(this);
        //   loginButton=(Button) findViewById(R.id.loginButton);
        //showPassword=(CheckBox) findViewById(R.id.content_registration_showPasswordCheckbox);
        email =(EditText) findViewById(R.id.content_registration_email);
        username=(EditText) findViewById(R.id.content_registration_username);
        id =(EditText) findViewById(R.id.content_registration_id);
        phonenumber =(EditText) findViewById(R.id.content_registration_phonenumber);


        //requestQueue = Volley.newRequestQueue(getApplicationContext());
        //   insertUrl = "http://192.168.0.39/fmc/index2.php?PIN=0001&password=abcd";


//        username.addTextChangedListener(new GenericTextWatcher(username));
//        id.addTextChangedListener(new GenericTextWatcher(id));
//        email.addTextChangedListener(new GenericTextWatcher(email));
//        phonenumber.addTextChangedListener(new GenericTextWatcher(phonenumber));

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });

        id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                } else {
                   // id.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, insertUrl, new Response.Listener<JSONObject>() {
//

                preferenceManager.editor.putBoolean(getResources().getString(R.string.is_user_logged_in), true);
                preferenceManager.editor.putString(getResources().getString(R.string.username), username.getText().toString());
                preferenceManager.editor.putString(getResources().getString(R.string.user_id), id.getText().toString());
                preferenceManager.editor.putString(getResources().getString(R.string.email), email.getText().toString());
                preferenceManager.editor.commit();

                Boolean is_user_logged_in=true;
                String name=username.getText().toString();
                String user_id=id.getText().toString();
                String mail= email.getText().toString();

                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put(getResources().getString(R.string.is_user_logged_in),true);
                params.put(getResources().getString(R.string.username), username.getText().toString());
                params.put(getResources().getString(R.string.user_id), id.getText().toString());
                params.put(getResources().getString(R.string.email), email.getText().toString());
                params.put("gcm_registration_id","gcm_id");
                String url= "http://192.168.0.106/tonu/v1/user/login";
                asyncHttpClient.post(EndPoints.LOGIN,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            Log.e("RegularRegistrationAct",response.toString(4));
                            if(response.getBoolean("error")==false) {
                                Toast.makeText(RegularRegistrationActivity.this,"Registration complete",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
              //  as.post(EndP)
                startActivity(new Intent(RegularRegistrationActivity.this, MainActivity.class));

//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            Log.i("TAG", response.getString("success") + ", " + response.getString("message"));
//                        } catch (JSONException e) {
//                            Log.i("TAG",e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });

                /*if(id.length()!=8){
                    Toast.makeText(RegularRegistrationActivity.this, "Invalid PIN, must be 8 digits", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                /*if (email.getText().toString().equals(phonenumber.getText().toString())) {
                    insertUrl = "http://192.168.0.39/fmc/index2.php?PIN="+id.getText().toString()+"&pass="+email.getText().toString();
                    request = new JsonObjectRequest(Request.Method.GET, insertUrl, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Log.i("TAG", response.getString("success") + ", " + response.getString("message"));
                            } catch (JSONException e) {
                                Log.i("TAG",e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Log.v("Registration",insertUrl);
                    Log.v("Registration", "" + request);
                    requestQueue.add(request);
                    Intent i=new Intent(RegularRegistrationActivity.this,MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(RegularRegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    phonenumber.setText("");
                }*/

            }
        });


//        insert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            Log.i("TAG",response.getString("success")+", "+response.getString("message"));
//                        } catch (JSONException e) {
//                            Log.i("TAG",e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//
//// {
//
//// @Override
//// protected Map<String, String> getParams() throws AuthFailureError {
//// Map<String,String> parameters = new HashMap<String, String>();
//// parameters.put("PIN",PIN.getText().toString());
//// parameters.put("email",email.getText().toString());
////
////
//// return parameters;
//// }
//                });
//                requestQueue.add(request);
//            }
//
//        });

//        username.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegularRegistrationActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        /*showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    email.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    phonenumber.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    email.setInputType(129);
                    phonenumber.setInputType(129);
                }
            }
        });*/






//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });




    }

    @OnClick(R.id.loginButton)
    public void submit(){
        Intent i = new Intent(RegularRegistrationActivity.this, MainActivity.class);
        startActivity(i);
    }

  /*  public void register(View view) {
        if(email.getText().toString().equals(phonenumber.getText().toString())){
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor;
            editor=prefs.edit();
           *//* editor.putString(getString(R.string.pref_user_username),username.getText().toString());
            editor.commit();*//*
            //  (new saveInDatabase(this)).execute(id.getText().toString(),email.getText().toString(),"Inactive","0.000","0.000");


        }*/
       /* else{
            Toast.makeText(RegularRegistrationActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
            email.setText("");
            phonenumber.setText("");
        }*/
    //}


//    private class GenericTextWatcher implements TextWatcher{
//
//        private View view;
//        private GenericTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//        public void afterTextChanged(Editable editable) {
//            String text = editable.toString();
//            switch(view.getId()){
//                case R.id.content_registration_username:
//                 //   model.setName(text);
//                    break;
//                case R.id.content_registration_pin:
//                //    model.setEmail(text);
//                    break;
//                case R.id.content_registration_password:
//                //    model.setPhone(text);
//
//                    break;
////                case R.id.content_registration_confirmPassword:
////                    //    model.setPhone(text);
////                 //   if((email.getText().toString()!=text)){
////                        Toast.makeText(Registration.this,"Password does not match",Toast.LENGTH_SHORT).show();
////                      //  email.setText("l");
////                     //   phonenumber.setText("");
////               //     }
////                    break;
//            }
//        }
   }


