import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.Scanner;

public class FolderLock {

    private static final String LOCK_FILE_NAME = ".folderlock";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the folder path to lock: ");
        String folderPath = scanner.nextLine();

        File folder = new File(folderPath);

        if (!folder.exists()) {
            System.out.println("The folder does not exist.");
            return;
        }

        System.out.println("Enter a password to lock the folder: ");
        String password = scanner.nextLine();

        if (!lockFolder(folder, password)) {
            System.out.println("The folder could not be locked.");
            return;
        }

        System.out.println("The folder has been locked.");

        // Add this code
        System.out.println("Do you want to unlock the folder? (Y/N)");
        String unlock = scanner.nextLine();

        if (unlock.equals("Y")) {
            System.out.println("Enter the password to unlock the folder: ");
            String password2 = scanner.nextLine();

            if (!unlockFolder(folder, password2)) {
                System.out.println("The folder could not be unlocked.");
                return;
            }

            System.out.println("The folder has been unlocked.");
        } else {
            System.out.println("The folder will remain locked.");
        }
    }

    private static boolean lockFolder(File folder, String password) throws IOException {
        File lockFile = new File(folder, LOCK_FILE_NAME);

        if (lockFile.exists()) {
            System.out.println("The folder is already locked.");
            return false;
        }

        try (
                FileWriter fileWriter = new FileWriter(lockFile);
                PrintWriter printWriter = new PrintWriter(fileWriter);
        ) {
            printWriter.println(password);
        }

        folder.setReadOnly();

        return true;
    }

    private static boolean unlockFolder(File folder, String password) throws IOException {
        File lockFile = new File(folder, LOCK_FILE_NAME);

        if (!lockFile.exists()) {
            System.out.println("The folder is not locked.");
            return false;
        }

        String storedPassword = null;
        try (
                FileReader fileReader = new FileReader(lockFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            storedPassword = bufferedReader.readLine();
        }

        if (storedPassword == null || !storedPassword.equals(password)) {
            System.out.println("The password is incorrect.");
            return false;
        }

        lockFile.delete();
        folder.setWritable(true);

        return true;
    }
}
