package com.example.easyshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easyshop.R;

public class DeveloperActivity extends AppCompatActivity {

    private Button submitFeedbackButton;
    private EditText eMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        submitFeedbackButton=(Button)findViewById(R.id.developer_submit_feedback_btn);
        eMessage=(EditText)findViewById(R.id.developer_messageBox);


        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eMessage.setText("");
                Toast.makeText(DeveloperActivity.this, "Thank You for your valuable time", Toast.LENGTH_LONG).show();
                Intent i= new Intent(DeveloperActivity.this, HomeActivity.class);
                startActivity(i);

            }
        });

    }

}
