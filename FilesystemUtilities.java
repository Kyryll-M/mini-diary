package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FilesystemUtilities {
    private static int count = 0;

    public static void saveToFile(String filePath, LocalDateTime[] dates, String[] texts, int count, DateTimeFormatter formatter) {
        try {
            FileWriter fw = new FileWriter(filePath);
            for (int i = 0; i < count; i++) {
                fw.write(dates[i].format(formatter) + "\n");
                fw.write(texts[i] + "\n\n");
            }
            fw.close();
            System.out.println("Щоденник збережено.");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл.");
        }
    }

    public static void loadFromFile(String filePath, LocalDateTime[] dates, String[] texts, DateTimeFormatter formatter) {
        count = 0;
        try {
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine() && count < 50) {
                String dateLine = fileScanner.nextLine();
                String textLine = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                if (fileScanner.hasNextLine()) fileScanner.nextLine();
                dates[count] = LocalDateTime.parse(dateLine, formatter);
                texts[count] = textLine;
                count++;
            }
            fileScanner.close();
            System.out.println("Щоденник відновлено.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено.");
        } catch (DateTimeParseException e) {
            System.out.println("Неможливо розпізнати дату у файлі.");
        }
    }

    public static int getCount() {
        return count;
    }
}