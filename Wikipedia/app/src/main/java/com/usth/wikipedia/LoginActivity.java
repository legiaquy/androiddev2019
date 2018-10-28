package com.usth.wikipedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Create_new_account(View view) {
        Intent intent = new Intent(this,CreateNewAccountActivity.class);
        startActivity(intent);
    }

    public void Forgot_password(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void Log_in(View view) {
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }
}
