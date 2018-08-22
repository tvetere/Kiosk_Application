package com.zebra.kiosk_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);


        Button registrationButton = findViewById(R.id.registrationButton);

        registrationButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View view){

        switch(view.getId()){
            case R.id.registrationButton:
                Intent registrationIntent = new Intent(this, RegistrationActivity.class);
                startActivity(registrationIntent);

                break;

            default:
                break;
        }

    }


}
