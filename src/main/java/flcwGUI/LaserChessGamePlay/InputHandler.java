package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.operate.*;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import flcwGUI.LaserChessGamePlay.operate.Rotate.RotateDirection;

import java.util.Scanner;

public class InputHandler {

    public static Operate getOperationFromInput(Board board, Chess.Color color) {
        Scanner scanner = new Scanner(System.in);

        // Read the first coordinate
        System.out.println("Enter the first coordinate (format: x,y): ");
        String firstInput = scanner.nextLine();
        int[] firstCoordinates = parseCoordinates(firstInput);
        if (firstCoordinates == null) {
            System.out.println("Invalid input for coordinates. Please enter again.");
            //scanner.close();
            return getOperationFromInput(board, color); // Restart
        }
        if(!isChessColorMatching(board, firstCoordinates[0], firstCoordinates[1], color)){
            System.out.println("This place is not your pawn. Please enter again.");
            //scanner.close();
            return getOperationFromInput(board, color); // Restart
        }
        if(isLaserEmitter(board, firstCoordinates[0], firstCoordinates[1])){
            System.out.println("This place is a laser emitter");
            //scanner.close();
            return getOperationFromInput(board, color); // Restart
        }


        // Read the second input
        System.out.println("Enter the second coordinate or rotation direction: ");
        String secondInput = scanner.nextLine();
        //scanner.close();

        

        // Check if the second input is a coordinate or a rotation direction
        int[] secondCoordinates = parseCoordinates(secondInput);
        if (secondCoordinates != null) {
            // If it's a coordinate, return a Move object
            if (areCoordinatesEqual(firstCoordinates, secondCoordinates)) {
                System.out.println("Second coordinate is the same as the first. You put down the pieces. Please enter again.");
                return getOperationFromInput(board, color); // Restart
            } else {
                return new Move(firstCoordinates[0], firstCoordinates[1], secondCoordinates[0], secondCoordinates[1]);
            }
        } else {
            // If it's a rotation direction, return a Rotate object
            RotateDirection rotationDirection = parseRotationDirection(secondInput);
            if (rotationDirection == null) {
                System.out.println("Invalid input. Please enter again.");
                return getOperationFromInput(board, color); // Restart
            } else {
                return new Rotate(firstCoordinates[0], firstCoordinates[1], rotationDirection);
            }
        }
    }
    
    private static boolean isChessColorMatching(Board board, int x, int y, Chess.Color expectedColor) {
        Chess chessPiece = board.getChessAt(x, y); // 这里需要你实现 Board 类中的获取棋子的方法
        return chessPiece != null && chessPiece.color == expectedColor;
    }

    private static boolean isLaserEmitter(Board board, int x, int y) {
        Chess chessPiece = board.getChessAt(x, y); // 这里需要你实现 Board 类中的获取棋子的方法
        return chessPiece instanceof ChessLaserEmitter;
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

    private static RotateDirection parseRotationDirection(String input) {
        input = input.trim().toLowerCase();
        if (input.equals("left")) {
            return RotateDirection.LEFT;
        } else if (input.equals("right")) {
            return RotateDirection.RIGHT;
        } else {
            return null;
        }
    }

    private static boolean areCoordinatesEqual(int[] coordinates1, int[] coordinates2) {
        return coordinates1[0] == coordinates2[0] && coordinates1[1] == coordinates2[1];
    }

    public static void main(String[] args) {
    }
}
