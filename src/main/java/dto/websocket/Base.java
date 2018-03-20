package dto.websocket;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/5.
 */
public class Base implements Serializable {
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
