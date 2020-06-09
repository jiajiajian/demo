package cn.com.tiza.protocol;

/**
 * 指令接口
 */
public interface ICmd {
    /**
     * 获取cmdId
     *
     * @return cmdId
     */
    int getCmdId();

    /**
     * 解析原始报文
     *
     * @param body 报文
     * @return 返回字节数组和状态
     */
    CmdBody parseBody(String body);

}
