package eduweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/")
public class BotControllerServlet extends HttpServlet {
    private final String botUsername = "my123robot";
    private final String botToken = "";
    private final String botPath = "";

    private final DefaultBotOptions.ProxyType proxyType = DefaultBotOptions.ProxyType.SOCKS5;
    private final String proxyHost = "localhost";
    private final int proxyPort = 9150;

    private MyTelegramBot myTelegramBot;

    @Override
    public void init() throws ServletException {
        ApiContextInitializer.init();

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyType(proxyType);
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);

        TelegramBotsApi botsApi = new TelegramBotsApi();

        myTelegramBot = new MyTelegramBot(botUsername, botToken, botPath, botOptions);

        try {
            botsApi.registerBot(myTelegramBot);
        } catch (Exception e) {
            System.out.println("Some error: " + e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Update update = objectMapper.readValue(CharStreams.toString(req.getReader()), Update.class);
        myTelegramBot.onWebhookUpdateReceived(update);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().append("Get");
    }
}
