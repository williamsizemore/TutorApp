package project.cse3310;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setContentView(R.layout.activity_main);
    }

    /* viewFlipper to registration form in activity_login */
    public void register(View view) {
        ViewAnimator vf = findViewById(R.id.viewFlipper);
        vf.getInAnimation();
        vf.showNext();
    }
    public void validateLogin(View view) {
        LoginAndReg login = new LoginAndReg();

    }
}
