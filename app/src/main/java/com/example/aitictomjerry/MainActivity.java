package com.example.aitictomjerry;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean playerOneActive;
        private TextView playerOneScore, playerTwoScore, playerStatus, drawScore,matchScore;
        private ImageView[] buttons = new ImageView[9];
        private Button reset, playAgain;
        private int playerOneScoreCount, playerTwoScoreCount, drawScoreCount, matchScoreCount;

       int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
   // int[] gameState = {EMPTY_STATE, EMPTY_STATE,
     //       EMPTY_STATE, EMPTY_STATE, EMPTY_STATE, EMPTY_STATE, EMPTY_STATE, EMPTY_STATE, EMPTY_STATE};

        int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
                {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        int round;


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            playerOneScore = findViewById(R.id.score_Player1);
            playerTwoScore = findViewById(R.id.score_Player2);
            drawScore=findViewById(R.id.drawScore);
            matchScore=findViewById(R.id.matchScore);
            playerStatus = findViewById(R.id.textStatus);
            reset = findViewById(R.id.btn_reset);
            playAgain = findViewById(R.id.btn_play_again);
            buttons[0] = findViewById(R.id.btn0);
            buttons[1] = findViewById(R.id.btn1);
            buttons[2] = findViewById(R.id.btn2);
            buttons[3] = findViewById(R.id.btn3);
            buttons[4] = findViewById(R.id.btn4);
            buttons[5] = findViewById(R.id.btn5);
            buttons[6] = findViewById(R.id.btn6);
            buttons[7] = findViewById(R.id.btn7);
            buttons[8] = findViewById(R.id.btn8);

            showUserInputDialog();
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        ImageView img = (ImageView) view;
                        int gameStatePointer = Integer.parseInt(view.getTag().toString());
                        if (gameState[gameStatePointer] != 2) {
                            return;
                        } else if (checkWinner()) {
                            return;
                        }
                        if (playerOneActive) {
                            img.setImageResource(R.drawable.tom);
                            gameState[gameStatePointer] = 0;
                        } else {
                            img.setImageResource(R.drawable.jerry);
                            gameState[gameStatePointer] = 1;
                        }
                        round++;

                        if (checkWinner()) {
                            if (playerOneActive) {
                                playerOneScoreCount++;
                                updatePlayerScore();
                                playerStatus.setText("Tom has won");
                                img.setImageResource(R.drawable.jerrycaught);

                            } else{
                                playerTwoScoreCount++;
                                updatePlayerScore();
                                playerStatus.setText("Jerry has won");
                                img.setImageResource(R.drawable.jerrywon);

                            }
                            matchScoreCount++;
                            updatePlayerScore();
                        } else if (round == 9) {
                            drawScoreCount++;
                            matchScoreCount++;
                            updatePlayerScore();
                            playerStatus.setText("It's draw ");
                            img.setImageResource(R.drawable.draw);
                        } else {
                            playerOneActive = !playerOneActive;
                            if (!playerOneActive) {
                                // Computer makes a random move
                                makeComputerMove();
                            }
                        }
                    }
                });
            }
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playAgain();
                    playerOneScoreCount = 0;
                    playerTwoScoreCount = 0;
                    drawScoreCount=0;
                    matchScoreCount=0;
                    updatePlayerScore();
                }
            });
            playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playAgain();
                }
            });
        }

    private void showUserInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        builder.setView(dialogView);
        builder.setPositiveButton("Start", (dialogInterface, i) -> {
            RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.radioTom) {
                playerOneActive = true; // User plays as Tom
            } else if (selectedId == R.id.radioJerry) {
                playerOneActive = false; // User plays as Jerry ,computer plays as Tom
                // Computer makes the first move
                makeComputerMove();
            }
            dialogInterface.dismiss();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void makeComputerMove() {
        int winningMove = getWinningMove(1);
        if (winningMove != -1) {
            buttons[winningMove].performClick();
            return;
        }
        int blockingMove = getWinningMove(0);
        if (blockingMove != -1) {
            buttons[blockingMove].performClick();
            return;
        }
        if (gameState[4] == 2) {
            buttons[4].performClick();
            return;
        }
        int emptyCell = -1;
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i] == 2) {
                emptyCell = i;
                break;
            }
        }

        if (emptyCell != -1) {
            buttons[emptyCell].performClick();
        }
    }
    private int getWinningMove(int player) {
        for (int[] winningPositions : winningPositions) {
            int count = 0;
            int emptyCell = -1;
            for (int position : winningPositions) {
                if (gameState[position] == player) {
                    count++;
                } else if (gameState[position] == 2) {
                    emptyCell = position;
                }
            }
            if (count == 2 && emptyCell != -1) {
                return emptyCell;
            }
        }
        return -1;
    }
            private boolean checkWinner() {
                boolean winnerResults = false;
                for (int[] winningPositions : winningPositions) {
                    if (gameState[winningPositions[0]] == gameState[winningPositions[1]] &&
                            gameState[winningPositions[1]] == gameState[winningPositions[2]] &&
                            gameState[winningPositions[0]] != 2) {
                        winnerResults = true;
                        break;
                    }
                }
                return winnerResults;
            }

            private void playAgain() {

                playerOneActive = true;
                round = 0;
                for (int i = 0; i < buttons.length; i++) {
                    gameState[i] =2;
                    buttons[i].setImageResource(0);
                }
                playerStatus.setText("Status");
            }

            private void updatePlayerScore() {
                playerOneScore.setText(Integer.toString(playerOneScoreCount));
                playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
                drawScore.setText(Integer.toString(drawScoreCount));
                matchScore.setText(Integer.toString(matchScoreCount));
            }
    }
