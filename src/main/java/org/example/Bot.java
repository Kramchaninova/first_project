
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

    /**
     * публичный конструктор, который инициализирует телеграм-клиента с переданным токеном
     */
    public Bot(String botToken) {
        //Добавлено сравнение
        //Сравниваем строки на равенство, если не равный значит реальный пользователь
        if ("test-token".equals(botToken)) {
            this.telegramClient = null; //Для тестов
        } else {
            this.telegramClient = new OkHttpTelegramClient(botToken);
            registerBotCommands();
        }
    }


    /**
     * Регистрирует команды бота в меню Telegram
     */
    private void registerBotCommands() {
        try {
            List<BotCommand> commands = new ArrayList<>();
            commands.add(new BotCommand("start", "начать работу с ботом"));
            commands.add(new BotCommand("help", "справка по командам"));

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

            SendMessage message;

            if (messageText.startsWith("/")) {
                message = handleCommand(messageText, chatId);
            } else {
                message = handleTextMessage(messageText, chatId);
            }

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *Обработка обычных сообщений,
     * еслли в бот был выслан текст, то создается эхо ответ
     */

    SendMessage handleTextMessage(String messageText, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Ваше сообщение: " + messageText + "\n\nдля помощи введите /help")
                .build();
    }

    /**
     * Если в сообщении была команда, т.е. текст начинается с /,
     * то обрабатываем ее
     *и высылаем текст, который привязан к командам
     */
    SendMessage handleCommand(String command, long chatId) {
        String responseText;

        switch (command) {
            case "/start":
                responseText = "Вас приветствует эхо телеграмм бот, созданный Никой и Настей\n\n" +
                        "Он ничего не умеет кроме вывода вашего сообщения и кнопки справки\n" +
                        "Введите /help чтобы телеграмм бот оказал вам бесполезную помощь.";
                break;

            case "/help":
                responseText = "  **Список доступных команд:**\n\n" +
                        "'/start' - начать работу с ботом\n" +
                        "'/help' - показать эту справку\n" +
                        "     **Как взаимодействовать с ботом:**\n" +
                        "Телеграмм бот работает по принципу ввода сообщение:\n" +
                        "- если сообщение начинается не '/' то он просто повторяет\n" +
                        "- если же начинается с '/' то он воспринимает это как команду";
                break;

            default:
                responseText = "Неизвестная команда. Введите /help для списка доступных команд.";
                break;
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build();
    }
}