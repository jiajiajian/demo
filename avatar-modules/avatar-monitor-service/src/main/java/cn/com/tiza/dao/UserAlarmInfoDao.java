package cn.com.tiza.dao;

import cn.com.tiza.domain.UserAlarmInfo;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 *
 * @author tiza
 */
public interface UserAlarmInfoDao extends BaseMapper<UserAlarmInfo> {

    /**
     * 查询未读报警消息
     * @param userId 用户id
     * @return 消息数
     */
    default Long countByUserNotRead(Long userId) {
        return createLambdaQuery().andEq(UserAlarmInfo::getUserId, userId)
                .andEq(UserAlarmInfo::getReadFlag, false)
                .count();
    }

    /**
     * 删除报警消息
     * @param userId 用户id
     * @return 消息数
     */
    default int deleteByUser(Long userId) {
        return createLambdaQuery().andEq(UserAlarmInfo::getUserId, userId)
                .delete();
    }

    /**
     * 更新为已读
     *
     * @param userId  用户id
     * @return 息数
     */
    Integer updateMsgRead(@Param("userId") Long userId);

    /**
     * 分页查询消息
     * @param pageQuery params
     */
    void pageQueryMsg(PageQuery<AlarmHistoryVM> pageQuery);
}
