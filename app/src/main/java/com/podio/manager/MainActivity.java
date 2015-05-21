package com.podio.manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.podio.manager.adapter.ManagerListAdapter;
import com.podio.manager.model.ItemListRow;
import com.podio.manager.model.Organization;
import com.podio.manager.task.LoadOrganizationDataTask;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LoadOrganizationDataTask.Delegate{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ManagerListAdapter adapter;
    private ManagerApp app;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (ManagerApp) getApplication();

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.mipmap.ic_launcher);
        this.setTitle(getString(R.string.app_name).substring(1, getString(R.string.app_name).length()));

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.loading));
        progress.show();
        new LoadOrganizationDataTask(this).execute(app.getToken());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ManagerListAdapter(new ArrayList<ItemListRow>(0));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            progress.show();
            new LoadOrganizationDataTask(this).execute(app.getToken());
            return true;
        }
        if (id == R.id.action_logout) {
            app.clearTokenData();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSuccessLoadOrganizations(List<Organization> organizations) {
        Log.i(TAG, "Success LoadOrganizations " + organizations.size() );
        Collections.sort(organizations);
        adapter.updateListOrg(organizations);
        progress.dismiss();
    }

    @Override
    public void onErrorLoadingOrganizations(Exception e) {
        Log.e(TAG,"ErrorLoadingOrganizations "+e.getLocalizedMessage());
        progress.dismiss();
    }
}
