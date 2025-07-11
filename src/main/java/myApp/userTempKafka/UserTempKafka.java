package myApp.userTempKafka;

public class UserTempKafka {
    private String email;
    private String createOrDelete;

    public UserTempKafka(String email, String createOrDelete) {
        this.email = email;
        this.createOrDelete = createOrDelete;
    }
    public UserTempKafka() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateOrDelete() {
        return createOrDelete;
    }

    public void setCreateOrDelete(String createOrDelete) {
        this.createOrDelete = createOrDelete;
    }
}
