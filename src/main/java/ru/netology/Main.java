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
            int maxAmount = 0;
            int textNumber = 0;
            for (int i = 0; i < TEXTS_AMOUNT; i++) {
                int aAmount = 0;
                try {
                    String text = queueA.take();
                    for (int j = 0; j < text.length(); j++) {
                        if (j != text.length() - 1) {
                            if (text.charAt(j) == 'a') {
                                aAmount++;
                                textNumber = i;
                            }
                        }
                    }
                    if (aAmount > maxAmount) {
                        maxAmount = aAmount;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("Максимальное количество символов A (%d) находится в потоке №%d\n", maxAmount, textNumber);
        }).start();

        new Thread(() -> {
            int maxAmount = 0;
            int textNumber = 0;
            for (int i = 0; i < TEXTS_AMOUNT; i++) {
                int bAmount = 0;
                try {
                    String text = queueB.take();
                    for (int j = 0; j < text.length(); j++) {
                        if (j != text.length() - 1) {
                            if (text.charAt(j) == 'b') {
                                bAmount++;
                            }
                        }
                    }
                    if (bAmount > maxAmount) {
                        maxAmount = bAmount;
                        textNumber = i;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("Максимальное количество символов A (%d) находится в потоке №%d\n", maxAmount, textNumber);
        }).start();

        new Thread(() -> {
            int maxAmount = 0;
            int textNumber = 0;
            for (int i = 0; i < TEXTS_AMOUNT; i++) {
                int cAmount = 0;
                try {
                    String text = queueC.take();
                    for (int j = 0; j < text.length(); j++) {
                        if (j != text.length() - 1) {
                            if (text.charAt(j) == 'c') {
                                cAmount++;
                            }
                        }
                    }
                    if (cAmount > maxAmount) {
                        maxAmount = cAmount;
                        textNumber = i;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("Максимальное количество символов A (%d) находится в потоке №%d\n", maxAmount, textNumber);
        }).start();
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