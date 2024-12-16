package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "YourBotUsername"; // Здесь укажи имя своего бота
    }

    @Override
    public String getBotToken() {
        return "YourBotToken"; // Здесь укажи токен, полученный от BotFather
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Логика обработки сообщений от пользователей
    }

    // Метод для установки Webhook
    public void setWebhook() {
        SetWebhook setWebhook = new SetWebhook();
        setWebhook.setUrl("https://your-heroku-app-name.herokuapp.com/" + getBotUsername());
        try {
            execute(setWebhook); // Отправляем запрос на установку Webhook
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
