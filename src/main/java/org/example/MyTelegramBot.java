package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class MyTelegramBot extends TelegramLongPollingBot {

    // Хранение состояния пользователя
    private final Map<Long, UserState> userStates = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            UserState state = userStates.computeIfAbsent(chatId, UserState::new);

            String response;
            try {
                response = handleUserInput(state, messageText);
            } catch (Exception e) {
                response = "Ошибка: " + e.getMessage();
                userStates.remove(chatId); // Сбрасываем состояние
            }

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(response);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleUserInput(UserState state, String input) {
        // Проверка на команду "/stop"
        if (input.equalsIgnoreCase("/stop")) {
            userStates.remove(state.getChatId()); // Удаляем состояние пользователя
            return "Ввод был прерван. Для начала заново введите команду /integrate.";
        }

        switch (state.getStep()) {
            case 0 -> {
                state.setStep(1);
                return "Введите функцию для интегрирования (например, x0*x0 + x1*x1):\n/stop - чтобы остановиться";
            }
            case 1 -> {
                state.setFunc(input);
                state.setStep(2);
                return "Введите область интегрирования (например, x0*x0 + x1*x1 <= 1):\n/stop - чтобы остановиться";
            }
            case 2 -> {
                state.setRegion(input);
                state.setStep(3);
                return "Введите количество переменных:\n/stop - чтобы остановиться";
            }
            case 3 -> {
                try {
                    int dimensions = Integer.parseInt(input);
                    if (dimensions <= 0) {
                        return "Ошибка: количество переменных должно быть положительным числом.\n/stop - чтобы остановиться";
                    }
                    state.setDimensions(dimensions);
                    state.setStep(4);
                    return "Введите нижнюю границу для переменной x0:\n/stop - чтобы остановиться";
                } catch (NumberFormatException e) {
                    return "Ошибка: введите целое число.\n/stop - чтобы остановиться";
                }
            }
            case 4 -> {
                try {
                    double lowerBound = Double.parseDouble(input);
                    state.addLowerBound(lowerBound);
                    state.setStep(5);
                    return "Введите верхнюю границу для переменной x" + state.getCurrentDimension() + ":\n/stop - чтобы остановиться";
                } catch (NumberFormatException e) {
                    return "Ошибка: введите вещественное число.\n/stop - чтобы остановиться";
                }
            }
            case 5 -> {
                try {
                    double upperBound = Double.parseDouble(input);
                    state.addUpperBound(upperBound);

                    // Проверяем, есть ли еще переменные для ввода границ
                    if (state.getCurrentDimension() < state.getDimensions() - 1) {
                        state.incrementDimension();
                        state.setStep(4); // Снова спрашиваем нижнюю границу для следующей переменной
                        return "Введите нижнюю границу для переменной x" + state.getCurrentDimension() + ":\n/stop - чтобы остановиться";
                    } else {
                        state.setStep(6);
                        return "Введите количество случайных точек:\n/stop - чтобы остановиться";
                    }
                } catch (NumberFormatException e) {
                    return "Ошибка: введите вещественное число.\n/stop - чтобы остановиться";
                }
            }
            case 6 -> {
                try {
                    int N = Integer.parseInt(input);
                    if (N <= 0) {
                        return "Ошибка: количество точек должно быть положительным числом.\n/stop - чтобы остановиться";
                    }
                    state.setN(N);

                    // Вычисляем результат
                    double result = MonteCarloIntegration.MCResult(
                            state.getFunc(),
                            state.getRegion(),
                            state.getBounds(),
                            state.getN()
                    );
                    userStates.remove(state.getChatId()); // Удаляем состояние после завершения
                    return "Результат интегрирования: " + result + "\nВведите /integrate для нового расчета.";
                } catch (NumberFormatException e) {
                    return "Ошибка: введите целое число.\n/stop - чтобы остановиться";
                } catch (Exception e) {
                    return "Ошибка: " + e.getMessage() + "\n/stop - чтобы остановиться";
                }
            }
            default -> {
                userStates.remove(state.getChatId()); // Сбрасываем состояние
                return "Что-то пошло не так. Попробуйте снова.\n/stop - чтобы остановиться";
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
