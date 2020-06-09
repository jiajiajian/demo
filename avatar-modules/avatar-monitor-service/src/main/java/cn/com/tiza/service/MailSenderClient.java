package cn.com.tiza.service;

import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dto.NoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: TZ0781
 * @create: 2018-10-30
 **/
@Slf4j
@Service
public class MailSenderClient {

    private MailSender mailSender;

    private String from;

    @Autowired
    public MailSenderClient(MailSender mailSender, ApplicationProperties properties) {
        this.mailSender = mailSender;
        this.from = properties.getNotice().getFrom();
    }

    @Async
    public void send(NoticeDTO dto) {
        dto.getReceivers().forEach(to -> sendMail(to, dto.getSubject(), dto.getContent()));
    }

    /**
     * 发送邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    private void sendMail(String to, String subject, String content) {
        log.debug("send mail to {}, subject {}, content {}", to, subject, content);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setText(content);
            message.setSubject(subject);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

}
