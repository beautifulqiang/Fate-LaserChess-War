import background.Background;
import chess.*;
import chess.Chess.Color;
import chess.ChessLaserEmitter.Direction;
import operate.*;
import operate.Rotate.RotateDirection;

public class Board {
    private boolean gameIsOver = false;
    private char theWinner;

    private Chess[][] chessboard;
    private Background[][] backgroundBoard;

    private int[] redLaserEmitterPos;
    private Direction redLaserEmitterDirection;
    private int[] blueLaserEmitterPos;
    private Direction blueLaserEmitterDirection;

    public Board(int type) {
        //初始化激光发射器部分
        redLaserEmitterPos = new int[2];
        blueLaserEmitterPos = new int[2];
        Direction[] tempD =new Direction[2];
        BoardInitializer.initializeLaserEmitter(type, redLaserEmitterPos, blueLaserEmitterPos, tempD);
        redLaserEmitterDirection = tempD[0];
        blueLaserEmitterDirection = tempD[1];

        //初始化棋子
        chessboard = BoardInitializer.initializeChess(type);
        //初始化背景
        backgroundBoard = BoardInitializer.initializeBackground(type);
    }

    public void getLaserEmitter(int[] pos, Direction[] d, char char_color){
        if(char_color=='R'){
            pos[0] = redLaserEmitterPos[0];
            pos[1] = redLaserEmitterPos[1];
            d[0] = redLaserEmitterDirection;
        }
        else{
            pos[0] = blueLaserEmitterPos[0];
            pos[1] = blueLaserEmitterPos[1];
            d[0] = blueLaserEmitterDirection;
        }
    }

    public Chess[][] getChessBoard(){
        return chessboard;
    }

    public int[] getBoardSize(){
        int[] ans =new int[2];
        ans[0] = chessboard.length;
        ans[1] = chessboard[0].length;
        return ans;
    }

    public Chess getChessAt(int x, int y){
        if(x<0 || x>=chessboard.length || y<0 || y>=chessboard[0].length){
            System.out.println("Error: Array is out of bounds");
            return null;
        }
        return chessboard[x][y];
    }

    public String getWinner(){
        if(theWinner=='R')return "RED";
        else return "BLUE";
    }

    public boolean gameOver(){
        return gameIsOver;
    }
    
    public void print(boolean[][] path) {
        BoardPrinter.printBoard(chessboard, backgroundBoard, path);
    }

    public void print() {
        boolean[][] path = new boolean[chessboard.length][chessboard[0].length];
        BoardPrinter.printBoard(chessboard, backgroundBoard, path);
    }

    public void killChess(int x, int y){
        if(x==-1 && y ==-1){
            //无需杀死
            return;
        }
        else if(x!=-1 && y!=-1){
            //讨论国王
            if(chessboard[x][y] instanceof ChessKing){
                if(chessboard[x][y].color == Color.BLUE){
                    //蓝色国王将要死了
                    theWinner ='R';
                }
                else{
                    //红色国王将要死了
                    theWinner ='B';
                }
                //将要杀死国王
                gameIsOver = true;
            }
            //杀死
            chessboard[x][y] = null;
            System.out.println("The pawns in the ("+x+','+y+") position have been killed");
        }
        else{
            System.out.println("Error: Unknown argument");
        }
    }


    //返回值代表操作是否成功，返回true则代表操作成功，传入的char_color代表当前正在操作的颜色
    public boolean operateChess(Operate opetation, char char_color) {
        if(opetation instanceof Move){
            int startX = ((Move)opetation).startX;
            int startY = ((Move)opetation).startY;
            int endX = ((Move)opetation).endX;
            int endY = ((Move)opetation).endY;
            if (isValidMove(startX, startY, endX, endY, char_color)) {
                //已经判断了这一步合理，开始移动
                Chess tempChess = chessboard[startX][startY];
                chessboard[startX][startY] = chessboard[endX][endY];
                chessboard[endX][endY] = tempChess;
                return true;
            } else {
                System.out.println("Invalid move: Check your coordinates.");
            }
        }
        else if(opetation instanceof Rotate){
            int x = ((Rotate)opetation).x;
            int y = ((Rotate)opetation).y;
            if(isValidRotate(x,y)){
                //已经判断这个位置可以旋转，开转
                if(((Rotate)opetation).direction == RotateDirection.LEFT){
                    //向左转
                    chessboard[x][y].rotate('l');
                }
                else{
                    //向右转
                    chessboard[x][y].rotate('r');
                }
                return true;
            }
        }
        return false;
    }

    private boolean isValidRotate(int x, int y){
        // 虽然数组越界检测应该在InputHandler类中已经做过了，但不确定后面会不会改，加上没坏处
        if(x<0 || x>=chessboard.length || y<0 || y>=chessboard[0].length){
            System.out.println("Error: The INITIAL position array is out of bounds");
            return false;
        }
        //讨论起始位置有没有棋子，这个也应该在InputHandler类中做过了
        if(chessboard[x][y]==null){
            System.out.println("Error: There are no pieces in the initial position");
            return false;
        }
        //起始位置的棋子不可移动
        if(chessboard[x][y] instanceof ChessLaserEmitter || chessboard[x][y] instanceof ChessKing){
            System.out.println("Error: The piece cannot be rotated");
            return false;
        }
        return true;
    }

    private boolean isValidMove(int startX, int startY, int endX, int endY, char char_color) {
        // 先讨论是否越界
        // 这里初始位置是否越界，应该在InputHandler类中已经检测过了
        if(startX<0 || startX>=chessboard.length || startY<0 || startY>=chessboard[0].length){
            System.out.println("Error: The INITIAL position array is out of bounds");
            return false;
        }
        if(endX<0||endX>=chessboard.length || endY<0 || endY>=chessboard[0].length){
            System.out.println("Error: The TARGET position array is out of bounds");
            return false;
        }
        //讨论是否是周围九格
        if(!diffSmallerThanOne(startX,endX) || !diffSmallerThanOne(startY,endY)){
            System.out.println("Error: Moving too far");
            return false;
        }
        //在另一边键盘读入的地方应该做一点逻辑处理，点相同的位置，应该是把棋子放下来的, 所以这也是死代码
        if(startX==endX && startY==endY){
            System.out.println("Error: The initial position and target position are the same");
            return false;
        }
        //讨论起始位置有没有棋子，这个也应该在InputHandler类中做过了
        if(chessboard[startX][startY]==null){
            System.out.println("Error: There are no pieces in the initial position");
            return false;
        }
        //起始位置的棋子不可移动
        if(chessboard[startX][startY] instanceof ChessLaserEmitter){
            System.out.println("Error: The piece cannot be moved");
            return false;
        }
        //如果目标有背景，如果当前正在操作的颜色和目标背景颜色不同
        Background bg = backgroundBoard[endX][endY];
        if(bg!=null){
            if((char_color=='R' && bg.color == Background.Color.BLUE)||(char_color=='B' && bg.color == Background.Color.RED)){
                System.out.println("Error: The background color of the target location doesn't match yours");
                return false;
            }
        }
        
        //不是双面镜，不能交换
        if(!(chessboard[startX][startY] instanceof ChessTwoWayMirror) && chessboard[endX][endY]!=null){
            System.out.println("Error: There are already pieces at the target location");
            return false;
        }
        //是双面镜，目标位置有棋子，并且这个棋子不能移动
        if((chessboard[startX][startY] instanceof ChessTwoWayMirror) &&(chessboard[endX][endY]!=null) &&(chessboard[endX][endY] instanceof ChessLaserEmitter || chessboard[endX][endY] instanceof ChessKing || chessboard[endX][endY] instanceof ChessTwoWayMirror)){
            System.out.println("Error: Pieces of this type cannot be exchanged");
            return false;
        }
        return true;
    }

    private boolean diffSmallerThanOne(int x, int y) {
        return Math.abs(x-y) <=1;
    }



    public static void main(String[] args) {
        //早期测试代码
        Board board = new Board(1);
        //board.print();
    }
}
