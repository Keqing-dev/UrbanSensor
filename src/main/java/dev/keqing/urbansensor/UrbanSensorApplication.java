package dev.keqing.urbansensor;

import dev.keqing.urbansensor.config.GeneralConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GeneralConfig.class})
public class UrbanSensorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanSensorApplication.class, args);
    }

}
