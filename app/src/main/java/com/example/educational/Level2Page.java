package com.example.educational;

import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FunAlgo.R;
import com.example.menu.SettingsPage;

import java.util.ArrayList;

public class Level2Page extends Level1Page {
    //variables
    //to determine the finish point
    final private int[] targetArea = { 400 , 8 };

    //to determine the X values that the user can go to
    final private int[] nonForbiddenAreaX = { 200 , 400 , 400 , 400 , 400 };

    //to determine the Y values that the user can go to
    final private int[] nonForbiddenAreaY = { 368 , 368 , 188, 188 , 8 };
    private TextView movements;
    private Spinner spinnerForward;
    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private Integer[] times = {1, 2, 3};
    private ArrayAdapter<Integer> timesAdapter;
    private ArrayList<String> list;
    private ImageView bee;
    private ImageView honey;
    private Button goForward;
    private Button turnRight;
    private Button turnLeft;
    private Button settings;
    private Button volume;
    private Button back;
    private Button info;
    private Button apply;
    private Button reset;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout.LayoutParams params;
    private int volumeoffID;
    private int volumeonID;
    private Drawable volumeoff;
    private Drawable volumeon;
    private int count = 0;
    private int starsCount;
    private int timesForward;
    private int timesLeft;
    private int timesRight;
    private boolean isGameOver;
    private boolean isVolumeOn;
    private int movementsCount;
    private Button show;
    private String code;
    final static private int changeX = 200;
    final static private int changeY = 180;
    //sharedPreferences to update and save levels
    private SharedPreferences sp;
    private SharedPreferences.Editor et;
    // sharedPreferences for transport data to AchievementsPage
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int background;
    private ConstraintLayout level2Page;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_page);
        level2Page = findViewById(R.id.level2_page_layout);
        background = getSharedPreferences("ShareTheme",MODE_PRIVATE).getInt("theme",0);
        level2Page.setBackgroundResource(background);
        //starting activity
        Intent i = getIntent();
        movementsCount = 0;
        //Views
        reset = findViewById(R.id.reset);
        apply = findViewById(R.id.apply);
        bee = findViewById(R.id.bee);
        honey = findViewById(R.id.flower);
        goForward = findViewById(R.id.goForward);
        turnLeft = findViewById(R.id.turnLeft);
        turnRight = findViewById(R.id.turnRight);
        settings = findViewById(R.id.settings);
        volume = findViewById(R.id.volume);
        back = findViewById(R.id.back);
        info = findViewById(R.id.info);
        layout1 = findViewById(R.id.leftLayout1);
        layout2 = findViewById(R.id.leftLayout2);
        spinnerForward = findViewById(R.id.spinnerForward);
        spinnerLeft = findViewById(R.id.spinnerLeft);
        spinnerRight = findViewById(R.id.spinnerRight);
        movements = findViewById(R.id.movements);
        show = findViewById(R.id.showCode_button);
        code = "";
        //volume
        isVolumeOn = true;
        volumeonID = R.drawable.volumeon;
        volumeoffID = R.drawable.volumeoff;
        volumeon = AppCompatDrawableManager.get().getDrawable(this, volumeonID);
        volumeoff = AppCompatDrawableManager.get().getDrawable(this, volumeoffID);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.daybreaker);

        //spinner
        timesAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, times);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForward.setAdapter(timesAdapter);
        spinnerRight.setAdapter(timesAdapter);
        spinnerLeft.setAdapter(timesAdapter);
        spinnerForward = findViewById(R.id.spinnerForward);
        spinnerLeft = findViewById(R.id.spinnerLeft);
        spinnerRight = findViewById(R.id.spinnerRight);

        list = new ArrayList<String>();
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80);

        isGameOver = false;

        //SharedPreferences to save Level
        sp = getSharedPreferences("isFinishedBooleans", MODE_PRIVATE);
        et = sp.edit();

        //targetArea
        isFinished(Level2Page.this, "2", 4, 5);

        //when the user presses the back button, he switches to Level Page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Level2Page.this, LevelPage.class);
                startActivity(i);
            }
        });

        //when the user presses the settings, he switches to Settings
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Level2Page.this, SettingsPage.class);
                startActivity(i);
            }
        });

        //the user can turn on or off the sound when pressing the sound icon
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVolumeOn) {
                    volume.setBackground(volumeoff);
                    isVolumeOn = false;
                    mediaPlayer.pause();
                } else {
                    volume.setBackground(volumeon);
                    isVolumeOn = true;
                    mediaPlayer.start();
                    ;
                }
            }
        });

        //when the user presses the info, he can get information about level
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Bee needs to reach to hive. Help it with your algorithm!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        //to show the current codes
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if (code == "") {
                    toast = Toast.makeText(getApplicationContext(), "No code here yet.", Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG);
                }
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        //to reset the game
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                code = "";
            }
        });

        //to apply the activities selected by the user
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply.setEnabled(false);
                //to create the object of the ApplyMove class
                ApplyMove applyMove = new ApplyMove(bee,list,changeX,changeY,targetArea,nonForbiddenAreaX,nonForbiddenAreaY,0,0,0,0,null,null,null,0,0,movementsCount);
                Thread t1 = new Thread(applyMove);
                t1.start(); //starting thread
            }
        });

        //when the user click the GO FORWARD
        goForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementsCount = goForwardButton(timesForward, layout1, layout2, list, count, movementsCount, movements, spinnerForward);
            }
        });

        //when the user click the TURN LEFT
        turnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementsCount = turnLeftButton(timesLeft, layout1, layout2, list, count, movementsCount, movements, spinnerLeft);
            }
        });

        //when the user click the TURN RIGHT
        turnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementsCount = turnRightButton(timesRight, layout1, layout2, list, count, movementsCount, movements, spinnerRight);
            }
        });

    }

    /** This method saves the data
     * @param, codeMessage
     * @return
     **/
    public void SaveData(String codeMessage) {
        SharedPreferences sharedPref = Level2Page.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CODEMESSAGE", codeMessage);
        editor.commit();
    }

    /** This method sets the code message
     * @param
     * @return
     **/
    public void setCodeMessage() {
        SharedPreferences sharedPref = Level2Page.this.getPreferences(Context.MODE_PRIVATE);
        code += sharedPref.getString("CODEMESSAGE", "") + "\n";
    }
}