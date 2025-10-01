package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;


import java.util.ArrayList;
import java.util.List;

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

            registerBotCommands(botToken);

            Bot bot = new Bot(botToken);

            botsApplication.registerBot(botToken, bot);

            System.out.println("Бот работает");

            Thread.currentThread().join();

        } catch (Exception e) {
            // Обработка исключений, возникающих во время работы бота
            e.printStackTrace();
        }
    }

    /**
     * Регистрирует команды бота в меню Telegram
     */
    private void registerBotCommands(String botToken) {
        try {
            List<BotCommand> commands = new ArrayList<>();

            commands.add(new BotCommand("start", "начать работу с ботом"));
            commands.add(new BotCommand("help", "справка по командам"));

            OkHttpTelegramClient client = new OkHttpTelegramClient(botToken);

            SetMyCommands setCommands = SetMyCommands.builder() // Создание меню команд
                    .commands(commands)          // Передача списка команд
                    .scope(new BotCommandScopeDefault()) // Область видимости (все чаты)
                    .build(); // Финальное создание объекта

            // Отправка команд на сервер Telegram (execute возвращает boolean)
            client.execute(setCommands);

            // Отчет в терминал об успешной регистрации команд
            System.out.println("Команды зарегистрированы");

        } catch (Exception e) {
            // Обработка ошибок при регистрации команд
            System.err.println("Ошибка регистрации команд: " + e.getMessage());

            // e - объект исключения
            // printStackTrace() - метод для вывода подробной информации об ошибке в консоль
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