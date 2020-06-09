package cn.com.tiza.service;

import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.dto.NoticeDTO;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tz0920
 */
@Component
public class AlarmContentHelper {
    @Autowired
    private GroupTemplate groupTemplate;

    public void parseEmailContent(NoticeDTO noticeDTO) {
        Template t;
        if (noticeDTO.getAlarmType().equals(AlarmType.ALARM)) {
            t = groupTemplate.getTemplate("/alarmEmail.md");
        } else if (noticeDTO.getAlarmType().equals(AlarmType.FAULT)) {
            t = groupTemplate.getTemplate("/faultEmail.md");
        } else {
            t = groupTemplate.getTemplate("/fenceEmail.md");
        }
        t.binding("vin", noticeDTO.getVin());
        t.binding("beginTime", noticeDTO.getAlarmTime());
        t.binding("content", noticeDTO.getNoticeContent());
        t.binding("spn", noticeDTO.getSpn());
        t.binding("fmi", noticeDTO.getFmi());
        t.binding("tla", noticeDTO.getTla());
        t.binding("address", noticeDTO.getAddress());
        t.binding("alarmType", noticeDTO.getAlarmType());
        t.binding("alarmItemName", noticeDTO.getAlarmItemName());
        String render = t.render();
        noticeDTO.setContent(render);
    }

    public void parseSmsContent(NoticeDTO noticeDTO) {
        Template t;
        if (noticeDTO.getAlarmType().equals(AlarmType.ALARM)) {
            t = groupTemplate.getTemplate("/alarmSms.md");
        } else if (noticeDTO.getAlarmType().equals(AlarmType.FAULT)) {
            t = groupTemplate.getTemplate("/faultSms.md");
        } else {
            t = groupTemplate.getTemplate("/fenceSms.md");
        }
        t.binding("vin", noticeDTO.getVin());
        t.binding("beginTime", noticeDTO.getAlarmTime());
        t.binding("alarmItemName", noticeDTO.getAlarmItemName());
        t.binding("spn", noticeDTO.getSpn());
        t.binding("fmi", noticeDTO.getFmi());
        t.binding("fenceType", noticeDTO.getFenceType());
        String render = t.render();
        noticeDTO.setTemplateParas(render);
    }
}
