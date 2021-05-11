package com.vinay.tapee;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textview.MaterialTextView;
import com.vinay.tapee.model.Box;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TapeeGameActivity extends AppCompatActivity implements View.OnClickListener {

    View topLeftBox, topRightBox, bottomLeftBox, bottomRightBox;
    MaterialTextView scoreView;
    Timer timer;
    AlertDialog gameOverDialog;
    Box previous, current;
    int score = 0, tickCounter = 0;
    Box[] boxes;
    final int TICK = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapee_game_activity);
        initViews(); //init all views
        initListeners(); //setup all listeners
        initBoxes(); // setup the box layout config
    }

    //init all views
    private void initViews() {
        scoreView = findViewById(R.id.scoreView);
        topLeftBox = findViewById(R.id.topLeftBox);
        topRightBox = findViewById(R.id.topRightBox);
        bottomLeftBox = findViewById(R.id.bottomLeftBox);
        bottomRightBox = findViewById(R.id.bottomRightBox);
    }

    //setup all listeners
    private void initListeners() {
        topLeftBox.setOnClickListener(this);
        topRightBox.setOnClickListener(this);
        bottomLeftBox.setOnClickListener(this);
        bottomRightBox.setOnClickListener(this);
    }

    // setup the box layout config
    private void initBoxes() {
        boxes = new Box[4];
        boxes[0] = new Box(topLeftBox, "#ED7D39");
        boxes[1] = new Box(topRightBox, "#5B9BD5");
        boxes[2] = new Box(bottomLeftBox, "#F8C146");
        boxes[3] = new Box(bottomRightBox, "#70AD47");
    }

    // start Tapee game
    private void restartTapeeGame() {
        resetGameParam();
        startTapeeGame();
    }

    //reset initial game params
    private void resetGameParam() {
        score = 0;
        tickCounter = 0;
        previous.resetColor();
        previous = null;
        current = null;
        scoreView.setText("Score: 0");
    }

    //start tapee game
    private void startTapeeGame() {
        Random random = new Random();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (score != tickCounter) {
                    stopTapeeGame();
                    runOnUiThread(() -> showGameOver());
                    return;
                }
                if (previous != null) {
                    previous.resetColor();
                }
                setRandomBox(boxes, random, previous);
                current.setSelected();
                previous = current;
                tickCounter++;
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, TICK);
    }

    // set random box
    private void setRandomBox(Box[] viewArray, Random r, Box previous) {
        Box selected;
        do {
            int randomIndex = r.nextInt(4);
            selected = viewArray[randomIndex];
        } while (selected.equals(previous));
        current = selected;
    }

    //show game-over popup
    private void showGameOver() {
        gameOverDialog = new AlertDialog.Builder(TapeeGameActivity.this).create();
        gameOverDialog.setTitle("Game Over");
        gameOverDialog.setMessage("Your Score is: " + score);
        gameOverDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Restart", (dialog, which) -> {
            gameOverDialog.dismiss();
            restartTapeeGame();
        });
        gameOverDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit Tapyee", (dialog, which) -> {
            finish();
        });
        gameOverDialog.setCancelable(false);
        gameOverDialog.show();
    }

    //stop Tapee game
    private void stopTapeeGame(){
        if(timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (timer != null) {
            if (v.getId() == current.getBoxView().getId()) {
                score += current.redeemTapScore();
                scoreView.setText(String.format("Score: %d", score));
            } else {
                stopTapeeGame();
                showGameOver();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameOverDialog != null){
            gameOverDialog.dismiss();
        }
        stopTapeeGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(() -> startTapeeGame(), 2000);
    }
}