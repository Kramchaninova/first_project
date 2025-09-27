package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BotTest {

    private Bot bot;
    private TelegramClient telegramClient;
    private Update update;
    private Message message;
    private Chat chat;

    private final long TEST_CHAT_ID = 12345L;

    @BeforeEach
    void setUp() throws Exception {
        // Создаем мок для TelegramClient
        telegramClient = mock(TelegramClient.class);

        // Создаем реальный экземпляр бота
        bot = new Bot("test-token");

        // Через рефлексию подменяем telegramClient на мок
        Field telegramClientField = Bot.class.getDeclaredField("telegramClient");
        telegramClientField.setAccessible(true);
        telegramClientField.set(bot, telegramClient);

        // Создаем моки для Update, Message и Chat
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);

        // Настраиваем моки
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("test");
        when(message.getChatId()).thenReturn(TEST_CHAT_ID);
        when(message.getChat()).thenReturn(chat);
        when(chat.getId()).thenReturn(TEST_CHAT_ID);
    }
    /*
    @Test
    void testConsumeWithTextMessage() throws TelegramApiException {
        // Подготовка
        String inputText = "Привет, бот!";
        when(message.getText()).thenReturn(inputText);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, times(1)).execute(messageCaptor.capture());
        SendMessage sentMessage = messageCaptor.getValue();

        assertEquals(String.valueOf(TEST_CHAT_ID), sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Ваше сообщение: " + inputText));
        assertTrue(sentMessage.getText().contains("/help"));
    }

    @Test
    void testConsumeWithStartCommand() throws TelegramApiException {//мб убрать
        // Подготовка
        when(message.getText()).thenReturn("/start");

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, times(1)).execute(messageCaptor.capture());
        SendMessage sentMessage = messageCaptor.getValue();

        assertEquals(String.valueOf(TEST_CHAT_ID), sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Вас приветствует эхо телеграмм бот"));
        assertTrue(sentMessage.getText().contains("/help"));
    }

    @Test
    void testConsumeWithHelpCommand() throws TelegramApiException { //мб убрать
        // Подготовка
        when(message.getText()).thenReturn("/help");

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, times(1)).execute(messageCaptor.capture());
        SendMessage sentMessage = messageCaptor.getValue();

        assertEquals(String.valueOf(TEST_CHAT_ID), sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Список доступных команд"));
        assertTrue(sentMessage.getText().contains("/start"));
    }

    @Test //мб убрать
    void testConsumeWithUnknownCommand() throws TelegramApiException {
        // Подготовка
        when(message.getText()).thenReturn("/unknown");

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, times(1)).execute(messageCaptor.capture());
        SendMessage sentMessage = messageCaptor.getValue();

        assertEquals(String.valueOf(TEST_CHAT_ID), sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Неизвестная команда"));
        assertTrue(sentMessage.getText().contains("/help"));
    }

    @Test //мб убрать?
    void testConsumeWithNoMessage() throws TelegramApiException {
        // Подготовка
        when(update.hasMessage()).thenReturn(false);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, never()).execute(any(SendMessage.class));
    }

    @Test //мб убрать?
    void testConsumeWithMessageWithoutText() throws TelegramApiException {
        // Подготовка
        when(message.hasText()).thenReturn(false);

        // Выполнение
        bot.consume(update);

        // Проверка
        verify(telegramClient, never()).execute(any(SendMessage.class));
    }
*/
    @Test
    void testHandleTextMessage() {
        // Подготовка
        String inputText = "Тестовое сообщение";

        // Выполнение
        SendMessage result = bot.handleTextMessage(inputText, TEST_CHAT_ID);

        // Проверка
        assertEquals(String.valueOf(TEST_CHAT_ID), result.getChatId());
        assertTrue(result.getText().contains("Ваше сообщение: " + inputText));
        assertTrue(result.getText().contains("/help"));
    }

    @Test
    void testHandleCommandStart() {
        // Выполнение
        SendMessage result = bot.handleCommand("/start", TEST_CHAT_ID);

        // Проверка
        assertEquals(String.valueOf(TEST_CHAT_ID), result.getChatId());
        assertTrue(result.getText().contains("Вас приветствует эхо телеграмм бот"));
        assertTrue(result.getText().contains("Никой и Настей"));
    }

    @Test
    void testHandleCommandHelp() {
        // Выполнение
        SendMessage result = bot.handleCommand("/help", TEST_CHAT_ID);

        // Проверка
        assertEquals(String.valueOf(TEST_CHAT_ID), result.getChatId());
        assertTrue(result.getText().contains("Список доступных команд"));
        assertTrue(result.getText().contains("/start"));
    }

    @Test
    void testHandleCommandUnknown() {
        // Выполнение
        SendMessage result = bot.handleCommand("/unknown", TEST_CHAT_ID);

        // Проверка
        assertEquals(String.valueOf(TEST_CHAT_ID), result.getChatId());
        assertTrue(result.getText().contains("Неизвестная команда"));
        assertTrue(result.getText().contains("/help"));
    }

    @Test //проверка на api
    void testConsumeWithTelegramApiException() throws TelegramApiException {
        // Подготовка
        when(message.getText()).thenReturn("test");
        doThrow(new TelegramApiException("Ошибка API")).when(telegramClient).execute(any(SendMessage.class));

        // Выполнение & Проверка - не должно бросать исключение
        assertDoesNotThrow(() -> bot.consume(update));

        // Проверка
        verify(telegramClient, times(1)).execute(any(SendMessage.class));
    }
}