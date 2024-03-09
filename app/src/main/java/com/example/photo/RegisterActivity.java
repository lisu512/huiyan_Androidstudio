package com.example.photo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirmPassword;
    Button registerBtn;
    TextView toLogin;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegisterUsername);
        edEmail = findViewById(R.id.editTextRegisterEmail);
        edPassword = findViewById(R.id.editTextRegisterPassword);
        edConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        registerBtn = findViewById(R.id.RegisterBtn);
        toLogin = findViewById(R.id.ToLogin);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入对应的栏位", Toast.LENGTH_SHORT).show();
                } else if (!isEmailValid(email)) {
                    Toast.makeText(getApplicationContext(), "请输入有效的邮箱地址！", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (!isValid(password)) {
                    Toast.makeText(getApplicationContext(), "密码必须包含8个字母、数字、特殊符号！", Toast.LENGTH_SHORT).show();
                } else {
                    // 这里假设db.register()是你注册用户的方法
                    db = new Database(getApplicationContext(), "LISU", null, 1);
                    db.register(username, email, password);
                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                    Log.d("Register", "Registering user: " + username);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValid(String password) {
        boolean hasLetter = false, hasDigit = false, hasSpecialChar = false;
        if (password.length() < 8) return false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ((c >= 33 && c <= 46) || c == 64) hasSpecialChar = true;
        }
        return hasLetter && hasDigit && hasSpecialChar;
    }
}
