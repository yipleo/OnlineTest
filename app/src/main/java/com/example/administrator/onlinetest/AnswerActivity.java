package com.example.administrator.onlinetest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AnswerActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView question;
    private RadioGroup radioGroup;
    private RadioButton CB_1;
    private RadioButton CB_2;
    private RadioButton CB_3;
    private RadioButton CB_4;
    private Button submit;

    private List<Question> questionList = new ArrayList<>();
    private int id;
    private int value = 100;
    private int index = 1;
    private String description;
    private String answer;
    private String choice_1;
    private String choice_2;
    private String choice_3;
    private String choice_4;

    private void queryInDatabase(int i) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Key where id=?", new String[]{i + ""});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            description = cursor.getString(cursor.getColumnIndex("description"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            choice_1 = cursor.getString(cursor.getColumnIndex("choice_1"));
            choice_2 = cursor.getString(cursor.getColumnIndex("choice_2"));
            choice_3 = cursor.getString(cursor.getColumnIndex("choice_3"));
            choice_4 = cursor.getString(cursor.getColumnIndex("choice_4"));
        }
        cursor.close();
    }

    private void queryInObject(int i) {
        Question question = questionList.get(i - 1);
        id = i;
        description = question.getDescription();
        answer = question.getAnswer();
        choice_1 = question.getChoice_1();
        choice_2 = question.getChoice_2();
        choice_3 = question.getChoice_3();
        choice_4 = question.getChoice_4();
    }

    public void setText(int i) {
        queryInDatabase(i);
        //queryInObject(i);
        question.setText(i + ".  " + description);
        CB_1.setText(choice_1);
        CB_2.setText(choice_2);
        CB_3.setText(choice_3);
        CB_4.setText(choice_4);
    }

    public String getAnswer() {
        return answer;
    }

    public int getIntAnswer() {
        return answer.charAt(0) - 65;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        question = (TextView) findViewById(R.id.question);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        CB_1 = (RadioButton) findViewById(R.id.choice_1);
        CB_2 = (RadioButton) findViewById(R.id.choice_2);
        CB_3 = (RadioButton) findViewById(R.id.choice_3);
        CB_4 = (RadioButton) findViewById(R.id.choice_4);
        submit = (Button) findViewById(R.id.submit);
        dbHelper = new DatabaseHelper(this, "Key.db", null, 1);

        questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        setText(index);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.isChecked()) {
                        if (radioButton.getId() == CB_1.getId() + getIntAnswer()) {
                            if (index < 10) {
                                index++;
                                radioButton.setChecked(false);
                                setText(index);
                            } else {
                                Intent intent = new Intent(AnswerActivity.this, EndActivity.class);
                                intent.putExtra("value", value);
                                startActivity(intent);
                            }
                        } else {
                            if (value >= 10)
                                value -= 10;
                            Toast.makeText(AnswerActivity.this, "正确答案是: " + getAnswer(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        });
    }
}
