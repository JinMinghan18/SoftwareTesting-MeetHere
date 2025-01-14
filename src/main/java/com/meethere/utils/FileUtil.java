package com.meethere.utils;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.UUID;

public class FileUtil {

    /**
     * 保存上传的文件
     *
     * @param picture
     * @return 文件下载的url
     * @throws Exception
     */
    public static String saveVenueFile(MultipartFile picture) throws Exception {

        if (picture.isEmpty()) {
            return "";
        }
        // 改进：使用UrlDecode解码 by hzw
        String url = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String urlStr = URLDecoder.decode(url, "UTF-8");
        String fileDirPath = urlStr + "/file/venue";
        return "file/venue/" + savePicture(fileDirPath, picture);
    }

    public static String saveUserFile(MultipartFile picture) throws Exception{
        if (picture.isEmpty()) {
            return "";
        }
        // 改进：使用UrlDecode解码 by hzw
        String url = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String urlStr = URLDecoder.decode(url, "UTF-8");
        String fileDirPath = urlStr + "/file/user";
        return "file/user/" + savePicture(fileDirPath, picture);
    }

    public static String savePicture(String filePath, MultipartFile picture) throws Exception{
        File fileDir = new File(filePath);
        System.out.println(fileDir.mkdirs());

        String filename = picture.getOriginalFilename();

        System.out.println(fileDir.getAbsolutePath());
        String suffixName = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID() + suffixName;
        File newFile = new File(fileDir.getAbsolutePath() + File.separator + filename);
        System.out.println(newFile.getAbsolutePath());
        // 上传图片到 -》 “绝对路径”
        picture.transferTo(newFile);
        return filename;
    }

}
