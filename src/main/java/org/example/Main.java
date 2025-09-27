package org.example;

// Импорты необходимых классов
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;



//позор для тлкена
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




import java.util.ArrayList;
import java.util.List;

//проблема заключается в том что без статика нужно будем сначало создавать объект, я потом уже с ним работать
//а со статиком можно в процессе создавать ...
//дело в том что статик работает со статическими полями и методами

public class Main {
    //у нас код на подобе "потребителя и чегото там" типо как на осях в 1 семе


    // ПЕРЕИМЕНОВАЛИ метод с main на startBot
    public void startBot(String botToken) {

        // создаем и запускаем приложение для long polling (долгих запросов)
        // try-with-resources - это автоматический ресурс который дает доступ, сам дает, сам закрывается, без ручного управления
        // TelegramBotsLongPollingApplication - ресурс, который берет данные с серверов, создавая потоки
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            // создание меню в тг боте, при вводе '/'
            registerBotCommands(botToken);

            // создаем экземпляр нашего бота и передаем ему токен
            Bot bot = new Bot(botToken);

            // регистрируем бота в приложении, те полная сборка произошла, после этой строчки все и начинает рабоать
            botsApplication.registerBot(botToken, bot);

            //отчет на терминал об удачной регистрации
            System.out.println("работает");

            // блокируем основной поток, чтобы приложение не завершилось сразу
            //где Thread.currentThread() - текущий поток при нашем бесконечном
            //а join() - заставляет ждать текущий поток до завершения другого
            Thread.currentThread().join();

        } catch (Exception e) { //(Exception e) - тип исключения
            // типо обработка любых исключений
            e.printStackTrace();
        }
    }

    //отображение команд в меню, так же перед этим мы их сделали (объявили) в фазер боте
    private void registerBotCommands(String botToken) {
        try {
            // создание списка команд
            List<BotCommand> commands = new ArrayList<>();

            //добоавляем команды, пишем без слеша '/' так как  это формально признак команды для фазер бота
            commands.add(new BotCommand("start", "начать работу с ботом"));
            commands.add(new BotCommand("help", "справка по командам"));

            // OkHttpTelegramClient - реализация возвразения данных с итоговым полученным ответом
            OkHttpTelegramClient client = new OkHttpTelegramClient(botToken);

            // SetMyCommands - метод Telegram Bot API для настройки команд бота
            SetMyCommands setCommands = SetMyCommands.builder() //создаем меню
                    .commands(commands)          // передаем наш список команд
                    .scope(new BotCommandScopeDefault()) // область видимости (все чаты)
                    .build(); //финальное создание объекта

            client.execute(setCommands); //отправвка команд на сервер, где escute возвращает булеан

            //снова отчет на терминал
            System.out.println("команды такие есть");

        } catch (Exception e) {
            //вариант ошибки
            System.err.println("не найдены такие команды: " + e.getMessage());

            //e - объект исключения
            //.printStackTrace() - метод "напечатать трассировку стека"
            //результат - подробная информация об ошибке в консоли
            e.printStackTrace();
        }
    }

    // cтатическая точка входа для вирт машины джавы, хз почему так по другому не работает
    public static void main(String[] args) {
        Token tocken = new Token();
        tocken.load();
        String botTocken = tocken.get();
        Main mainInstance = new Main();
        mainInstance.startBot(botTocken); // ВЫЗЫВАЕМ ПЕРЕИМЕНОВАННЫЙ МЕТОД
    }

}

