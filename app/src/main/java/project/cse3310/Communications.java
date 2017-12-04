package project.cse3310;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Communications extends AppCompatActivity {
    private UserData userData;
    private TextView profileName;
    private TextView profileSubject;
    private Button phoneButton;
    private Button emailButton;
    private RatingBar ratingBar;
    private TextView ratingNum;
    private TextView times;
    private int contentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setContentView(R.layout.activity_communications);
            contentID = 1;
        }
        else {
            setContentView(R.layout.comm_not_signedin);
            contentID = 2;
        }
        userData = getIntent().getExtras().getParcelable("User");
        profileName = findViewById(R.id.profile_name);
        profileSubject = findViewById(R.id.profile_subject);
        phoneButton = findViewById(R.id.profile_phone_button);
        emailButton = findViewById(R.id.profile_email_button);
        ratingBar = findViewById(R.id.ratingBar);
        ratingNum = findViewById(R.id.rating_number);


        profileName.setText(userData.getName());
        profileSubject.setText(userData.getCategory());
        ratingBar.setRating(userData.getRating());
        ratingNum.setText(String.format("%.1f",userData.getRating()));
        if (contentID == 1) {
            times = findViewById(R.id.availTimes);
            times.setText(userData.getDays() + " - " + userData.getHours());
        }

    }

    public void callTutor(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + userData.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void emailTutor(View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, userData.getEmail());
        email.putExtra(Intent.EXTRA_SUBJECT, "Message from Frog Tutoring");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
    /**
     * Send to review page for tutor, passing userData object
     */
    public void apptTutor(View view){
        Intent intent = new Intent(Communications.this, Appointments_Menu.class);
        intent.putExtra("User", userData);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    public void displayReviews(View view){
        Intent intent = new Intent(Communications.this, ReviewsPage.class);
        intent.putExtra("User",userData);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);

    }
    /*************************************
     * TOP BAR MENU CREATION METHODS     *
     ************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_communications, menu);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem loginItem = menu.findItem(R.id.action_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            loginItem.setIcon(getResources().getDrawable(R.drawable.logout_icon));
        else
            loginItem.setIcon(getResources().getDrawable(R.drawable.login_icon));
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_login:
                if (!userLoggedIn()){
                    startActivity(new Intent(this, LoginAndReg.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }
                else
                    logoutUser();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    /* returns whether a user is currently logged in or not */
    private boolean userLoggedIn(){
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Communications.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
        //restart activity to refresh content and UI
        recreate();
    }
    @Override
    public void onPause(){
        super.onPause();
        Intent intent = new Intent();
        intent.putExtra("User", userData);
        setResult(RESULT_OK, intent);
        //overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
        finish();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                userData = data.getExtras().getParcelable("User");
            }
        }
    }
    @Override
    public void onBackPressed(){
        if (contentID == 3) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                setContentView(R.layout.activity_communications);
                contentID = 1;
            }
            else {
                setContentView(R.layout.comm_not_signedin);
                contentID = 2;
            }
        }
        else {
            Intent intent = new Intent();
            intent.putExtra("User", userData);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
