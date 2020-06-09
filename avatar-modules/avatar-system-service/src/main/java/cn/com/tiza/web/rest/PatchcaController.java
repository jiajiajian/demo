package cn.com.tiza.web.rest;

import cn.com.tiza.web.util.PatchcaHelper;
import lombok.extern.slf4j.Slf4j;
import org.patchca.filter.predefined.*;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @author tiza
 */
@Slf4j
@Controller
@RequestMapping("auth")
public class PatchcaController {

    @Autowired
    private PatchcaHelper patchcaHelper;

    private static ConfigurableCaptchaService ccs = new ConfigurableCaptchaService();
    private static Random random = new SecureRandom();

    static {
        ccs.setColorFactory(n -> {
            int[] c = new int[3];
            int i = random.nextInt(c.length);
            for (int j = 0; j < c.length; j++) {
                if (j == i) {
                    c[j] = random.nextInt(71);
                } else {
                    c[j] = random.nextInt(256);
                }
            }
            return new Color(c[0], c[1], c[2]);
        });
        RandomWordFactory rwf = new RandomWordFactory();
        rwf.setCharacters("23456789abcdefghgkmnpqrstuvwxyzABCDEFGHGKLMNPQRSTUVWXYZ");
        rwf.setMaxLength(4);
        rwf.setMinLength(4);
        ccs.setWordFactory(rwf);
    }

    @GetMapping("patchca/{username}/{timestamp}")
    public @ResponseBody
    ResponseEntity patchca(@PathVariable String username, @PathVariable String timestamp) {
        switch (random.nextInt(5)) {
            case 0:
                ccs.setFilterFactory(new CurvesRippleFilterFactory(ccs.getColorFactory()));
                break;
            case 1:
                ccs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                ccs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                ccs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                ccs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
            default:
                ccs.setFilterFactory(new CurvesRippleFilterFactory(ccs.getColorFactory()));
                break;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String token = EncoderHelper.getChallangeAndWriteImage(ccs, "png", outputStream);

            //缓存到redis，登录时取出校验
            patchcaHelper.save(username, timestamp, token.toLowerCase());
            log.debug("当前的用户= {} ，验证码= {}", username, token);
            return ResponseEntity.ok(Base64.getEncoder().encode(outputStream.toByteArray()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.badRequest().build();
    }
}
