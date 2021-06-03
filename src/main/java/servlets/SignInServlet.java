package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    //
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        if(login == null && pass == null){
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserProfile profile = accountService.getUserByLogin(login);
        // если профиля нет или пароли не совпадают
        if (profile == null) { //|| !profile.getPass().equals(pass)
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //ошибка 401
            response.getWriter().println("Status code (401)\nUnauthorized");
            return; // выходим с Post
        }
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("Status code (200)\nAuthorized: " + login);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
