package kits;


/**
 * Created by BOGONm on 16/8/9.
 */
public class PageKit {
    /**
     * 分页获取开始
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int getStart(int pageIndex, int pageSize) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        return (pageIndex - 1) * pageSize;
    }
}
