package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final int LENGTH = 100_000;
    public static final int TEXTS_AMOUNT = 10_000;

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < TEXTS_AMOUNT; i++) {
                String text = generateText("abc", LENGTH);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            findMaxSymbols(queueA, 'a');
        }).start();

        new Thread(() -> {
            findMaxSymbols(queueB, 'b');
        }).start();

        new Thread(() -> {
            findMaxSymbols(queueC, 'c');
        }).start();
    }

    private static void findMaxSymbols(BlockingQueue<String> queue, char symbol) {
        int maxAmount = 0;
        int textNumber = 0;
        for (int i = 0; i < TEXTS_AMOUNT; i++) {
            int amount = 0;
            try {
                String text = queue.take();
                for (int j = 0; j < text.length(); j++) {
                    if (j != text.length() - 1) {
                        if (text.charAt(j) == symbol) {
                            amount++;
                        }
                    }
                }
                if (amount > maxAmount) {
                    maxAmount = amount;
                    textNumber = i;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("Максимальное количество символов \"%C\" (%d) находится в потоке №%d\n", symbol, maxAmount, textNumber);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}