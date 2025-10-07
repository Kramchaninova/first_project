package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Класс BotTest для проверки логики программы
 * Проверяет обработку обычных сообщений.
 * Тестирует что бот повторяет текст пользователя
 * Тестирует команды
 */


class BotTest {
    private final BotLogic botLogic = new BotLogic();
    /** Проверка команды на эхо
     * При входе слова отправляет эхо (то же самое)
     */
    @Test
    void testHandleTextMessage() {
        String result = botLogic.handleTextMessage("Тестовое сообщение", 12345L);
        assertEquals("Ваше сообщение: Тестовое сообщение\n\n" +
                "для помощи введите /help", result);
    }

    /**
     * Проверка команды /start
     * Проверяет приветственное сообщение с описанием бота и создателями.
     * Убеждается что есть ссылка на помощь и корректный ID чата.
     */

    @Test
    void testStartCommand() {
        String result = botLogic.handleCommand("/start", 12345L);

        assertEquals("Вас приветствует эхо телеграмм бот, созданный Никой и Настей\n\n" +
                "Он ничего не умеет кроме вывода вашего сообщения и кнопки справки\n" +
                "Введите /help чтобы телеграмм бот оказал вам бесполезную помощь.", result);
    }

    /**
     * Проверка команды /help
     * Проверяет наличие списка команд и описания работы бота.
     */

    @Test
    void testHelpCommand() {
        String result = botLogic.handleCommand("/help", 12345L);

        assertEquals("  **Список доступных команд:**\n\n" +
                "'/start' - начать работу с ботом\n" +
                "'/help' - показать эту справку\n" +
                "     **Как взаимодействовать с ботом:**\n" +
                "Телеграмм бот работает по принципу ввода сообщение:\n" +
                "- если сообщение начинается не '/' то он просто повторяет\n" +
                "- если же начинается с '/' то он воспринимает это как команду", result);
    }

    /**
     * Проверка неизвестной команды
     * Проверяет сообщение об ошибке для неподдерживаемых команд.
     * а также, что бот предлагает помощь
     */

    @Test
    void testUnknownCommand() {
        String result = botLogic.handleCommand("/unknown", 12345L);

        assertEquals("Неизвестная команда. Введите /help для списка доступных команд.", result);
    }

}
