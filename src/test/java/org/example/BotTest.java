package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Класс BotTest для проверки логики программы
 * Проверяет обработку обычных сообщений.
 * Тестирует что бот повторяет текст пользователя
 * Тестирует команды
 */


class BotTest {

    /** Проверка команды на эхо
     * При входе слова отправляет эхо (то же самое)
     */
    @Test
    void testHandleTextMessage() {
        Bot bot = new Bot("test-token");
        var result = bot.handleTextMessage("Тестовое сообщение", 12345L);

        assertEquals("12345", result.getChatId());
        assertEquals("Ваше сообщение: Тестовое сообщение\n\n" +
                "для помощи введите /help", result.getText());
    }

    /**
     * Проверка команды /start
     * Проверяет приветственное сообщение с описанием бота и создателями.
     * Убеждается что есть ссылка на помощь и корректный ID чата.
     */

    @Test
    void testStartCommand() {
        Bot bot = new Bot("test-token");
        var result = bot.handleCommand("/start", 12345L);

        assertEquals("12345", result.getChatId());
        assertEquals("Вас приветствует эхо телеграмм бот, созданный Никой и Настей\n\n" +
                "Он ничего не умеет кроме вывода вашего сообщения и кнопки справки\n" +
                "Введите /help чтобы телеграмм бот оказал вам бесполезную помощь.", result.getText());
    }

    /**
     * Проверка команды /help
     * Проверяет наличие списка команд и описания работы бота.
     */

    @Test
    void testHelpCommand() {
        Bot bot = new Bot("test-token");
        var result = bot.handleCommand("/help", 12345L);

        assertEquals("12345", result.getChatId());
        assertEquals("  **Список доступных команд:**\n\n" +
                "'/start' - начать работу с ботом\n" +
                "'/help' - показать эту справку\n" +
                "     **Как взаимодействовать с ботом:**\n" +
                "Телеграмм бот работает по принципу ввода сообщение:\n" +
                "- если сообщение начинается не '/' то он просто повторяет\n" +
                "- если же начинается с '/' то он воспринимает это как команду", result.getText());
    }

    /**
     * Проверка неизвестной команды
     * Проверяет сообщение об ошибке для неподдерживаемых команд.
     * а также, что бот предлагает помощь
     */

    @Test
    void testUnknownCommand() {
        Bot bot = new Bot("test-token");
        var result = bot.handleCommand("/unknown", 12345L);

        assertEquals("12345", result.getChatId());
        assertEquals("Неизвестная команда. Введите /help для списка доступных команд.", result.getText());
    }

}
