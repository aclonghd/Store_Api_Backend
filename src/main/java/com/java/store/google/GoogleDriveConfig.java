package com.java.store.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Configuration
public class GoogleDriveConfig {
    @Autowired
    private final GoogleDriveConfigProperties googleDriveConfigProperties;
    public Drive getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT,
                JacksonFactory.getDefaultInstance(), googleCredential())
                .setApplicationName(googleDriveConfigProperties.getApplicationName())
                .build();
    }

    public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
        Collection<String> scopes = new ArrayList<>();
        scopes.add(googleDriveConfigProperties.getScope());
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(googleDriveConfigProperties.getServiceAccountId())
                .setServiceAccountScopes(scopes)
                .setServiceAccountPrivateKeyFromP12File(new File(new ClassPathResource(googleDriveConfigProperties.getPrivateKeyFromP12FileName()).getURI()))
                .build();
    }

    public String getParentFolderIdOfSharedByNormalAccount(){
        return googleDriveConfigProperties.getParentFolderIdOfSharedByNormalAccount();
    }
}
