package wechatpay;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.*;

/**
 * Created by Administrator on 2017/8/26.
 */
public class WxPayUtil implements WXPayConfig {

    public WxPayUtil(String appId, String mchId, String mchKey, String certPath) throws Exception {
        this.appId = appId;
        this.mchId = mchId;
        this.mchKey = mchKey;
        this.certPath = certPath;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    private byte[] certData;
    private String appId;
    private String mchId;
    private String mchKey;
    private String certPath;

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.mchKey;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
