package project.cse3310;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginAndReg extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private static final String TAG = "CustomAuthActivity";
    private String mCustomToken;
    private ProgressDialog progressDialog;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private EditText emailView;
    private EditText passwordNewView;
    private EditText passwordConfirmView;
    private EditText nameView;
    private EditText addressView;
    private EditText stateView;
    private EditText zipView;
    private EditText phoneView;
    private EditText birthDateView;

    private View mProgressView;
    private View mLoginFormView;
    private View regFormView;
    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);


        /* underline register textView */
        TextView tv = findViewById(R.id.register_text);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL;
            }
        });
        //login button listener
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailView.getText().toString().isEmpty())
                    showToast(LoginAndReg.this, "Please enter your email!");
                else if (mPasswordView.getText().toString().isEmpty())
                    showToast(LoginAndReg.this, "Please enter your password!");
                else {
                    showLoginDialog();
                    attemptLogin();
                }
            }
        });
        // register button listener
        Button regButton = findViewById(R.id.register_account);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReg(); }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void startSignIn(){
        mAuth.signInWithCustomToken(mCustomToken).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   Log.d(TAG, "signInWithCustomToken:success");
                   FirebaseUser user = mAuth.getCurrentUser();

               } else {
                   Log.w(TAG, "signInWithCustomToken:failure",task.getException());
                   Toast.makeText(LoginAndReg.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginAndReg.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showToast(LoginAndReg.this, "Login Error!");
                            progressDialog.dismiss();
                        }
                        else {
                            Intent intent = new Intent(LoginAndReg.this, Main.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                });
    }
    /********************************
     *   Register User Account      *
     ********************************/
    private void attemptReg() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        emailView= findViewById(R.id.new_email);
        passwordNewView = findViewById(R.id.new_password);
        passwordConfirmView = findViewById(R.id.confirm_password);
        nameView = findViewById(R.id.full_name);
        addressView = findViewById(R.id.address);
        stateView = findViewById(R.id.state);
        zipView = findViewById(R.id.zip_code);
        phoneView = findViewById(R.id.phone);
        birthDateView = findViewById(R.id.birth_date);

        String email = emailView.getText().toString();
        String password = passwordNewView.getText().toString();
        String password2 = passwordConfirmView.getText().toString();
        String name = nameView.getText().toString();
        String address = addressView.getText().toString();
        String state = stateView.getText().toString();
        String zip = zipView.getText().toString();
        String phone = phoneView.getText().toString();
        String birthDate = birthDateView.getText().toString();

        DatabaseReference fb = mDatabase.getReference("user");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordNewView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!password.equals(password2)){
            passwordConfirmView.setError("The passwords do not match");
        }
        // Check for a valid email address.
       if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showRegDialog();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginAndReg.this, new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        showToast(LoginAndReg.this, "Register failed!");
                    } else {
                        showToast(LoginAndReg.this, "Register successful!");
                        startActivity(new Intent(LoginAndReg.this,LoginAndReg.class));
                        progressDialog.dismiss();
                        finish();
                    }
                }
            });

            /* show toast on successfull login -- possibly inccorect spot or interrupted */
            Context context = getApplicationContext();
            CharSequence text = "Logged In Successfully";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
           // mAuthTask.execute((Void) null);
        }
    }
    /* textView click to register */
    public void register(View view) {
        // fill spinner with categories
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ViewAnimator vf = findViewById(R.id.viewFlipper);
        vf.showNext();
    }
    public void cancelReg(View view) {
        ViewAnimator va = findViewById(R.id.viewFlipper);
        va.showPrevious();
    }
    public static boolean hasText(TextInputLayout inputLayout) {
        return !inputLayout.getEditText().getText().toString().trim().equals("");
    }

    public static String getText(TextInputLayout inputLayout) {
        return inputLayout.getEditText().getText().toString().trim();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    private void showLoginDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sign in");
        progressDialog.setMessage("Verifying login...");
        progressDialog.show();
    }
    private void showRegDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Register a new account...");
        progressDialog.show();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
       // if (!password.contains("1234567890"))
       //     return false;
        return password.length() > 4;
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginAndReg.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

