package anoop.myprojects.eventapplication;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayEvent extends Fragment {

    String JSON_STRING, json_string;
    JSONArray jsonArray;
    JSONObject jsonObject;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private List<String> listDataDesc;
    public int count=0;
    private HashMap<String,String> listHash;
    ProgressDialog progressDialog;

    TextView textView;


    public TodayEvent() {
        // Required empty public constructor
    }

    /*public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_home, null);
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_event, container, false);

        System.out.println("hello");


        listView = (ExpandableListView)view.findViewById(R.id.list_today);
        textView = view.findViewById(R.id.noevents);

        textView.setText("");

        listDataHeader = new ArrayList<>();
        listDataDesc = new ArrayList<>();
        listHash = new HashMap<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Events....");
        progressDialog.show();

        TodayEvent.BackgroundTask backgroundTask = new TodayEvent.BackgroundTask();
        backgroundTask.execute("event");



        // Inflate the layout for this fragment
        return view;
    }
    class BackgroundTask extends AsyncTask<String, Void, String> {


        String addInfoUrl;
        //SimpleDateFormat simpleDateFormat;
        //String time;
        //Calendar calander;



        @Override
        protected void onPreExecute() {

            addInfoUrl = "http://palaeobotanical-com.000webhostapp.com/json_parsing.php";


        }

        @Override
        protected String doInBackground(String... args) {

            String msg;

            try {
                URL url = new URL(addInfoUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String data_string = URLEncoder.encode("event", "UTF-8") + "=" + URLEncoder.encode("event", "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");


                }


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
            json_string = result;

            try {
                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");


                int count = 0;

                String event_name,event_desc,
                        event_time,
                        event_date,
                        event_venue,
                        event_coordinator;

                while (count < jsonArray.length()) {



                    JSONObject JO = jsonArray.getJSONObject(count);

                    event_name=JO.getString("event_name");
                    event_desc=JO.getString("event_desc");
                    event_time=JO.getString("event_time");
                    event_date=JO.getString("event_date");
                    event_venue=JO.getString("event_venue");
                    event_coordinator=JO.getString("coordinator");

                    String key=String.valueOf(count+1)+". "+event_name;
                    String value=event_desc+"\nDate  :"+event_date+"\nTime :"
                            +event_time+"\nVenue :"+event_venue+"\nCoordinator :"+event_coordinator;

                    listDataHeader.add(key);
                    listDataDesc.add(value);

                    listHash.put(listDataHeader.get(count),listDataDesc.get(count));

                    count++;

                }


                progressDialog.dismiss();

                listAdapter = new ExpandableListAdapter(getActivity(),listDataHeader,listHash);
                int l;

                if (listAdapter.isEmpty())
                    textView.setText("No Events for Today");



                listView.setAdapter(listAdapter);




            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
