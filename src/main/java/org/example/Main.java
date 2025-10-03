package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Главный класс для запуска и настройки Telegram бота
 * Реализует механизм long-polling для получения обновлений
 */
public class Main {

    /**
     * Запускает и настраивает Telegram бота с использованием long-polling
     * Создает экземпляр бота, регистрирует команды и поддерживает работу приложения
     */
    public void startBot(String botToken) {

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            Bot bot = new Bot(botToken);

            botsApplication.registerBot(botToken, bot);

            System.out.println("Бот работает");
            //Создание потоков
            Thread.currentThread().join();

        } catch (Exception e) {
            // Обработка исключений, возникающих во время работы бота
            e.printStackTrace();
        }
    }


    /**
     * Главный метод приложения
     * Загружает токен бота и запускает его
     */
    public static void main(String[] args) {
        Token token = new Token();
        token.load();
        String botToken = token.get();
        Main mainInstance = new Main();
        mainInstance.startBot(botToken);
    }
}