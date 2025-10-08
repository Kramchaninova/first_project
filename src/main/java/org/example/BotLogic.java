package org.example;

import java.util.HashMap;
import java.util.Map;

/** BotLogic -  класс обрабатывает логику бота
 *
 */

public class BotLogic {

    // Хэшмап для хранения кол-ва сообщений каждого пользователя
    private final Map<Long, Integer> userMessageCount = new HashMap<>();

    /**
     * Обработка сообщения - увеличение счётчика сообщений пользователя,
     * распознавание команды или обычного сообщения
     */

    public String handleMessage(String messageText, long chatId) {
        userMessageCount.put(chatId, userMessageCount.getOrDefault(chatId, 0) + 1);
        if (messageText.startsWith("/")) {
            return handleCommand(messageText, chatId);
        } else {
            return handleTextMessage(messageText, chatId);
        }

    }

    /**
     *Обработка обычных сообщений,
     * еслли в бот был выслан текст, то создается эхо ответ
     */

    public String handleTextMessage(String messageText, long chatId) {
        return "Ваше сообщение: " + messageText + "\n\nдля помощи введите /help";
    }



    /**
     * Если в сообщении была команда, т.е. текст начинается с /,
     * то обрабатываем ее
     *и высылаем текст, который привязан к командам
     */
    public String handleCommand(String command, long chatId) {

        switch (command) {
            case "/start":
                return "Вас приветствует эхо телеграмм бот, созданный Никой и Настей\n\n" +
                        "Он ничего не умеет кроме вывода вашего сообщения и кнопки справки\n" +
                        "Введите /help чтобы телеграмм бот оказал вам бесполезную помощь.";

            case "/help":
                return "  **Список доступных команд:**\n\n" +
                        "'/start' - начать работу с ботом\n" +
                        "'/help' - показать эту справку\n" +
                        "'/stats' - показать кол-во отправленных сообщений\n" +
                        "     **Как взаимодействовать с ботом:**\n" +
                        "Телеграмм бот работает по принципу ввода сообщение:\n" +
                        "- если сообщение начинается не '/' то он просто повторяет\n" +
                        "- если же начинается с '/' то он воспринимает это как команду";

            case "/stats":
                int count = userMessageCount.getOrDefault(chatId, 0);
                return "Вы отправили сообщений: " + count;

            default:
                return "Неизвестная команда. Введите /help для списка доступных команд.";

        }

    }
}
