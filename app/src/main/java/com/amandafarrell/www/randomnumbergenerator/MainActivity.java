package com.amandafarrell.www.randomnumbergenerator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    private int randomNumber;

    private int firstChoice;
    private int secondChoice;

    private int previousNumber;
    private int previousNumber2;
    private int previousNumber3;
    private int previousNumber4;

    private EditText firstNumberChoiceEditText;
    private EditText secondNumberChoiceEditText;

    private TextView previousNumberTextView;
    private TextView previousNumber2TextView;
    private TextView previousNumber3TextView;
    private TextView previousNumber4TextView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find TextViews
        previousNumberTextView = (TextView) findViewById(R.id.previous_number);
        previousNumber2TextView = (TextView) findViewById(R.id.previous_number2);
        previousNumber3TextView = (TextView) findViewById(R.id.previous_number3);
        previousNumber4TextView = (TextView) findViewById(R.id.previous_number4);

        final TextView randomNumberDisplayTextView = (TextView) findViewById(R.id.random_number_display);
        firstNumberChoiceEditText = (EditText) findViewById(R.id.first_number_choice);
        secondNumberChoiceEditText = (EditText) findViewById(R.id.second_number_choice);

        //Set variable values from Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        randomNumber = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_random_number_key),
                getString(R.string.settings_random_number_default)));

        firstChoice = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_first_choice_key),
                getString(R.string.settings_first_choice_default)));

        secondChoice = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_second_choice_key),
                getString(R.string.settings_second_choice_default)));

        previousNumber = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_previous_number_key),
                getString(R.string.settings_previous_number_default)));
        previousNumber2 = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_previous_number2_key),
                getString(R.string.settings_previous_number2_default)));
        previousNumber3 = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_previous_number3_key),
                getString(R.string.settings_previous_number3_default)));
        previousNumber4 = Integer.parseInt(sharedPreferences.getString(
                getString(R.string.settings_previous_number4_key),
                getString(R.string.settings_previous_number4_default)));

        //Display variables
        randomNumberDisplayTextView.setText(String.valueOf(randomNumber));

        firstNumberChoiceEditText.setText(String.valueOf(firstChoice));
        secondNumberChoiceEditText.setText(String.valueOf(secondChoice));

        previousNumberTextView.setText(String.valueOf(previousNumber));
        previousNumber2TextView.setText(String.valueOf(previousNumber2));
        previousNumber3TextView.setText(String.valueOf(previousNumber3));
        previousNumber4TextView.setText(String.valueOf(previousNumber4));

        //Add TextWatchers
        firstNumberChoiceEditText.addTextChangedListener(firstChoiceTextWatcher);
        secondNumberChoiceEditText.addTextChangedListener(secondChoiceTextWatcher);

        //Generate a new random number between the first and second number choices
        Button newNumberButton = (Button) findViewById(R.id.new_number_button);
        newNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Update previous numbers
                previousNumber4 = previousNumber3;
                previousNumber3 = previousNumber2;
                previousNumber2 = previousNumber;
                previousNumber = randomNumber;

                //Generate new random number
                randomNumber = generateRandomNumber(firstChoice, secondChoice);

                //Display new numbers
                randomNumberDisplayTextView.setText(String.valueOf(randomNumber));
                previousNumberTextView.setText(String.valueOf(previousNumber));
                previousNumber2TextView.setText(String.valueOf(previousNumber2));
                previousNumber3TextView.setText(String.valueOf(previousNumber3));
                previousNumber4TextView.setText(String.valueOf(previousNumber4));

                //Save numbers to SharedPreferences
                sharedPreferences.edit()
                        .putString(getString(R.string.settings_random_number_key),
                                String.valueOf(randomNumber))
                        .putString(getString(R.string.settings_previous_number_key),
                                String.valueOf(previousNumber))
                        .putString(getString(R.string.settings_previous_number2_key),
                                String.valueOf(previousNumber2))
                        .putString(getString(R.string.settings_previous_number3_key),
                                String.valueOf(previousNumber3))
                        .putString(getString(R.string.settings_previous_number4_key),
                                String.valueOf(previousNumber4))
                        .apply();
            }
        });
    }

    int generateRandomNumber(int firstChoice, int secondChoice) {
        if (firstChoice == secondChoice) {
            return firstChoice;
        }
        Random rand = new Random();
        return rand.nextInt(Math.abs((firstChoice - secondChoice)) + 1)
                + Math.min(firstChoice, secondChoice);
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
            try {
                firstChoice = Integer.parseInt(firstNumberChoiceEditText.getText().toString());
                sharedPreferences.edit()
                        .putString(getString(R.string.settings_first_choice_key),
                                String.valueOf(firstChoice))
                        .apply();
            } catch (NumberFormatException e) {
                //This will catch the "-" when a negative number is entered
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
            try {
                secondChoice = Integer.parseInt(secondNumberChoiceEditText.getText().toString());
                sharedPreferences.edit()
                        .putString(getString(R.string.settings_second_choice_key),
                                String.valueOf(secondChoice))
                        .apply();
            } catch (NumberFormatException e) {
                //This will catch the "-" when a negative number is entered
            }
        }
    };
}
