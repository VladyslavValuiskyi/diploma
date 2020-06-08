package com.proarea.api.security.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordMd5Encoder extends Md5PasswordEncoder {

    private String sault1;

    private String sault2;

    public PasswordMd5Encoder() {
        super();
    }

    @Override
    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        log.trace("[PasswordMd5Encoder] [mergePasswordAndSalt]");
        if (password == null) {
            password = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }
        return sault1 + password + sault2;
    }

}
