package org.example;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    /**
     * Проверяет обработку обычных сообщений.
     * Тестирует что бот повторяет текст пользователя
     * Проверяет что сообщение отправляется в тот чат
     * Проверка обработки текстового сообщения
     */
    @Test
    void testHandleTextMessage() {
        Bot bot = new Bot("test-token");
        SendMessage result = bot.handleTextMessage("Тестовое сообщение", 12345L);

        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains("Ваше сообщение: Тестовое сообщение"));
        assertTrue(result.getText().contains("/help"));
    }

    /**
     * Проверка команды /start
     * Проверяет приветственное сообщение с описанием бота и создателями.
     * Убеждается что есть ссылка на помощь и корректный ID чата.
     */

    @Test
    void testStartCommand() {
        Bot bot = new Bot("test-token");
        SendMessage result = bot.handleCommand("/start", 12345L);

        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains("Вас приветствует эхо телеграмм бот"));
        assertTrue(result.getText().contains("Никой и Настей"));
    }

    /**
     * Проверка команды /help
     * Проверяет наличие списка команд и описания работы бота.
     */

    @Test
    void testHelpCommand() {
        Bot bot = new Bot("test-token");
        SendMessage result = bot.handleCommand("/help", 12345L);

        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains("Список доступных команд"));
        assertTrue(result.getText().contains("/start"));
    }

    /**
     * Проверка неизвестной команды
     * Проверяет сообщение об ошибке для неподдерживаемых команд.
     * а также, что бот предлагает помощь
     */
  
    @Test
    void testUnknownCommand() {
        Bot bot = new Bot("test-token");
        SendMessage result = bot.handleCommand("/unknown", 12345L);

        assertEquals("12345", result.getChatId());
        assertTrue(result.getText().contains("Неизвестная команда"));
        assertTrue(result.getText().contains("/help"));
    }

}
