package com.java.store.service;

import com.google.api.services.drive.model.File;
import com.java.store.google.GoogleDriveManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GDriveCloudService {

    private final GoogleDriveManager googleDriveManager;
    @Autowired
    public GDriveCloudService(GoogleDriveManager googleDriveManager) {
        this.googleDriveManager = googleDriveManager;
    }

    public List<File> getAllGoogleDriveFiles() throws IOException, GeneralSecurityException {
        return googleDriveManager.getAllGoogleDriveFiles();
    }
    public void createNewFolder(String folderName) throws IOException, GeneralSecurityException {
        googleDriveManager.createNewFolder(folderName);
    }

    public String uploadFile(MultipartFile file, String folderName) {
        return googleDriveManager.uploadFile(file, folderName, "anyone", "reader");
    }
    public boolean isExistFolder(String folderName) throws GeneralSecurityException, IOException {
        return googleDriveManager.isExistFolder(folderName);
    }

    public void deleteFile(String fileId){
        try {
            googleDriveManager.deleteFile(fileId);
        } catch (Exception ex){
            Logger.getLogger("GDriveCloudService").log(Level.WARNING, "Delete file not complete");
        }
    }

    public void deleteFolder(String folderName) {
        try {
            googleDriveManager.deleteFolder(folderName);
        } catch (Exception ex){
            Logger.getLogger("GDriveCloudService").log(Level.WARNING, "Delete folder not complete");
        }
    }
}
