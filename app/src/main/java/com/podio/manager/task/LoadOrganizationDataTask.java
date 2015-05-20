package com.podio.manager.task;

import android.os.AsyncTask;
import android.util.Log;

import com.podio.manager.model.Organization;
import com.podio.manager.model.Workspace;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoadOrganizationDataTask extends AsyncTask<String, Void, List<Organization>> {
    private static final String TAG = LoadOrganizationDataTask.class.getSimpleName();

    private static final String API_URL_ORG = "https://api.podio.com/org/";
    private static final String API_URL_SPACE = "https://api.podio.com/space/org/";
    private Delegate _d;

    public interface Delegate {
        void onSuccessLoadOrganizations(List<Organization> organizationList);
        void onErrorLoadingOrganizations(Exception e);
    }

    public LoadOrganizationDataTask(Delegate d) {
        this._d = d;
    }

    @Override
    protected List<Organization> doInBackground(String... params) {
        //TODO Change to Volley for a better management
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            if (null == params || params.length < 1 || params[0].length() < 1) {
                return new ArrayList<>(0);
            }
            URL url = new URL(API_URL_ORG);
            conn = (HttpURLConnection) url.openConnection();
//            String basicAuth = "OAuth2 " + new String(new Base64().encode(params[0].getBytes(),0));
            String basicAuth = "OAuth2 " + params[0];
            conn.setRequestProperty("Authorization", basicAuth);
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

//            Log.d(TAG, ">>> JSON Result:" + jsonResults.toString());
//            {"premium":false,"status":"active","contract_status":"none","name":"My 1st organisation","sales_agent_id":659442,"rights":["add_space","add_conversation","add_contract","view","statistics","view_admins","update"],"url":"https:\/\/podio.com\/myorg-ak86msqi8u","grants_count":0,"image":null,"user_limit":5,"org_id":508749,"rank":2,"tier":null,"spaces":[{"archived":false,
//                    "premium":false,"name":"Employee Network","rights":["update","add_item","add_contact","add_widget","add_task","view_members","add_file","add_status","subscribe","add_user",

            JSONArray predsJsonArray = new JSONArray(jsonResults.toString());

            List<Organization> organizationList = new ArrayList<>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject pacoteJson = predsJsonArray.getJSONObject(i);
                Organization organization = new Organization();
                organization.setId("" + pacoteJson.getString("org_id"));
                organization.setName("" + pacoteJson.getString("name"));



                HttpURLConnection conn2;
                StringBuilder jsonResults2 = new StringBuilder();
                URL url2 = new URL(API_URL_SPACE + organization.getId() + "/");
                conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setRequestProperty("Authorization", basicAuth);
                InputStreamReader in2 = new InputStreamReader(conn2.getInputStream());
                // Load the results into a StringBuilder
                int read2;
                char[] buff2 = new char[1024];
                while ((read2 = in2.read(buff2)) != -1) {
                    jsonResults2.append(buff2, 0, read2);
                }
//            Log.d(TAG, ">>> JSON Result wrk:" + jsonResults2.toString());
//            [{"name":"Employee Network","url":"https:\/\/podio.com\/my-2nd-organization\/employeenetwork","url_label":"employeenetwork","space_id":1725645,"org_id":508751,"type":"emp_network"},{"name":"My 4th workspace","url":"https:\/\/podio.com\/my-2nd-organization\/my-4th-workspace","url_label":"my-4th-workspace","space_id":1725647,"org_id":508751,"type":"regular"},{"name":"My 5th workspace","url":"https:\/\/podio.com\/my-2nd-organization\/my-5th-workspace","url_label":"my-5th-workspace","space_id":1725649,"org_id":508751,"type":"regular"},{"name":"My 6th workspace","url":"https:\/\/podio.com\/my-2nd-organization\/my-6th-workspace","url_label":"my-6th-workspace","space_id":1725650,"org_id":508751,"type":"regular"},{"name":"Qwerty","url":"https:\/\/podio.com\/my-2nd-organization\/qwerty","url_label":"qwerty","space_id":2592897,"org_id":508751,"type":"regular"}]
                JSONArray predsJsonArray2 = new JSONArray(jsonResults2.toString());
                organization.setWorkspaceList(new ArrayList<Workspace>(predsJsonArray2.length()));
                for (int j = 0; j < predsJsonArray2.length(); j++) {
                    JSONObject pacoteJson2 = predsJsonArray2.getJSONObject(j);
                    Workspace workspace = new Workspace();
                    workspace.setId("" + pacoteJson2.getString("space_id"));
                    workspace.setName("" + pacoteJson2.getString("name"));
                    organization.getWorkspaceList().add(workspace);
                }



                organizationList.add(organization);
            }
            return organizationList;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing URL", e);
            _d.onErrorLoadingOrganizations(e);
            return new ArrayList<>(0);
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to API", e);
            _d.onErrorLoadingOrganizations(e);
            return new ArrayList<>(0);
        } catch (Exception e) {
            e.printStackTrace();
            _d.onErrorLoadingOrganizations(e);
            return new ArrayList<>(0);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Organization> result) {
        if (result != null)
            _d.onSuccessLoadOrganizations(result);
    }
}