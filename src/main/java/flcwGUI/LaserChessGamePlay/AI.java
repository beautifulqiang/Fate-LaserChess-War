package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.*;
import flcwGUI.LaserChessGamePlay.operate.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AI {
    private static int[] redKingPos = new int[2];
    private static int[] blueKingPos = new int[2];
    private static int countOfChesses = 0;

    private static class OperateWithScore {
        Operate operate;
        int score;

        public OperateWithScore(Operate operate, int score) {
            this.operate = operate;
            this.score = score;
        }
    }


    private static List<OperateWithScore> getMovesOfThisBlock(Board board, Chess.Color color, int x, int y){
        List<OperateWithScore> operations = new ArrayList<>();
        

        // 记录初始状态是否朝向
        // 如果是盾牌或者国王，那就会是全程false
        boolean originalFaceToMyKIng = false;
        boolean originalFaceToOtherKIng = false;
        boolean finallyFaceToMyKIng = false;
        boolean finallyFaceToOtherKIng = false;

        //对于单面镜的状态
        if(board.chessboard[x][y] instanceof ChessOneWayMirror){
            ChessOneWayMirror chess = (ChessOneWayMirror)board.chessboard[x][y];
            //先讨论对于自己的王
            originalFaceToMyKIng = testOneFaceOrNot(color, x, y, chess.direction);
            //再讨论对于别的王
            if(color == Chess.Color.BLUE){
                originalFaceToOtherKIng = testOneFaceOrNot(Chess.Color.RED, x, y, chess.direction);
            }
            else{
                originalFaceToOtherKIng = testOneFaceOrNot(Chess.Color.BLUE, x, y, chess.direction);
            }
        }
        else if(board.chessboard[x][y] instanceof ChessTwoWayMirror){
            //讨论双面镜，这里和单面镜是互斥的
            //先讨论对于自己的王
            originalFaceToMyKIng = testTwoFaceOrNot(color, x, y);
            //再讨论对于别的王
            if(color == Chess.Color.BLUE){
                originalFaceToOtherKIng = testTwoFaceOrNot(Chess.Color.RED, x, y);
            }
            else{
                originalFaceToOtherKIng = testTwoFaceOrNot(Chess.Color.BLUE, x, y);
            }
        }

        for(int i = x-1; i<=x+1; i++){
            for(int j = y-1; j<=y+1; j++){
                if(i==x&&j==y)continue;

                if (i < 0 || i >= board.chessboard.length || j < 0 || j >= board.chessboard[0].length) {
                    // 移动的目标位置超出棋盘
                    continue;
                }
                if(board.backgroundBoard[i][j]!=null&&board.backgroundBoard[i][j].color==Background.Color.BLUE){
                    // 移动的目标位置背景是蓝色
                    if(color == Chess.Color.RED){
                        //我的棋子是红色，那就重新
                        continue;
                    }
                }
                if(board.backgroundBoard[i][j]!=null&&board.backgroundBoard[i][j].color==Background.Color.RED){
                    // 移动的目标位置背景是红色
                    if(color == Chess.Color.BLUE){
                        //我的棋子是蓝色，那就重新
                        continue;
                    }
                }
                if(board.chessboard[i][j]!=null){
                    //目标位置是有棋子的
                    if(!(board.chessboard[x][y] instanceof ChessTwoWayMirror)){
                        // 如果起始位置不是双面镜
                        continue;
                    }
                    if((board.chessboard[i][j] instanceof ChessLaserEmitter)||(board.chessboard[i][j] instanceof ChessKing)||(board.chessboard[i][j] instanceof ChessTwoWayMirror)){
                        // 虽然是双面镜，但是目标位置棋子不可被交换
                        continue;
                    }
                }

                //判断为可交换
                //TODO 如果是双面镜的话，还得判断两个棋子，在此处先略过，只讨论x,y位置的棋子

                //进行交换
                Chess temp = board.chessboard[i][j];
                board.chessboard[i][j] = board.chessboard[x][y];
                board.chessboard[x][y] = temp;

                Move move = new Move(x, y, i, j);
                int score=0;

                //对于单面镜的状态
                if(board.chessboard[i][j] instanceof ChessOneWayMirror){
                    ChessOneWayMirror chess = (ChessOneWayMirror)board.chessboard[i][j];
                    //先讨论对于自己的王
                    finallyFaceToMyKIng = testOneFaceOrNot(color, i, j, chess.direction);
                    //再讨论对于别的王
                    if(color == Chess.Color.BLUE){
                        finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.RED, i, j, chess.direction);
                    }
                    else{
                        finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.BLUE, i, j, chess.direction);
                    }
                }
                if(board.chessboard[i][j] instanceof ChessTwoWayMirror){
                    //讨论双面镜
                    //先讨论对于自己的王
                    finallyFaceToMyKIng = testTwoFaceOrNot(color, i, j);
                    //再讨论对于别的王
                    if(color == Chess.Color.BLUE){
                        finallyFaceToOtherKIng = testTwoFaceOrNot(Chess.Color.RED, i, j);
                    }
                    else{
                        finallyFaceToOtherKIng = testTwoFaceOrNot(Chess.Color.BLUE, i, j);
                    }
                }
                //讨论朝向变化带来的分数变化
                if(originalFaceToMyKIng&&!finallyFaceToMyKIng)score+=4;
                if(!originalFaceToMyKIng&&finallyFaceToMyKIng)score-=4;
                if(originalFaceToOtherKIng&&!finallyFaceToOtherKIng)score-=3;
                if(!originalFaceToOtherKIng&&finallyFaceToOtherKIng)score+=3;
            
                //接下来讨论棋子方位
                if(board.chessboard[i][j] instanceof ChessShield){
                    //是盾牌，讨论距离自己的王的距离
                    int[] pos;
                    if(color == Chess.Color.RED){
                        pos = redKingPos;
                    }
                    else{
                        pos = blueKingPos;
                    }
                    int old_delta_x = Math.abs(x-pos[0]);
                    int old_delta_y = Math.abs(y-pos[1]);
                    int new_delta_x = Math.abs(i-pos[0]);
                    int new_delta_y = Math.abs(j-pos[1]);
                    score+=(old_delta_x-new_delta_x);
                    score+=(old_delta_y-new_delta_y);
                }
                if(board.chessboard[i][j] instanceof ChessOneWayMirror){
                    //是镜子，讨论距离对方的距离
                    int[] pos;
                    if(color == Chess.Color.BLUE){
                        pos = redKingPos;
                    }
                    else{
                        pos = blueKingPos;
                    }
                    int old_delta_x = Math.abs(x-pos[0]);
                    int old_delta_y = Math.abs(y-pos[1]);
                    int new_delta_x = Math.abs(i-pos[0]);
                    int new_delta_y = Math.abs(j-pos[1]);
                    score+=(old_delta_x-new_delta_x);
                    score+=(old_delta_y-new_delta_y);
                }
                if(board.chessboard[i][j] instanceof ChessTwoWayMirror){
                    //是镜子，讨论距离对方的距离
                    int[] pos;
                    if(color == Chess.Color.BLUE){
                        pos = redKingPos;
                    }
                    else{
                        pos = blueKingPos;
                    }
                    int old_delta_x = Math.abs(x-pos[0]);
                    int old_delta_y = Math.abs(y-pos[1]);
                    int new_delta_x = Math.abs(i-pos[0]);
                    int new_delta_y = Math.abs(j-pos[1]);

                    //双面镜权重是单面镜的两倍
                    score+=2*(old_delta_x-new_delta_x);
                    score+=2*(old_delta_y-new_delta_y);
                }


                //从Board类中取得激光发射器参数
                int[] LaserEmitterpos = new int[2];
                ChessLaserEmitter.Direction[] LaserEmitterdirection = new ChessLaserEmitter.Direction[1];
                int[] boardSize = board.getBoardSize();
                char char_color;
                if(color == Chess.Color.BLUE){
                    char_color = 'B';
                }
                else {
                    char_color = 'R';
                }
                board.getLaserEmitter(LaserEmitterpos, LaserEmitterdirection, char_color);
                //生成激光对象
                Laser laser = new Laser(LaserEmitterpos[0], LaserEmitterpos[1], LaserEmitterdirection[0], boardSize[0], boardSize[1]);
                //取得要杀死的棋子的坐标
                int[] posToKill = laser.disseminate(board.getChessBoard());
                if(posToKill[0]==-1){
                    //pass
                }
                else if(board.chessboard[posToKill[0]][posToKill[1]]==null){
                    //pass，这种情况应该不会出现
                }
                else{
                    //确实要杀死棋子的情况
                    Chess chess =board.chessboard[posToKill[0]][posToKill[1]];
                    if(chess.color != color){
                        //杀死对方的棋子，全都是加分，可以杀的可能有三种，盾牌，单面镜子，国王
                        if(chess instanceof ChessKing){
                            score+=1000;
                        }
                        if(chess instanceof ChessShield){
                            if(countOfChesses>12){
                                score+=4;
                            }
                            else score+=2;
                        }
                        if(chess instanceof ChessOneWayMirror){
                            if(countOfChesses>12){
                                score+=2;
                            }
                            else score+=4;
                        }
                    }
                    else{
                        //杀死了自己的棋子
                        if(chess instanceof ChessKing){
                            score-=1000;
                        }
                        if(chess instanceof ChessShield){
                            if(countOfChesses>12){
                                score-=4;
                            }
                            else score-=2;
                        }
                        if(chess instanceof ChessOneWayMirror){
                            if(countOfChesses>12){
                                score-=2;
                            }
                            else score-=4;
                        }
                    }
                }
            
                //现在讨论完了杀死棋子的情况，前面也讨论过了朝向的问题，现在可以把这一个操作打包了
                OperateWithScore ows = new OperateWithScore(move,score);
                operations.add(ows);
                //把棋盘复位
                temp = board.chessboard[i][j];
                board.chessboard[i][j] = board.chessboard[x][y];
                board.chessboard[x][y] = temp;

            }
        }

        return operations;
    }

    private static List<OperateWithScore> getRotatesOfThisBlock(Board board, Chess.Color color,int x, int y){
        List<OperateWithScore> operations = new ArrayList<>();
        
        // 记录初始状态是否朝向
        // 如果是盾牌，那就会是全程false
        boolean originalFaceToMyKIng = false;
        boolean originalFaceToOtherKIng = false;
        boolean finallyFaceToMyKIng = false;
        boolean finallyFaceToOtherKIng = false;
        
        //对于单面镜的状态
        if(board.chessboard[x][y] instanceof ChessOneWayMirror){
            ChessOneWayMirror chess = (ChessOneWayMirror)board.chessboard[x][y];
            //先讨论对于自己的王
            originalFaceToMyKIng = testOneFaceOrNot(color, x, y, chess.direction);

            //再讨论对于别的王
            if(color == Chess.Color.BLUE){
                originalFaceToOtherKIng = testOneFaceOrNot(Chess.Color.RED, x, y, chess.direction);
            }
            else{
                originalFaceToOtherKIng = testOneFaceOrNot(Chess.Color.BLUE, x, y, chess.direction);
            }
        }
        //在这里，双面镜的“朝向”不会改变，因为不改变坐标
        
        
        // 先讨论左旋
        Rotate rotate = new Rotate(x, y, Rotate.RotateDirection.LEFT);
        int score=0;
        board.chessboard[x][y].rotate('l');//直接在棋盘上做了修改
        //对于单面镜的状态
        if(board.chessboard[x][y] instanceof ChessOneWayMirror){
            ChessOneWayMirror chess = (ChessOneWayMirror)board.chessboard[x][y];
            //先讨论对于自己的王
            finallyFaceToMyKIng = testOneFaceOrNot(color, x, y, chess.direction);
            //再讨论对于别的王
            if(color == Chess.Color.BLUE){
                finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.RED, x, y, chess.direction);
            }
            else{
                finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.BLUE, x, y, chess.direction);
            }
        }
        //讨论朝向变化带来的分数变化
        if(originalFaceToMyKIng&&!finallyFaceToMyKIng)score+=4;
        if(!originalFaceToMyKIng&&finallyFaceToMyKIng)score-=4;
        if(originalFaceToOtherKIng&&!finallyFaceToOtherKIng)score-=3;
        if(!originalFaceToOtherKIng&&finallyFaceToOtherKIng)score+=3;


        //从Board类中取得激光发射器参数
        int[] LaserEmitterpos = new int[2];
        ChessLaserEmitter.Direction[] LaserEmitterdirection = new ChessLaserEmitter.Direction[1];
        int[] boardSize = board.getBoardSize();
        char char_color;
        if(color == Chess.Color.BLUE){
            char_color = 'B';
        }
        else {
            char_color = 'R';
        }
        board.getLaserEmitter(LaserEmitterpos, LaserEmitterdirection, char_color);
        //生成激光对象
        Laser laser = new Laser(LaserEmitterpos[0], LaserEmitterpos[1], LaserEmitterdirection[0], boardSize[0], boardSize[1]);
        //取得要杀死的棋子的坐标
        int[] posToKill = laser.disseminate(board.getChessBoard());
        if(posToKill[0]==-1){
            //pass
        }
        else if(board.chessboard[posToKill[0]][posToKill[1]]==null){
            //pass，这种情况应该不会出现
        }
        else{
            //确实要杀死棋子的情况
            Chess chess =board.chessboard[posToKill[0]][posToKill[1]];
            if(chess.color != color){
                //杀死对方的棋子，全都是加分，可以杀的可能有三种，盾牌，单面镜子，国王
                if(chess instanceof ChessKing){
                    score+=1000;
                }
                if(chess instanceof ChessShield){
                    if(countOfChesses>12){
                        score+=4;
                    }
                    else score+=2;
                }
                if(chess instanceof ChessOneWayMirror){
                    if(countOfChesses>12){
                        score+=2;
                    }
                    else score+=4;
                }
            }
            else{
                //杀死了自己的棋子
                if(chess instanceof ChessKing){
                    score-=1000;
                }
                if(chess instanceof ChessShield){
                    if(countOfChesses>12){
                        score-=4;
                    }
                    else score-=2;
                }
                if(chess instanceof ChessOneWayMirror){
                    if(countOfChesses>12){
                        score-=2;
                    }
                    else score-=4;
                }
            }
        }

        //现在讨论完了杀死棋子的情况，前面也讨论过了朝向的问题，现在可以把这一个操作打包了
        OperateWithScore ows = new OperateWithScore(rotate,score);
        operations.add(ows);
        board.chessboard[x][y].rotate('r');//把棋盘复位

        
        //接下来讨论右旋！
        rotate = new Rotate(x, y, Rotate.RotateDirection.RIGHT);
        score=0;
        board.chessboard[x][y].rotate('r');//直接在棋盘上做了修改
        //对于单面镜的状态
        if(board.chessboard[x][y] instanceof ChessOneWayMirror){
            ChessOneWayMirror chess = (ChessOneWayMirror)board.chessboard[x][y];
            //先讨论对于自己的王
            finallyFaceToMyKIng = testOneFaceOrNot(color, x, y, chess.direction);
            //再讨论对于别的王
            if(color == Chess.Color.BLUE){
                finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.RED, x, y, chess.direction);
            }
            else{
                finallyFaceToOtherKIng = testOneFaceOrNot(Chess.Color.BLUE, x, y, chess.direction);
            }
        }
        //讨论朝向变化带来的分数变化
        if(originalFaceToMyKIng&&!finallyFaceToMyKIng)score+=4;
        if(!originalFaceToMyKIng&&finallyFaceToMyKIng)score-=4;
        if(originalFaceToOtherKIng&&!finallyFaceToOtherKIng)score-=3;
        if(!originalFaceToOtherKIng&&finallyFaceToOtherKIng)score+=3;


        //从Board类中取得激光发射器参数
        LaserEmitterpos = new int[2];
        LaserEmitterdirection = new ChessLaserEmitter.Direction[1];
        boardSize = board.getBoardSize();
        if(color == Chess.Color.BLUE){
            char_color = 'B';
        }
        else {
            char_color = 'R';
        }
        board.getLaserEmitter(LaserEmitterpos, LaserEmitterdirection, char_color);
        //生成激光对象
        laser = new Laser(LaserEmitterpos[0], LaserEmitterpos[1], LaserEmitterdirection[0], boardSize[0], boardSize[1]);
        //取得要杀死的棋子的坐标
        posToKill = laser.disseminate(board.getChessBoard());
        if(posToKill[0]==-1){
            //pass
        }
        else if(board.chessboard[posToKill[0]][posToKill[1]]==null){
            //pass，这种情况应该不会出现
        }
        else{
            //确实要杀死棋子的情况
            Chess chess =board.chessboard[posToKill[0]][posToKill[1]];
            if(chess.color != color){
                //杀死对方的棋子，全都是加分，可以杀的可能有三种，盾牌，单面镜子，国王
                if(chess instanceof ChessKing){
                    score+=1000;
                }
                if(chess instanceof ChessShield){
                    if(countOfChesses>12){
                        score+=4;
                    }
                    else score+=2;
                }
                if(chess instanceof ChessOneWayMirror){
                    if(countOfChesses>12){
                        score+=2;
                    }
                    else score+=4;
                }
            }
            else{
                //杀死了自己的棋子
                if(chess instanceof ChessKing){
                    score-=1000;
                }
                if(chess instanceof ChessShield){
                    if(countOfChesses>12){
                        score-=4;
                    }
                    else score-=2;
                }
                if(chess instanceof ChessOneWayMirror){
                    if(countOfChesses>12){
                        score-=2;
                    }
                    else score-=4;
                }
            }
        }
        board.chessboard[x][y].rotate('l');//把棋盘复位
        ows = new OperateWithScore(rotate,score);
        operations.add(ows);

        return operations;
    }


    //检验这个棋子，和这个颜色的王是否是朝向的
    private static boolean testOneFaceOrNot(Chess.Color color,int x, int y, ChessOneWayMirror.Direction d){
        int[] pos;
        if(color == Chess.Color.RED){
            pos = redKingPos;
        }
        else{
            pos = blueKingPos;
        }
        int diff_x = x - pos[0];
        int diff_y = y - pos[1];
        if(diff_x==0||diff_y==0){
            return true;
        }
        if(diff_x<0&&diff_y<0){
            //棋子在王的左上角
            if(d == ChessOneWayMirror.Direction.RIGHT_BOTTOM)return true;
        }
        else if(diff_x>0 && diff_y<0){
            //棋子在王的左下角
            if(d == ChessOneWayMirror.Direction.RIGHT_TOP)return true;
        }
        else if(diff_x<0 && diff_y>0){
            //棋子在王的右上角
            if(d == ChessOneWayMirror.Direction.LEFT_BOTTOM)return true;
        }
        else if(diff_x>0 && diff_y>0){
            //棋子在王的右下角
            if(d == ChessOneWayMirror.Direction.LEFT_TOP)return true;
        }
        return false;
    }


    private static boolean testTwoFaceOrNot(Chess.Color color,int x, int y){
        int[] pos;
        if(color == Chess.Color.RED){
            pos = redKingPos;
        }
        else{
            pos = blueKingPos;
        }
        int diff_x = x - pos[0];
        int diff_y = y - pos[1];
        if(diff_x==0||diff_y==0){
            return true;
        }
        return false;
    }


    private static List<OperateWithScore> getOperations(Board board, Chess.Color color){
        getKingPos(board);
        List<OperateWithScore> operations = new ArrayList<>();

        for(int i=0;i<board.chessboard.length;i++){
            for(int j=0;j<board.chessboard[0].length;j++){
                Chess chess = board.chessboard[i][j];
                if(chess==null){//这个位置没有棋子
                    continue;
                }
                if(chess.color != color){//这个位置颜色不匹配
                    continue;
                }
                if(chess instanceof ChessLaserEmitter){//棋子不可操作
                    continue;
                }
                if(chess instanceof ChessKing){
                    //是王，获取所有Move
                    operations.addAll(getMovesOfThisBlock(board, color, i, j));
                }
                else{
                    //其他情况，单、双、盾牌
                    //获取所有Move和Rotate
                    operations.addAll(getMovesOfThisBlock(board, color, i, j));
                    operations.addAll(getRotatesOfThisBlock(board, color, i, j));
                }
            }
        }
        // 对operations进行排序，按照score从高到低排序
        Collections.sort(operations, (o1, o2) -> Integer.compare(o2.score, o1.score));
        return operations;
    }

    private static void getKingPos(Board board){
        countOfChesses = 0;
        for(int i=0;i<board.chessboard.length;i++){
            for(int j=0;j<board.chessboard[0].length;j++){
                Chess chess = board.chessboard[i][j];
                if(chess==null){
                    continue;
                }
                if(chess instanceof ChessKing){
                    if(chess.color == Chess.Color.BLUE){
                        blueKingPos[0] = i;
                        blueKingPos[1] = j;
                    }
                    else{
                        redKingPos[0] = i;
                        redKingPos[1] = j;
                    }
                }
                countOfChesses++;
            }
        }
    }


    public static Operate random(Board board,Chess.Color color){
        List<OperateWithScore> operations = getOperations(board, color);
        // 使用Random类生成随机数
        Random random = new Random();
        // 生成一个介于0（包括）和operations.size()（不包括）之间的随机数
        int randomIndex = random.nextInt(operations.size());
        return operations.get(randomIndex).operate;
    }

    public static Operate best(Board board,Chess.Color color){
        List<OperateWithScore> operations = getOperations(board, color);
        return operations.get(0).operate;
    }

    public static Operate worst(Board board,Chess.Color color){
        List<OperateWithScore> operations = getOperations(board, color);
        int l = operations.size();
        return operations.get(l-1).operate;
    }
}
