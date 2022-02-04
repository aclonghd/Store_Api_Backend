package com.java.store.service;

import com.google.api.services.drive.model.File;
import com.java.store.dto.FileDto;
import com.java.store.google.GoogleDriveManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@AllArgsConstructor
@Service
public class GDriveCloudService {
    @Autowired
    private final GoogleDriveManager googleDriveManager;
    public List<File> getAllGoogleDriveFiles() throws IOException, GeneralSecurityException {
        return googleDriveManager.getAllGoogleDriveFiles();
    }
    public void createNewFolder(String folderName) throws IOException, GeneralSecurityException {
        googleDriveManager.createNewFolder(folderName);
    }

    public String uploadFile(FileDto file, String folderName) {
        return googleDriveManager.uploadFile(file, folderName, "anyone", "reader");
    }
    public boolean isExistFolder(String folderName) throws GeneralSecurityException, IOException {
        return googleDriveManager.isExistFolder(folderName);
    }

    public boolean deleteFile(String fileId){
        try {
            googleDriveManager.deleteFile(fileId);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
