package project.cse3310;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Communications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**The savedInstanceState needs to have Tutor's data in this, else it won't work.
         I also have it set up so it shows the name, and subject the tutor is tutoring in.
         Need to get the data for that as well
         Right now I have my data in, I don't care at all for the presentation**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communications);
    }

    /**It works, but actions on what to do after it is done needs to have implemented.
     Personally I'd say main menu, but if we can go back that works too.**/
    public void callTutor(View view) {

        //Add method to get the phone number from Firebase
        String numb = "817-555-555";  //Dummy so that this compliles
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numb));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void emailTutor(View view) {

        //Add method to get email from Firebase
        String[] sendEmail = {"NotZacksRealEmail@gmail.com"};
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, sendEmail);
        email.putExtra(Intent.EXTRA_SUBJECT, "Message from Frog Tutoring");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));

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



}
