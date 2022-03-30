package com.mitya.calculator;

import static com.mitya.calculator.helper.Expression.isAlreadyAnExpression;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mitya.calculator.helper.Expression;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements Keyboard.OnKeyboardFragmentListener {

    private static final String KEYBOARD_TAG = "keyboard";
    private static final String DISPLAY_TAG = "display";
    Keyboard mKeyboardFragment;
    Display mDisplayFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        mDisplayFragment = (Display) fragmentManager.findFragmentByTag(DISPLAY_TAG);
        if (mDisplayFragment == null) {
            mDisplayFragment = new Display();
            fragmentManager.beginTransaction().add(R.id.displayFragmentContainer, mDisplayFragment, DISPLAY_TAG).commit();
        }

        mKeyboardFragment = (Keyboard) fragmentManager.findFragmentByTag(KEYBOARD_TAG);
        if (mKeyboardFragment == null) {
            mKeyboardFragment = new Keyboard();
            fragmentManager.beginTransaction().add(R.id.keyboardFragmentContainer, mKeyboardFragment, KEYBOARD_TAG).commit();
        }

    }

    @Override
    public void messageFromGreenFragment(String text) {
        mDisplayFragment.youveGotMail(text);
    }

    public void numberClicked(View v) {
        String currentText = mDisplayFragment.displayTextView.getText().toString();
        TextView number = (TextView) v;

        if (currentText.charAt(0) == '0') {
            currentText = number.getText().toString();
            mDisplayFragment.displayTextView.setText(currentText);
            return;
        }

        currentText += number.getText().toString();

        mDisplayFragment.displayTextView.setText(currentText);
    }

    public void operatorClicked(View v) {
        String currentText = mDisplayFragment.displayTextView.getText().toString();
        TextView operatorTextView = (TextView) v;

        if (operatorTextView.getText().toString().equalsIgnoreCase("CE")) {
            mDisplayFragment.displayTextView.setText("0");
            return;
        }
        if (operatorTextView.getText().toString().equalsIgnoreCase("C")) {
            currentText = currentText.substring(0, currentText.length() - 1);
            mDisplayFragment.displayTextView.setText(currentText);
            return;
        }


        char operator = operatorTextView.getText().toString().charAt(0);

        if (operator == '±') {
            if (currentText.charAt(0) == '0' || currentText.charAt(0) == '√') {
                //Do nothing
                return;
            } else if (currentText.charAt(0) == '—') {
                currentText = currentText.substring(1);
                mDisplayFragment.displayTextView.setText(currentText);
                return;
            } else {
                currentText = '—' + currentText;
                mDisplayFragment.displayTextView.setText(currentText);
                return;
            }
        }

        if (isAlreadyAnExpression(currentText)) {
            Toast.makeText(this, "This already an expression", Toast.LENGTH_SHORT).show();
        } else if (operator == '√') {
            currentText = '√' + currentText;
            mDisplayFragment.displayTextView.setText(currentText);
            return;
        } else {
            currentText += operator;
            mDisplayFragment.displayTextView.setText(currentText);
            return;
        }
    }

    public void equalsClicked(View v) {
        String currentText = mDisplayFragment.displayTextView.getText().toString();

        if (isAlreadyAnExpression(currentText)) {
            //Now we parse the expression
            Expression expression = new Expression(currentText);
            //then we calculate it
            double answer = expression.calculate();
            //then we set the display to the answer we have
            mDisplayFragment.displayTextView.setText(String.valueOf(answer));
        } else {
            Toast.makeText(this, "This is not yet an expression", Toast.LENGTH_SHORT).show();
        }

    }


}