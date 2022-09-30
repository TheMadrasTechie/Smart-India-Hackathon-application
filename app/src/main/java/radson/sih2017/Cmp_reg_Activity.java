package radson.sih2017;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNDAR on 02-04-2017.
 */
public class Cmp_reg_Activity extends AppCompatActivity {

    String myJSON;
    public EditText et_cn,et_ad,et_ct,et_pn,et_st,et_mb,et_em,et_ps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmp_reg_acty);

        //final Button btn_reg = (Button)findViewById(R.id.btn_reg);

    }

    public void insert(View view){
        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
        et_cn = (EditText)findViewById(R.id.et_cn);
        et_ad = (EditText)findViewById(R.id.et_ad);
        et_ct = (EditText)findViewById(R.id.et_ct);
        et_pn = (EditText)findViewById(R.id.et_pn);
        et_st = (EditText)findViewById(R.id.et_st);
        et_mb = (EditText)findViewById(R.id.et_mb);
        et_em = (EditText)findViewById(R.id.et_em);
        et_ps = (EditText)findViewById(R.id.et_ps);

        final String cn = et_cn.getText().toString();
        final String ad = et_ad.getText().toString();
        final String ct = et_ct.getText().toString();
        final String pn = et_pn.getText().toString();
        final String st = et_st.getText().toString();
        final String mb = et_mb.getText().toString();
        final String em = et_em.getText().toString();
        final String ps = et_ps.getText().toString();

        insertToDatabase(cn, ad,ct,pn,st,mb,em,ps);
        //Intent a = new Intent(Std_login_Activity.this,Std_Activity.class);
        //startActivity(a);
    }

    private void insertToDatabase(final String cn, final String ad ,final String ct, final String pn , final String st , final String mb , final String em , final String ps){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramAddress = params[1];



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("company_name",cn));
                nameValuePairs.add(new BasicNameValuePair("address1", ad));
                nameValuePairs.add(new BasicNameValuePair("city", ct));
                nameValuePairs.add(new BasicNameValuePair("pin", pn));
                nameValuePairs.add(new BasicNameValuePair("state", st));
                nameValuePairs.add(new BasicNameValuePair("mobile", mb));
                nameValuePairs.add(new BasicNameValuePair("email", em));
                nameValuePairs.add(new BasicNameValuePair("password", ps));
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://192.168.43.28:8000/api/addcompany/");
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
        sendPostReqAsyncTask.execute(cn, ad,ct,pn,st,mb,em,ps);

    }
  /*  protected void showList(){


        final SharedPreferences shadPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shadPreferences.edit();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);

                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

                editor.putString("user_id",id);
                editor.commit();

            }





        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/
}
