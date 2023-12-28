package flcwGUI.LaserChessGamePlay;

import java.util.Scanner;

import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import flcwGUI.LaserChessGamePlay.chess.Chess.Color;
import flcwGUI.LaserChessGamePlay.operate.Operate;

// 注意，本类并未在GUI开发中使用，本类更多的作为项目底层开发时的测试手段

public class Game {
    private static Color turn = null;
    public static void main(String[] args) {

        Board board;
        Scanner scanner = new Scanner(System.in);

        String username;//后面要用来构造User类

        System.out.println("Do you want to log in or register?(L,R)");
        char registerOrNot = scanner.nextLine().charAt(0);
        if (registerOrNot=='L') {
            //执行登录的逻辑
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            //在这里也可以调用isValidNameOrPasswd函数来测试
            while(!User.isValidNameOrPasswd(username)||!User.isValidNameOrPasswd(password)
                    || !User.login(username, password)){
                System.out.println("Failed to login");
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
            }
        } else {
            //执行注册的逻辑
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            while (User.userExists(username) || !User.isValidNameOrPasswd(username)) {
                System.out.println("Unvalid username!");
                System.out.print("Enter username: ");
                username = scanner.nextLine();
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            while(!User.isValidNameOrPasswd(password)){
                System.out.println("Unvalid password!");
                System.out.print("Enter password: ");
                password = scanner.nextLine();
            }
            //检测成功，开始注册
            User.register(username, password);
        }

        User user = new User(username);



        System.out.println("Do you want to use a saved file?(Y,N)");
        char UseSavedBoardOrNot = scanner.nextLine().charAt(0);
        if(UseSavedBoardOrNot=='Y'){
            board = new Board(0, true, true);
        }
        else{
            System.out.println("Do you want to DIY?(Y,N)");
            char DiyOrNot = scanner.nextLine().charAt(0);
            if(DiyOrNot=='Y'){
                board = new Board(0, true, false);
            }
            else{
                System.out.println("Please select the type of board (enter the number 1 , 2 or 3):");
                //错误输入处理之后再说
                int boardType = scanner.nextInt();
                board = new Board(boardType, false, false);
            }            
        }

        

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
        

        while(!board.gameOver()){
            SaveBoard.saveBoard(board.chessboard, board.backgroundBoard, user, false, "none");//只是为了测试，你们GUI在做的时候可以在需要保存的时候再调用这个函数
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
            board.getLaserEmitter(LaserEmitterpos, LaserEmitterdirection, char_color);
            //生成激光对象
            Laser laser = new Laser(LaserEmitterpos[0], LaserEmitterpos[1], LaserEmitterdirection[0], boardSize[0], boardSize[1]);
            //取得要杀死的棋子的坐标
            int[] posToKill = laser.disseminate(board.getChessBoard());
            System.out.println(posToKill[0]+","+posToKill[1]);
            //输出激光路径
            System.out.println("Laser path");
//            board.print(laser.getPath(),laser.getVec_path());
            board.print(laser.getPath(),laser.getVec_path());
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
