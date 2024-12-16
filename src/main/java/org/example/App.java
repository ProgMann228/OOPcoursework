package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot()); // clearWebhook вызывается внутри
            System.out.println("Бот успешно запущен!");
        } catch (Exception e) {
            e.printStackTrace(); // Логируем ошибки
        }
    }
}