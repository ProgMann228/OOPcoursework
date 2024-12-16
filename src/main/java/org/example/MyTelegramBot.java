package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        // Проверка на наличие сообщения и текста
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            // Ответ на сообщение
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Вы написали: " + messageText);

            try {
                execute(message); // Отправка сообщения
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "SuperIntegrationBot";
    }

    @Override
    public String getBotToken() {
        return "7865223799:AAFCtsF0O2IPHCecHQRvA6VeNUdohbek27g";
    }
}
