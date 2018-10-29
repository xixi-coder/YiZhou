package wechatpay;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import wechatpay.http.HttpClientConnectionManager;


public class GetWxOrderno {

    public static Logger logger = LoggerFactory.getLogger(GetWxOrderno.class);

    public static DefaultHttpClient httpclient;

    static {
        httpclient = new DefaultHttpClient();
        httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
    }

    private GetWxOrderno() {

    }

    private static class GetWxOrdernoHolder {
        static GetWxOrderno instance = new GetWxOrderno();
    }

    public static GetWxOrderno getInstance() {
        return GetWxOrderno.GetWxOrdernoHolder.instance;
    }


    public static Map<String, String> getPayNo(String url, String xmlParam) {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
        try {
            httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
            HttpResponse response = httpclient.execute(httpost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(jsonStr);
            Map map = doXMLParse(jsonStr);
            return map;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws Exception
     */
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        if (null == strxml || "".equals(strxml))
            return null;
        Map data = new HashMap<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        String FEATURE = null;
        try {

            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            documentBuilderFactory.setFeature(FEATURE, true);

            FEATURE = "http://xml.org/sax/features/external-general-entities";
            documentBuilderFactory.setFeature(FEATURE, false);

            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            documentBuilderFactory.setFeature(FEATURE, false);

            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            documentBuilderFactory.setFeature(FEATURE, false);

            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);

        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException was thrown. The feature '" + FEATURE + "' is probably not supported by your XML processor.");
        }
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx=0; idx<nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

}