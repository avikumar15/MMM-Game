package com.example.mmm.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mmm.NotificationService;
import com.example.mmm.R;
import com.example.mmm.Utils;
import com.example.mmm.game.GamePlay;
import com.example.mmm.game.GameStatusInterface;
import com.example.mmm.viewmodel.GameViewModel;

import static com.example.mmm.Utils.SP_KEY;
import static com.example.mmm.Utils.USER_SCORE;

public class GameActivity extends AppCompatActivity implements GameStatusInterface {

    GamePlay gamePlay;
    FrameLayout frameLayout;

    final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels ;

    private final static String TAG = "MainActivity";

    Button btnLeader;
    Button btnLogOut;

    Button tv;

    SharedPreferences sharedPref;

    String userName="";

    Long current= 0L;
    GameViewModel model;

    boolean hasGameStarted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        sharedPref = getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
        userName = sharedPref.getString(Utils.USER_KEY, "");
        current = sharedPref.getLong(Utils.USER_SCORE, 0L);

        gamePlay = new GamePlay(this, screenWidth, screenHeight);

        frameLayout = findViewById(R.id.GameBackground);

        btnLeader = findViewById(R.id.btnLeaderGame);
        btnLogOut = findViewById(R.id.btnLogoutGame);
        tv = findViewById(R.id.tvHelpText);

        tv.setText("WELCOME "+userName+"!!!\nSWIPE TO GET GOING");

        model = (new ViewModelProvider(this)).get(GameViewModel.class);

        frameLayout.addView(gamePlay);

        btnLogOut.setOnClickListener(view -> {
            Intent intent = new Intent(GameActivity.this, LoginActivity.class);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Utils.USER_KEY, "");
            editor.apply();
            model.removeListener(); startActivity(intent);
            finish();
            overridePendingTransition(0,0);
        });

        btnLeader.setOnClickListener(view -> {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            model.removeListener(); startActivity(intent);
            overridePendingTransition(0,0);
        });

    }

    @Override
    public void onGameStarted() {
        if(!hasGameStarted) {
            tv.setVisibility(View.INVISIBLE);
            btnLogOut.setVisibility(View.INVISIBLE);
            btnLeader.setVisibility(View.INVISIBLE);
            model.removeListener();
            hasGameStarted = true;
        }
    }

    @Override
    public void onGameEnded(Long score) {

        tv.setVisibility(View.VISIBLE);
        tv.setText("GAME OVER!!\nTap to go back");

        if(score>=10)
            startService("Gg, your score is greater than 100!");
        else
            startService("Your score- "+score+" Better luck next time :)");

        model.updateScore(userName, current + score);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(USER_SCORE,score+current);
        editor.apply();

    }

    public void startService(String msg) {

        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("INTENT_TEXT", msg);

        ContextCompat.startForegroundService(this, serviceIntent);

    }

}
