package frontend;

public interface IServerLoginListener {
    void loginOK(String name);
    void loginFailed(String msg);
}
