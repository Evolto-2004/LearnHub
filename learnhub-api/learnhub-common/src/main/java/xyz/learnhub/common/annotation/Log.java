package xyz.learnhub.common.annotation;

import java.lang.annotation.*;
import xyz.learnhub.common.constant.BusinessTypeConstant;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /** 标题 */
    public String title() default "";

    /** 功能 */
    public BusinessTypeConstant businessType() default BusinessTypeConstant.OTHER;
}
