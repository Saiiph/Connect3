package com.example.connect3;

import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

public class ConnectGame {
    public enum State{
        empty,player_1,player_2
    }
    private final ArrayList<State> gameState;
    private final ConnectActivity activity;
    private final int maxRows;
    private final int maxCols;

    public ConnectGame(ConnectActivity activity, int rows, int cols){
        this.gameState =new ArrayList<>();
        this.activity = activity;
        this.maxRows = rows;
        this.maxCols = cols;
        setupGameState();
        emptyGameState();
    }
    private void setupGameState() {
        gameState.clear();

        for(int idx = 0; idx < (maxRows * maxCols); idx++) {
            gameState.add(State.empty);
        }
    }

    public void emptyGameState() {
        Collections.fill(gameState, State.empty);
    }

    private int findEmptyRow(int column, int rowIndex) {
        if(rowIndex < 0) {
            return -1;
        }

        if(gameState.get(rowIndex * maxCols + column).equals(State.empty)) {
            return rowIndex;
        }

        return findEmptyRow(column, rowIndex - 1);
    }

    private void toggleGameState(int index) {
        ConnectActivity.playerTurn currentPlayer = activity.getCurrentPlayerTurn();
            if(currentPlayer.equals(ConnectActivity.playerTurn.player_1)) {
                gameState.set(index, State.player_1);
            } else {
                gameState.set(index, State.player_2);
            }
    }

    private boolean checkWin(int currentCell) {
        if (checkWinVertical(currentCell)) {
            return true;
        } else if (checkWinHorizontal(currentCell)) {
            return true;
        } else return checkWinDiagonal(currentCell);
    }

    private boolean checkWinHorizontal(int currentCell) {
        if(checkWinHorizontalLeftFull(currentCell)) {
            return true;
        } else if (checkWinHorizontalRightFull(currentCell)) {
            return true;
        } else return (checkWinHorizontalHalf(currentCell));
    }

    private boolean checkWinHorizontalRightFull(int currentCell) {
        if(currentCell % 5 > 2) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell + 2, currentCell + 1);
    }

    private boolean checkWinHorizontalLeftFull(int currentCell) {
        if(currentCell % 5 < 2) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell - 2, currentCell - 1);
    }

    private boolean checkWinHorizontalHalf(int currentCell) {
        if(currentCell % 5 == 0 || currentCell % 5 == 4) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell - 1, currentCell + 1);
    }


    private boolean checkWinVertical(int currentCell) {
        if (currentCell >= 15) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell + 5, currentCell + 10);
    }

    private boolean checkWinDiagonal(int currentCell) {
        if(checkWinDiagonalLeftFull(currentCell)) {
            return true;
        } else if (checkWinDiagonalRightFull(currentCell)) {
            return true;
        } else if (checkWinDiagonalLeftHalf(currentCell)) {
            return true;
        } else return checkWinDiagonalRightHalf(currentCell);
    }

    private boolean checkWinDiagonalLeftFull(int currentCell) {
        if(currentCell % 5 < 2 || currentCell >= 15) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell + 4, currentCell + 8);
    }

    private boolean checkWinDiagonalRightFull(int currentCell) {
        if(currentCell % 5 > 2 || currentCell >= 15) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell + 6, currentCell + 12);
    }

    private boolean checkWinDiagonalLeftHalf(int currentCell) {
        if(currentCell > 19 || currentCell < 5 || currentCell % 5 == 0 || currentCell % 5 == 4) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell - 6, currentCell + 6);
    }

    private boolean checkWinDiagonalRightHalf(int currentCell) {
        if(currentCell > 19 || currentCell < 5 || currentCell % 5 == 0 || currentCell % 5 == 4) {
            return false;
        }

        return checkWinHelper(currentCell, currentCell - 4, currentCell + 4);
    }

    private boolean checkWinHelper(int currentCell, int cellOneCheck, int cellTwoCheck) {
        if(gameState.get(currentCell).equals(State.empty)) {
            return false;
        }
        return  gameState.get(currentCell) == gameState.get(cellOneCheck) &&
                gameState.get(currentCell) == gameState.get(cellTwoCheck);
    }

    public void addGameClickFunctionality(int column) {
        ArrayList<Button> buttonArrayList = activity.getButtonCellsArrayList();
        int emptyRow = findEmptyRow(column, maxRows - 1);
            if(emptyRow == -1) {
                return;
            }

        int buttonIndex = emptyRow * maxCols + column;
            if(buttonIndex >= 0 && buttonIndex < buttonArrayList.size()) {
                Button button = buttonArrayList.get(buttonIndex);

                activity.paintButton(button);
                toggleGameState(buttonIndex);

                if(checkWin(buttonIndex)) {
                    activity.declareWinner();
                } else {
                    activity.togglePlayerTurn();
                }
            }
    }
}
