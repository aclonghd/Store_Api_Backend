package com.java.store.google;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.java.store.dto.FileDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class GoogleDriveManager {
    @Autowired
    private final GoogleDriveConfig googleDriveConfig;

    public List<File> getAllGoogleDriveFiles() throws IOException, GeneralSecurityException {
        FileList result = googleDriveConfig.getService().files().list()
                .setFields("nextPageToken, files(id, name, parents, mimeType)")
                .execute();
        return result.getFiles();
    }

    public void createNewFolder(String folderName) throws IOException, GeneralSecurityException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        List<String> parents = new ArrayList<>();
        parents.add(googleDriveConfig.getParentFolderIdOfSharedByNormalAccount());
        fileMetadata.setParents(parents);
        googleDriveConfig.getService().files().create(fileMetadata).setFields("id").execute();
    }

    public String uploadFile(FileDto file, String folderName, String type, String role){
        try {
            String folderId = getFolderId(folderName);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getFileName());
                File uploadFile = googleDriveConfig.getService()
                        .files()
                        .create(fileMetadata, new InputStreamContent(file.getMimeType(), new ByteArrayInputStream(file.getFileData()))
                        )
                        .setFields("id").execute();

                if (!type.equals("private") && !role.equals("private")){
                    // Call Set Permission drive
                    googleDriveConfig.getService().permissions().create(uploadFile.getId(), setPermission(type, role)).execute();
                }

                return uploadFile.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<File> getAllFolder() throws GeneralSecurityException, IOException {
        String query = "mimeType = 'application/vnd.google-apps.folder' and '"
                + googleDriveConfig.getParentFolderIdOfSharedByNormalAccount() + "' in parents";
        List<File> res = new ArrayList<>();
        String pageToken = null;
        do{
            FileList fileList = googleDriveConfig.getService().files().list()
                    .setQ(query)
                    .setFields("nextPageToken, files(id, name, parents, mimeType)")
                    .setSpaces("drive")
                    .setPageToken(pageToken)
                    .execute();
            res = fileList.getFiles();
            pageToken = fileList.getNextPageToken();
        } while (pageToken != null);
        return res;

    }
    public boolean isExistFolder(String folderName) throws GeneralSecurityException, IOException {
        List<File> fileList = getAllFolder();
        for(File file : fileList){
            if(file.getName().equalsIgnoreCase(folderName)) return true;
        }
        return false;
    }

    public String getFolderId(String folderName) throws GeneralSecurityException, IOException {

        List<File> fileList = getAllFolder();
        for(File file : fileList){
            if(file.getName().equalsIgnoreCase(folderName)) return file.getId();
        }

        return null;
    }

    public void deleteFolder(String folderName) throws GeneralSecurityException, IOException {

        String folderId = getFolderId(folderName);
        googleDriveConfig.getService().files().delete(folderId).execute();

    }

    public Permission setPermission(String type, String role){
        Permission permission = new Permission();
        permission.setType(type);
        permission.setRole(role);
        return permission;
    }

    public void deleteFile(String fileId) throws GeneralSecurityException, IOException{
        googleDriveConfig.getService().files().delete(fileId).execute();
    }
}
