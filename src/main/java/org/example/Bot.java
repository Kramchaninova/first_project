
package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс бота, реализующий функционал эхо-бота
 * Обрабатывает текстовые сообщения и команды, возвращая ответы пользователю
 */

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    /**
     * Клиент для взаимодействия с Telegram API
     */
    private final TelegramClient telegramClient;

    private final BotLogic botLogic;

    /**
     * публичный конструктор, который инициализирует телеграм-клиента с переданным токеном
     */
    public Bot(String botToken) {
        this.botLogic = new BotLogic();

        this.telegramClient = new OkHttpTelegramClient(botToken);
        registerBotCommands();
    }


    /**
     * Регистрирует команды бота в меню Telegram
     */
    private void registerBotCommands() {
        try {
            List<BotCommand> commands = new ArrayList<>();
            commands.add(new BotCommand("start", "начать работу с ботом"));
            commands.add(new BotCommand("help", "справка по командам"));
            commands.add(new BotCommand("stats", "счётчик сообщений"));

            SetMyCommands setCommands = SetMyCommands.builder() // Создание меню команд
                    .commands(commands)          // Передача списка команд
                    .scope(new BotCommandScopeDefault()) // Область видимости (все чаты)
                    .build(); // Финальное создание объекта

            //Проверка на отправку запроса
            boolean success = telegramClient.execute(setCommands);

            if (success) {
                System.out.println("Команды бота успешно зарегистрированы");
            } else {
                System.err.println("Не удалось зарегистрировать команды бота");
            }

        } catch (Exception e) {
            System.err.println("Ошибка регистрации команд: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Основный метод, в котором мы обрабатываем входящие сообщения,
     * извлекает текст, ID, отправляет ответ с обработкой ошибок
     */
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String message = botLogic.handleMessage(messageText, chatId);

            sendResponse(chatId, message);
        }
    }

    /**
     * sendResponse - метод отправки сообщения пользователю
     * @param chatId
     * @param text
     */

    private void sendResponse(long chatId, String text) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();


            telegramClient.execute(message);

        } catch (TelegramApiException e) {
            // Обрабатываем ошибки отправки сообщения
            System.err.println("ошибка отправки");
            e.printStackTrace();
        }


    }
}