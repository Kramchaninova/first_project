
package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

 /**
  * Телеграм-бот, реализующий функционал эхо-бота.
  * Обрабатывает текстовые сообщения и команды, возвращая ответы пользователю.
  */

/**
 * Класс, который реализует бота, пишем весь функционал
 *
 */
public class Bot implements LongPollingSingleThreadUpdateConsumer {

    TelegramClient telegramClient;

    public Bot(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }
/**
 * Основный метод, в котором мы обрабатываем входящие сообщения,
 * извлекаем текст, ID, отправляем ответ с обработкой ошибок
 *
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
     *Обработка обычных сообщений, еслли в бот был выслан текст, то создается эхо ответ
     */

    SendMessage handleTextMessage(String messageText, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Ваше сообщение: " + messageText + "\n\nдля помощи введите /help")
                .build();
    }

    /**
     * Если в сообщении была команда, т.е. текст начинается с /, то обрабатываем ее
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