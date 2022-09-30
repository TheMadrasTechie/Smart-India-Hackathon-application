package radson.sih2017;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SUNDAR on 01-04-2017.
 */
public class Intern_details extends ActionBarActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_cp = "company_name";
    private static final String TAG_tn = "training_name";
    private static final String TAG_desc ="description";
    private static final String TAG_elg ="eligibility";
    private static final String TAG_sd ="start_date";
    private static final String TAG_dr ="duration";
    private static final String TAG_st ="stipend";
    private static final String TAG_dl ="deadline";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intrn_details);
        final SharedPreferences shadPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shadPreferences.edit();
        list = (ListView) findViewById(R.id.istView);
        personList = new ArrayList<HashMap<String,String>>();
        getData();
        final TextView tv_aply = (TextView)findViewById(R.id.tv_ap);tv_aply.setVisibility(TextView.GONE);
        final Button apply_btn = (Button)findViewById(R.id.intrn_apply);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
insertToDatabase(shadPreferences.getString("user_id","tr"),shadPreferences.getString("id","ww"));
              //  Toast.makeText(getApplicationContext(),shadPreferences.getString("user_id","tr") , Toast.LENGTH_LONG).show();
                tv_aply.setVisibility(TextView.VISIBLE);
                apply_btn.setVisibility(Button.GONE);
            }
        });
    }

    private void insertToDatabase(final String name , final String aa){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramAddress = params[1];




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("training", aa));
                nameValuePairs.add(new BasicNameValuePair("student", name));
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://192.168.43.28:8000/api/apply/?format=json");
                httpPost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                } try {
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String asdd = "{\"result\":["+result+"]}";
                myJSON=asdd;




            }


        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name , aa);

    }
    protected void showList(){

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String[] testArray = new String[500];
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String cp = c.getString(TAG_cp);
                String tn = c.getString(TAG_tn);
                String desc = c.getString(TAG_desc);
                String elg = c.getString(TAG_elg);
                String sd = c.getString(TAG_sd);
                String dr = c.getString(TAG_dr);
                String st = c.getString(TAG_st);
                String dl = c.getString(TAG_dl);




                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_cp, cp);
                persons.put(TAG_tn, tn);
                persons.put(TAG_desc, desc);
                persons.put(TAG_elg, elg);
                persons.put(TAG_sd, sd);
                persons.put(TAG_dr, dr);
                persons.put(TAG_st, st);
                persons.put(TAG_dl, dl);
                //Toast.makeText(getApplicationContext() , cp, Toast.LENGTH_SHORT).show();
                personList.add(persons);
            }


            ListAdapter adapter = new SimpleAdapter(
                    Intern_details.this, personList, R.layout.list_item_intern,
                    new String[]{TAG_cp, TAG_tn, TAG_desc , TAG_elg , TAG_sd , TAG_dr , TAG_st , TAG_dl},
                    new int[]{R.id.tv_cn, R.id.tv_tn, R.id.tv_desc ,R.id.tv_elg , R.id.tv_sd , R.id.tv_dr , R.id.tv_st , R.id.tv_dl}
            );

            list.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData() {
        final SharedPreferences shadPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shadPreferences.edit();
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.43.28:8000/api/training/"+shadPreferences.getString("id","uu")+"/?format=json");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String asdd = "{\"result\":["+result+"]}";
                myJSON=asdd;

                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
