package flcwGUI.LaserChessGamePlay;

import java.io.*;
import java.util.Objects;

public class User {
    // 获取资源文件夹的路径
    // ClassLoader classLoader = SaveBoard.class.getClassLoader();
    // String folderPath = Objects.requireNonNull(classLoader.getResource("saveBoard/")).getPath();

    private static final String FOLDER_PATH = Objects.requireNonNull(SaveBoard.class.getClassLoader().getResource("saveBoard/")).getPath();
    private static final File ACCOUNT = new File(FOLDER_PATH + "account.txt");
    private static final File DIRECTORY = new File(FOLDER_PATH);

    private String userName = "";

    public User(String name) {
        this.userName = name;
    }

    @Override
    public String toString() {
        return this.userName;
    }

    public static void register(String username, String password) {
        try {
            // 创建用户数据文件和用户数据文件夹
            // File accountFile = new File(FOLDER_PATH + "account.txt");
            // File userDirectory = new File(FOLDER_PATH);

            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }
            if (!ACCOUNT.exists()) {
                boolean created = ACCOUNT.createNewFile();
                if (created) {
                    System.out.println("File created successfully.");
                } else {
                    System.out.println("File already exists.");
                }
            }

            //这里应该不用再检查，因为在游戏主驱动函数中检查过了
            // // 检查用户名格式
            // if (!isValidUsername(username)) {
            //     System.out.println("Invalid username format. Please choose another username.");
            //     return;
            // }
            // // 检查是否存在同名用户
            // if (userExists(username)) {
            //     System.out.println("Username already exists. Please choose another username.");
            //     return;
            // }

            // 将用户名和密码写入文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT, true));
            writer.write(username + "," + password);
            writer.newLine();
            writer.close();

            // 创建用户文件夹
            File userFile = new File(DIRECTORY, username);
            userFile.mkdir();

            System.out.println("Registration successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断这个Username是否可以登录成功
    public static boolean login(String username, String password) {
        try {
            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }
            if (!ACCOUNT.exists()) {
                System.out.println("There are no accounts in the system! Please regiser first.");
                return false;
            }

            // 读取用户数据文件，检查用户名和密码是否匹配
            BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 2 && userData[0].equals(username) && userData[1].equals(password)) {
                    System.out.println("Login successful!");
                    reader.close();
                    return true;
                }
            }

            reader.close();
            System.out.println("Login failed. Invalid username or password.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean userExists(String username) {
        if (!ACCOUNT.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 1 && userData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception according to your application's needs
        }

        return false;
    }

    public static boolean isValidNameOrPasswd(String username) {
        // 文件夹名不能包含以下特殊字符，可以根据需要扩展
        String invalidCharacters = "/\\:*?\"<>|";

        // 文件夹名长度限制，可以根据需要调整
        int maxLength = 255;

        // 检查长度
        if (username.length() > maxLength) {
            return false;
        }

        // 检查特殊字符
        for (char c : invalidCharacters.toCharArray()) {
            if (username.indexOf(c) != -1) {
                return false;
            }
        }

        // 检查空格
        return !username.contains(" ");
    }

    public String getName() {
        return userName;
    }

}
