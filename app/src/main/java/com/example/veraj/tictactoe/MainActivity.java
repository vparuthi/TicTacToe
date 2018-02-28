//Created by Veraj Paruthi

package com.example.veraj.tictactoe;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static com.example.veraj.tictactoe.R.mipmap.x;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // true is x's turn and false is o's turn
    private boolean turn = true;
    private int round = 0;
    private int player1points = 0;
    private int player2points = 0;

    private TextView textViewplayer1;
    private TextView textViewplayer2;
    private int p1points = 0;
    private int p2points = 0;

    Button[][] buttons = new Button[3][3];
    private final static String TAG = "com.example.veraj.tictactoe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewplayer1 = findViewById(R.id.player1);
        textViewplayer2 = findViewById(R.id.player2);

        textViewplayer1.setTextColor(Color.RED);

        //Getting all the button ids
        for (int i =0; i<3; i++){
            for (int j = 0; j<3;j++){
                String buttonID = "b" +i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons [i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        //Reset Button
        Button resetbutton = (Button) findViewById(R.id.resetbutton);
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clean(-1);
                p1points = 0;
                p2points = 0;
                textViewplayer1.setText("Player 1: 0");
                textViewplayer2.setText("Player 2: 0");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {

        //clicking on already filled cell
        if (((Button) view).getAlpha() == 1) {
            Log.i(TAG, "visible click");
            return;
        }

        //clicking on empty cell
        if (((Button) view).getAlpha() == 0) {

            if (turn == true){
                view.setBackgroundResource(R.mipmap.x);
                view.setAlpha((float) 0.99);
                Log.i(TAG, "X clicked");
                textViewplayer2.setTextColor(Color.BLUE);
                textViewplayer1.setTextColor(Color.LTGRAY);
                turn = false;
            }
            else if (turn == false) {
                view.setBackgroundResource(R.mipmap.circle);
                view.setAlpha(1);
                Log.i(TAG, "O clicked");
                textViewplayer1.setTextColor(Color.RED);
                textViewplayer2.setTextColor(Color.LTGRAY);
                turn = true;
            }
        }
        round++;

        int winner = checkWinner();
        if (winner == 1) {
            p1points++;
            textViewplayer1.setText("Player 1: " + p1points);
            Toast.makeText(this, "Player 1 is the Winner! Loser gets first move.", Toast.LENGTH_LONG).show();
            clean(winner);

        } else if (winner == 2) {
            p2points++;
            textViewplayer2.setText("Player 2: " + p2points);
            Toast.makeText(this, "Player 2 is the Winner! Loser gets first move.", Toast.LENGTH_LONG).show();
            clean(winner);
        }
    }

    private void clean (int winner) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setAlpha(0);
            }
        }

        if (winner == 1) {
            textViewplayer1.setTextColor(Color.LTGRAY);
            textViewplayer2.setTextColor(Color.BLUE);
            turn = false;
        }
        else if (winner == 2 || winner == -1){
            textViewplayer1.setTextColor(Color.RED);
            textViewplayer2.setTextColor(Color.LTGRAY);
            turn = true;
        }
        else if (winner == 0){
            if (turn){
                textViewplayer1.setTextColor(Color.RED);
                textViewplayer2.setTextColor(Color.LTGRAY);
            }
            else{
                textViewplayer1.setTextColor(Color.LTGRAY);
                textViewplayer2.setTextColor(Color.BLUE);
            }
        }
        round =0;
    }

    private int checkWinner (){
        String [][] boardtext = new String [3][3];
        Float [][] floatboard = new Float [3][3];

        for (int i = 0; i<3;i++){
            for (int j =0; j<3; j++){
                floatboard[i][j] = buttons[i][j].getAlpha();
                boardtext[i][j] = floatboard[i][j].toString();
                //Log.i(TAG, boardtext[i][j]);
            }
        }
        for (int i =0; i<3;i++) {
            //Checking for a win across a row
            if (boardtext[i][0].equals(boardtext[i][1]) && boardtext[i][0].equals(boardtext[i][2]) && (!Objects.equals(boardtext[i][0], "0.0"))){
                if (boardtext[i][0].equals("0.99")){
                    return 1;
                }
                else {
                    return 2;
                }
            }
            //Checking for a win down a column
            if (boardtext[0][i].equals(boardtext[1][i]) && boardtext[0][i].equals(boardtext[2][i]) && (!Objects.equals(boardtext[0][i], "0.0"))){
                if (boardtext[0][i].equals("0.99")){
                    return 1;
                }
                else {
                    return 2;
                }
            }

            //Checking negative slope diagonal
            if (boardtext [0][0].equals(boardtext[1][1]) && boardtext [0][0].equals(boardtext[2][2]) && !Objects.equals(boardtext[0][0], "0.0")){
                if (boardtext[1][1].equals("0.99")){
                    return 1;
                }
                else {
                    return 2;
                }
            }

            //Checking positive slope diagonal
            if (boardtext [0][2].equals(boardtext[1][1]) && boardtext [0][2].equals(boardtext[2][0]) && !Objects.equals(boardtext[1][1], "0.0")){
                if (boardtext[1][1].equals("0.99")){
                    return 1;
                }
                else {
                    return 2;
                }
            }
        }
        if (round == 9){
        Toast.makeText(this, "Tie Game", Toast.LENGTH_LONG).show();
            clean(0);
            return 0;
        }
        return 0;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putBoolean("turn", turn);
//        outState.putInt("p1point", p1points);
//        outState.putInt("p2point", p2points);
//        outState.putInt("round", round);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//        turn = savedInstanceState.getBoolean("turn");
//        p1points = savedInstanceState.getInt("p1point");
//        p2points = savedInstanceState.getInt("p2point");
//        round = savedInstanceState.getInt("round");
//    }
}
