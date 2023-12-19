package flcwGUI.LaserChessGamePlay;

import java.util.Scanner;

import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import flcwGUI.LaserChessGamePlay.chess.Chess.Color;
import flcwGUI.LaserChessGamePlay.operate.Operate;

public class Game {
    private static Color turn = null;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select the type of board (enter the number 1 or 2, etc.):");
        //错误输入处理之后再说
        int boardType = scanner.nextInt();
        //读取谁先手
        System.out.println("Who comes first? (R or B)");
        String input = scanner.next();
        char firstPersonToGo = input.charAt(0);
        if(firstPersonToGo=='R'){
            turn = Color.RED;
        }
        else if(firstPersonToGo=='B'){
            turn = Color.BLUE;
        }
        else{
            //wdnmd
        }

        Board board = new Board(boardType);

        while(!board.gameOver()){
            System.out.println("Now the chess game");
            //print有重载，不带参数的是不考虑激光，带参数的考虑激光
            board.print();
            System.out.println("Now the turn:"+getTurn());
            //读入“鼠标点击”，并操作棋子
            Operate operation = InputHandler.getOperationFromInput(board, turn);
            //这里的char_color，只可能是'R'和'B'
            char char_color = getTurn().charAt(0);
            while(!board.operateChess(operation,char_color)){
//                这一步操作没有成功
                operation = InputHandler.getOperationFromInput(board, turn);
            }
            //谁下了棋，接下来谁的激光就发射
            //从Board类中取得激光发射器参数
            int[] LaserEmitterpos = new int[2];
            ChessLaserEmitter.Direction[] LaserEmitterdirection = new ChessLaserEmitter.Direction[1];
            int[] boardSize = board.getBoardSize();
//            board.getLaserEmitter(LaserEmitterpos, LaserEmitterdirection, char_color);
            //生成激光对象
            Laser laser = new Laser(LaserEmitterpos[0], LaserEmitterpos[1], LaserEmitterdirection[0], boardSize[0], boardSize[1]);
            //取得要杀死的棋子的坐标
            int[] posToKill = laser.disseminate(board.getChessBoard());
            //输出激光路径
            System.out.println("Laser path");
            board.print(laser.getPath());
            //杀死棋子。杀死大海！杀死大海！
            board.killChess(posToKill[0], posToKill[1]);

            changeTurn();
            
        }
        //游戏结束
        System.out.println("Game is over. The winner is "+ board.getWinner());
    }

    private static void changeTurn(){
        if(turn == Color.RED){
            turn = Color.BLUE;
        }
        else{
            turn = Color.RED;
        }
    }

    public static String getTurn(){
        if(turn == Color.RED){
            return "RED";
        }
        else{
            return "BLUE";
        }
    }
}
