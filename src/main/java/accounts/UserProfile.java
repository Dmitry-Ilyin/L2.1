package accounts;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class UserProfile {
    //  у юзера есть логин, пароль, сервер
    private final String login;
    private final String pass;
    private final String email;

    // два конструктора
    //1. по всем ээлементам
    public UserProfile(String login, String pass, String email) {
        this.login = login;
        this.pass = pass;
        this.email = email;
    }
    //2. параметр логин присваивается всем элементам
    public UserProfile(String login) {
        this.login = login;
        this.pass = login;
        this.email = login;
    }
    // геттеры для всех элементов
    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }
}
