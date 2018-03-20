package dto.pushDto;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class PushMap<T> implements Serializable {
    private static final long serialVersionUID = -7483057219576037601L;
    private Long uniquelyId;
    private Integer pushType;
    private Integer title;
    private T content;
    
    public PushMap() {
    }
    
    public PushMap(Long uniquelyId, Integer pushType, Integer title, T content) {
        this.uniquelyId = uniquelyId;
        this.pushType = pushType;
        this.title = title;
        this.content = content;
    }
    
    public Long getUniquelyId() {
        return uniquelyId;
    }
    
    public void setUniquelyId(Long uniquelyId) {
        this.uniquelyId = uniquelyId;
    }
    
    public Integer getPushType() {
        return pushType;
    }
    
    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }
    
    public Integer getTitle() {
        return title;
    }
    
    public void setTitle(Integer title) {
        this.title = title;
    }
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}
