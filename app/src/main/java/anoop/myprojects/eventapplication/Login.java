package anoop.myprojects.eventapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    String USERNAME;

    String JSON_STRING,json_string,code;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            //
            //textView.setText("");

        }
        else {

            open();
        }


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString().trim();
                USERNAME=username;
                String password = txtPassword.getText().toString().trim();

                // Check if username, password is filled
                if(!username.equals("''or'='") && !password.equals("''or'='")) {
                    if (username.trim().length() > 0 && password.trim().length() > 0) {

                        BackgroundTask backgroundTask = new BackgroundTask();
                        backgroundTask.execute(username, password);
                        // For testing puspose username, password is checked with sample data
                        // username = test
                        // password = test
                        //if(username.equals("test") && password.equals("test")){
                    /*if(code.trim().equals("login_success")){

                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data
                        session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                        // Staring MainActivity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }else{
                        // username / password doesn't match
                        alert.showAlertDialog(Login.this, "Login failed..", "Username/Password is incorrect", false);
                    }*/
                    } else {
                        // user didn't entered username or password
                        // Show alert asking him to enter the details
                        alert.showAlertDialog(Login.this, "Login failed..", "Please enter username and password", false);
                    }
                }
                else {
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(Login.this, "Login failed..", "Please enter username and password", false);
                }

            }
        });






    }
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Network Not Found ! \nPlease enable network and try again.");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

        /*alertDialogBuilder.setNegativeButton("",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    class BackgroundTask extends AsyncTask<String,Void,String>
    {


        String addInfoUrl;

        @Override
        protected void onPreExecute() {

            addInfoUrl ="http://palaeobotanical-com.000webhostapp.com/login.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String usrname,password;
            usrname=args[0];
            password=args[1];

            System.out.println("User :"+usrname);
            System.out.println("Pass :"+password);

            try {
                URL url = new URL(addInfoUrl);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                String data_string = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(usrname,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));


                StringBuilder stringBuilder =new StringBuilder();

                JSON_STRING = bufferedReader.readLine();

                System.out.println(JSON_STRING);

                    stringBuilder.append(JSON_STRING+"\n");


                /*while ((JSON_STRING = bufferedReader.readLine())!=null){

                    stringBuilder.append(JSON_STRING+"\n");



                }*/




                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            //textView.setText(result);
            json_string =result;

            System.out.println("json string :"+json_string);

            try {
                jsonObject=new JSONObject(json_string);

                jsonArray=jsonObject.getJSONArray("server_response");


                    JSONObject JO= jsonArray.getJSONObject(0);

                    code=JO.getString("code");

                if(code.trim().equals("login_success")){

                    // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    //session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                    // Staring MainActivity
                    session.createLoginSession(USERNAME, "anroidhive@gmail.com","000");
                    Intent i = new Intent(getApplicationContext(), Admin.class);
                    startActivity(i);
                    finish();

                }else{
                    // username / password doesn't match
                    alert.showAlertDialog(Login.this, "Login failed..", "Username/Password is incorrect", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }







}
