package com.ivan.blog.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @Auther: Ivan
 * @Date: 2020/3/1 14:59
 * @Description:
 */
@Component
public class CommonFileUtil {

    private final Logger logger = LoggerFactory.getLogger(CommonFileUtil.class);

    @Autowired
    private FastFileStorageClient storageClient;


    /**
     *	MultipartFile类型的文件上传ַ
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }

    /**
     * 普通的文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadFile(File file) throws IOException {
        StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(),
                FilenameUtils.getExtension(file.getName()), null);
        return storePath.getFullPath();
    }

    /**
     * 带输入流形式的文件上传
     *
     * @param is
     * @param size
     * @param fileName
     * @return
     */
    public String uploadFileStream(InputStream is, long size, String fileName) {
        StorePath storePath = storageClient.uploadFile(is, size, fileName, null);
        return storePath.getFullPath();
    }

    /**
     * 将一段文本文件写到fastdfs的服务器上
     *
     * @param content
     * @param fileExtension
     * @return
     */
    public String uploadFileAndWrite(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return storePath.getFullPath();
    }

    /**
     * 删除文件
     * @param fileUrl
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        fileUrl = fileUrl.replace("/ivan","/group");
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storePath.setGroup("ivan");
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }

}
