package myApp.utils;

public enum StatusSendKafka {
    CREATE ("Create"),
    DELETE("Delete");

    final String status;

    StatusSendKafka(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
