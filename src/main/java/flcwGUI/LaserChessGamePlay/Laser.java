package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.chess.*;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter.Direction;

public class Laser {
    private int x;
    private int y;
    private Direction d;
    private boolean[][] path;

    public boolean[][] getPath(){
        return path;
    }

    Laser(int x, int y, Direction d, int size_x, int size_y){
        this.x = x;
        this.y = y;
        this.d = d;
        path = new boolean[size_x][size_y];
        path[x][y] = true;
    }

    //返回二维数组，如果杀死了棋子，则返回棋子坐标，如果没有，返回-1-1
    public int[] disseminate(Chess[][] board){
        int ret[] = new int[2];
        ret[0] = ret[1] =-1;
        while(true){
            int diff_x = 0;
            int diff_y = 0;
            diff_x -= (d == Direction.TOP)? 1:0;
            diff_x += (d == Direction.BOTTOM)? 1:0;
            diff_y += (d == Direction.RIGHT)? 1:0;
            diff_y -= (d == Direction.LEFT)? 1:0;
            //更新激光头部的x和y坐标
            x+=diff_x;
            y+=diff_y;
            if(transborder(board, x, y)){
                System.out.println("The laser has been shot out of boundary");
                break;
            }
            //没有越界，也就是还在棋盘上，更新一下path
            path[x][y] = true;
            if(board[x][y] == null){
                //传播到了一个空格子上，继续传播
                continue;
            }
        
            boolean[] kill = new boolean[1];
            if(!event(board[x][y], kill)){
                //如果返回了false，也就是进入了这个代码块，则激光传输结束了
                if(kill[0]){
                    //kill为真，当前棋子被杀了
                    ret[0] = x;
                    ret[1] = y;
                }
                break;
            }
        }
        return ret;
    }

    //可能碰到的棋子有以下几种：两种镜子，盾牌，国王 前三种需要讨论朝向
    // 如果返回false，则代表这个激光传输结束，返回true，激光继续传输，传进来的kill默认是false
    private boolean event(Chess chess, boolean[] kill){
        if(chess instanceof ChessKing){
            kill[0] = true;
            return false;
        }
        else if (chess instanceof ChessShield) {
            ChessShield chessShield = (ChessShield) chess;
            //这几种情况是要杀的
            if ((d == Direction.TOP && !(chessShield.direction == ChessShield.Direction.BOTTOM))
            ||  (d == Direction.BOTTOM && !(chessShield.direction == ChessShield.Direction.TOP))
            ||  (d == Direction.LEFT && !(chessShield.direction == ChessShield.Direction.RIGHT))
            ||  (d == Direction.RIGHT && !(chessShield.direction == ChessShield.Direction.LEFT))) {
                kill[0] = true;
            }
            //无论如何，激光都会停止传播
            return false;
        }
        else if(chess instanceof ChessTwoWayMirror){
            ChessTwoWayMirror chessTwoWayMirror = (ChessTwoWayMirror)chess;
            if(d == Direction.TOP){
                if(chessTwoWayMirror.direction == ChessTwoWayMirror.Direction.LEFT_TOP){
                    d = Direction.RIGHT;
                }
                else{
                    d= Direction.LEFT;
                }
            }
            else if(d == Direction.BOTTOM){
                if(chessTwoWayMirror.direction == ChessTwoWayMirror.Direction.LEFT_TOP){
                    d = Direction.LEFT;
                }
                else{
                    d= Direction.RIGHT;
                }
            }
            else if(d == Direction.LEFT){
                if(chessTwoWayMirror.direction == ChessTwoWayMirror.Direction.LEFT_TOP){
                    d = Direction.BOTTOM;
                }
                else{
                    d= Direction.TOP;
                }
            }
            else if(d == Direction.RIGHT){
                if(chessTwoWayMirror.direction == ChessTwoWayMirror.Direction.LEFT_TOP){
                    d = Direction.TOP;
                }
                else{
                    d= Direction.BOTTOM;
                }
            }
            //激光不会停止传播，也不会杀棋子
            return true;
        }
        else if (chess instanceof ChessOneWayMirror){
            ChessOneWayMirror chessOneWayMirror = (ChessOneWayMirror)chess;
            if(d == Direction.TOP){
                if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.LEFT_BOTTOM)){
                    d = Direction.LEFT;
                    return true;
                }
                else if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.RIGHT_BOTTOM)){
                    d = Direction.RIGHT;
                    return true;
                }
                else{
                    kill[0] = true;
                    return false;
                }
                
            }
            else if(d == Direction.BOTTOM){
                if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.LEFT_TOP)){
                    d = Direction.LEFT;
                    return true;
                }
                else if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.RIGHT_TOP)){
                    d = Direction.RIGHT;
                    return true;
                }
                else{
                    kill[0] = true;
                    return false;
                }
            }
            else if(d == Direction.LEFT){
                if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.RIGHT_BOTTOM)){
                    d = Direction.BOTTOM;
                    return true;
                }
                else if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.RIGHT_TOP)){
                    d = Direction.TOP;
                    return true;
                }
                else{
                    kill[0] = true;
                    return false;
                }
            }
            else if(d == Direction.RIGHT){
                if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.LEFT_BOTTOM)){
                    d = Direction.BOTTOM;
                    return true;
                }
                else if((chessOneWayMirror.direction == ChessOneWayMirror.Direction.LEFT_TOP)){
                    d = Direction.TOP;
                    return true;
                }
                else{
                    kill[0] = true;
                    return false;
                }
            }
        }
        System.out.println("Error: Unknown type");
        return false;
    }

    private boolean transborder(Chess[][] board, int x, int y){
        if(x<0 || x>=board.length || y<0 || y>=board[0].length){
            // 确实越界了
            return true;
        }
        return false;
    }

}
