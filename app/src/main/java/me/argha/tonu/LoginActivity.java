package me.argha.tonu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.argha.tonu.utils.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Author: ARGHA K ROY
 * Date: 4/6/2016.
 */
public class LoginActivity extends AppCompatActivity {

    CallbackManager mCallbackManager;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @OnClick(R.id.loginGuestBtn)
    public void loginAsGuest(){
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.gPlusLoginBtn)
    public void startGPlusLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Util.printDebug("Gplus connection failed",connectionResult.getErrorMessage());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1001);
    }

    @OnClick(R.id.fbLoginBtn)
    public void startFbLogin() {
      //  FacebookSdk.sdkInitialize(getApplicationContext());
       // mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        profile = Profile.getCurrentProfile();
                        Log.d("user pro: ", profile.getName());



                        /*Util.printDebug("FB login", "success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Util.printDebug("FB response", response.toString());
*//*                                        try {
                                            //loginUser(null, object.getString("name"), object.getString("email"), object.getJSONObject("picture").getJSONObject("data").getString("url"), null, null, null);
                                        //} catch (JSONException e) {
                                            e.printStackTrace();
                                            Util.printDebug("Fb login excep", e.getMessage());
                                        }*//*
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();*/
                    }

                    @Override
                    public void onCancel() {
                        //     Log.d("user pro: ", profile.getName());
                        Util.printDebug("FB login", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Util.printDebug("FB login error", exception.getMessage());
                    }
                });

        profile = Profile.getCurrentProfile();

        Log.d("user pro: ", profile.getName());
        Log.d("user id: ", profile.getId());
       // Bitmap bitmap = getFacebookProfilePicture(profile.getId());
        try {
            Bitmap returned_bitmap = getCircleBitmap(new AsyncTaskRunner().execute(profile.getId()).get());
          //  imageView2.setImageBitmap(returned_bitmap);

         /*   try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            returned_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();*/

            Intent in1 = new Intent(this, MainActivity.class);
           // in1.putExtra("image",byteArray);
            in1.putExtra("BitmapImage",returned_bitmap);
            in1.putExtra("userName", profile.getName());

            startActivity(in1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //   LoginManager.getInstance().logOut();

    }

    /*public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");

            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    //    Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());


    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        Util.printDebug("Google sign result", result.isSuccess() + "");
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Util.printDebug("gPlus name",acct.getDisplayName());
            Util.printDebug("gPlus email", acct.getEmail());
            Util.printDebug("gPlus photo", acct.getPhotoUrl().toString());
            //loginUser(null, acct.getDisplayName(), acct.getEmail(), acct.getPhotoUrl().toString(), null, null, null);
        } else {
            Util.printDebug("Google sign", "signout");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Util.printDebug("Request code", requestCode + "");
        switch (requestCode){
            case 64206:
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
            case 1001:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, Bitmap> {

        private String resp;

        @Override
        protected Bitmap doInBackground(String... user) {
            Bitmap bitmap = null;
            String userI=Arrays.toString(user);
            String userID=userI.substring(1, (userI.length()-1));
            try {
                Log.d("Usid in asy: ", userID);
                URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=small");

                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            // execution of result of Long time consuming operation


        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }



}
