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
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean ignoreNextTextChange = false;

    private int randomNumber;

    private String firstChoiceString;
    private String secondChoiceString;

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
        previousNumberTextView = findViewById(R.id.previous_number);
        previousNumber2TextView = findViewById(R.id.previous_number2);
        previousNumber3TextView = findViewById(R.id.previous_number3);
        previousNumber4TextView = findViewById(R.id.previous_number4);

        final TextView randomNumberDisplayTextView = findViewById(R.id.random_number_display);
        firstNumberChoiceEditText = findViewById(R.id.first_number_choice);
        secondNumberChoiceEditText = findViewById(R.id.second_number_choice);

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
        Button newNumberButton = findViewById(R.id.new_number_button);
        newNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstChoiceString = firstNumberChoiceEditText.getText().toString();
                secondChoiceString = secondNumberChoiceEditText.getText().toString();
                if (firstChoiceString.isEmpty() ||
                        secondChoiceString.isEmpty() ||
                        firstChoiceString.equals("-") ||
                        secondChoiceString.equals("-")) {
                    Toast.makeText(getBaseContext(), R.string.error_message_invalid_number,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

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

            if (ignoreNextTextChange) {
                ignoreNextTextChange = false;
                return;
            } else {
                ignoreNextTextChange = true;
            }

            firstChoiceString = firstNumberChoiceEditText.getText().toString();
            //remove any leading zeroes
            if (firstChoiceString.length() > 1) {
                firstChoiceString = firstChoiceString.replaceFirst("^0", "");
            }

            if (isParsableInteger(firstChoiceString) == 1){
                firstChoice = Integer.parseInt(firstChoiceString);
                sharedPreferences.edit()
                        .putString(getString(R.string.settings_first_choice_key), firstChoiceString)
                        .apply();

                //This redundantly sets the text so that the textWatcher is called and keeps the
                //boolean ignoreNextTextChange consistent
                firstNumberChoiceEditText.setText(firstChoiceString);
                firstNumberChoiceEditText.setSelection(firstChoiceString.length());
            } else if(isParsableInteger(firstChoiceString) == 0) {
                //This redundantly sets the text so that the textWatcher is called and keeps the
                //boolean ignoreNextTextChange consistent
                firstNumberChoiceEditText.setText(firstChoiceString);
                firstNumberChoiceEditText.setSelection(firstChoiceString.length());
            }
            else {
                Toast.makeText(getBaseContext(), R.string.error_message_invalid_number,
                        Toast.LENGTH_SHORT).show();
                firstChoice = 0;
                firstNumberChoiceEditText.setText(R.string.default_number);
                firstNumberChoiceEditText.setSelection(getString(R.string.default_number).length());
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

            if (ignoreNextTextChange) {
                ignoreNextTextChange = false;
                return;
            } else {
                ignoreNextTextChange = true;
            }

            secondChoiceString = secondNumberChoiceEditText.getText().toString();
            //remove any leading zeroes
            if (secondChoiceString.length() > 1) {
                secondChoiceString = secondChoiceString.replaceFirst("^0", "");
            }

            if (isParsableInteger(secondChoiceString) == 1){
                secondChoice = Integer.parseInt(secondChoiceString);
                sharedPreferences.edit()
                        .putString(getString(R.string.settings_second_choice_key),secondChoiceString)
                        .apply();

                //This redundantly sets the text so that the textWatcher is called and keeps the
                //boolean ignoreNextTextChange consistent
                secondNumberChoiceEditText.setText(secondChoiceString);
                secondNumberChoiceEditText.setSelection(secondChoiceString.length());
            } else if (isParsableInteger(secondChoiceString) == 0){
                //This redundantly sets the text so that the textWatcher is called and keeps the
                //boolean ignoreNextTextChange consistent
                secondNumberChoiceEditText.setText(secondChoiceString);
                secondNumberChoiceEditText.setSelection(secondChoiceString.length());
            }
            else {
                Toast.makeText(getBaseContext(), R.string.error_message_invalid_number,
                        Toast.LENGTH_SHORT).show();
                secondChoice = 0;
                secondNumberChoiceEditText.setText(R.string.default_number);
                secondNumberChoiceEditText.setSelection(getString(R.string.default_number).length());
            }
        }
    };

    /**
     *
     * @param numberString string to parse as an int
     * @return 1 is the String is parsable as an int, -1 if it is not, and 0 for the special
     * cases when a negative number is being entered or no number is entered
     */
    public int isParsableInteger(String numberString){
        if (numberString.equals("-") || numberString.isEmpty()){
            return 0;
        }

        try {
            int test = Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            return -1;
        }
        return 1;
    }
}
