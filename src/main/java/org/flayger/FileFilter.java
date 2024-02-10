package org.flayger;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileFilter {


//     private Path inPath;
//    private Path outPath;
//    private Type type;
//    private Order order;

    /*Значения по умолчанию:
     * inPath - может быть просто имя файла относительно рабочей дирректории
     * outPath - out.txt рядом с файлом источником
     * type - строка
     * order - по возрастанию*/
//    public SortFile(String inString) {
//        inPath = Paths.get(inString).toAbsolutePath();
//        outPath = Paths.get(inPath.getParent() + "\\out.txt");
//        type = Type.STRING;
//        order = Order.ASCENDING;
//    }
//
//    public SortFile(String inString, String outString) {
//        this(inString);
//        outPath = Paths.get(outString).toAbsolutePath();
//    }
//
//    public SortFile(String inString, String outString, String typeString) {
//        this(inString, outString);
//        type = Type.findEnum(typeString);
//    }
//
//    public SortFile(String inString, String outString, String typeString, String orderString) {
//        this(inString, outString, typeString);
//        order = Order.findEnum(orderString);
//    }
//

    static boolean floatOverride = true;
    static boolean intOverride = true;
    static boolean stringOverride = true;


    static List<String> fileNames = new ArrayList<>();
    static String outputFilesPath = "";
    static String filePrefix = "";
    static boolean isOverride = true;
    static boolean isShortStatistic = false;
    static boolean isFullStatistic = false;
    //filenames - по сути это же поток? у него есть работа с тредами -> в каждом файле запускать метод на прохождение и синхронизованную запись результатов в файлы результаты
    //ExecutorService executor = Executors.newFixedThreadPool(10);?
                /*
                        for (String filePath : filePaths) {
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Process each line here
                        // Example: System.out.println(line);
                    }
                } catch (IOException e) {
                    // Handle file processing exception
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown(); // Shutdown the executor after all tasks are submitted
    }
                 */
                /* или
                public void processFiles(List<String> filePaths) {
        filePaths.parallelStream().forEach(filePath -> {
            try {
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                // Process each line here
                lines.forEach(line -> {
                    // Process each line of the file
                    // Example: System.out.println(line);
                });
            } catch (IOException e) {
                // Handle file processing exception
                e.printStackTrace();
            }
        });
    }
                 */

    //short stats
    private static Integer intsCount = 0;
    private static Integer floatsCount = 0;
    private static Integer stringsCount = 0;

    //full stats в отдельный класс? отдельный класс для каждого типа? и создавать экземпляры с методами для увеличения-изменения?
    private static BigInteger intsMaxValue;
    private static BigInteger intsMinValue;
    private static BigInteger intsAverage = new BigInteger("0");
    private static BigInteger intsSum = new BigInteger("0");

    //float static
    private static BigDecimal floatsMaxValue;
    private static BigDecimal floatsMinValue;
    private static BigDecimal floatsAverage = new BigDecimal("0.0");
    private static BigDecimal floatsSum = new BigDecimal("0");

    //string
    private static BigInteger stringsMaxLength;
    private static BigInteger stringsMinLength;

    public static void main(String[] args) {
        argsCommandLineProcessing(args);

        //здесь создать статистику по всем типам?
        if (isShortStatistic) {

        }
        if (isFullStatistic) {

        }

        System.out.println("start files");
        System.out.println(fileNames);
        //обработка файлов
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            for (String file : fileNames) {
                executor.submit(() -> readFileAndFilterData(file));
            }
            executor.shutdown(); // Shutdown the executor after all tasks are submitted
        }

        showStatistics();

//        for (String file : fileNames) {
//            //мб можно распараллелить?
//            System.out.println("open " + file);
//            //ошибка открытия файла?
//            //Scanner scanner = new Scanner(openFile);
//            readFileAndFilterData(file);
//        }


    }

    private static void showStatistics() {
        System.out.printf("integers stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", intsCount, intsMinValue, intsMaxValue, intsAverage, intsSum);

        System.out.printf("floats stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", floatsCount, floatsMinValue, floatsMaxValue, floatsAverage, floatsSum);

        System.out.printf("strings stats - count - %S, minLength - %S, maxLength - %S \n", stringsCount, stringsMinLength, stringsMaxLength);
    }

    public static void readFileAndFilterData(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String line = reader.readLine();
            while (line != null) {
                // for (int i = 0; i < openFile.length(); i++) {
                //   = scanner.nextLine();
                System.out.println("line " + line);
                //если ошибка в считывании строки - читать следующую
                if (line.matches("^-?\\d+\\.\\d*E?.\\d*$")) {
                    //с минусом или нет с любым (от 1) количеством цифр и одной точкой и любым количеством цифр после точки до конца строки
                    //Float dataFloat = Float.parseFloat(line);
                    //запись в файл здесь? проверка перезаписи? сделать отдельный метод для записи - передавать в него тип записываемых данных,
                    // данные и уже в том методе определять, в какой файл записывать и какие параметры записи (перезапись или нет)
                    File floatsOutput = new File(outputFilesPath + filePrefix + "floats" + ".txt");
                    saveFloatData(line);
                    //checkStats(line, Float.class);
                    //если ошибка в записи в файл - ждать или читать следующую строку
                } else if (line.matches("^-?\\d+$")) {
                    //с минусом или нет с любым (от 1) количеством цифр до конца строки
                    //Integer dataInteger = Integer.parseInt(line);
                    saveIntegerData(line);

                } else {
                    //остаются только строки
                    saveStringData(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    //при запуске - подается насколько файлов в args
    //в файлах это могут быть целые числа, строки, вещественные
    //разделитель - перенос строки /n
    //строки читаются по очереди в файле - файлы читаются по очереди в args
    //

    //task - записать разные типы данных в разные файлы
    //целые в один integers.txt
    //вещественные в другой floats.txt
    //строки в третий strings.txt

    //в args можно указать с помощью -o задавать путь для результатов
    //параметр -p для префикса файлов

    //по умолчанию файлы перезаписываются
    //параметр -a можно задать режим добавления в существующие файлы

    //создавать файлы только если нужно добавить в них что-то

    //статистика при фильтрировании - у каждого типа данных (3 класса?)
    //2 вида - краткая или полная
    //опции -s или -f
    //-s содержит только количество записанных элементов
    //-f количество записанных и min, max, sum, average.
    //для строк - количетсво, размер самой короткой и самой длинной

    //вывести статистику в консоль

    //обработка ошибок. вывод причины.

    //if needed
//        File integersOutput = new File(outputFilesPath + filePrefix + "integers" + ".txt");
//        File floatsOutput = new File(outputFilesPath + filePrefix + "floats" + ".txt");
//        File stringsOutput = new File(outputFilesPath + filePrefix + "strings" + ".txt");

    //в виде классов сделать? 2 класса - для полной и короткой статистики
    //short statistic
    //if found
//        int counterIntegers;
//        int counterFloats;
//        int counterStrings;

    //full statistic
//        int minInteger;
//        int maxInteger;
//        int sumInteger;
//        int averageInteger;
//
//        double minFloat;
//        double maxFloat;
//        double sumFloat;
//        double averageFloat;
//
//        int minLengthString;
//        int maxLengthString;

    //тело - в отдельном методе прохождение файла и запись статистики
    //цикл, пока не пройду все файлы

    //в теле метода - проверка на правильность введенных значений.
    // определение строки - integer float or string?

    //sout short or short+full statistic


    public static void saveFloatData(String dataFloat) throws IOException {
        //можно сделать универсальный метод, который просто пришедший тип будет разбивать. generics?
        //вставка в конец файла
        //write dataFloat
        System.out.println("saving float");
        File floatsOutput = new File(outputFilesPath + filePrefix + "floats" + ".txt");

        if (isOverride && floatOverride) {
            Files.deleteIfExists(floatsOutput.toPath());
            floatOverride = false;
        }
        floatsOutput.createNewFile();
//        try (FileWriter myWriter = new FileWriter(floatsOutput)) {
//            myWriter.write(String.valueOf(dataFloat));
//        }
        //Files.write(floatsOutput.toPath(), String.valueOf(dataFloat).getBytes(), StandardOpenOption.APPEND);
        try (FileWriter fw = new FileWriter(floatsOutput, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(dataFloat);
        }

        //if short stats
        floatsCount++;
        //if full stats
        //new stringStats if null
        BigDecimal data = new BigDecimal(dataFloat);
        if (floatsMaxValue == null) {
            floatsMaxValue = data;
        } else if (floatsMaxValue.compareTo(data) < 0) {
            floatsMaxValue = data;
        }

        if (floatsMinValue == null) {
            floatsMinValue = data;
        } else if (floatsMinValue.compareTo(data) > 0) {
            floatsMinValue = data;
        }
        //avg and sum
        floatsSum = floatsSum.add(data);
        floatsAverage = floatsSum.divide(BigDecimal.valueOf(intsCount));

    }

    public static void saveIntegerData(String dataInteger) throws IOException {
        System.out.println("saving int");
        File integersOutput = new File(outputFilesPath + filePrefix + "integers" + ".txt");

        if (isOverride && intOverride) {
            Files.deleteIfExists(integersOutput.toPath());
            intOverride = false;
        }
        integersOutput.createNewFile();
//        try (FileWriter myWriter = new FileWriter(integersOutput)) {
//            myWriter.write(String.valueOf(dataInteger));
//        }
        //Files.write(integersOutput.toPath(), String.valueOf(dataInteger).getBytes(),StandardOpenOption.APPEND);
        try (FileWriter fw = new FileWriter(integersOutput, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(dataInteger);
        }
        //if short stats
        intsCount++;
        //if full stats
        //new stringStats if null
        BigInteger data = new BigInteger(dataInteger);
        if (intsMaxValue == null) {
            intsMaxValue = data;
        } else if (intsMaxValue.compareTo(data) < 0) {
            intsMaxValue = data;
        }

        if (intsMinValue == null) {
            intsMinValue = data;
        } else if (intsMinValue.compareTo(data) > 0) {
            intsMinValue = data;
        }
        //avg and sum
        intsSum = intsSum.add(data);
        intsAverage = intsSum.divide(BigInteger.valueOf(intsCount));

    }

    public static void saveStringData(String dataString) throws IOException {
        System.out.println("saving string");
        File stringsOutput = new File(outputFilesPath + filePrefix + "strings" + ".txt");
        if (isOverride && stringOverride) {
            Files.deleteIfExists(stringsOutput.toPath());
            stringOverride = false;
        }
        stringsOutput.createNewFile();
//        if(isOverride){
//            Files.deleteIfExists(stringsOutput.toPath());
//        }
        //(FileWriter myWriter = new FileWriter( stringsOutput)) {
        //Files.write(Paths.get(stringsOutput.toURI()), data.getBytes(), StandardOpenOption.APPEND);
        try (FileWriter fw = new FileWriter(stringsOutput, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(dataString);
        }
        //if short stats
        stringsCount++;
        //if full stats
        //new stringStats if null
        BigInteger lineSize = BigInteger.valueOf(dataString.length());
        if (stringsMaxLength == null) {
            stringsMaxLength = lineSize;
        } else if (stringsMaxLength.compareTo(lineSize) < 0) {
            stringsMaxLength = lineSize;
        }

        if (stringsMinLength == null) {
            stringsMinLength = lineSize;
        } else if (stringsMinLength.compareTo(lineSize) > 0) {
            stringsMinLength = lineSize;
        }

    }

    public static void argsCommandLineProcessing(String[] args) {
        if (args.length == 0)
            System.out.println("Ошибка. Должен быть указан хотя бы один параметр - путь к файлу источнику");
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                    //считать префикс. удалить?
                    System.out.println("before ++" + i);
                    if (!args[i++].isEmpty()) {
                        System.out.println("after ++" + i);
                        filePrefix = args[i];
                        System.out.println("assign" + i);
                    } else {
                        System.out.println("Ошибка - неправильно указан префикс к файлам с результатами");
                    }
                    break;
                case "-a":
                    isOverride = false;
                    break;
                case "-s":
                    isShortStatistic = true;
                    //короткая статистика
                    break;
                case "-f":
                    isFullStatistic = true;
                    //полная статистика
                    break;
                case "-o":
                    if (!args[i++].isEmpty()) {
                        System.out.println("after ++" + i);
                        outputFilesPath = args[i];
                        System.out.println("assign" + i);
                    } else {
                        System.out.println("Ошибка - неправильно указан путь к файлам с результатами");
                    }
                    //и сразу считать путь. удалить параметр из аргументов?
                    break;
                default:
                    fileNames.add(args[i]);
                    //здесь добавлять в список?
            }
        }
    }


//        SortFile sortFile;
//
//        try {
//            switch (args.length) {
//                case 0:
//                    System.out.println("Ошибка! Должен быть указан хотя бы один параметр - путь к файлу источнику");
//                    return;
//                case 1:
//                    sortFile = new SortFile(args[0]);
//                    break;
//                case 2:
//                    sortFile = new SortFile(args[0], args[1]);
//                    break;
//                case 3:
//                    sortFile = new SortFile(args[0], args[1], args[2]);
//                    break;
//                default:
//                    sortFile = new SortFile(args[0], args[1], args[2], args[3]);
//                    break;
//            }
//
//            List<String> lines = Files.readAllLines(sortFile.inPath);
//            lines = sortFile.insertionSort(lines);
//            Files.deleteIfExists(sortFile.outPath);
//            Files.write(sortFile.outPath, lines, StandardOpenOption.CREATE);
//
//        } catch (InvalidPathException e) {
//            System.out.printf("Некорректные символы в пути - %s", e.getMessage());
//        } catch (NumberFormatException e) {
//            System.out.printf("Невозможно преобразовать строку к числу - %s", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.out.printf("Некорректный параметр типа данных или порядка сортировки %s", e.getMessage());
//        } catch (IOException e) {
//            System.out.printf("Произошла ошибка чтения или записи файла - %s", e.getMessage());
//        }
}

