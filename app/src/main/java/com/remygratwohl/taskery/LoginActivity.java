package com.remygratwohl.taskery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.remygratwohl.taskery.models.ApiError;
import com.remygratwohl.taskery.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailTextField;
    private EditText mPasswordTextField;
    private Button mLoginButton;
    private TextView mSignUpLink;

    private View mLoginFormView;
    private View mProgressView;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup UI connections
        mEmailTextField = (EditText) findViewById(R.id.email);
        mPasswordTextField = (EditText) findViewById(R.id.password);
        mSignUpLink = (TextView) findViewById(R.id.sign_up);
        mLoginButton = (Button) findViewById(R.id.login);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        mPasswordTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {    // If
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in the user specified by the login form.
     * If there are form errors, the errors are presented and no
     * actual login attempt is made.
     */
    private void attemptLogin(){
        if (mAuthTask != null) {
            return;
        }

        // Reset the errors.
        mEmailTextField.setError(null);
        mPasswordTextField.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailTextField.getText().toString();
        String password = mPasswordTextField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Make sure a username was entered. TODO: Any extra validation
        if (TextUtils.isEmpty(email)) {
            mEmailTextField.setError(getString(R.string.error_field_required));
            focusView = mEmailTextField;
            cancel = true;
        /*} else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true; */
        }

        // Make sure a password was entered. TODO: Any extra validation
        if (TextUtils.isEmpty(password)) {
            mPasswordTextField.setError(getString(R.string.error_field_required));
            focusView = mPasswordTextField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Response<User>> {

        private final String mEmail;
        private final String mPassword;

        static final String BASE_URL = "https://git.eclipse.org/r/";

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Response<User> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            TaskeryAPI taskeryAPI = TaskeryAPI.retrofit.create(TaskeryAPI.class);

            try{
                Call<User> call = taskeryAPI.loginUser(mEmail,mPassword);
                Response<User> response = call.execute();

                return response;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<User> response) {
            mAuthTask = null;
            showProgress(false);


            if(response.isSuccessful()){    // User data retrieved successfully
                User user = response.body();
                Log.d("DATA",user.toString());

                //TODO: SAVE USER DATA IN PREFS AND GO TO NEXT ACTIVITY
            }else{  //
                ApiError error = ErrorUtils.parseError(response);
                Log.d("DATA",error.toString());

                if(error.getName().equals("WrongPassword")){
                    mPasswordTextField.setError(error.getDescription());
                    mPasswordTextField.requestFocus();
                }else if(error.getName().equals("MissingUser")){
                    mEmailTextField.setError(error.getDescription());
                    mEmailTextField.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
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
