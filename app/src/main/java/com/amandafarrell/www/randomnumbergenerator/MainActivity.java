package com.amandafarrell.www.randomnumbergenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int firstChoice;
    private int secondChoice;

    private EditText firstNumberChoiceEditText;
    private EditText secondNumberChoiceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView randomNumberDisplayTextView = (TextView) findViewById(R.id.random_number_display);
        firstNumberChoiceEditText = (EditText) findViewById(R.id.first_number_choice);
        secondNumberChoiceEditText = (EditText) findViewById(R.id.second_number_choice);

        firstChoice = Integer.parseInt(firstNumberChoiceEditText.getText().toString());
        secondChoice = Integer.parseInt(secondNumberChoiceEditText.getText().toString());

        firstNumberChoiceEditText.addTextChangedListener(firstChoiceTextWatcher);
        secondNumberChoiceEditText.addTextChangedListener(secondChoiceTextWatcher);

        //Generate a new random number between the first and second number choices
        Button newNumberButton = (Button) findViewById(R.id.new_number_button);
        newNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomNumber = generateRandomNumber(firstChoice, secondChoice);
                randomNumberDisplayTextView.setText(String.valueOf(randomNumber));
            }
        });
    }

    int generateRandomNumber (int firstChoice, int secondChoice) {
        if (firstChoice == secondChoice){
            return firstChoice;
        }
        Random rand = new Random();
        return rand.nextInt(Math.abs((firstChoice - secondChoice)) + 1)
                + Math.min(firstChoice,secondChoice);
    }

    //TextWatchers listen for the user to make changes to the number range
    private TextWatcher firstChoiceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try{
                firstChoice = Integer.parseInt(firstNumberChoiceEditText.getText().toString());
            }
            catch (NumberFormatException e) {
                //this will catch the "-" when a negative number is entered
            }
        }
    };

    private TextWatcher secondChoiceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try{
                secondChoice = Integer.parseInt(secondNumberChoiceEditText.getText().toString());
            }
            catch (NumberFormatException e) {
                //this will catch the "-" when a negative number is entered
            }
        }
    };
}
