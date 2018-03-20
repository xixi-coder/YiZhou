package annotation;

import java.lang.annotation.*;

/**
 * Created by BOGONm on 16/8/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface TableBind {
    String tableName() default "";//这里默认为Model的名字小写

    String pks() default "id";
}
