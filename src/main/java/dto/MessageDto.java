package dto;

/**
 * Created by Administrator on 2016/8/20.
 */
public class MessageDto {
    private String msg;
    private String status;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MessageDto(String message) {
        this.msg = message;
    }

    public MessageDto(String message, String status) {
        this.msg = message;
        this.status = status;
    }

    public MessageDto(String message, String status, Object data) {
        this.msg = message;
        this.status = status;
        this.data = data;
    }
}
