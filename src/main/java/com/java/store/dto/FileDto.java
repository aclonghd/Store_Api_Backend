package com.java.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private Integer fileSize;
    private String mimeType;
}
