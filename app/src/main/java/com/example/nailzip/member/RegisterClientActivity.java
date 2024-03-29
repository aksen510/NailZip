package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailzip.HomeFragment;
import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.example.nailzip.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterClientActivity extends AppCompatActivity {

    private static final String TAG = "RegisterClientActivity";

    private EditText edt_username, edt_email, edt_pw, edt_checkPw, edt_phonenum;
    private Button btn_complete;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = edt_email.getText().toString().trim();
                String pwd = edt_pw.getText().toString().trim();
                String pwdcheck = edt_checkPw.getText().toString().trim();
                String username = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                if (email.isEmpty() || pwd.isEmpty() || pwdcheck.isEmpty() || username.isEmpty() || phonenum.isEmpty()) {
                    Toast.makeText(RegisterClientActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if(pwd.equals(pwdcheck)) {
                        if (pwd.length() >= 6) {

                            User userAccount = new User();
                            userAccount.setPosition(0);
                            userAccount.setEmail(email);
                            userAccount.setUsername(username);
                            userAccount.setPhonenum(phonenum);
//                        userAccount.setUid(firebaseAuth.getCurrentUser().getUid());

                            final ProgressDialog mDialog = new ProgressDialog(RegisterClientActivity.this);

                            memberViewModel.register(email, pwd, userAccount);
                            mDialog.setMessage("가입중입니다");
                            mDialog.show();
                            memberViewModel.getSaveUserInfoMutableLiveData().observe(RegisterClientActivity.this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean) {
                                        mDialog.dismiss();
                                        Toast.makeText(RegisterClientActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterClientActivity.this, MainActivity.class);
                                        startActivity(intent);
//                                    getSupportFragmentManager().beginTransaction().replace(R.id.menu_home, MainActivity()).commitAllowingStateLoss();

                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(RegisterClientActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterClientActivity.this, "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(RegisterClientActivity.this, "6글자 이상으로 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
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