package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.*;
import flcwGUI.LaserChessGamePlay.chess.Chess.Color;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter.Direction;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Scanner;

import static flcwGUI.ButtonController.map_path;

public class DIYBoard {
    public static void DIY(Chess[][] chessboard, Background[][] backgroundBoard, int[] redLaserEmitterPos, int[] blueLaserEmitterPos, Direction[] tempD) {
        Scanner scanner = new Scanner(System.in);

        //TODO 在加入AI算法之后，还可以检测这个棋局有没有解，没有解不行

        boolean saveBoard = false;
        File file = new File("defaultDummyFile.txt");


        boolean redKing = false;
        boolean blueKing = false;
        boolean redLaserEmitter = false;
        boolean blueLaserEmitter = false;

        String type = "";
        Color color;
        char char_color;

        //判断是否要保存棋盘
        System.out.println("Whether you want to save the current board configuration?(Y,N)");
        String saveChoice = scanner.nextLine();
        if (saveChoice.equals("Y")) {
            saveBoard = true;
            String currentDirectory = System.getProperty("user.dir");
            // 提示用户输入文件名
            System.out.println("Please enter a file name to save the board configuration:");
            String fileName = scanner.nextLine();
            // TODO: 这里需要检验格式
            // 创建目录（如果不存在）
            File saveBoardDirectory = new File("saveBoard");
            if (!saveBoardDirectory.exists()) {
                saveBoardDirectory.mkdirs();
            }
            // 创建文件
            file = new File(saveBoardDirectory, fileName + ".txt");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            System.out.println("Board configuration saved successfully!");
        } catch (IOException e) {
            System.out.println("Error while saving the board configuration.");
            e.printStackTrace();
        }


        while (true) {
            //读入颜色和类型
            System.out.println("Enter the color ( R or B )");
            char_color = scanner.nextLine().charAt(0);
            if (char_color == 'B') {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }

            //在文件中写入颜色
            if (saveBoard) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                    writer.println(char_color);
                } catch (IOException e) {
                    System.out.println("Error while saving the board configuration.");
                    e.printStackTrace();
                }
            }


            System.out.println("Enter the pieces you want to place (enter END to consider the placement complete)");
            type = scanner.nextLine();
            //在文件中写入棋子类型
            if (saveBoard) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                    writer.println(type);
                } catch (IOException e) {
                    System.out.println("Error while saving the board configuration.");
                    e.printStackTrace();
                }
            }

            if (type.equals("END")) {
                if (!redLaserEmitter || !blueLaserEmitter || !redKing || !blueKing) {
                    System.out.println("The requisites for the game are not met, two kings of different colors and two le are required");
                    continue;
                }
                //确实满足了棋局结束的条件
                break;
            }

            if (type.equals("bg")) {
                //让用户输入正确的棋子位置
                System.out.println("Please enter coordinate. (format: x,y):");
                String coordinate = scanner.nextLine();
                int[] coordinates = parseCoordinates(coordinate);
                while (coordinates[0] < 0 || coordinates[0] >= chessboard.length || coordinates[1] < 0 || coordinates[1] >= chessboard[0].length || backgroundBoard[coordinates[0]][coordinates[1]] != null) {
                    System.out.println("Locations that cannot be deployed, please re-enter");
                    coordinate = scanner.nextLine();
                    coordinates = parseCoordinates(coordinate);
                }
                //背景情况下，在文件中写入背景坐标
                if (saveBoard) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                        writer.println(coordinate);
                    } catch (IOException e) {
                        System.out.println("Error while saving the board configuration.");
                        e.printStackTrace();
                    }
                }

                if (color == Color.BLUE) {
                    backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.BLUE);
                } else {
                    backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.RED);
                }
                System.out.println("Placed " + "background" + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
                continue;
            }

            Chess chess;
            boolean toRecordEmiter = false;
            //后面的最终else错误情况的棋子是瞎生成的
            if (type.equals("king")) {
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
            } else if (type.equals("le") || type.equals("laseremiter")) {
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
                //读入方向
                if (saveBoard) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                        writer.println(dir);
                    } catch (IOException e) {
                        System.out.println("Error while saving the board configuration.");
                        e.printStackTrace();
                    }
                }
                if (dir.equals("t")) {
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                    if (color == Color.BLUE) {
                        tempD[1] = Direction.TOP;
                    } else {
                        tempD[0] = Direction.TOP;
                    }
                } else if (dir.equals("r")) {
                    chess = new ChessLaserEmitter(Direction.RIGHT, color);
                    if (color == Color.BLUE) {
                        tempD[1] = Direction.RIGHT;
                    } else {
                        tempD[0] = Direction.RIGHT;
                    }
                } else if (dir.equals("b")) {
                    chess = new ChessLaserEmitter(Direction.BOTTOM, color);
                    if (color == Color.BLUE) {
                        tempD[1] = Direction.BOTTOM;
                    } else {
                        tempD[0] = Direction.BOTTOM;
                    }
                } else if (dir.equals("l")) {
                    chess = new ChessLaserEmitter(Direction.LEFT, color);
                    if (color == Color.BLUE) {
                        tempD[1] = Direction.LEFT;
                    } else {
                        tempD[0] = Direction.LEFT;
                    }
                } else {
                    System.out.println("Error, unknown direction of laseremiter.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
                toRecordEmiter = true;
            } else if (type.equals("one")) {
                System.out.println("oneWayMirror, Please enter a direction(format lt lb rt rb)");
                String dir = scanner.nextLine();

                //读入方向
                if (saveBoard) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                        writer.println(dir);
                    } catch (IOException e) {
                        System.out.println("Error while saving the board configuration.");
                        e.printStackTrace();
                    }
                }

                if (dir.equals("lt")) {
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_TOP, color);
                } else if (dir.equals("lb")) {
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_BOTTOM, color);
                } else if (dir.equals("rt")) {
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_TOP, color);
                } else if (dir.equals("rb")) {
                    chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_BOTTOM, color);
                } else {
                    System.out.println("Error, unknown direction of OneWayMirror.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }

            } else if (type.equals("two")) {
                System.out.println("twoWayMirror, Please enter a direction(format lt rt)");
                String dir = scanner.nextLine();

                //读入方向
                if (saveBoard) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                        writer.println(dir);
                    } catch (IOException e) {
                        System.out.println("Error while saving the board configuration.");
                        e.printStackTrace();
                    }
                }

                if (dir.equals("lt")) {
                    chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.LEFT_TOP, color);
                } else if (dir.equals("rt")) {
                    chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.RIGHT_TOP, color);
                } else {
                    System.out.println("Error, unknown direction of TwoWayMirror.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
            } else if (type.equals("shield")) {
                System.out.println("shield, Please enter a direction(format t,r,b,l)");
                String dir = scanner.nextLine();

                //读入方向
                if (saveBoard) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                        writer.println(dir);
                    } catch (IOException e) {
                        System.out.println("Error while saving the board configuration.");
                        e.printStackTrace();
                    }
                }

                if (dir.equals("t")) {
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.TOP, color);
                } else if (dir.equals("r")) {
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.RIGHT, color);
                } else if (dir.equals("b")) {
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.BOTTOM, color);
                } else if (dir.equals("l")) {
                    chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.LEFT, color);
                } else {
                    System.out.println("Error, unknown direction of Shield.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }
            } else {
                System.out.println("Error, unknown pieces.");
                chess = new ChessLaserEmitter(Direction.TOP, color);
            }

            //让用户输入正确的棋子位置
            System.out.println("Please enter coordinate. (format: x,y):");
            String coordinate = scanner.nextLine();
            int[] coordinates = parseCoordinates(coordinate);
            while (coordinates[0] < 0 || coordinates[0] >= chessboard.length || coordinates[1] < 0 || coordinates[1] >= chessboard[0].length || chessboard[coordinates[0]][coordinates[1]] != null) {
                System.out.println("Locations that cannot be deployed, please re-enter");
                coordinate = scanner.nextLine();
                coordinates = parseCoordinates(coordinate);
            }

            //读入方向
            if (saveBoard) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                    writer.println(coordinate);
                } catch (IOException e) {
                    System.out.println("Error while saving the board configuration.");
                    e.printStackTrace();
                }
            }

            if (toRecordEmiter) {
                if (color == Color.BLUE) {
                    blueLaserEmitterPos[0] = coordinates[0];
                    blueLaserEmitterPos[1] = coordinates[1];
                } else {
                    redLaserEmitterPos[0] = coordinates[0];
                    redLaserEmitterPos[1] = coordinates[1];
                }
            }


            chessboard[coordinates[0]][coordinates[1]] = chess;
            System.out.println("Placed " + type + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
            //GUI或许在此就可以渲染棋盘？我不懂

        }
    }

    public static void LoadSavedBoard(Chess[][] chessboard, Background[][] backgroundBoard, int[] redLaserEmitterPos, int[] blueLaserEmitterPos, Direction[] tempD) {
        boolean redKing = false;
        boolean blueKing = false;
        boolean redLaserEmitter = false;
        boolean blueLaserEmitter = false;

        String type = "";
        Color color;
        char char_color;

        try {
            File file = new File(map_path);

            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建BufferedReader对象，用于读取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            //读入文件的过程中不需要这么多输出，注释掉
            while (true) {
                //读入颜色和类型
                char_color = reader.readLine().charAt(0);
                if (char_color == 'B') {
                    color = Color.BLUE;
                } else {
                    color = Color.RED;
                }

                type = reader.readLine();

                if (type.equals("END")) {
                    if (!redLaserEmitter || !blueLaserEmitter || !redKing || !blueKing) {
                        // 此时的棋局是无解的
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("棋局加载警告");
                        alert.setContentText("该棋局无解！");
                        alert.showAndWait();
                    }
                    //确实满足了棋局结束的条件
                    break;
                }


                if (type.equals("bg")) {
                    //让用户输入正确的棋子位置
                    String coordinate = reader.readLine();
                    int[] coordinates = parseCoordinates(coordinate);

                    if (color == Color.BLUE) {
                        backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.BLUE);
                    } else {
                        backgroundBoard[coordinates[0]][coordinates[1]] = new Background(flcwGUI.LaserChessGamePlay.background.Background.Color.RED);
                    }
                    System.out.println("Placed " + "background" + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
                    continue;
                }

                Chess chess;
                boolean toRecordEmiter = false;
                //后面的最终else错误情况的棋子是瞎生成的
                if (type.equals("king")) {
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
                } else if (type.equals("le") || type.equals("laseremiter")) {
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
                    String dir = reader.readLine();

                    if (dir.equals("t")) {
                        chess = new ChessLaserEmitter(Direction.TOP, color);
                        if (color == Color.BLUE) {
                            tempD[1] = Direction.TOP;
                        } else {
                            tempD[0] = Direction.TOP;
                        }
                    } else if (dir.equals("r")) {
                        chess = new ChessLaserEmitter(Direction.RIGHT, color);
                        if (color == Color.BLUE) {
                            tempD[1] = Direction.RIGHT;
                        } else {
                            tempD[0] = Direction.RIGHT;
                        }
                    } else if (dir.equals("b")) {
                        chess = new ChessLaserEmitter(Direction.BOTTOM, color);
                        if (color == Color.BLUE) {
                            tempD[1] = Direction.BOTTOM;
                        } else {
                            tempD[0] = Direction.BOTTOM;
                        }
                    } else if (dir.equals("l")) {
                        chess = new ChessLaserEmitter(Direction.LEFT, color);
                        if (color == Color.BLUE) {
                            tempD[1] = Direction.LEFT;
                        } else {
                            tempD[0] = Direction.LEFT;
                        }
                    } else {
                        System.out.println("Error, unknown direction of laseremiter.");
                        chess = new ChessLaserEmitter(Direction.TOP, color);
                    }
                    toRecordEmiter = true;
                } else if (type.equals("one")) {
                    String dir = reader.readLine();
                    //读入方向
                    if (dir.equals("lt")) {
                        chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_TOP, color);
                    } else if (dir.equals("lb")) {
                        chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.LEFT_BOTTOM, color);
                    } else if (dir.equals("rt")) {
                        chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_TOP, color);
                    } else if (dir.equals("rb")) {
                        chess = new ChessOneWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessOneWayMirror.Direction.RIGHT_BOTTOM, color);
                    } else {
                        System.out.println("Error, unknown direction of OneWayMirror.");
                        chess = new ChessLaserEmitter(Direction.TOP, color);
                    }

                } else if (type.equals("two")) {
                    String dir = reader.readLine();
                    //读入方向
                    if (dir.equals("lt")) {
                        chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.LEFT_TOP, color);
                    } else if (dir.equals("rt")) {
                        chess = new ChessTwoWayMirror(flcwGUI.LaserChessGamePlay.chess.ChessTwoWayMirror.Direction.RIGHT_TOP, color);
                    } else {
                        System.out.println("Error, unknown direction of TwoWayMirror.");
                        chess = new ChessLaserEmitter(Direction.TOP, color);
                    }
                } else if (type.equals("shield")) {
                    String dir = reader.readLine();
                    //读入方向
                    if (dir.equals("t")) {
                        chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.TOP, color);
                    } else if (dir.equals("r")) {
                        chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.RIGHT, color);
                    } else if (dir.equals("b")) {
                        chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.BOTTOM, color);
                    } else if (dir.equals("l")) {
                        chess = new ChessShield(flcwGUI.LaserChessGamePlay.chess.ChessShield.Direction.LEFT, color);
                    } else {
                        System.out.println("Error, unknown direction of Shield.");
                        chess = new ChessLaserEmitter(Direction.TOP, color);
                    }
                } else {
                    System.out.println("Error, unknown pieces.");
                    chess = new ChessLaserEmitter(Direction.TOP, color);
                }

                //让用户输入正确的棋子位置
                String coordinate = reader.readLine();
                int[] coordinates = parseCoordinates(coordinate);

                //读入方向

                if (toRecordEmiter) {
                    if (color == Color.BLUE) {
                        blueLaserEmitterPos[0] = coordinates[0];
                        blueLaserEmitterPos[1] = coordinates[1];
                    } else {
                        redLaserEmitterPos[0] = coordinates[0];
                        redLaserEmitterPos[1] = coordinates[1];
                    }
                }

                chessboard[coordinates[0]][coordinates[1]] = chess;
                System.out.println("Placed " + type + " at position (" + coordinates[0] + "," + coordinates[1] + ").");
            }


            // 关闭资源
            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
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
