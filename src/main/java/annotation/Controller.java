package annotation;

import java.lang.annotation.*;

/**
 * Created by BOGONm on 16/8/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Controller {
    String value();
}
