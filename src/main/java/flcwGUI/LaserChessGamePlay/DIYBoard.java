package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.chess.*;
import flcwGUI.LaserChessGamePlay.chess.Chess.Color;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter.Direction;

import java.util.Scanner;

import flcwGUI.LaserChessGamePlay.background.*;

public class DIYBoard {
    public static void DIY(Chess[][] chessboard, Background[][] backgroundBoard, int[] redLaserEmitterPos, int[] blueLaserEmitterPos, Direction[] tempD){
        //TODO 加入让用户选择是否保存本棋盘的功能
        //TODO 在加入AI算法之后，还可以检测这个棋局有没有解，没有解不行
        boolean redKing = false;
        boolean blueKing = false;
        boolean redLaserEmitter = false;
        boolean blueLaserEmitter = false;

        String type = "";
        Color color;

        Scanner scanner = new Scanner(System.in);
        char char_color;

        while(true){
            //读入颜色和类型
            System.out.println("Enter the color ( R or B )");
            char_color = scanner.nextLine().charAt(0);
            if(char_color=='B'){
                color = Color.BLUE;
            }
            else{
                color = Color.RED;
            }
            System.out.println("Enter the pieces you want to place (enter END to consider the placement complete)");
            type = scanner.nextLine();
            if(type.equals("END")){
                if(!redLaserEmitter||!blueLaserEmitter||!redKing||!blueKing){
                    System.out.println("The requisites for the game are not met, two kings of different colors and two le are required");
                    continue;
                }
                //确实满足了棋局结束的条件
                break;
            }

            if(type.equals("board")){
                //让用户输入正确的棋子位置
                System.out.println("Please enter coordinate. (format: x,y):");
                String coordinate = scanner.nextLine();
                int[] coordinates = parseCoordinates(coordinate);
                while(coordinates[0]<0||coordinates[0]>=chessboard.length||coordinates[1]<0||coordinates[1]>=chessboard[0].length||backgroundBoard[coordinates[0]][coordinates[1]]!=null){
                    System.out.println("Locations that cannot be deployed, please re-enter");
                    coordinate = scanner.nextLine();
                    coordinates = parseCoordinates(coordinate);
                }

                if(color == Color.BLUE){
                    backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.BLUE);
                }
                else{
                    backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.RED);
                }
                System.out.println("Placed " + "background" + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
                continue;
            }
            
            Chess chess;
            boolean toRecordEmiter = false;
            //后面的最终else错误情况的棋子是瞎生成的
            if(type.equals("king")){
                if (color == Color.RED) {
                    if (!redKing) {
                        redKing = true;
                    } else {
                        System.out.println("Error: Red king already exists. Please choose a different piece.");
                        continue; // 继续循环，让用户重新输入
                    }
                } else {
                    if (!blueKing) {
                        blueKing = true;
                    } else {
                        System.out.println("Error: Blue king already exists. Please choose a different piece.");
                        continue; // 继续循环，让用户重新输入
                    }
                }
                chess = new ChessKing(color);
            }
            else if (type.equals("le")||type.equals("laseremiter")){
                if (color == Color.RED) {
                    if (!redLaserEmitter) {
                        redLaserEmitter = true;
                    } else {
                        System.out.println("Error: Red laser emitter already exists. Please choose a different piece.");
                        continue; // 继续循环，让用户重新输入
                    }
                } else {
                    if (!blueLaserEmitter) {
                        blueLaserEmitter = true;
                    } else {
                        System.out.println("Error: Blue laser emitter already exists. Please choose a different piece.");
                        continue; // 继续循环，让用户重新输入
                    }
                }
                System.out.println("laserEmiter, Please enter a direction(format t,r,b,l)");
                String dir = scanner.nextLine();
                if(dir.equals("t")){
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                    if(color == Color.BLUE){
                        tempD[1] = Direction.TOP;
                    }
                    else{
                        tempD[0] = Direction.TOP;
                    }
                }
                else if(dir.equals("r")){
                    chess = new ChessLaserEmitter(Direction.RIGHT, color);
                    if(color == Color.BLUE){
                        tempD[1] = Direction.RIGHT;
                    }
                    else{
                        tempD[0] = Direction.RIGHT;
                    }
                }
                else if(dir.equals("b")){
                    chess = new ChessLaserEmitter(Direction.BOTTOM, color);
                    if(color == Color.BLUE){
                        tempD[1] = Direction.BOTTOM;
                    }
                    else{
                        tempD[0] = Direction.BOTTOM;
                    }
                }
                else if(dir.equals("l")){
                    chess = new ChessLaserEmitter(Direction.LEFT, color);
                    if(color == Color.BLUE){
                        tempD[1] = Direction.LEFT;
                    }
                    else{
                        tempD[0] = Direction.LEFT;
                    }
                }
                else{
                    System.out.println("Error, unknown direction of laseremiter.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
                toRecordEmiter = true;
            }
            else if(type.equals("one")){
                System.out.println("oneWayMirror, Please enter a direction(format lt lb rt rb)");
                String dir = scanner.nextLine();
                if(dir.equals("lt")){
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_TOP, color);
                }
                else if(dir.equals("lb")){
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_BOTTOM, color);
                }
                else if(dir.equals("rt")){
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_TOP, color);
                }
                else if(dir.equals("rb")){
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_BOTTOM, color);
                }
                else{
                    System.out.println("Error, unknown direction of OneWayMirror.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }

            }
            else if(type.equals("two")){
                System.out.println("twoWayMirror, Please enter a direction(format lt rt)");
                String dir = scanner.nextLine();
                if(dir.equals("lt")){
                    chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.LEFT_TOP, color);
                }
                else if(dir.equals("rt")){
                    chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.RIGHT_TOP, color);
                }
                else{
                    System.out.println("Error, unknown direction of TwoWayMirror.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
            }
            else if(type.equals("shield")){
                System.out.println("shield, Please enter a direction(format t,r,b,l)");
                String dir = scanner.nextLine();
                if(dir.equals("t")){
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.TOP, color);
                }
                else if(dir.equals("r")){
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.RIGHT, color);
                }
                else if(dir.equals("b")){
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.BOTTOM, color);
                }
                else if(dir.equals("l")){
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.LEFT, color);
                }
                else{
                    System.out.println("Error, unknown direction of Shield.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
            }
            else{
                System.out.println("Error, unknown pieces.");
                chess = new ChessLaserEmitter(Direction.TOP, color);
            }

            //让用户输入正确的棋子位置
            System.out.println("Please enter coordinate. (format: x,y):");
            String coordinate = scanner.nextLine();
            int[] coordinates = parseCoordinates(coordinate);
            while(coordinates[0]<0||coordinates[0]>=chessboard.length||coordinates[1]<0||coordinates[1]>=chessboard[0].length||chessboard[coordinates[0]][coordinates[1]]!=null){
                System.out.println("Locations that cannot be deployed, please re-enter");
                coordinate = scanner.nextLine();
                coordinates = parseCoordinates(coordinate);
            }
            if(toRecordEmiter){
                if(color == Color.BLUE){
                    blueLaserEmitterPos[0] = coordinates[0];
                    blueLaserEmitterPos[1] = coordinates[1];
                }
                else{
                    redLaserEmitterPos[0] = coordinates[0];
                    redLaserEmitterPos[1] = coordinates[1];
                }
            }


            chessboard[coordinates[0]][coordinates[1]] = chess;
            System.out.println("Placed " + type + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
            //GUI或许在此就可以渲染棋盘？我不懂

        }
    }

    private static int[] parseCoordinates(String input) {
        String[] parts = input.split(",");
        if (parts.length != 2) {
            return null;
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            return new int[]{x, y};
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
