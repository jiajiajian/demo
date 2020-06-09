package cn.com.tiza.api;

import cn.com.tiza.dto.*;
import cn.com.tiza.errors.GrampusErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Fallback for hystrix
 * @author tiza
 */
@Component
public class HystrixGrampusApiFallBack implements GrampusApiClient {

    @Override
    public RestResult register(String apiKey, String terminalType, String terminalId) {
        return new RestResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestResult unregister(String apiKey, String terminalType, String terminalId) {
        return new RestResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public CmdSendResult send(String apiKey, TerminalCmd terminalCmd) {
        return new CmdSendResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public CmdSendResult sendTimer(String apiKey, TerminalTimingCmd terminalTimingCmd) {
        return new CmdSendResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public CmdCheckResult cmdChecker(String cmdCheckId, String apiKey) {
        return new CmdCheckResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public CmdSendResult upgradeCmdSend(String apiKey, String terminalId, Map<String, Object> data) {
        return new CmdSendResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestCreateDataQueryResult createDataQuery(String apiKey, QueryCommand command) {
        return new RestCreateDataQueryResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestCreateDataQueryResult createDataQueryRow(String apiKey, QueryCommand command) {
        return new RestCreateDataQueryResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestCreateDataQueryResult createDataQueryTracker(String apiKey, QueryCommand command) {
        return new RestCreateDataQueryResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestDataQueryResult dataQuery(String apiKey, Integer queryId) {
        return new RestDataQueryResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public RestDataQueryResult dataQuery(String apiKey, Integer queryId, Integer row) {
        return new RestDataQueryResult(false, GrampusErrorCode.E9999.getCode());
    }

    @Override
    public void closeDataQuery(String apiKey, Integer queryId) {

    }
}
