public class User {
    private String username;

    public User(String u) throws Exception {
        setUsername(u);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String u) {
        this.username = u;
    }
}