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

import com.facebook.stetho.Stetho;
import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.ApiError;
import com.remygratwohl.taskery.models.SessionManager;
import com.remygratwohl.taskery.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // UI Components
    private EditText mEmailTextField;
    private EditText mPasswordTextField;
    private Button mLoginButton;
    private TextView mSignUpLink;

    private View mLoginFormView;
    private View mProgressView;

    // Keep track and allow only one concurrent login task.
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enable Stetho Tools
        Stetho.initializeWithDefaults(this);

        // Bypass login if user session already exists;
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SessionManager sManager = new SessionManager(getApplicationContext());

        // Check for cached user data
        if (sManager.doesSessionAlreadyExist() ){ // Does the user's data exists in shared prefs

            User u = sManager.retrieveSessionsUser();
            Intent intent;

            // The user does not have a character (they quit during selection)
            if(dbHelper.getCharacter(u.getEmail()) == null){
                intent = new Intent(getApplicationContext(), CharacterClassSelectActivity.class);
            }else{ // found a saved character
                intent = new Intent(getApplicationContext(), QuestLogActivity.class);
            }

            startActivity(intent);
            finish();
        }

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

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_the_right,R.anim.slide_to_the_left);

            }
        });

    }

    @Override
    public void onBackPressed(){
        // Do nothing :^)
    }

    /**
     * Attempts to sign in the user specified by the login form.
     * If there are form errors, the errors are presented and no
     * actual login attempt is made.
     */
    private void attemptLogin(){

        // Make sure another login task is not running
        if (mAuthTask != null) { return; }

        // Reset the errors on the text fields.
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
    private class UserLoginTask extends AsyncTask<Void, Void, Response<User>> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Response<User> doInBackground(Void... params) {

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

            if(response == null){ // network timeout

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Network Timeout")
                        .setMessage("Unable to connect to the Taskery Server")
                        .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }})
                        .setCancelable(true)
                        .show();
                return;
            }

            if(response.isSuccessful()){    // User data retrieved successfully
                User user = response.body();
                Log.d("DATA",user.toString());

                SessionManager sManager = new SessionManager(getApplicationContext());
                sManager.createUserSession(user);

                Intent intent;

                if (user.getCharacter() == null){
                    /*
                     * If no character exists, it's a new user so go to the creation screen
                     */
                    intent = new Intent(getApplicationContext(),
                            CharacterClassSelectActivity.class);
                }else{
                    // Write the data to the database
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.createCharacter(user.getCharacter(),user);

                    intent = new Intent(getApplicationContext(), QuestLogActivity.class);

                }

                startActivity(intent);
                finish();

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
        // Use these APIs to fade-in the progress spinner.
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
    }
}
