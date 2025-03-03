package fr.codecake.ecom.wire.security.primary;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Configuration // 🔹 Indique que cette classe est une configuration Spring (équivalent à un fichier de config XML).
public class CorsProperties {
  @Bean
  @ConfigurationProperties(prefix = "application.cors", ignoreUnknownFields = false)
  public CorsConfiguration corsConfiguration (){
    return new CorsConfiguration();
  }


}
