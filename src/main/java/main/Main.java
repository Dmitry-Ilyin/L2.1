package main;

import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SessionsServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import servlets.UsersServlet;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // создаем accountService. В нем две мапы хранящие логин=юзер и сессия=юзер
        AccountService accountService = new AccountService();
        //добавляем двух Юзеров с admin и test. У них все поля будут так проинициализированы
        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));
        //обработчик сервлета
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //создаются два сервелта UsersServlet и SessionsServlet. Второй параметр это адрес
//        context.addServlet(new ServletHolder(new UsersServlet(accountService)), "/api/v1/users");
//        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/api/v1/sessions");

        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");


        //Обращение к статическим ресурсам. Будет брать статические файлы из директории public_html
        //Если забросить корень нашего сервера, то программа будет отдавать index.html файл
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        //сначала запрос будет обращаться к статике, а потом к сервлетам
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        //кладем все в сервер
        server.setHandler(handlers);

        server.start();
        java.util.logging.Logger.getGlobal().info("Server started");
        server.join();
    }
}
