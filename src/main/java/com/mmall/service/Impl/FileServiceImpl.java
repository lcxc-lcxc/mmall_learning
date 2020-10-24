package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 返回值是上传之后的文件名（因为文件名上传后需要加一大堆后缀）
     * @return
     */
    public String upload(MultipartFile file, String path){
        //得到原始文件名
        String filename = file.getOriginalFilename();
        //获取扩展名 -- jpg
        String fileNameExtensionName = filename.substring(filename.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileNameExtensionName;
        logger.info("开始上传文件，上传的文件的文件名:{},上传的路径:{},新文件名:{}",filename,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()){//如果文件夹不存在，就创建
            fileDir.setWritable(true);
            fileDir.mkdirs();//使fileDir成为文件夹，并且支持多级目录创建，即A/B/C
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件已经上传成功

            //将targetFile上传到我们的ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上

            //上传完之后，删除upload下面的文件
            targetFile.delete();



        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();



    }
}
