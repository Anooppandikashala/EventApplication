package anoop.myprojects.eventapplication;

import android.app.ProgressDialog;
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

public class AddResult extends AppCompatActivity {

    String event_name,event_desc,event_date,event_organize,event_prize,winner1,winner2,winner3;

    EditText txtevent_name,txtevent_desc,txtevent_date,txtevent_organize,txtevent_prize,txtwinner1,txtwinner2,txtwinner3;

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
        setContentView(R.layout.activity_add_result);


        txtevent_name=findViewById(R.id.eventname);
        txtevent_desc=findViewById(R.id.eventdesc);
        txtevent_date=findViewById(R.id.date);
        txtevent_organize=findViewById(R.id.organized);
        txtevent_prize=findViewById(R.id.prize);
        txtwinner1=findViewById(R.id.winner1);
        txtwinner2=findViewById(R.id.winner2);
        txtwinner3=findViewById(R.id.winner3);


    }

    public void addResults(View view){


        event_name=txtevent_name.getText().toString();
        event_desc=txtevent_desc.getText().toString();
        event_date=txtevent_date.getText().toString();
        event_organize=txtevent_organize.getText().toString();
        event_prize=txtevent_prize.getText().toString();
        winner1=txtwinner1.getText().toString();
        winner2=txtwinner2.getText().toString();
        winner3=txtwinner3.getText().toString();


        String username="username";
        String password="password";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Result Please Wait...");
        progressDialog.show();

        AddResult.BackgroundTask backgroundTask =new AddResult.BackgroundTask();
        backgroundTask.execute(username,password);


    }


    class BackgroundTask extends AsyncTask<String,Void,String>
    {


        String addInfoUrl;

        @Override
        protected void onPreExecute() {

            addInfoUrl ="http://palaeobotanical-com.000webhostapp.com/addResultsApp.php";

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
                        URLEncoder.encode("event_org","UTF-8")+"="+URLEncoder.encode(event_organize,"UTF-8")+"&"+
                        URLEncoder.encode("event_date","UTF-8")+"="+URLEncoder.encode(event_date,"UTF-8")+"&"+
                        URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(event_prize,"UTF-8")+"&"+
                        URLEncoder.encode("winner1","UTF-8")+"="+URLEncoder.encode(winner1,"UTF-8")+"&"+
                        URLEncoder.encode("winner2","UTF-8")+"="+URLEncoder.encode(winner2,"UTF-8")+"&"+
                        URLEncoder.encode("winner3","UTF-8")+"="+URLEncoder.encode(winner3,"UTF-8");
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

                    alert.showAlertDialog(AddResult.this, "Success..", "Result Added Successfully", true);

                    // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    //session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                    // Staring MainActivity


                }else{
                    // username / password doesn't match
                    progressDialog.dismiss();
                    alert.showAlertDialog(AddResult.this, "Failed..", "Result Added Failed Please try again", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

