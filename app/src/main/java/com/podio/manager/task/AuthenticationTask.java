package com.podio.manager.task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationTask extends AsyncTask<String, Void, String> {
    private static final String TAG = AuthenticationTask.class.getSimpleName();

    private HttpURLConnection conn;
    private static final String API_URL = "https://podio.com/oauth/token";
    private static final String MY_API_KEY = "podio-puzzle";
    private static final String MY_SECRET = "2u8c5SbylhvJ1uzeYMIsNS9fePA6hlkAyGtGjlWaN2r9FrThhcmwBh67EPHUpCHd";
    private Delegate _d;

    public interface Delegate {
        void onSuccessAuth(String result);
        void onErrorAuth(Exception e);
    }

    public AuthenticationTask(Delegate d) {
        this._d = d;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d(TAG, "JSON parameters:" + params.length);

            if (params.length < 2 || params[0].length()<1 || params[1].length()<1){
                return "";
            }
            StringBuilder jsonResults = new StringBuilder();

            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("grant_type", "password"));
            postParams.add(new BasicNameValuePair("username", params[0]));//TODO Encrypt email
            postParams.add(new BasicNameValuePair("password", params[1]));//TODO Encrypt password
            postParams.add(new BasicNameValuePair("client_id", MY_API_KEY));
            postParams.add(new BasicNameValuePair("redirect_uri", "www.podio.com/manager"));
            postParams.add(new BasicNameValuePair("client_secret", MY_SECRET));

            URL url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getQuery(postParams));
            writer.close();
            os.close();
            conn.connect();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
//            Log.d(TAG, "JSON Result:" + jsonResults.toString());
            JSONObject jsonObject = new JSONObject(jsonResults.toString());
            return jsonObject.getString("access_token");
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing URL", e);
            _d.onErrorAuth(e);
            return "";
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to API", e);
            _d.onErrorAuth(e);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            _d.onErrorAuth(e);
            return "";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null)
            _d.onSuccessAuth(result);
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}