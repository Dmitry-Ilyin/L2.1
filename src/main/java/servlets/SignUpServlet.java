package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
//регистрация пользователя
public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    //
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //получаем параметры из запроса
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        // если параметров нет, то бед статус и выход с метода
        if(login == null && pass == null){
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // если с параметрами все ок, о вывод User
        UserProfile user = new UserProfile(login, pass, "@");
        accountService.addNewUser(user);

        Gson gson = new Gson(); //json
        String json = gson.toJson(user); // кладем в него юзера
        response.setContentType("text/html;charset=utf-8");
        UserProfile getNewUser = accountService.getUserByLogin(login);
        response.getWriter().println(getNewUser.getLogin()+":"+getNewUser.getPass()+":"+getNewUser.getEmail());
        response.getWriter().println(json); // выводим Юзера

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
