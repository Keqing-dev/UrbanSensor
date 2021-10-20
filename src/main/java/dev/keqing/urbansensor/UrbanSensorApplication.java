package dev.keqing.urbansensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UrbanSensorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanSensorApplication.class, args);
    }

}
