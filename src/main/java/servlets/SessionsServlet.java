package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class SessionsServlet extends HttpServlet {

    private final AccountService accountService;
    //инициализация SessionsServlet
    public SessionsServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    // get logged user Profile
    //получить зарегистрированного пользователя
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();  // создаем сессию и получаем Id сессии
        //получить юзера по сессии id
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        //если он был в accountService
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        //если он есть с accountService
        else {
            Gson gson = new Gson(); //json
            String json = gson.toJson(profile); // кладем в него юзера
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(json); // выводим Юзера
            response.setStatus(HttpServletResponse.SC_OK); // все ок
        }
    }

    //sign in (войти)
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        // с запроса получаем значения параметров
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        // если какой то из параметров тull
        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //ошибка 400
            return; // уходим с Post
        }
        // если данные есть
        UserProfile profile = accountService.getUserByLogin(login);
        //если юзер если профиля нет или пароли не совпадают
        if (profile == null || !profile.getPass().equals(pass)) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //ошибка 401
            return; // выходим с Post
        }
        // если все ок
        // добавляем id сессию и профиль
        accountService.addSession(request.getSession().getId(), profile);
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK); // все ок
    }

    //sign out
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Goodbye!");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
