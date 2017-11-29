package project.cse3310;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Communications extends AppCompatActivity {
    private UserData userData;
    private TextView profileName;
    private TextView profileSubject;
    private Button phoneButton;
    private Button emailButton;
    private RatingBar ratingBar;
    private TextView ratingNum;
    private TextView times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**The savedInstanceState needs to have Tutor's data in this, else it won't work.
         I also have it set up so it shows the name, and subject the tutor is tutoring in.
         Need to get the data for that as well
         Right now I have my data in, I don't care at all for the presentation**/
        super.onCreate(savedInstanceState);
        int contentID;
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
        ratingNum.setText(String.valueOf(userData.getRating()));
        if (contentID == 1) {
            times = findViewById(R.id.availTimes);
            times.setText(userData.getDays() + " - " + userData.getHours());
        }
    }

    /**It works, but actions on what to do after it is done needs to have implemented.
     Personally I'd say main menu, but if we can go back that works too.**/
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
    public void reviewTutor(View view){
        Intent review = new Intent(this, Review.class);
        review.putExtra("User",userData);
        startActivity(review);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    /** public void textTutor(View view)
     {
     Get_Text mesg = new Get_Text();
     String msg = "";
     mesg.getMessage(view, msg);
     //add method to get number from Firebase
     String numb = "0";  //Dummy so that this compliles
     Intent intent = new Intent(Intent.ACTION_SEND);
     intent.setData(Uri.parse("smsto:" + numb));  // This ensures only SMS apps respond
     /**intent.putExtra("sms_body", msg);
     if (intent.resolveActivity(getPackageManager()) != null) {
     startActivity(intent);
     }
     }**/


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
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }


}
