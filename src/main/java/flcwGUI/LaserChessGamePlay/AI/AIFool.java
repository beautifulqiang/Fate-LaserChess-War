package flcwGUI.LaserChessGamePlay.AI;

import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import flcwGUI.LaserChessGamePlay.operate.Rotate;
import flcwGUI.LaserChessGamePlay.operate.Rotate.RotateDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIFool {
    private final Chess[][] chessboard;

    public AIFool(Chess[][] chessboard) {
        this.chessboard = chessboard;
    }

    // 决策下一步操作的方法
    public Operate decideNextMove() {
        // 生成所有合法操作列表
        List<Operate> legalMoves = generateLegalMoves();

        // 随机选择一个操作
        return chooseRandomMove(legalMoves);
    }

    // 生成所有合法操作列表
    private List<Operate> generateLegalMoves() {
        List<Operate> legalMoves = new ArrayList<>();

        // 遍历棋盘
        for (int x = 0; x < chessboard.length; x++) {
            for (int y = 0; y < chessboard[0].length; y++) {
                // 生成从当前位置移动的所有合法操作
                legalMoves.addAll(generateLegalMovesFromPosition(x, y));
                // 生成在当前位置旋转的所有合法操作
                legalMoves.addAll(generateLegalRotatesFromPosition(x, y));
            }
        }

        return legalMoves;
    }

    // 从指定位置生成所有可能的合法移动
    private List<Operate> generateLegalMovesFromPosition(int x, int y) {
        List<Operate> legalMoves = new ArrayList<>();

        // 示例：假设可以向上、下、左、右移动一步
        legalMoves.add(new Move(x, y, x - 1, y)); // 上
        legalMoves.add(new Move(x, y, x + 1, y)); // 下
        legalMoves.add(new Move(x, y, x, y - 1)); // 左

        // TODO: 添加其他具体的操作逻辑，判断是否越界、是否有棋子等

        return legalMoves;
    }

    // 从指定位置生成所有可能的合法旋转
    private List<Operate> generateLegalRotatesFromPosition(int x, int y) {
        List<Operate> legalRotates = new ArrayList<>();

        // 示例：假设可以向左、右旋转
        legalRotates.add(new Rotate(x, y, RotateDirection.LEFT));
        legalRotates.add(new Rotate(x, y, RotateDirection.RIGHT));

        // TODO: 添加其他具体的操作逻辑，判断是否越界、是否有棋子等

        return legalRotates;
    }

    // 从操作列表中随机选择一个操作
    private Operate chooseRandomMove(List<Operate> moves) {
        if (moves.isEmpty()) {
            return null; // 如果没有合法操作，返回 null 或者其他适当的标志
        }

        Random random = new Random();
        int randomIndex = random.nextInt(moves.size());
        return moves.get(randomIndex);
    }
}
