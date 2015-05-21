package com.podio.manager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.podio.manager.task.AuthenticationTask;
import java.util.Date;

public class LoginActivity extends ActionBarActivity implements AuthenticationTask.Delegate{

    private ManagerApp app;
    private EditText email;
    private EditText pwd;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (ManagerApp) getApplication();
        super.onCreate(savedInstanceState);

        if (app.isTokenValid() && !app.getToken().isEmpty()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.text_email);
        pwd = (EditText) findViewById(R.id.text_pwd);

        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticationTask(LoginActivity.this).execute(email.getText().toString(), pwd.getText().toString());
            }
        });
    }

    @Override
    public void onSuccessAuth(Pair<String, Date> result) {
        if (null != result) {
            app.saveToken(result);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.login_error),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorAuth(Exception e) {
        Toast.makeText(getApplicationContext(),getString(R.string.login_error),Toast.LENGTH_LONG).show();
    }
}
