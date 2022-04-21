package org.example.passwordmanager.Database;

import java.io.*;

public class DBConfig {

    private static DBConfig dbInstance;
    private static final String PATH_TO_DB_FILE = System.getProperty("user.home") + "/.pwdb";
    private static final String PATH_TO_DB_FILE_WIN = System.getProperty("user.home") + File.separator + ".pwdb";
    private static final String DB_NAME = "db.json";

    public static final String OS_NAME = System.getProperty("os.name");
    public static final String pathInLinux = PATH_TO_DB_FILE + "/" + DB_NAME;
    public static final String pathInWindows = PATH_TO_DB_FILE_WIN + File.pathSeparator + DB_NAME;

    public static DBConfig getInstance() throws IOException {
        if (dbInstance == null)
            dbInstance = new DBConfig();
        return dbInstance;
    }

    private DBConfig() throws IOException {
        File file = new File(pathInLinux);
        if (!file.exists()) {
            System.out.println("DBConfig: Database file doesn't exist, creating one");
            if (OS_NAME.equals("Linux"))
                createDBFile(PATH_TO_DB_FILE);
            else if (OS_NAME.contains("Windows"))
                createDBFile(PATH_TO_DB_FILE_WIN);
        }
    }

    public FileInputStream getInputStream() throws FileNotFoundException {
        if (OS_NAME.equals("Linux"))
            return new FileInputStream(pathInLinux);
        else if (OS_NAME.contains("Windows"))
            return new FileInputStream(pathInWindows);
        return null;
    }

    public FileOutputStream getOutputStream() throws FileNotFoundException {
        if (OS_NAME.equals("Linux"))
            return new FileOutputStream(pathInLinux);
        else if (OS_NAME.contains("Windows"))
            return new FileOutputStream(pathInWindows);
        return null;
    }

    private static void createDBFile(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdir();
            if (!dirCreated) System.out.println("ERROR: Cannot create directory for database");
        }
        File file = new File(path + /*File.pathSeparator*/ "/" + DB_NAME);
        boolean fileCreated = file.createNewFile();
        if (!fileCreated) System.out.println("File already exists");
    }
}
