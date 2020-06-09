package cn.com.tiza.protocol;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Data
public abstract class AbstractCmd implements ICmd {

    protected int cmdId = 0xFF;

    @Override
    public CmdBody parseBody(String body) {
        byte[] bytes = Base64.getDecoder().decode(body);
        CmdBody cmdBody = new CmdBody(getStatus(bytes));
        if (cmdBody.getState() == 0) {
            cmdBody.setSuccessMap(resSuccess(bytes));
        }
        cmdBody.setBody(bytes);
        return cmdBody;
    }

    /**
     * 指令响应成功
     *
     * @param body 响应报文
     * @return 解析报文的结果
     */
    protected abstract Map<String, Object> resSuccess(byte[] body);

    /**
     * 根据报文获取成功失败的状态值
     *
     * @param bytes 报文
     * @return 状态值
     */
    protected abstract int getStatus(byte[] bytes);
}
