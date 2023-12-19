package com.example.flcw.LaserChessGamePlay.chess;
public class ChessOneWayMirror extends Chess {
    public enum Direction {
        LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM
    }

    public Direction direction;

    public ChessOneWayMirror(Direction direction, Color color) {
        this.direction = direction;
        this.color = color;
    }

    //要求d的取值是r或者l
    public void rotate(char d){
        // 将枚举转换为数组
        Direction[] values = Direction.values();
        int index = -1;

        // 查找当前方向在枚举数组中的索引
        for (int i = 0; i < values.length; i++) {
            if (values[i] == direction) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // 根据顺时针（CW）或逆时针（CCW）旋转方向更新索引
            int rotation = (d == 'r') ? 1 : -1;
            index = (index + rotation + values.length) % values.length;

            // 更新方向
            direction = values[index];
        }
    }
}