package dto.websocket;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/7.
 */
public class MessageContent implements Serializable {
    private int title;
    private String message;

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
