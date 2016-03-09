package net.dvt32.chesstime;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Global views
    TextView playerOneTime, playerTwoTime, playerOneMoves, playerTwoMoves;
    ImageView whiteKnight, blackKnight;
    ColorStateList defaultTextColor, lightTextColor, defaultMoveTextColor;
    RadioGroup timeRadioGroup;
    RadioButton fiveMins, tenMins, fifteenMins, twentyMins, thirtyMins, sixtyMins;
    CheckBox flipMode, countMoves;
    Button startButton, switchButton, resetButton;

    CountDownTimer playerOneTimer, playerTwoTimer;

    int currentPlayer = 1;
    long playerOneTimeLeft = 0, playerTwoTimeLeft = 0;
    int timeInMilliseconds = 0;
    boolean doubleBackToExitPressedOnce = false;
    boolean flipModeIsEnabled = false, countMovesIsEnabled = false;
    int numberOfPlayerOneMoves = 0, numberOfPlayerTwoMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViews initialization
        playerOneTime = (TextView) findViewById(R.id.player1_time);
        playerTwoTime = (TextView) findViewById(R.id.player2_time);
        playerOneMoves = (TextView) findViewById(R.id.player1_moves);
        playerTwoMoves = (TextView) findViewById(R.id.player2_moves);
        defaultTextColor = playerOneTime.getTextColors();
        defaultMoveTextColor = playerOneMoves.getTextColors();
        playerOneTime.setTextColor(Color.parseColor("#d8d1d1"));
        playerTwoTime.setTextColor(Color.parseColor("#d8d1d1"));
        lightTextColor = playerTwoTime.getTextColors();

        // Set transparency
        whiteKnight = (ImageView) findViewById(R.id.white_image_view);
        blackKnight = (ImageView) findViewById(R.id.black_image_view);

        // RadioButtons initialization
        timeRadioGroup = (RadioGroup) findViewById(R.id.set_time_radio_group);

        fiveMins = (RadioButton) findViewById(R.id.five_radio_button);
        tenMins = (RadioButton) findViewById(R.id.ten_radio_button);
        fifteenMins = (RadioButton) findViewById(R.id.fifteen_radio_button);
        twentyMins = (RadioButton) findViewById(R.id.twenty_radio_button);
        thirtyMins = (RadioButton) findViewById(R.id.thirty_radio_button);
        sixtyMins = (RadioButton) findViewById(R.id.sixty_radio_button);

        // CheckBoxes initialization
        flipMode = (CheckBox) findViewById(R.id.flip_mode_checkbox);
        countMoves = (CheckBox) findViewById(R.id.count_moves_checkbox);

        // Buttons initialization
        startButton = (Button) findViewById(R.id.start_button);
        switchButton = (Button) findViewById(R.id.switch_button);
        resetButton = (Button) findViewById(R.id.reset_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    // Method for setting the time
    public void setTimer(View v) {
        if (fiveMins.isChecked()) {
            timeInMilliseconds = 300000;
            playerOneTime.setText("5:00");
            playerTwoTime.setText("5:00");
        }
        if (tenMins.isChecked()) {
            timeInMilliseconds = 600000;
            playerOneTime.setText("10:00");
            playerTwoTime.setText("10:00");
        }
        if (fifteenMins.isChecked()) {
            timeInMilliseconds = 900000;
            playerOneTime.setText("15:00");
            playerTwoTime.setText("15:00");
        }
        if (twentyMins.isChecked()) {
            timeInMilliseconds = 1200000;
            playerOneTime.setText("20:00");
            playerTwoTime.setText("20:00");
        }
        if (thirtyMins.isChecked()) {
            timeInMilliseconds = 1800000;
            playerOneTime.setText("30:00");
            playerTwoTime.setText("30:00");
        }
        if (sixtyMins.isChecked()) {
            timeInMilliseconds = 3600000;
            playerOneTime.setText("1:00:00");
            playerTwoTime.setText("1:00:00");
        }
    }

    // Method for setting the additional options
    public void toggleFlipMode(View v) { flipModeIsEnabled = !flipModeIsEnabled;}
    public void toggleCountMoves(View v) { countMovesIsEnabled = !countMovesIsEnabled;}

    // Method for starting the timer
    public void startTimer(View v) {
        // Check if there's a RadioButton checked before starting the timer.
        if (fiveMins.isChecked() || tenMins.isChecked() || fifteenMins.isChecked() || twentyMins.isChecked() || thirtyMins.isChecked() || sixtyMins.isChecked()) {
            // Check if any additional options are enabled
            if (flipModeIsEnabled) {
                if (countMovesIsEnabled) playerOneMoves.setRotation(180);
                whiteKnight.setRotation(180);
                whiteKnight.setScaleX(-1f);
                playerOneTime.setRotation(180);
                switchButton.setRotation(180);
            }

            if (countMovesIsEnabled) {
                playerOneMoves.setVisibility(View.VISIBLE);
                playerTwoMoves.setVisibility(View.VISIBLE);
                numberOfPlayerOneMoves++;
                playerOneMoves.setText("Move " + numberOfPlayerOneMoves);
            }

            // Disable RadioButton functionality
            for (int i = 0; i < timeRadioGroup.getChildCount(); ++i){
                timeRadioGroup.getChildAt(i).setEnabled(false);
            }

            // Disable CheckBox functionality
            flipMode.setEnabled(false);
            countMoves.setEnabled(false);

            // Set transparency
            whiteKnight.setAlpha(1f);
            playerOneTime.setTextColor(defaultTextColor);

            // Initialize Player 1 timer and start it
            playerOneTimer = new CountDownTimer(timeInMilliseconds, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    playerOneTime.setText(String.format("%01d:%02d", minutes, seconds));
                    playerOneTimeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    playerOneTime.setTypeface(playerOneTime.getTypeface(), Typeface.ITALIC);
                    playerOneTime.setTextColor(Color.RED);
                    if (flipModeIsEnabled) playerOneTime.setPadding(0,0, 150, 0);
                    else playerOneTime.setPadding(100, 0,0,0);
                    playerOneTime.setText("Time's up!");
                    switchButton.setVisibility(View.GONE);
                    resetButton.setVisibility(View.VISIBLE);
                }
            };
            playerOneTimer.start();

            // Make Start button invisible once it's been pressed and the Switch button visible
            startButton.setVisibility(View.GONE);
            switchButton.setVisibility(View.VISIBLE);

            // Set an initial start time for Player 2
            playerTwoTimeLeft = timeInMilliseconds;

        } else {
            Toast selectTimeFirst = Toast.makeText(getApplicationContext(), "Please set the time limit first.", Toast.LENGTH_SHORT);
            selectTimeFirst.show();
        }
    }

    // Method for switching the players' timer
    public void switchTimer(View v) {
        if (currentPlayer == 1) {
            currentPlayer = 2;

            // Check if any additional options are enabled
            if (flipModeIsEnabled) {
                switchButton.setRotation(0);
            }

            if (countMovesIsEnabled) {
                numberOfPlayerTwoMoves++;
                playerTwoMoves.setText("Move " + numberOfPlayerTwoMoves);
            }

            // Set transparency
            whiteKnight.setAlpha(0.4f);
            playerOneTime.setTextColor(lightTextColor);
            if (countMovesIsEnabled) {
                playerOneMoves.setTextColor(lightTextColor);
                playerTwoMoves.setTextColor(defaultMoveTextColor);

            }
            blackKnight.setAlpha(1f);
            playerTwoTime.setTextColor(defaultTextColor);

            playerOneTimer.cancel();
            playerTwoTimer = new CountDownTimer(playerTwoTimeLeft, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    playerTwoTime.setText(String.format("%01d:%02d", minutes, seconds));
                    playerTwoTimeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    playerTwoTime.setTypeface(playerTwoTime.getTypeface(), Typeface.ITALIC);
                    playerTwoTime.setTextColor(Color.RED);
                    playerTwoTime.setPadding(0, 0,150,0);
                    playerTwoTime.setText("Time's up!");
                    switchButton.setVisibility(View.GONE);
                    resetButton.setVisibility(View.VISIBLE);
                }
            };

            playerTwoTimer.start();
            return;
        }
        if (currentPlayer == 2) {
            currentPlayer = 1;

            // Check if any additional options are enabled
            if (flipModeIsEnabled) {
                switchButton.setRotation(180);
            }

            if (countMovesIsEnabled) {
                numberOfPlayerOneMoves++;
                playerOneMoves.setText("Move " + numberOfPlayerOneMoves);
            }

            // Set transparency
            blackKnight.setAlpha(0.4f);
            playerTwoTime.setTextColor(lightTextColor);
            if (countMovesIsEnabled) {
                playerTwoMoves.setTextColor(lightTextColor);
                playerOneMoves.setTextColor(defaultMoveTextColor);
            }
            whiteKnight.setAlpha(1f);
            playerOneTime.setTextColor(defaultTextColor);

            playerTwoTimer.cancel();
            playerOneTimer = new CountDownTimer(playerOneTimeLeft, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    playerOneTime.setText(String.format("%01d:%02d", minutes, seconds));
                    playerOneTimeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    playerOneTime.setTypeface(playerOneTime.getTypeface(), Typeface.ITALIC);
                    playerOneTime.setTextColor(Color.RED);
                    if (flipModeIsEnabled) playerOneTime.setPadding(0,0, 150, 0);
                    else playerOneTime.setPadding(100, 0,0,0);
                    playerOneTime.setText("Time's up!");
                    switchButton.setVisibility(View.GONE);
                    resetButton.setVisibility(View.VISIBLE);
                }
            };

            playerOneTimer.start();
            return;
        }
    }

    public void resetTimer(View v){
        // Re-enable RadioButtons
        for (int i = 0; i < timeRadioGroup.getChildCount(); ++i){
            timeRadioGroup.getChildAt(i).setEnabled(true);
        }

        timeRadioGroup.clearCheck(); // clear check for radio group

        // Reset transparency
        whiteKnight.setAlpha(0.4f);
        blackKnight.setAlpha(0.4f);

        // Reset values for timer TextViews
        playerOneTime.setText("0:00");
        playerTwoTime.setText("0:00");

        // Reset TextView style
        playerOneTime.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        playerOneTime.setTextColor(lightTextColor);
        playerOneTime.setPadding(0, 0, 0, 0);
        playerTwoTime.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        playerTwoTime.setTextColor(lightTextColor);
        playerTwoTime.setPadding(0, 0, 0, 0);

        // Make the Start button re-appear.
        resetButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        // Re-enable CheckBoxes
        flipMode.setEnabled(true);
        flipMode.setChecked(false);
        countMoves.setEnabled(true);
        countMoves.setChecked(false);

        // Reset move counter (if enabled)
        if (countMovesIsEnabled){
            countMovesIsEnabled = false;

            numberOfPlayerOneMoves = 0;
            numberOfPlayerTwoMoves = 0;
            playerOneMoves.setVisibility(View.GONE);
            playerOneMoves.setTextColor(defaultMoveTextColor);
            playerTwoMoves.setText("");
            playerTwoMoves.setVisibility(View.GONE);
            playerTwoMoves.setTextColor(defaultMoveTextColor);
        }

        // Reset flip mode (if enabled)
        if (flipModeIsEnabled) {
            flipModeIsEnabled = false;

            if (countMovesIsEnabled) playerOneMoves.setRotation(0);

            whiteKnight.setRotation(0);
            whiteKnight.setScaleX(1);
            playerOneTime.setRotation(0);
            switchButton.setRotation(0);
        }

        currentPlayer = 1;
    }

    public void sendFeedback(View v){
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {"dtrifonov32@gmail.com"} );
        email.putExtra(Intent.EXTRA_SUBJECT, "ChessTime Feedback");
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        };
    }
}
