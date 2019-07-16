package anoop.myprojects.eventapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    Button btnaddevent,btnaddresult;

    // Session Manager Class
    SessionManager session;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnaddevent=findViewById(R.id.addeventbtn);
        btnaddresult=findViewById(R.id.addresultbtn);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        btnLogout = (Button) findViewById(R.id.btnLogout);

        if(!session.isLoggedIn()){

            btnaddevent.setEnabled(false);
            btnaddresult.setEnabled(false);
            btnLogout.setEnabled(false);
        }



        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                session.logoutUser();
                finish();
            }
        });
    }

    public void addEvent(View view){

        Intent i = new Intent(this, AddEvent.class);
        startActivity(i);


    }
    public void addResult(View view){
        Intent i = new Intent(this, AddResult.class);
        startActivity(i);


    }
}
