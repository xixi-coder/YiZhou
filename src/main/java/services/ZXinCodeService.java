package services;

import base.Constant;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by admin on 2016/11/18.
 */
public class ZXinCodeService {

    public static String createByURL(String url) throws WriterException, IOException {
        String filePath = Constant.properties.getProperty("attachment.dir");
        String fileName = UUID.randomUUID().toString() + ".png";
        String content = url;// 内容
        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = null;// 生成矩阵
        bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);
        Path path = FileSystems.getDefault().getPath(filePath, fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
        return (filePath.substring(filePath.indexOf("attachment"), filePath.length()-1)+"/"+fileName);
    }

    private ZXinCodeService() {
    }

    private static class ZXinCodeServiceHolder {
        static ZXinCodeService instance = new ZXinCodeService();
    }

    public static ZXinCodeService getInstance() {
        return ZXinCodeServiceHolder.instance;
    }
}
