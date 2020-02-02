package com.example.enlight;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText id, name, password, passwordcheck;
    Button regiBtn;

    //Firebase Authentication(회원가입), Database 기능 가져오기
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //StatusBar 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //전체화면
        setContentView(R.layout.activity_register);

        id = findViewById(R.id.reg_id);
        name = findViewById(R.id.reg_name);
        password = findViewById(R.id.reg_password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod()); //패스워드 입력 글자를 '*' 로 바꾸는 함수
        passwordcheck = findViewById(R.id.reg_passwordcheck);
        passwordcheck.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        firebaseAuth = FirebaseAuth.getInstance();// FireBase에서 현재 로그인 정보 가져오기

        regiBtn = findViewById(R.id.btn_register);
        regiBtn.setOnClickListener(v -> { //회원가입 버튼이 클릭되었을 때

            if (!id.getText().toString().equals("") && !name.getText().toString().equals("") &&
                    !password.getText().toString().equals("")
                    && !passwordcheck.getText().toString().equals("")) { //모든 내용이 기입되었을 경우
                createAccount(id.getText().toString(), password.getText().toString(), passwordcheck.getText().toString()); //기입한 정보로 회원가입 진행
            }
            else { //채워지지 않은 항목이 있을 경우
                Toast.makeText(getApplicationContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPasswd(String target) { //패스워드 유효성 검사 함수
        Pattern p = Pattern.compile("(^.*(?=.{4,50})(?=.*[0-9])(?=.*[a-z]).*$)"); //패스워드 생성 규칙, 4~50자, 알파벳+숫자
        Matcher m = p.matcher(target); //정규식 대입 검사
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"); //정규식 검사값이 옳고, 한글이 포함되지 않았다면 통과
    }

    private boolean isValidEmail(String target) { //Email 유효성 검사 함수
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches(); //Email 유효성 검사 결과 반환
    }

    private void createAccount(final String email, final String password, final String passwordcheck) { //회원가입 함수

        if (!isValidEmail(email)) { //이메일 유효성 검사에 실패할 경우
            Toast.makeText(getApplicationContext(), "이메일이 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //이메일 유효성검사 실패 토스트
            return;
        }

        if (!isValidPasswd(password)) { //패스워드 유효성 검사에 실패할 경우
            Toast.makeText(getApplicationContext(), "패스워드가 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //패스워드 유효성 검사 실패 토스트
            return;
        }

        if (!password.equals(passwordcheck)) { //패스워드와 패스워드 재입력이 일치하지 않는 경우
            Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show(); //재입력 불일치 토스트
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password) //Firebase에 회원가입 요청
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) { //회원가입에 성공했다면
                        FirebaseUser user = firebaseAuth.getCurrentUser(); //회원가입된 유저 정보 가져오기

                        //회원가입과 동시에, Database에 유저 정보 추가
                        UserModel model = new UserModel(name.getText().toString(), email, user.getUid());
                        databaseReference.child("user").push().setValue(model); //DB의 "user" 항목에 유저 정보 추가
                        finish();
                    }
                    else { //회원가입에 실패했다면
                        Toast.makeText(getApplicationContext(), "이미 가입된 계정입니다.", Toast.LENGTH_SHORT).show(); //화원가입 실패 토스트
                    }
                });


    }

    //패스워드 글자를 '*'로 바꾸는 함수
    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        static class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '*';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }

}
