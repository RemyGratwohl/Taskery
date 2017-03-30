package com.remygratwohl.taskery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.remygratwohl.taskery.models.ApiError;
import com.remygratwohl.taskery.models.User;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailTextField;
    private EditText mPasswordTextField;
    private Button mRegisterButton;

    private View mLoginFormView;
    private View mProgressView;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private RegistrationTask mRegTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Setup UI connections
        mEmailTextField = (EditText) findViewById(R.id.email);
        mPasswordTextField = (EditText) findViewById(R.id.password);
        mRegisterButton = (Button) findViewById(R.id.register);
        mProgressView = findViewById(R.id.register_progress);
        mLoginFormView = findViewById(R.id.register_form);

        mPasswordTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {    // If
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration(){

        // Make sure only one attempt can happen at once
        if (mRegTask != null) {
            return;
        }

        // Reset the errors.
        mEmailTextField.setError(null);
        mPasswordTextField.setError(null);

        // Store values at the time of the registration attempt.
        String email = mEmailTextField.getText().toString();
        String password = mPasswordTextField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailTextField.setError(getString(R.string.error_field_required));
            focusView = mEmailTextField;
            cancel = true;
        }else if(!ErrorUtils.validateEmail(email)){
            mEmailTextField.setError(getString(R.string.error_invalid_email));
            focusView = mEmailTextField;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordTextField.setError(getString(R.string.error_field_required));
            focusView = mPasswordTextField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt registration and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mRegTask = new RegistrationTask(email,password);
            mRegTask.execute((Void) null);
        }

    }

    public class RegistrationTask extends AsyncTask<Void, Void, Response<JsonObject>>{

        private final String mEmail;
        private final String mPassword;

        RegistrationTask(String email, String password){
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Response<JsonObject> doInBackground(Void... params){

            TaskeryAPI taskeryAPI = TaskeryAPI.retrofit.create(TaskeryAPI.class);

            try{

                Call<JsonObject> call = taskeryAPI.registerUser(mEmail,mPassword);
                Response<JsonObject> response = call.execute();

                return response;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<JsonObject> response) {
            mRegTask = null;
            showProgress(false);

            if(response == null){ // network timeout

                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Network Timeout")
                        .setMessage("Unable to connect to the Taskery Server")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }})
                        .setCancelable(true)
                        .show();
                return;
            }

            if(response.isSuccessful()){

                Log.d("DATA",response.body().toString());

                Toast toast = Toast.makeText(getApplicationContext(),
                        response.body().get("message").toString(),Toast.LENGTH_SHORT);
                toast.show();

                // Go back to sign in
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }else{
                ApiError error = ErrorUtils.parseError(response);
                Log.d("DATA",error.toString());

                if(error.getName().equals("EmailExists")){
                    mEmailTextField.setError(error.getDescription());
                    mEmailTextField.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and disable the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

