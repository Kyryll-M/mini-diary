import utils.FilesystemUtilities;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DiaryApplication {
    static LocalDateTime[] dates = new LocalDateTime[50];
    static String[] texts = new String[50];
    static int count = 0;
    static DateTimeFormatter inputFormatter;
    static DateTimeFormatter displayFormatter;

    public static void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Оберіть формат відображення дати та часу:");
        System.out.println("1. yyyy-MM-dd HH:mm");
        System.out.println("2. dd/MM/yyyy HH:mm");
        System.out.println("3. Ввести свій формат (наприклад: MMM dd, yyyy HH:mm)");
        System.out.print("Ваш вибір: ");
        String formatChoice = sc.nextLine();
        String formatPattern;

        if (formatChoice.equals("1")) {
            formatPattern = "yyyy-MM-dd HH:mm";
        } else if (formatChoice.equals("2")) {
            formatPattern = "dd/MM/yyyy HH:mm";
        } else {
            System.out.print("Введіть свій формат дати: ");
            formatPattern = sc.nextLine();
        }

        try {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            displayFormatter = DateTimeFormatter.ofPattern(formatPattern);
        } catch (IllegalArgumentException e) {
            displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }

        System.out.print("Відновити існуючий щоденник? (так/ні): ");
        String loadChoice = sc.nextLine();
        if (loadChoice.equalsIgnoreCase("так")) {
            System.out.print("Введіть шлях до файлу: ");
            String filePath = sc.nextLine();
            FilesystemUtilities.loadFromFile(filePath, dates, texts, inputFormatter);
            count = FilesystemUtilities.getCount();
        }

        boolean run = true;
        while (run) {
            System.out.println("\n1. Додати запис");
            System.out.println("2. Видалити запис");
            System.out.println("3. Показати всі записи");
            System.out.println("4. Вийти");
            System.out.print("Ваш вибір: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Введіть дату та час (yyyy-MM-dd HH:mm): ");
                String inputDateTime = sc.nextLine();
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, inputFormatter);
                    System.out.print("Введіть текст запису: ");
                    String text = sc.nextLine();
                    dates[count] = dateTime;
                    texts[count] = text;
                    count++;
                    System.out.println("Запис додано.");
                } catch (DateTimeParseException e) {
                    System.out.println("Невірний формат дати.");
                }
            } else if (choice.equals("2")) {
                System.out.print("Введіть точну дату та час для видалення (yyyy-MM-dd HH:mm): ");
                String d = sc.nextLine();
                boolean found = false;
                for (int i = 0; i < count; i++) {
                    if (dates[i].format(inputFormatter).equals(d)) {
                        for (int j = i; j < count - 1; j++) {
                            dates[j] = dates[j + 1];
                            texts[j] = texts[j + 1];
                        }
                        count--;
                        found = true;
                        System.out.println("Запис видалено.");
                        break;
                    }
                }
                if (!found) System.out.println("Запис не знайдено.");
            } else if (choice.equals("3")) {
                if (count == 0) {
                    System.out.println("Записів немає.");
                } else {
                    for (int i = 0; i < count; i++) {
                        System.out.println("Дата й час: " + dates[i].format(displayFormatter));System.out.println("Запис: " + texts[i]);
                        System.out.println("-----------");
                    }
                }
            } else if (choice.equals("4")) {
                System.out.print("Зберегти щоденник у файл перед виходом? (так/ні): ");
                String saveChoice = sc.nextLine();
                if (saveChoice.equalsIgnoreCase("так")) {
                    System.out.print("Введіть шлях до файлу: ");
                    String filePath = sc.nextLine();
                    FilesystemUtilities.saveToFile(filePath, dates, texts, count, inputFormatter);
                }
                run = false;
            } else {
                System.out.println("Невірний вибір.");
            }
        }

        sc.close();
    }
}