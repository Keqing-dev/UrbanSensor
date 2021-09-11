package dev.keqing.urbansensor.utils;

import com.fasterxml.jackson.databind.util.StdConverter;
import dev.keqing.urbansensor.config.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvatarURLConverter extends StdConverter<String, String> {

    @Autowired
    private GeneralConfig generalConfig;

    @Override
    public String convert(String value) {
        return generalConfig.getDomainName() + "uploads/avatar/" + value;
    }
}
