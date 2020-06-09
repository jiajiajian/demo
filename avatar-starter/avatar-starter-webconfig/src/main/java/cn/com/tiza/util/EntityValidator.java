package cn.com.tiza.util;

import cn.com.tiza.domain.Entity;
import cn.com.tiza.web.rest.errors.BadRequestException;

import java.util.Objects;
import java.util.Optional;

/**
 * 实体验证类
 *
 * @author tiza
 */
public class EntityValidator {

    /**
     * 验证重复
     * @param obj db中存在的对象
     * @param id 当前id
     * @param errorKey 错误码
     * @param <T> 实体
     */
    public static <T extends Entity> void checkUnique(Optional<T> obj, Long id, String errorKey) {
        obj.ifPresent(o -> {
            if (Objects.isNull(id)) {
                throw new BadRequestException(errorKey);
            }
            if (!Objects.equals(o.getId(), id)) {
                throw new BadRequestException(errorKey);
            }
        });
    }
}
