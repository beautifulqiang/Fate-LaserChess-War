package com.example.flcw.LaserChessGamePlay;

import background.Background;
import chess.*;
import chess.ChessLaserEmitter.Direction;

public class BoardInitializer {
    //这里redP和blueP代表两个发射器坐标，d[0]是红发射器的方向，d[1]是蓝发射器的方向
    public static void initializeLaserEmitter(int type, int[] redP, int[] blueP, Direction[] d){
        if(type==1){
            redP[0] = 0;
            redP[1] = 0;
            d[0] = Direction.BOTTOM;
            blueP[0] = 7;
            blueP[1] = 9;
            d[1] = Direction.TOP;
        }
        else if(type==2){
            redP[0] = 0;
            redP[1] = 0;
            d[0] = Direction.BOTTOM;
            blueP[0] = 7;
            blueP[1] = 9;
            d[1] = Direction.TOP;
        }
    }

    public static Chess[][] initializeChess(int type){
        if(type==1){
            Chess[][] chessboard = new Chess[8][10];
            // 第零列
            chessboard[0][0] = new ChessLaserEmitter(ChessLaserEmitter.Direction.BOTTOM, ChessLaserEmitter.Color.RED);
            chessboard[3][0] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.RED);
            chessboard[4][0] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);

            //第一列

            //第二列
            chessboard[1][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.RED);
            chessboard[3][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[4][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);
            chessboard[7][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);

            //第三列
            chessboard[2][3] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);
            chessboard[7][3] = new ChessShield(ChessShield.Direction.TOP, ChessShield.Color.BLUE);

            //第四列
            chessboard[0][4] = new ChessShield(ChessShield.Direction.BOTTOM, ChessShield.Color.RED);
            chessboard[3][4] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.RIGHT_TOP, ChessTwoWayMirror.Color.RED);
            chessboard[4][4] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.BLUE);
            chessboard[7][4] = new ChessKing(ChessKing.Color.BLUE);

            //第五列
            chessboard[0][5] = new ChessKing(ChessKing.Color.RED);
            chessboard[3][5] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.RED);
            chessboard[4][5] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.RIGHT_TOP, ChessTwoWayMirror.Color.BLUE);
            chessboard[7][5] = new ChessShield(ChessShield.Direction.TOP, ChessShield.Color.BLUE);

            //第六列
            chessboard[0][6] = new ChessShield(ChessShield.Direction.BOTTOM, ChessShield.Color.RED);
            chessboard[5][6] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);

            //第七列
            chessboard[0][7] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);
            chessboard[3][7] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);
            chessboard[4][7] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.RED);
            chessboard[6][7] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.BLUE);

            //第八列

            //第九列
            chessboard[3][9] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);
            chessboard[4][9] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[7][9] = new ChessLaserEmitter(ChessLaserEmitter.Direction.TOP, ChessLaserEmitter.Color.BLUE);

            return chessboard;
        }
        else if(type==2){
            Chess[][] chessboard = new Chess[8][10];
            // 第零列
            chessboard[0][0] = new ChessLaserEmitter(ChessLaserEmitter.Direction.BOTTOM, ChessLaserEmitter.Color.RED);
            chessboard[3][0] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.RED);
            chessboard[4][0] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);

            //第一列
            chessboard[3][1] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[4][1] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);

            //第二列
            //chessboard[1][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.RED);
            chessboard[7][2] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.BLUE);
            //chessboard[7][2] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);

            //第三列
            chessboard[2][3] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);
            chessboard[5][3] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[7][3] = new ChessShield(ChessShield.Direction.TOP, ChessShield.Color.BLUE);

            //第四列
            chessboard[0][4] = new ChessShield(ChessShield.Direction.BOTTOM, ChessShield.Color.RED);
            chessboard[3][4] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[4][4] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.BLUE);
            chessboard[7][4] = new ChessKing(ChessKing.Color.BLUE);

            //第五列
            chessboard[0][5] = new ChessKing(ChessKing.Color.RED);
            chessboard[3][5] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.RED);
            chessboard[4][5] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.RED);
            chessboard[7][5] = new ChessShield(ChessShield.Direction.TOP, ChessShield.Color.BLUE);

            //第六列
            chessboard[0][6] = new ChessShield(ChessShield.Direction.BOTTOM, ChessShield.Color.RED);
            chessboard[2][6] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.RED);
            chessboard[5][6] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);

            //第七列
            chessboard[0][7] = new ChessTwoWayMirror(ChessTwoWayMirror.Direction.LEFT_TOP, ChessTwoWayMirror.Color.RED);

            //第八列
            chessboard[3][8] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_BOTTOM, ChessOneWayMirror.Color.RED);
            chessboard[4][8] = new ChessOneWayMirror(ChessOneWayMirror.Direction.RIGHT_TOP, ChessOneWayMirror.Color.RED);

            //第九列
            chessboard[3][9] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_TOP, ChessOneWayMirror.Color.BLUE);
            chessboard[4][9] = new ChessOneWayMirror(ChessOneWayMirror.Direction.LEFT_BOTTOM, ChessOneWayMirror.Color.BLUE);
            chessboard[7][9] = new ChessLaserEmitter(ChessLaserEmitter.Direction.TOP, ChessLaserEmitter.Color.BLUE);

            return chessboard;
        }
        return null;
    }

    public static Background[][] initializeBackground(int type){
        if(type==1){
            Background[][] backgroundBoard = new Background[8][10];
            // 第零列
            backgroundBoard[1][0] = new Background(Background.Color.RED);
            backgroundBoard[2][0] = new Background(Background.Color.RED);
            backgroundBoard[3][0] = new Background(Background.Color.RED);
            backgroundBoard[4][0] = new Background(Background.Color.RED);
            backgroundBoard[5][0] = new Background(Background.Color.RED);
            backgroundBoard[6][0] = new Background(Background.Color.RED);
            backgroundBoard[7][0] = new Background(Background.Color.RED);

            //第一列
            backgroundBoard[0][1] = new Background(Background.Color.BLUE);
            backgroundBoard[7][1] = new Background(Background.Color.BLUE);

            //第八列
            backgroundBoard[0][8] = new Background(Background.Color.RED);
            backgroundBoard[7][8] = new Background(Background.Color.RED);

            // 第九列
            backgroundBoard[0][9] = new Background(Background.Color.BLUE);
            backgroundBoard[1][9] = new Background(Background.Color.BLUE);
            backgroundBoard[2][9] = new Background(Background.Color.BLUE);
            backgroundBoard[3][9] = new Background(Background.Color.BLUE);
            backgroundBoard[4][9] = new Background(Background.Color.BLUE);
            backgroundBoard[5][9] = new Background(Background.Color.BLUE);
            backgroundBoard[6][9] = new Background(Background.Color.BLUE);
            
            return backgroundBoard;
        }
        else if(type==2){
            Background[][] backgroundBoard = new Background[8][10];
            // 第零列
            backgroundBoard[1][0] = new Background(Background.Color.RED);
            backgroundBoard[2][0] = new Background(Background.Color.RED);
            backgroundBoard[3][0] = new Background(Background.Color.RED);
            backgroundBoard[4][0] = new Background(Background.Color.RED);
            backgroundBoard[5][0] = new Background(Background.Color.RED);
            backgroundBoard[6][0] = new Background(Background.Color.RED);
            backgroundBoard[7][0] = new Background(Background.Color.RED);

            //第一列
            backgroundBoard[0][1] = new Background(Background.Color.BLUE);
            backgroundBoard[7][1] = new Background(Background.Color.BLUE);

            //第八列
            backgroundBoard[0][8] = new Background(Background.Color.RED);
            backgroundBoard[7][8] = new Background(Background.Color.RED);

            // 第九列
            backgroundBoard[0][9] = new Background(Background.Color.BLUE);
            backgroundBoard[1][9] = new Background(Background.Color.BLUE);
            backgroundBoard[2][9] = new Background(Background.Color.BLUE);
            backgroundBoard[3][9] = new Background(Background.Color.BLUE);
            backgroundBoard[4][9] = new Background(Background.Color.BLUE);
            backgroundBoard[5][9] = new Background(Background.Color.BLUE);
            backgroundBoard[6][9] = new Background(Background.Color.BLUE);
            
            return backgroundBoard;
        }
        return null;
    }
}
