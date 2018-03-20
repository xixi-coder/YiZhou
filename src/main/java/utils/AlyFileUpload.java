package utils;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/10/17.
 */
public class AlyFileUpload {
    private Logger logger = LoggerFactory.getLogger(AlyFileUpload.class);
    
    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
    private static String endpoint ; //用华南1(深圳)的节点
    private static String visitPath ; //访问地址
    
    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private static String accessKeyId;
    private static String accessKeySecret;
    
    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private static String bucketName;
    
    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    private static String firstKey = "my-first-key";
    
    private static OSSClient ossClient;
    
    //生成上传凭证，然后准备上传
    static {
        InputStream inputStream = AlyFileUpload.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            Object ak = properties.get("aK");//accessKeyId
            Object sk = properties.get("aKS");//accessKeySecret
            Object bk = properties.get("bucketName");
            Object point = properties.get("endpoint");
            Object path = properties.get("visitPath");
            if (ak == null) {
                throw new RuntimeException("aK在config.properties中不存在");
            }
            if (sk == null) {
                throw new RuntimeException("aKS在config.properties中不存在");
            }
            if (bk == null) {
                throw new RuntimeException("bucketName在config.properties中不存在");
            }
            if (point == null) {
                throw new RuntimeException("point在config.properties中不存在");
            }
            if (path == null) {
                throw new RuntimeException("path在config.properties中不存在");
            }
            endpoint = point.toString();
            visitPath = path.toString();
            accessKeyId = ak.toString();
            accessKeySecret = sk.toString();
            bucketName = bk.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    
    private AlyFileUpload() {
    
    }
    
    private static class AlyFileUploadHolder {
        static AlyFileUpload intance = new AlyFileUpload();
    }
    
    public static AlyFileUpload getIntance() {
        return AlyFileUploadHolder.intance;
    }
    
    
    /**
     * TODO 暂时没有 后期再进行功能更改
     */
    public void exist() {
        if (ossClient.doesBucketExist(bucketName)) {
            System.out.println("您已经创建Bucket：" + bucketName + "。");
        } else {
            System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
            // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            ossClient.createBucket(bucketName);
        }
    }
    
    /**
     * 上传字符串
     *
     * @param str
     * @param sName 名称
     * @return
     */
    public String uploadByStr(String str, String sName) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        PutObjectResult result = ossClient.putObject(bucketName, sName, new ByteArrayInputStream(str.getBytes()));
        // 关闭client
        ossClient.shutdown();
        
        return visitPath + "/" + sName;
    }
    
    /**
     * 上传byte数组
     *
     * @param str
     * @param sName 名称
     * @return
     */
    public String uploadByByte(byte[] str, String sName) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        PutObjectResult result = ossClient.putObject(bucketName, sName, new ByteArrayInputStream(str));
        // 关闭client
        ossClient.shutdown();
        
        return visitPath + "/" + sName;
    }
    
    
 /*   *//**
     * 上传网络流
     *
     * @param inputStream
     * @param sName       名称
     * @return
     *//*
    public String uploadByInputStream(InputStream inputStream, String sName) {
        PutObjectResult result = ossClient.putObject(bucketName, sName, inputStream);
        // 关闭client
        ossClient.shutdown();
        return visitPath + "/" + sName;
    }
    */
    
    /**
     * 上传文件流
     *
     * @param inputStream
     * @param sName       名称
     * @return
     */
    public String uploadByInputStream(InputStream inputStream, String sName) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        PutObjectResult result = ossClient.putObject(bucketName, sName, inputStream);
        // 关闭client
        ossClient.shutdown();
        return visitPath + "/" + sName;
    }
    
    
    /**
     * 上传本地文件
     *
     * @param file
     * @param sName 名称
     * @return
     */
    public String uploadByInputStream(File file, String sName) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, sName, file);
        // 关闭client
        ossClient.shutdown();
        return visitPath + "/" + sName;
    }
    
    //Test
    public static void main(String[] args) {
    
        /*oss :sjdd-oss
        AccessKeyID：
        LTAI8CriWWpbtFrm
        AccessKeySecret：
        uYt2ORHrbZMPVT2cHDiF3AETM4LGNd*/
        
        OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "LTAI8CriWWpbtFrm", "uYt2ORHrbZMPVT2cHDiF3AETM4LGNd");
        
        PutObjectResult result = ossClient.putObject("sjdd-oss", "sjdd.jpg", new File("C:\\Users\\Administrator\\Desktop\\123.jpg"));
        
        System.out.println(result.getETag());
        
        
    }
    
}