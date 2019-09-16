package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class UploadFile {
    private String name;
    private String MD5;
    private String path;
    private Date uploadDate;

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + name + '\'' +
                ", MD5='" + MD5 + '\'' +
                ", path='"+ path+'\''+
                ", uploadDate=" + uploadDate +
                '}';
    }

    public UploadFile(String name, String MD5,String path, Date uploadDate) {
        this.name = name;
        this.MD5 = MD5;
        this.path = path;
        this.uploadDate = uploadDate;
    }

}
