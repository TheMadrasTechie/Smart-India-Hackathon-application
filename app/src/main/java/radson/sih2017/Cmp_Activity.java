package radson.sih2017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNDAR on 31-03-2017.
 */
public class Cmp_Activity extends ActionBarActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_TN = "training_name";
    private static final String TAG_ac = "applicants_count";
    private static final String TAG_st ="status";
    private static final String TAG_id ="id";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.std_acty);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();
        getData();
    }


    protected void showList(){
        final SharedPreferences shadPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shadPreferences.edit();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String[] testArray = new String[500];
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String tn = c.getString(TAG_TN);
                String ac = c.getString(TAG_ac);
                String st = c.getString(TAG_st);
                String id = c.getString(TAG_id);

            /*if(st.equals("published")){

                TextView dd = (TextView)findViewById(R.id.tv_st);
                dd.set
            }*/

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_TN, tn);
                persons.put(TAG_ac, ac);
                persons.put(TAG_st, st);
                persons.put(TAG_id, id);

                personList.add(persons);

            }


            ListAdapter adapter = new SimpleAdapter(
                    Cmp_Activity.this, personList, R.layout.list_item_cmpny_intrns,
                    new String[]{TAG_TN, TAG_ac, TAG_st , TAG_id },
                    new int[]{R.id.tv_tn, R.id.tv_ac, R.id.tv_st ,R.id.tv_id}

            );


            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.tv_id);
                    String text = textView.getText().toString();

                    editor.putString("id", text);
                    editor.commit();

                    Intent aa = new Intent(Cmp_Activity.this , Intern_details.class);
                    startActivity(aa);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.43.28:8000/api/cviewtraining/1/?format=json");

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
                String asdd = "{\"result\":"+result+"}";
                myJSON=asdd;

                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
