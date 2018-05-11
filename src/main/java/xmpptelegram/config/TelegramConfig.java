package xmpptelegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {
    @NotBlank
    private String token;

    @NotBlank
    private String username;

    @NotBlank
    private String path;

    private String cert;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPath() {
        return path;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnnotationConfiguration{");
        sb.append("token='").append(token).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append('}');
        return sb.toString();
    }
}