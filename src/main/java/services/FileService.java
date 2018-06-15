package services;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileService {
    Logger logger = LoggerFactory.getLogger(FileService.class);

    public void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();// 得到对应的文件通道

            out = fo.getChannel();// 得到对应的文件通道

            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                fi.close();

                in.close();

                fo.close();

                out.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }

    /**
     * c创建目录
     *
     * @param uploadPath
     * @return
     */
    public String mkdir(String uploadPath) {
        DateTime now = DateTime.now();
        String year = now.getYear()+"";
        String month = now.getMonthOfYear()+"";
        String day = now.getDayOfMonth()+"";
        uploadPath += year + File.separator + month + File.separator + day + File.separator;
        File file = new File(uploadPath);
        file.mkdirs();
        return uploadPath;
    }


    private FileService() {

    }

    private static class FileServiceHolder {
        static FileService instance = new FileService();
    }

    public static FileService getInstance() {
        return FileServiceHolder.instance;
    }

    public static void main(String[] args) {
        String a = "http://127.0.0.1:8080/Jfinal/attachment/2016/8/23/97921bcd-07c6-405b-af03-533ba08f1981.jpg";
        System.out.printf(a.substring(a.indexOf("attachment"), a.length()));
    }
}