package anoop.myprojects.eventapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AddEvent extends AppCompatActivity {

    String event_name,event_desc,event_date,event_time,event_venue,event_coordinator;

    EditText txtevent_name,txtevent_desc,txtevent_date,txtevent_time,txtevent_venue,txtevent_coordinator;

    // login button
    Button btnaddevent;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    String USERNAME;

    String JSON_STRING,json_string,code;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        txtevent_name=findViewById(R.id.eventname);
        txtevent_desc=findViewById(R.id.eventdesc);
        txtevent_date=findViewById(R.id.date);
        txtevent_time=findViewById(R.id.time);
        txtevent_venue=findViewById(R.id.venue);
        txtevent_coordinator=findViewById(R.id.coordinator);




    }

    public void addEvents(View view){

        event_name=txtevent_name.getText().toString();
        event_desc=txtevent_desc.getText().toString();
        event_date=txtevent_date.getText().toString();
        event_time=txtevent_time.getText().toString();
        event_venue=txtevent_venue.getText().toString();
        event_coordinator=txtevent_coordinator.getText().toString();



        System.out.println(event_date);


        String username="username";
        String password="password";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Events Please Wait...");
        progressDialog.show();

        AddEvent.BackgroundTask backgroundTask =new AddEvent.BackgroundTask();
        backgroundTask.execute(username,password);








    }


    class BackgroundTask extends AsyncTask<String,Void,String>
    {


        String addInfoUrl;

        @Override
        protected void onPreExecute() {

            addInfoUrl ="http://palaeobotanical-com.000webhostapp.com/addEventsApp.php";

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


                String data_string = URLEncoder.encode("event_name","UTF-8")+"="+URLEncoder.encode(event_name,"UTF-8")+"&"+
                        URLEncoder.encode("event_desc","UTF-8")+"="+URLEncoder.encode(event_desc,"UTF-8")+"&"+
                        URLEncoder.encode("event_time","UTF-8")+"="+URLEncoder.encode(event_time,"UTF-8")+"&"+
                        URLEncoder.encode("event_date","UTF-8")+"="+URLEncoder.encode(event_date,"UTF-8")+"&"+
                        URLEncoder.encode("event_venue","UTF-8")+"="+URLEncoder.encode(event_venue,"UTF-8")+"&"+
                        URLEncoder.encode("event_coordinator","UTF-8")+"="+URLEncoder.encode(event_coordinator,"UTF-8");
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

                if(code.trim().equals("success")){
                    progressDialog.dismiss();

                    alert.showAlertDialog(AddEvent.this, "Success..", "Event Added Successfully", true);

                    // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    //session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                    // Staring MainActivity


                }else{
                    // username / password doesn't match
                    progressDialog.dismiss();
                    alert.showAlertDialog(AddEvent.this, "Failed..", "Event Added Failed Please try again", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
/*

$event_name=$_POST["event_name"];
	$event_desc=$_POST["event_desc"];

	//test_input($_POST["comment"]);
	$event_time=$_POST["event_time"];
	$event_date=$_POST["event_date"];
	$event_venue=$_POST["event_venue"];
	$event_coordinator=$_POST["event_coordinator"];


	$sql="INSERT INTO EVENTS(event_name,description,date,time,venue,coordinator)VALUES('$event_name',
	'$event_desc','$event_date','$event_time','$event_venue','$event_coordinator');";





 */