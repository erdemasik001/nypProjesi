package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileManager {

    private static String dataDirectory = null;

    public static String getDataDirectory() {
        if (dataDirectory != null) {
            return dataDirectory;
        }

        try {

            String jarPath = FileManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            File jarFile = new File(jarPath);

            String path = jarFile.getAbsolutePath();
            if (path.startsWith("/") && path.length() > 2 && path.charAt(2) == ':') {
                path = path.substring(1);
            }
            jarFile = new File(path);

            if (jarFile.isFile() && jarFile.getName().endsWith(".jar")) {

                dataDirectory = jarFile.getParent() + File.separator;
            } else {

                File current = jarFile;
                while (current != null) {
                    File srcFolder = new File(current, "src");
                    if (srcFolder.exists() && srcFolder.isDirectory()) {

                        File testFile = new File(srcFolder, "flights.txt");
                        if (testFile.exists()) {
                            dataDirectory = srcFolder.getAbsolutePath() + File.separator;
                            break;
                        }
                    }
                    current = current.getParentFile();
                }

                if (dataDirectory == null) {
                    dataDirectory = "src" + File.separator;
                }
            }
        } catch (Exception e) {
            System.err.println("Error determining data directory: " + e.getMessage());

            dataDirectory = "src" + File.separator;
        }

        System.out.println("Data directory: " + dataDirectory);
        return dataDirectory;
    }

    public static String getDataFilePath(String filename) {
        return getDataDirectory() + filename;
    }

    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean writeLines(String filePath, List<String> lines, boolean append) {
        File file = new File(filePath);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
