package project.cse3310;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
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
import java.util.regex.Pattern;


public class LoginAndReg extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private static final String TAG = "CustomAuthActivity";
    private String mCustomToken;
    private ProgressDialog progressDialog;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private int attemptLoginCount;
    private EditText emailView;
    private EditText passwordNewView;
    private EditText passwordConfirmView;
    private EditText nameView;
    private EditText addressView;
    private EditText stateView;
    private EditText zipView;
    private EditText phoneView;
    private EditText birthDateView;
    private RadioGroup userTypeview;
    private Spinner categoryView;
    private RadioButton studentTypeView;
    private RadioButton tutorTypeView;
    private ToggleButton sunday;
    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private EditText startHoursView;
    private EditText stopHoursView;
    private Switch optionalSwitchView;
    private View mProgressView;
    private View mLoginFormView;
    private View regFormView;
    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailView= findViewById(R.id.new_email);
        passwordNewView = findViewById(R.id.new_password);
        passwordConfirmView = findViewById(R.id.confirm_password);
        nameView = findViewById(R.id.full_name);
        addressView = findViewById(R.id.address);
        stateView = findViewById(R.id.state);
        zipView = findViewById(R.id.zip_code);
        phoneView = findViewById(R.id.phone);
        birthDateView = findViewById(R.id.birth_date);
        userTypeview = findViewById(R.id.userTypeGroup);
        categoryView = findViewById(R.id.categorySpinner);
        studentTypeView = findViewById(R.id.userRadioButton);
        tutorTypeView = findViewById(R.id.tutorRadioButton);
        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        startHoursView = findViewById(R.id.time_start);
        stopHoursView = findViewById(R.id.time_stop);
        optionalSwitchView = findViewById(R.id.optional_switch);
        mEmailView = findViewById(R.id.email);
        attemptLoginCount = 0;
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

        /* setup listener for radio buttons in register */
        studentTypeView = findViewById(R.id.userRadioButton);
        tutorTypeView = findViewById(R.id.tutorRadioButton);
        studentTypeView.setChecked(true);
        userTypeview = findViewById(R.id.userTypeGroup);

        userTypeview.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                if (checkedId == tutorTypeView.getId()){
                    showTutorFields(true);
                }
                else {
                    showTutorFields(false);
                }
            }
        } );
        //login button listener
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailView.getText().toString().isEmpty()){
                    mEmailView.setError("Email Required");
                    showToast(LoginAndReg.this, "Please enter your email!");
                }
                else if (mPasswordView.getText().toString().isEmpty()) {
                    mPasswordView.setError("Password Required");
                    showToast(LoginAndReg.this, "Please enter your password!");
                }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        LoginAndReg.this.overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
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
                            invalidLoginHandler();
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
    private void attemptLogin(String email, String password) {
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
    private void invalidLoginHandler(){
        attemptLoginCount += 1;
        if (attemptLoginCount >= 5)
        {
            showToast(this, "Too many attempts to login");
            mEmailView.setError("Too many failed attempts!");
        }
    }

    /********************************
     *   Register User Account      *
     ********************************/
    private void attemptReg() {

        final String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String phoneRegex = "\\d{3}-\\d{3}-\\d{4}";
        final String email = emailView.getText().toString();
        final String password = passwordNewView.getText().toString();
        final String password2 = passwordConfirmView.getText().toString();
        final String name = nameView.getText().toString();
        final String address = addressView.getText().toString();
        final String state = stateView.getText().toString();
        final String zip = zipView.getText().toString();
        final String phone = phoneView.getText().toString();
        final String birthDate = birthDateView.getText().toString();
        final String category = categoryView.getSelectedItem().toString();
        String days = "";
        String hours = "";
        String startHours = "" + startHoursView.getText().toString();
        String stopHours = "" + stopHoursView.getText().toString();

        if (sunday.isChecked())
            days += "Sun";
        if (monday.isChecked())
            days += "Mon";
        if (tuesday.isChecked())
            days += "Tues";
        if (wednesday.isChecked())
            days += "Wed";
        if (thursday.isChecked())
            days += "Thur";
        if (friday.isChecked())
            days += "Fri";
        if (saturday.isChecked())
            days += "Sat";
        hours += startHours + "-" + stopHours;

        emailView.setError(null);
        passwordNewView.setError(null);
        passwordConfirmView.setError(null);
        tutorTypeView.setError(null);
        nameView.setError(null);
        phoneView.setError(null);
        addressView.setError(null);
        stateView.setError(null);
        startHoursView.setError(null);
        stopHoursView.setError(null);

        String userType1 = "";
        if (studentTypeView.isChecked())
            userType1 = studentTypeView.getText().toString();
        else if (tutorTypeView.isChecked())
            userType1 = tutorTypeView.getText().toString();
        final String userType =userType1;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(emailRegex,email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordNewView.setError(getString(R.string.error_invalid_password));
            focusView = passwordNewView;
            cancel = true;
        }
        //Confirm password matches in both fields
        if (!password.isEmpty() && !password.equals("") &&!password.equals(password2)){
            passwordConfirmView.setError("The passwords do not match");
            focusView = passwordConfirmView;
            cancel = true;
        }

        /* name validation */
        if (name.equals("")){
            showToast(this,"Please Enter your name");
            nameView.setError("First Last");
            focusView = nameView;
            cancel = true;
        }
        /* birthdate validation */
        if (birthDate.isEmpty() || birthDate.equals("")){
            showToast(this, "Please enter your date of birth");
            birthDateView.setError("MM-DD-YYYY format");
            focusView = birthDateView;
            cancel = true;
        }
        /* zip code validation */
        if (zip.isEmpty() || zip.equals("")) {
            showToast(this, "Please enter zip code");
            zipView.setError("Required");
            focusView = zipView;
            cancel = true;
        }
        //Get available days and Error is none selected
        if (tutorTypeView.isChecked()){
            if (days.isEmpty() || days.equals("")){
                showToast(this, "Please select available days");
                cancel = true;
            }
            if (startHours.equals("")){
                showToast(this, "Please enter start time");
                startHoursView.setError("Required: ##:##");
                focusView = startHoursView;
                cancel = true;
            }
            if (stopHours.equals("")) {
                showToast(this, "Please enter stop time");
                stopHoursView.setError("Required: ##:##");
                focusView = stopHoursView;
                cancel = true;
            }
            if (phone.isEmpty() || phone.equals("")){
                showToast(this, "Please enter your phone number");
                phoneView.setError("###-###-####");
                focusView = phoneView;
                cancel = true;
            }
            else if (!isPhoneValid(phone)){
                showToast(this, "Please enter valid phone number");
                phoneView.setError("###-###-####");
                focusView = phoneView;
                cancel = true;
            }
            if (address.isEmpty() || address.equals("")){
                showToast(this, "Please enter your address");
                addressView.setError("Required");
                focusView = addressView;
                cancel = true;
            }
            if (state.isEmpty() || state.equals("")){
                showToast(this, "Please enter your state");
                stateView.setError("Required");
                focusView = addressView;
                cancel = true;
            }
        }
        if (optionalSwitchView.isChecked()){
            if (phone.isEmpty() || phone.equals("")){
                showToast(this, "Please enter your phone number");
                phoneView.setError("###-###-####");
                focusView = phoneView;
                cancel = true;
            }
            else if (!isPhoneValid(phone)){
                showToast(this, "Please enter valid phone number");
                phoneView.setError("###-###-####");
                focusView = phoneView;
                cancel = true;
            }
            if (address.isEmpty() || address.equals("")){
                showToast(this, "Please enter your address");
                addressView.setError("Required");
                focusView = addressView;
                cancel = true;
            }
            if (state.isEmpty() || state.equals("")){
                showToast(this, "Please enter your state");
                stateView.setError("Required");
                focusView = addressView;
                cancel = true;
            }
        }
        if (cancel) {
            //focus field with error
            if (focusView != null)
                focusView.requestFocus();
        } else {
            showRegDialog();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginAndReg.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                showToast(LoginAndReg.this, "Register failed!");
                            } else {
                                showToast(LoginAndReg.this, "Registration successful!");
                                startActivity(new Intent(LoginAndReg.this,LoginAndReg.class));
                                progressDialog.dismiss();
                                finish();
                                setUserData();
                            }
                        }
                    });
        }
    }
    /* assign the users data to the database */
    private void setUserData(){
        final String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String phoneRegex = "\\d{3}-\\d{3}";
        final String email = emailView.getText().toString();
        final String password = passwordNewView.getText().toString();
        final String password2 = passwordConfirmView.getText().toString();
        final String name = nameView.getText().toString();
        final String address = addressView.getText().toString();
        final String state = stateView.getText().toString();
        final String zip = zipView.getText().toString();
        final String phone = phoneView.getText().toString();
        final String birthDate = birthDateView.getText().toString();
        final String category = categoryView.getSelectedItem().toString();
        String days = "";
        String hours = "";
        String startHours = "" + startHoursView.getText().toString();
        String stopHours = "" + stopHoursView.getText().toString();

        if (sunday.isChecked())
            days += "Sun";
        if (monday.isChecked())
            days += "Mon";
        if (tuesday.isChecked())
            days += "Tues";
        if (wednesday.isChecked())
            days += "Wed";
        if (thursday.isChecked())
            days += "Thur";
        if (friday.isChecked())
            days += "Fri";
        if (saturday.isChecked())
            days += "Sat";
        hours += startHours + "-" + stopHours;

        DatabaseReference fb = mDatabase.getReference();
        mEmailView.setError(null);
        mPasswordView.setError(null);
        tutorTypeView.setError(null);
        nameView.setError(null);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user1.getUid();
        DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference();

        String userType1 = "";
        if (studentTypeView.isChecked())
            userType1 = studentTypeView.getText().toString();
        else if (tutorTypeView.isChecked())
            userType1 = tutorTypeView.getText().toString();
        final String userType =userType1;

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            Log.d("FirebaseInstance", "NULL USER");
        else {
            //TUTOR TYPE -- REQUIRES ALL FIELDS
            if (tutorTypeView.isChecked()) {
                UserData userData = new UserData(email, name, userType, category, zip, birthDate, address, state, phone, days, hours, 0);
                Log.d("userDataContents", userData.toString());
                fDatabase.child("tutors").child(uid).setValue(userData);
            }
            //STUDENT OPTIONAL - register with optional data
            else if (optionalSwitchView.isChecked()) {
                UserData userData = new UserData(email, name, userType, category, zip, state, address, phone, birthDate);
                Log.d("USerDateContents", userData.toString());
                fDatabase.child("users").child(uid).setValue(userData);
            } else {
                UserData userData = new UserData(email, name, userType, category, zip, birthDate);
                Log.d("USerDateContents", userData.toString());
                fDatabase.child("users").child(uid).setValue(userData);
            }
        }
    }
    /* shows or hides the tutor specific fields */
    private void showTutorFields(Boolean show){
        TextView tutorDayLabel = findViewById(R.id.tutorDayLabel);
        LinearLayout weekLayout = findViewById(R.id.weekLayout);
        ConstraintLayout timeLayout = findViewById(R.id.tutorTimeLayout);
        Switch optionalSwitch = findViewById(R.id.optional_switch);
        TextView optionalLabel = findViewById(R.id.optional_label);
        phoneView = findViewById(R.id.phone);
        stateView = findViewById(R.id.state);
        addressView = findViewById(R.id.address);
        if (show){
            tutorDayLabel.setVisibility(View.VISIBLE);
            weekLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);

            //optional fields required for tutors
            phoneView.setVisibility(View.VISIBLE);
            stateView.setVisibility(View.VISIBLE);
            addressView.setVisibility(View.VISIBLE);
            optionalLabel.setVisibility(View.GONE);
            optionalSwitch.setVisibility(View.GONE);
        }
        else {
            tutorDayLabel.setVisibility(View.GONE);
            weekLayout.setVisibility(View.GONE);
            timeLayout.setVisibility(View.GONE);
            phoneView.setVisibility(View.GONE);
            stateView.setVisibility(View.GONE);
            addressView.setVisibility(View.GONE);
            optionalLabel.setVisibility(View.VISIBLE);
            optionalSwitch.setVisibility(View.VISIBLE);
        }
    }
    public void optionalSwitch(View view) {
        Switch optionalSwitch = findViewById(R.id.optional_switch);
        phoneView = findViewById(R.id.phone);
        stateView = findViewById(R.id.state);
        addressView = findViewById(R.id.address);

        if (optionalSwitch.isChecked()) {
            phoneView.setVisibility(View.VISIBLE);
            stateView.setVisibility(View.VISIBLE);
            addressView.setVisibility(View.VISIBLE);
        }
        else {
            phoneView.setVisibility(View.GONE);
            stateView.setVisibility(View.GONE);
            addressView.setVisibility(View.GONE);
        }
    }
    /* textView click to register */
    public void registerTextClick(View view) {
        // fill spinner with categories
        Spinner spinner = findViewById(R.id.categorySpinner);
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
        progressDialog.setMessage("Registering new account...");
        progressDialog.show();
    }

    private boolean isEmailValid(String regex,String email) {
        if (!Pattern.matches(regex, email))
            return false;
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //if (!password.contains("1234567890"))
       //     return false;
        if (password.isEmpty())
            return false;
        return password.length() > 4;
    }
    private boolean isPhoneValid(String phone) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
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

