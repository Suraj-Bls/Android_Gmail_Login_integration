package com.careator.demogooglelog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout profid;
    private Button signout;
    private SignInButton signin;
    private ImageView imageview;
    private TextView email;
    private TextView name;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profid = findViewById(R.id.profid);
        signout = findViewById(R.id.signout);
        signin = findViewById(R.id.signin);
        name = findViewById(R.id.nametv);
        email = findViewById(R.id.emailtv);
        imageview = findViewById(R.id.imageview);

        signin.setOnClickListener(this);
        signout.setOnClickListener(this);

        profid.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signin:
                signIn();
                break;
            case R.id.signout:
                signOut();
                break;


        }



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {

        Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String namea= account.getDisplayName();
            String emaila= account.getEmail();
            String image_url= account.getPhotoUrl().toString();

            name.setText(namea);
            email.setText(emaila);
            Glide.with(this).load(image_url).into(imageview);
            updateUI(true);
        }
        else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin){
    if(isLogin)
    {
        profid.setVisibility(View.VISIBLE);
        signin.setVisibility(View.GONE);
    }
        else{

        profid.setVisibility(View.GONE);
        signin.setVisibility(View.VISIBLE);

    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==REQ_CODE)
    {
        GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        handleResult(result);

    }

    }
}
