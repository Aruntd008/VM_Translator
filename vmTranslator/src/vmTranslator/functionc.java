package vmTranslator;

import java.io.*;
import java.util.*;

public class functionc {

    

    private static List<String> readLines(File path) throws IOException {
        List<String> lines = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                lines.add(line);
            }
        }
        return removeblank.clean(lines);
    }
    public static boolean funcheck(String path ) 
	{
        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            List<String> tempLines = removeblank.clean(file);
            List<String> tempLabels = new ArrayList<>();

            for (String line : tempLines) {
                String[] split = line.split(" ");
                String type = split[0];
                String segment = (split.length > 1) ? split[1] : "";

                if (type.equals("function")) {
                    tempLabels.add(segment);
                }
            }

            for (String line : tempLines) {
                String[] split = line.split(" ");
                String type = split[0];
                String segment = (split.length > 1) ? split[1] : "";

                if (type.equals("call")) {
                    if (tempLabels.contains(segment)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
     
    public static List<String> readFile(String path) throws IOException {
        List<String> outLines = new ArrayList<>();

        if (funcheck(path)) {
            String directory = path.substring(0, path.lastIndexOf(File.separator));
            System.out.println(directory);
            File directoryPath = new File(directory);
            File[] filesList = directoryPath.listFiles();

            for (File file : filesList) {
                if (file.getName().endsWith(".vm")) {
                    outLines.addAll(readLines(file));
                }
            }
        } else {
            outLines.addAll(readLines(new File(path)));
        }
        return outLines;
    }

    public static List<String> addSysInit(String filePath) throws IOException {
        List<String> lines = readFile(filePath);
        for (String line : lines) {
            String[] split = line.split(" ");
            String type = split[0];
            String segment = (split.length > 1) ? split[1] : "";

            if (type.equals("function") && segment.contains("Sys.init")) {
                lines.add(0, "call Sys.init 0");
                break;
            }
        }
        return lines;
    }
}