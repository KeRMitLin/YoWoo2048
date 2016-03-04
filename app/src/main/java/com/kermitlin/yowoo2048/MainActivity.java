package com.kermitlin.yowoo2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static MainActivity mainActivity = null;
    private int score = 0;
    private TextView tvScore, tvBestScore, btnNewGame;
    private GameView gameView;
    private AnimLayer animLayer = null;
    public static final String SP_KEY_BEST_SCORE = "bestScore";

    public MainActivity() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.startGame();
            }
        });
    }

    private void findViews() {
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        gameView = (GameView) findViewById(R.id.gameView);
        btnNewGame = (TextView) findViewById(R.id.btnNewGame);
        animLayer = (AnimLayer) findViewById(R.id.animLayer);
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(String.valueOf(score));
    }

    public void addScore(int s) {
        score = score + s;
        showScore();

        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    //用SharePreference的方式存取最高分數
    public void saveBestScore(int s) {
        SharedPreferences.Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putInt(SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getBestScore() {
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int score) {
        tvBestScore.setText(String.valueOf(score));
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
