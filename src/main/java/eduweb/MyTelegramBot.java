package eduweb;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.annotation.JSONP;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramWebhookBot {

    private final String botUsername;
    private final String botToken;
    private final String botPath;

    public MyTelegramBot(String botUsername, String botToken, String botPath, DefaultBotOptions botOptions) {
        super(botOptions);

        this.botUsername = botUsername;
        this.botToken = botToken;
        this.botPath = botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            String messageText = "This message from new code. You wrote me :\"" +
                    update.getMessage().getText() + "\". Thank you for your message! " +
                    "I'm waiting for new messages from you!";
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage()
                    .setText(messageText)
                    .setChatId(chatId);

            try {
                execute(message);
            } catch (TelegramApiException ex) {
                return new SendMessage()
                        .setText("Sorry, I can't send message to you because "
                                + ex.getMessage()
                                + ". Please, try again :(")
                        .setChatId(chatId);
            } catch (Exception ex) {
                return new SendMessage()
                        .setText("Sorry, That was terrible error" + ex.getMessage()
                                + "and I can't send message to you"
                                + ". Please, try again :(")
                        .setChatId(chatId);
            }

        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }
}
