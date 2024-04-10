package com.example.connect3;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect3.databinding.ActivityConnectBinding;

import java.util.ArrayList;


public class ConnectActivity extends AppCompatActivity {
    public enum playerTurn{
        player_1,player_2
    }
    private static final String ACTIVITY_TAG="ConnectThree";
    private static final int max_rows=5;
    private static final int max_cols=5;
    private ActivityConnectBinding binding;
    private ArrayList<Button> buttonCellsArrayList;
    private ConnectGame connectGame;
    private playerTurn currentTurn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRestartGame.setOnClickListener(v -> restartGame());
        startconnectGame();
    }

    private void startconnectGame() {
        setupGame();
        setupButtonCells();
        setupButtonListeners();
        startGame();
    }

    private void setupButtonCells() {
        buttonCellsArrayList = new ArrayList<>();
        addButtonCellHelper(binding.containerButtons);
    }

    private void startGame() {
        currentTurn = playerTurn.player_1;
    }

    private void stopGame() {
        for(int i = 0; i < 5; i++) {
            Button button = buttonCellsArrayList.get(i);
            button.setEnabled(false);
        }
    }

    private void restartGame() {
        for(Button button : buttonCellsArrayList) {
            button.setBackgroundResource(R.drawable.circle_empty_cell);
        }

        for(int i = 0; i < 5; i++) {
            Button button = buttonCellsArrayList.get(i);
            button.setEnabled(true);
        }

        connectGame.emptyGameState();
        binding.tvResult.setText("Player One's Turn");
        startGame();
    }

    private void addButtonCellHelper(ViewGroup container) {
        for(int row = 0; row < container.getChildCount(); row++) {
            View child = container.getChildAt(row);
            if(child instanceof Button) {
                buttonCellsArrayList.add((Button) child);
            } else if (child instanceof ViewGroup) {
                addButtonCellHelper((ViewGroup) child);
            }
        }
    }
    private void setupButtonListeners() {
        for(int i = 0; i < 5; i++) {
            Button button = buttonCellsArrayList.get(i);
            int finalIndex = i;
            button.setOnClickListener(v -> connectGame.addGameClickFunctionality(finalIndex));
        }
    }
    private void setupGame() {
        connectGame = new ConnectGame(this, max_rows, max_cols);

    }

    public void paintButton(Button button) {
        if(currentTurn.equals(playerTurn.player_1)) {
            button.setBackgroundResource(R.drawable.circle_player_one);
        } else {
            button.setBackgroundResource(R.drawable.circle_player_two);
        }
    }

    public void togglePlayerTurn() {
        currentTurn = (currentTurn == playerTurn.player_1) ? playerTurn.player_2 : playerTurn.player_1;

        if(currentTurn.equals(playerTurn.player_1)) {
            binding.tvResult.setText("Player One's Turn");
        } else {
            binding.tvResult.setText("Player Two's Turn");
        }
    }

    public void declareWinner() {
        if(currentTurn.equals(playerTurn.player_1)) {
            binding.tvResult.setText("Player One Won");
        } else {
            binding.tvResult.setText("Player Two Won");
        }

        stopGame();
    }

    public playerTurn getCurrentPlayerTurn() {
        return currentTurn;
    }

    public ArrayList<Button> getButtonCellsArrayList() {
        return buttonCellsArrayList;
    }




}
