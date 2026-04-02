package xyz.learnhub.common.types;

import lombok.Data;

/**
 *
 * @create 2023/2/26 18:43
 */
@Data
public class SelectOption<T> {
    private String key;
    private T value;
}
