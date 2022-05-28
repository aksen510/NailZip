package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;

public class RegisterClientActivity extends AppCompatActivity {

    private EditText edt_username, edt_email, edt_pw, edt_checkPw, edt_phonenum;
    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMainActivity = new Intent(RegisterClientActivity.this, MainActivity.class);
                startActivity(startMainActivity);
            }
        });
    }

    public void init(){
        edt_username = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_pw = findViewById(R.id.edt_pw);
        edt_checkPw = findViewById(R.id.edt_pw_check);
        edt_phonenum = findViewById(R.id.edt_phonenum);
        btn_complete = (Button) findViewById(R.id.btn_complete);
    }
}