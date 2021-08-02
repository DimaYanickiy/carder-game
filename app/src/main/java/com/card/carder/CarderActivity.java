package com.card.carder;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarderActivity extends AppCompatActivity {

    @BindView(R.id.player_1)
    ImageView player_1;

    @BindView(R.id.pl_1)
    LinearLayout pl_1;
    @BindView(R.id.pl_2)
    LinearLayout pl_2;
    @BindView(R.id.pl_3)
    LinearLayout pl_3;
    @BindView(R.id.pl_4)
    LinearLayout pl_4;
    @BindView(R.id.main_l)
    LinearLayout main_l;
    @BindView(R.id.buttons_group)
    LinearLayout buttons_group;

    @BindView(R.id.card_1)
    ImageView card_1;
    @BindView(R.id.card_2)
    ImageView card_2;
    @BindView(R.id.card_3)
    ImageView card_3;
    @BindView(R.id.card_4)
    ImageView card_4;
    @BindView(R.id.card_5)
    ImageView card_5;

    @BindView(R.id.imageView16)
    ImageView card_my_1;
    @BindView(R.id.imageView17)
    ImageView card_my_2;

    @BindView(R.id.player_1_balance)
    TextView player_1_balance;
    @BindView(R.id.player_2_balance)
    TextView player_2_balance;
    @BindView(R.id.player_3_balance)
    TextView player_3_balance;
    @BindView(R.id.player_4_balance)
    TextView player_4_balance;
    @BindView(R.id.my_stavka)
    TextView my_stavka;

    @BindView(R.id.text_balance)
    TextView text_balance;
    @BindView(R.id.text_timer)
    TextView text_timer;

    @BindView(R.id.text_raise)
    TextView text_raise;

    @BindView(R.id.imageButton)
    ImageButton btn_fold;
    @BindView(R.id.imageButton2)
    ImageButton btn_raise;
    @BindView(R.id.imageButton3)
    ImageButton btn_check;
    @BindView(R.id.seekBar2)
    SeekBar seekBar;

    private int width;
    private int height;
    private boolean running;
    private boolean nextMove;
    private int seconds = 60;
    Random random;

    private int[] bolshemenshe = {10, 10, 10, 10, 10};
    private int tokensToRaise = 10;

    private int playerLayoutsXY[] = new int[8];

    private int movedCards[] = new int[7];

    private int cardsOnTable = 0;

    private int tokens = 10000;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carder);
        ButterKnife.bind(this);
        random = new Random();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        setImagesLocations();
        startGame();
        startTimer();
        running = true;
        btn_fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMove = true;
                move();
            }
        });
        btn_raise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokensToRaise > 0){
                    if(cardsOnTable <= 4){
                        tokens -= tokensToRaise;
                        text_balance.setText("Your balance: " + tokens);
                        for(int i = 0; i < bolshemenshe.length; i++){
                            bolshemenshe[i] += tokensToRaise;
                        }
                        setTextTokens();
                        nextMove = true;
                        move();
                    }
                } else {

                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tokensToRaise = seekBar.getProgress()*10;
                text_raise.setText(Integer.toString(tokensToRaise));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tokensToRaise = seekBar.getProgress()*10;
                text_raise.setText(Integer.toString(tokensToRaise));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tokensToRaise = seekBar.getProgress()*10;
                text_raise.setText(Integer.toString(tokensToRaise));
            }
        });
    }

    private void setImagesLocations(){
        playerLayoutsXY[0] = 0;
        playerLayoutsXY[1] = height-height/10*9;
        playerLayoutsXY[2] = 0;
        playerLayoutsXY[3] = height-height/10*5;
        playerLayoutsXY[4] = width-(int)(width/6*1.5);
        playerLayoutsXY[5] = height-height/10*9;
        playerLayoutsXY[6] = width-(int)(width/6*1.5);
        playerLayoutsXY[7] = height-height/10*5;
        pl_1.setX(playerLayoutsXY[0]);
        pl_1.setY(playerLayoutsXY[1]);
        pl_3.setX(playerLayoutsXY[2]);
        pl_3.setY(playerLayoutsXY[3]);
        pl_2.setX(playerLayoutsXY[4]);
        pl_2.setY(playerLayoutsXY[5]);
        pl_4.setX(playerLayoutsXY[6]);
        pl_4.setY(playerLayoutsXY[7]);
        buttons_group.setX((int)(width/10*1.5));
        buttons_group.setY(height-200);
    }

    public void startGame(){
        card_1.setVisibility(View.INVISIBLE);
        card_2.setVisibility(View.INVISIBLE);
        card_3.setVisibility(View.INVISIBLE);
        card_4.setVisibility(View.INVISIBLE);
        card_5.setVisibility(View.INVISIBLE);
        for(int i = 0; i < bolshemenshe.length; i++){
            bolshemenshe[i] = 10;
        }
        counter = 0;
        generateCard();
        setTextTokens();
        tokens -= 10;
        text_balance.setText("Your balance: " + tokens);
        cardsOnTable = 0;
        seconds = 60;

    }

    public void checkCardsCombinations(){
        for(int i = 0; i < counter; i++){
            for(int j = 1; j < counter+1; j++){
                if((movedCards[i] == 1 && movedCards[j] == 2) ||
                        (movedCards[i] == 2 && movedCards[j] == 3) ||
                        (movedCards[i] == 3 && movedCards[j] == 4) ||
                        (movedCards[i] == 4 && movedCards[j] == 1) ||
                        (movedCards[i] == 5 && movedCards[j] == 6) ||
                        (movedCards[i] == 6 && movedCards[j] == 7) ||
                        (movedCards[i] == 7 && movedCards[j] == 8) ||
                        (movedCards[i] == 8 && movedCards[j] == 5) ||
                        (movedCards[i] == 9 && movedCards[j] == 10) ||
                        (movedCards[i] == 10 && movedCards[j] == 11) ||
                        (movedCards[i] == 11 && movedCards[j] == 12) ||
                        (movedCards[i] == 12 && movedCards[j] == 9) ||
                        (movedCards[i] == 13 && movedCards[j] == 14) ||
                        (movedCards[i] == 14 && movedCards[j] == 15) ||
                        (movedCards[i] == 15 && movedCards[j] == 16) ||
                        (movedCards[i] == 16 && movedCards[j] == 13) ||
                        (movedCards[i] == 17 && movedCards[j] == 18) ||
                        (movedCards[i] == 18 && movedCards[j] == 19) ||
                        (movedCards[i] == 19 && movedCards[j] == 20) ||
                        (movedCards[i] == 20 && movedCards[j] == 17) ||
                        (movedCards[i] == 21 && movedCards[j] == 22) ||
                        (movedCards[i] == 22 && movedCards[j] == 23) ||
                        (movedCards[i] == 23 && movedCards[j] == 24) ||
                        (movedCards[i] == 24 && movedCards[j] == 21) ||
                        (movedCards[i] == 25 && movedCards[j] == 26) ||
                        (movedCards[i] == 26 && movedCards[j] == 27) ||
                        (movedCards[i] == 27 && movedCards[j] == 28) ||
                        (movedCards[i] == 28 && movedCards[j] == 25) ||
                        (movedCards[i] == 29 && movedCards[j] == 30) ||
                        (movedCards[i] == 30 && movedCards[j] == 31) ||
                        (movedCards[i] == 31 && movedCards[j] == 32) ||
                        (movedCards[i] == 32 && movedCards[j] == 29) ||
                        (movedCards[i] == 33 && movedCards[j] == 34) ||
                        (movedCards[i] == 34 && movedCards[j] == 35) ||
                        (movedCards[i] == 35 && movedCards[j] == 36) ||
                        (movedCards[i] == 36 && movedCards[j] == 33)){
                    tokens+=tokensToRaise*3;
                    text_balance.setText(Integer.toString(tokens));
                    break;
                }
            }
        }
        for(int i = 0; i < counter-1; i++){
            for(int j = 1; j < counter; j++){
                for(int k = 2; k < counter+1; k++){
                    if((movedCards[i] == 1 && movedCards[j] == 2 && movedCards[k] == 3) ||
                            (movedCards[i] == 2 && movedCards[j] == 3 && movedCards[k] == 4) ||
                            (movedCards[i] == 1 && movedCards[j] == 3 && movedCards[k] == 4) ||
                            (movedCards[i] == 1 && movedCards[j] == 2 && movedCards[k] == 4) ||
                            (movedCards[i] == 5 && movedCards[j] == 6 && movedCards[k] == 7) ||
                            (movedCards[i] == 6 && movedCards[j] == 7 && movedCards[k] == 8) ||
                            (movedCards[i] == 5 && movedCards[j] == 7 && movedCards[k] == 8) ||
                            (movedCards[i] == 5 && movedCards[j] == 6 && movedCards[k] == 8) ||
                            (movedCards[i] == 9 && movedCards[j] == 10 && movedCards[k] == 11) ||
                            (movedCards[i] == 10 && movedCards[j] == 11 && movedCards[k] == 12) ||
                            (movedCards[i] == 9 && movedCards[j] == 11 && movedCards[k] == 12) ||
                            (movedCards[i] == 9 && movedCards[j] == 10 && movedCards[k] == 12) ||
                            (movedCards[i] == 13 && movedCards[j] == 14 && movedCards[k] == 15) ||
                            (movedCards[i] == 14 && movedCards[j] == 15 && movedCards[k] == 16) ||
                            (movedCards[i] == 13 && movedCards[j] == 15 && movedCards[k] == 16) ||
                            (movedCards[i] == 13 && movedCards[j] == 14 && movedCards[k] == 16) ||
                            (movedCards[i] == 17 && movedCards[j] == 18 && movedCards[k] == 19) ||
                            (movedCards[i] == 18 && movedCards[j] == 19 && movedCards[k] == 20) ||
                            (movedCards[i] == 17 && movedCards[j] == 19 && movedCards[k] == 20) ||
                            (movedCards[i] == 17 && movedCards[j] == 18 && movedCards[k] == 20) ||
                            (movedCards[i] == 21 && movedCards[j] == 22 && movedCards[k] == 23) ||
                            (movedCards[i] == 22 && movedCards[j] == 23 && movedCards[k] == 24) ||
                            (movedCards[i] == 21 && movedCards[j] == 23 && movedCards[k] == 24) ||
                            (movedCards[i] == 21 && movedCards[j] == 22 && movedCards[k] == 24) ||
                            (movedCards[i] == 25 && movedCards[j] == 26 && movedCards[k] == 27) ||
                            (movedCards[i] == 26 && movedCards[j] == 27 && movedCards[k] == 28) ||
                            (movedCards[i] == 25 && movedCards[j] == 27 && movedCards[k] == 28) ||
                            (movedCards[i] == 25 && movedCards[j] == 26 && movedCards[k] == 28) ||
                            (movedCards[i] == 29 && movedCards[j] == 30 && movedCards[k] == 31) ||
                            (movedCards[i] == 30 && movedCards[j] == 31 && movedCards[k] == 32) ||
                            (movedCards[i] == 29 && movedCards[j] == 31 && movedCards[k] == 32) ||
                            (movedCards[i] == 29 && movedCards[j] == 30 && movedCards[k] == 32) ||
                            (movedCards[i] == 33 && movedCards[j] == 34 && movedCards[k] == 35) ||
                            (movedCards[i] == 34 && movedCards[j] == 35 && movedCards[k] == 36) ||
                            (movedCards[i] == 33 && movedCards[j] == 35 && movedCards[k] == 36) ||
                            (movedCards[i] == 33 && movedCards[j] == 34 && movedCards[k] == 36)){
                        tokens+=tokensToRaise*6;
                        text_balance.setText(Integer.toString(tokens));
                        break;
                    }
                }
            }

        }


    }

    public void generateCard(){
        setImage(card_my_1, 1+Math.abs(random.nextInt()%36), counter);
        checkDoubledCards(card_my_1);
        counter++;
        setImage(card_my_2, 1+Math.abs(random.nextInt()%36), counter);
        checkDoubledCards(card_my_2);
        counter++;
        card_my_1.setRotationY(0);
        card_my_2.setRotationY(0);
    }

    public void setTextTokens(){
        player_1_balance.setText(Integer.toString(bolshemenshe[0]));
        player_2_balance.setText(Integer.toString(bolshemenshe[1]));
        player_3_balance.setText(Integer.toString(bolshemenshe[2]));
        player_4_balance.setText(Integer.toString(bolshemenshe[3]));
        my_stavka.setText(Integer.toString(bolshemenshe[4]));
    }

    public void startCircle(){
        int minutes = seconds/60;
        int secs = seconds%60;
        String text = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
        text_timer.setText(text);
    }

    public void move(){
        if(cardsOnTable == 0 && nextMove){
            setImage(card_1, 1+Math.abs(random.nextInt()%36), counter);
            checkDoubledCards(card_1);
            counter++;
            setImage(card_2, 1+Math.abs(random.nextInt()%36), counter);
            checkDoubledCards(card_2);
            counter++;
            setImage(card_3, 1+Math.abs(random.nextInt()%36), counter);
            checkDoubledCards(card_3);
            counter++;

            card_1.setVisibility(View.VISIBLE);
            card_2.setVisibility(View.VISIBLE);
            card_3.setVisibility(View.VISIBLE);
            cardsOnTable = 3;
            nextMove = false;
        }
        if(cardsOnTable == 3 && nextMove){
            setImage(card_4, 1+Math.abs(random.nextInt()%36), counter);
            checkDoubledCards(card_4);
            counter++;
            card_4.setVisibility(View.VISIBLE);
            cardsOnTable = 4;
            nextMove = false;
        }
        if(cardsOnTable == 4 && nextMove){
            setImage(card_5, 1+Math.abs(random.nextInt()%36), counter);
            checkDoubledCards(card_5);
            card_5.setVisibility(View.VISIBLE);
            cardsOnTable = 5;
            nextMove = false;
        }
        if(cardsOnTable == 5 && nextMove){
            checkCardsCombinations();
            nextMove = false;
            startGame();
        }
    }

    public void checkDoubledCards(ImageView v){
        for(int i = 0; i < counter; i++){
            if(movedCards[counter] == movedCards[i]){
                setImage(v, 1+Math.abs(random.nextInt()%36), counter);
            }
        }
    }

    public void startTimer(){
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if(running){
                    seconds--;
                    if(seconds == 0){
                        seconds = 60;
                        nextMove = true;
                        move();
                    }
                    startCircle();
                }
                h.postDelayed(this, 1000);
            }
        });
    }

    public void setImage(ImageView v, int number, int counter){
        switch(number){
            case 1:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_1));
                movedCards[counter] = 1;
                break;
            case 2:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_2));
                movedCards[counter] = 2;
                break;
            case 3:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_3));
                movedCards[counter] = 3;
                break;
            case 4:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_4));
                movedCards[counter] = 4;
                break;
            case 5:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_5));
                movedCards[counter] = 5;
                break;
            case 6:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_6));
                movedCards[counter] = 6;
                break;
            case 7:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_7));
                movedCards[counter] = 7;
                break;
            case 8:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_8));
                movedCards[counter] = 8;
                break;
            case 9:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_9));
                movedCards[counter] = 9;
                break;
            case 10:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_10));
                movedCards[counter] = 10;
                break;
            case 11:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_11));
                movedCards[counter] = 11;
                break;
            case 12:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_12));
                movedCards[counter] = 12;
                break;
            case 13:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_13));
                movedCards[counter] = 13;
                break;
            case 14:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_14));
                movedCards[counter] = 14;
                break;
            case 15:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_15));
                movedCards[counter] = 15;
                break;
            case 16:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_16));
                movedCards[counter] = 16;
                break;
            case 17:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_17));
                movedCards[counter] = 17;
                break;
            case 18:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_18));
                movedCards[counter] = 18;
                break;
            case 19:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_19));
                movedCards[counter] = 19;
                break;
            case 20:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_20));
                movedCards[counter] = 20;
                break;
            case 21:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_21));
                movedCards[counter] = 21;
                break;
            case 22:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_22));
                movedCards[counter] = 22;
                break;
            case 23:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_23));
                movedCards[counter] = 23;
                break;
            case 24:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_24));
                movedCards[counter] = 24;
                break;
            case 25:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_25));
                movedCards[counter] = 25;
                break;
            case 26:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_26));
                movedCards[counter] = 26;
                break;
            case 27:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_27));
                movedCards[counter] = 27;
                break;
            case 28:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_28));
                movedCards[counter] = 28;
                break;
            case 29:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_29));
                movedCards[counter] = 29;
                break;
            case 30:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_30));
                movedCards[counter] = 30;
                break;
            case 31:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_31));
                movedCards[counter] = 31;
                break;
            case 32:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_32));
                movedCards[counter] = 32;
                break;
            case 33:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_33));
                movedCards[counter] = 33;
                break;
            case 34:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_34));
                movedCards[counter] = 34;
                break;
            case 35:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_35));
                movedCards[counter] = 35;
                break;
            case 36:
                v.setImageDrawable(getResources().getDrawable(R.drawable.c_36));
                movedCards[counter] = 36;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}