package com.java.store.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "com.google.api.drive")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleDriveConfigProperties {
    private String scope;
    private String applicationName;
    private String serviceAccountId;
    private String parentFolderIdOfSharedByNormalAccount;
    private String privateKeyFromP12FileName;
    private String privateKeyFromJsonFileName;
}
