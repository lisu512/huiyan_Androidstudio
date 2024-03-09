package com.example.photo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class QuestionAsker extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private EditText questionEditText;
    private TextView answerTextView;
    private Button askButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionasker); // 使用你的XML布局文件

        questionEditText = findViewById(R.id.editTextText);
        answerTextView = findViewById(R.id.textView3);
        askButton = findViewById(R.id.button);

        askButton.setOnClickListener(view -> askQuestion(questionEditText.getText().toString()));
    }


    private void askQuestion(String question) {

        String url = "http://i-1.gpushare.com:27417/";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("question", question);
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> answerTextView.setText("Error creating JSON"));
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> answerTextView.setText("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String answer = jsonResponse.getString("answer");
                        runOnUiThread(() -> answerTextView.setText(answer));
                    } catch (JSONException | IOException e) {
                        runOnUiThread(() -> answerTextView.setText("Error parsing JSON"));
                    }
                } else {
                    runOnUiThread(() -> answerTextView.setText("Unexpected code " + response));
                }
            }
        });
    }
}
