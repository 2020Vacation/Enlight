package com.example.enlight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gelitenight.waveview.library.WaveView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    EditText id, password;
    LinearLayout loginBtn;
    TextView regiBtn;

    LinearLayout titleBox, editBox;

    private WaveHelper mWaveHelper; //물결 효과 클래스

    Animation fadeAnimation1, fadeAnimation2;

    //Firebase Authentication(회원가입) 기능 가져오기
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //StatusBar 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //전체화면
        setContentView(R.layout.activity_login);

        //물결 효과
        final WaveView waveView = findViewById(R.id.wave);

        mWaveHelper = new WaveHelper(waveView);

        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveColor(
                //물결 색상 설정
                Color.parseColor("#2866bb6a"),
                Color.parseColor("#3c66bb6a"));

        titleBox = findViewById(R.id.login_title_box);
        editBox = findViewById(R.id.login_edit_box);
        regiBtn = findViewById(R.id.btn_newaccount);

        fadeAnimation1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        fadeAnimation1.setStartOffset(1500); //앱 이름 Fade in 효과(1.5초)
        fadeAnimation2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        fadeAnimation2.setStartOffset(2500); //입력 창, 로그인 버튼 Fade in 효과(2.5초)

        //Fade in 효과 실행
        titleBox.startAnimation(fadeAnimation1);
        editBox.startAnimation(fadeAnimation2);
        regiBtn.startAnimation(fadeAnimation2);

        firebaseAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.login_password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod()); //비밀번호 입력 시 문자를 '*'로 바꿔줌

        //자동 로그인 구현
        authStateListener = firebaseAuth -> { //로그인 상태 변경 감지
            FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인된 유저 정보 가져오기
            if (user != null) { //이미 로그인이 되어있는 상태라면
                //자동 로그인 실행
                startActivity(new Intent(LoginActivity.this, DBLoadActivity.class));
                finish(); //DBLoadActivity로 이동 후 로그인 액티비티 종료
            }
        };

        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(v -> { //로그인 버튼이 클릭되었을 때

            id = findViewById(R.id.login_id);
            if (!id.getText().toString().equals("") && !password.getText().toString().equals("")) { //모든 항목을 작성했다면
                login(id.getText().toString(), password.getText().toString()); //입력한 ID와 PW로 로그인 요청
            } else { //입력하지 않은 항목이 있다면
                Toast.makeText(getApplicationContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
            }
        });

        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원가입 버튼이 클릭되었을 때
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)); //회원가입 액티비티 실행
            }
        });
    }

    @Override
    protected void onStart() { //액티비티가 시작할 때
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener); //로그인 상태 리스너 추가(자동 로그인을 위함)
    }

    @Override
    protected void onStop() { //액티비티가 끝날 때
        super.onStop();
        if (authStateListener != null) { //만약 리스너가 남아있는 상태라면
            firebaseAuth.removeAuthStateListener(authStateListener); //리스너 제거
        }
    }

    @Override
    protected void onPause() { //액티비티가 일지중지 되었을 때
        super.onPause();
        mWaveHelper.cancel(); //물결 효과 중단
    }

    @Override
    protected void onResume() { //액티비티가 다시 시작되었을 때
        super.onResume();
        //물결 효과, Fade 효과 다시 실행
        mWaveHelper.start();
        titleBox.startAnimation(fadeAnimation1);
        editBox.startAnimation(fadeAnimation2);
        regiBtn.startAnimation(fadeAnimation2);
    }

    private void login(String email, String password) { //로그인 함수
        firebaseAuth.signInWithEmailAndPassword(email, password) //FireBase에 로그인 요청
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) { //로그인에 성공했다면
                        startActivity(new Intent(LoginActivity.this, DBLoadActivity.class));
                        finish(); //DBLoadActivity로 이동 후 액티비티 종료
                    } else { //로그인에 실패했다면
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //패스워드를 '*'로 바꿔주는 함수
    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
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
