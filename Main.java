import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    // Метод для поиска максимального количества подряд идущих пар AB или CB
    public static int findMaxPairs(String data) {
        int maxCount = 0;
        int currentCount = 0;

        // Перебор строки для поиска пар
        for (int i = 0; i < data.length() - 1; i++) {
            String pair = data.substring(i, i + 2);
            if (pair.equals("AB") || pair.equals("CB")) {
                currentCount++;
                maxCount = Math.max(maxCount, currentCount);
                i++; // Пропускаем следующий символ, так как пара уже учтена
            } else {
                currentCount = 0; // Сбрасываем счетчик, если пара не найдена
            }
        }

        return maxCount;
    }

    public static void main(String[] args) {
        // Конструкция try с автоматическим освобождением ресурсов
        try (BufferedReader reader = new BufferedReader(new FileReader("fl.txt"));
             ExecutorService executor = Executors.newFixedThreadPool(5)) { // Используем ExecutorService для работы с потоками

            // Список для хранения результатов выполнения потоков
            List<Future<Integer>> futures = new ArrayList<>();
            String line;

            // Чтение файла построчно
            while ((line = reader.readLine()) != null) {
                // Использование многопоточности для обработки строк
                String finalLine = line;
                Future<Integer> future = executor.submit(() -> findMaxPairs(finalLine));
                futures.add(future);
            }

            // Ждем завершения работы всех потоков и находим максимальное значение
            int max = 0;
            for (Future<Integer> future : futures) {
                max = Math.max(max, future.get());
            }

            System.out.println("Максимальное количество подряд идущих пар AB или CB: " + max);

        } catch (IOException | InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
