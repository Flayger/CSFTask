package org.flayger;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private static boolean floatOverride = true;
    private static boolean intOverride = true;
    private static boolean stringOverride = true;
    private static final List<String> fileNames = new ArrayList<>();
    private static String outputFilesPath = "";
    private static String filePrefix = "";
    private static boolean isOverride = true;
    private static FileFilterStatistics selectedStatistics;
    private static Path outputPath;

    public static void main(String[] args) {
        argsCommandLineProcessing(args);

        try {
            outputPath = Path.of(new File(FileFilter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath() + outputFilesPath);
            Files.createDirectories(outputPath);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Ошибка - Не удалось создать директорию для файлов вывода");
            outputPath = null;
        }
        for (String file : fileNames) {
            try {
                readFileAndFilterData(file);
            } catch (Exception e) {
                System.out.println("Ошибка во время работы с файлом");
            }
        }
        if (selectedStatistics != null)
            selectedStatistics.showStatistics();
    }

    public static void argsCommandLineProcessing(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Ошибка. Должен быть указан хотя бы один параметр - путь к файлу источнику");
        }
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p" -> {
                    if (++i < args.length) {
                        filePrefix = args[i];
                    } else {
                        System.out.println("Ошибка в параметрах - не указан префикс к файлам с результатами");
                    }
                }
                case "-a" -> isOverride = false;
                case "-s" -> selectedStatistics = new FileFilterShortStatistics();
                case "-f" -> selectedStatistics = new FileFilterFullStatistics();
                case "-o" -> {
                    if (i++ < args.length) {
                        outputFilesPath = args[i];
                    } else {
                        System.out.println("Ошибка в параметрах - не указан путь к файлам с результатами");
                    }
                }
                default -> fileNames.add(args[i]);
            }
        }
    }

    public static void readFileAndFilterData(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.matches("^-?\\d+\\.\\d*E?.\\d*$")) {
                    saveFloatData(line);
                } else if (line.matches("^-?\\d+$")) {
                    saveIntegerData(line);
                } else {
                    saveStringData(line);
                }
                line = reader.readLine();
            }
        }
    }

    public static void saveFloatData(String dataFloat) throws IOException {
        File floatsOutput = new File(outputPath + "/" + filePrefix + "floats" + ".txt");

        if (isOverride)
            if (floatOverride) {
                Files.deleteIfExists(floatsOutput.toPath());
                floatOverride = false;
            }

        saveToFile(dataFloat, floatsOutput);

        if (selectedStatistics != null) {
            BigDecimal data = new BigDecimal(dataFloat);
            selectedStatistics.updateStatistics(data);
        }

    }

    public static void saveIntegerData(String dataInteger) throws IOException {
        File integersOutput = new File(outputPath + "/" + filePrefix + "integers" + ".txt");

        if (isOverride)
            if (intOverride) {
                Files.deleteIfExists(integersOutput.toPath());
                intOverride = false;
            }

        saveToFile(dataInteger, integersOutput);

        if (selectedStatistics != null) {
            BigInteger data = new BigInteger(dataInteger);
            selectedStatistics.updateStatistics(data);
        }

    }

    public static void saveStringData(String dataString) throws IOException {
        File stringsOutput = new File(outputPath + "/" + filePrefix + "strings" + ".txt");
        if (isOverride)
            if (stringOverride) {
                Files.deleteIfExists(stringsOutput.toPath());
                stringOverride = false;
            }

        saveToFile(dataString, stringsOutput);

        if (selectedStatistics != null) {
            selectedStatistics.updateStatistics(dataString);
        }

    }

    public static void saveToFile(String data, File file) throws IOException {
        file.createNewFile();
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(data);
        }
    }
}

