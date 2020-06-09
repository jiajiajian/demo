package cn.com.tiza.web.app.vm;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class PasswordChangeDTO {

    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
