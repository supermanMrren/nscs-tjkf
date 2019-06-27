package com.boco.nscs.soap;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.WebTestBase;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class INscsPortTest extends WebTestBase {

    @Test
    public void releaseEngineerInfo2() throws Exception {
        //1
        System.out.println("test1");
        releaseEngineerInfo();
        System.out.println("test2");
        releaseEngineerInfo();
    }
    @Test
    public void releaseEngineerInfo() throws Exception {
        String soapStr ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.nscs.boco.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <soap:releaseEngineerInfo>\n" +
                "      <param>{}</param></soap:releaseEngineerInfo>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String surl = base+"/services/Nscs/kf";

        String queryStr ="{" +
                "    \"params\": {" +
                "        \"province\": \"\"," +
                "        \"city\": \"\"," +
                "        \"county\": \"成华区\"," +
                "        \"influence\": \"\"," +
//                "        \"crmpfPubInfo\": {" +
//                "            \"staffId\": \"0001\"," +
//                "            \"orgId\": \"\"," +
//                "            \"cityCode\": \"\"," +
//                "            \"countryCode\": \"\"," +
//                "            \"paging\": \"1\"," +
//                "            \"rowsPerPage\": \"10\"," +
//                "            \"pageNum\": \"1\"" +
//                "        }\n" +
                "    }\n" +
                "}";

        String cmd = StrUtil.format(soapStr,queryStr);
        HttpResponse execute = HttpUtil.createPost(surl)
                //.header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(cmd).execute();
        String body = execute.body();
        System.out.println(body);
        JSONObject obj = JSONUtil.xmlToJson(body);
        //s:Envelope/s:Body
        JSONObject objRoot = obj.getJSONObject("soap:Envelope")
                .getJSONObject("soap:Body")
                .getJSONObject("ns2:releaseEngineerInfoResponse");
        String rtnStr = objRoot.getStr("return");
        System.out.println(rtnStr);
        assertThat(rtnStr).isNotNull();
    }

    @Test
    public void releaseEngineerInfoError() throws Exception {
        String soapStr ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.nscs.boco.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <soap:releaseEngineerInfo>\n" +
                "      <param>{}</param></soap:releaseEngineerInfo>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String surl = base+"/services/Nscs/kf";

        String queryStr ="{";

        String cmd = StrUtil.format(soapStr,queryStr);
        HttpResponse execute = HttpUtil.createPost(surl)
                //.header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(cmd).execute();
        String body = execute.body();
        System.out.println(body);
        JSONObject obj = JSONUtil.xmlToJson(body);
        //s:Envelope/s:Body
        JSONObject objRoot = obj.getJSONObject("soap:Envelope")
                .getJSONObject("soap:Body")
                .getJSONObject("ns2:releaseEngineerInfoResponse");
        String rtnStr = objRoot.getStr("return");
        System.out.println(rtnStr);
        assertThat(rtnStr).isNotNull();
    }

    @Test
    public void testSoap1(){
        String soapStr ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.nscs.boco.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <soap:hello>\n" +
                "           <name>cc</name>\n" +
                "      </soap:hello>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String surl = base+"/services/Nscs/kf";

        HttpResponse execute = HttpUtil.createPost(surl)
                //.header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(soapStr).execute();
        String body = execute.body();

        System.out.println(body);
        JSONObject obj = JSONUtil.xmlToJson(body);
        //s:Envelope/s:Body
        JSONObject objRoot = obj.getJSONObject("soap:Envelope")
                .getJSONObject("soap:Body")
                .getJSONObject("ns2:helloResponse");
        String rtnStr = objRoot.getStr("return");
        System.out.println(rtnStr);
        assertThat(rtnStr).isNotNull();
    }

    @Test
    public void testSoapRateLimit(){
        String soapStr ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.nscs.boco.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <soap:hello>\n" +
                "      <name>cc</name>\n" +
                "      </soap:hello>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String surl = base+"/services/Nscs/kf";

        List<HttpResponse> runlst = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            System.out.println("test" + i);
            ThreadUtil.execute(new Runnable() {
                @Override
                public void run() {
                    HttpResponse execute = HttpUtil.createPost(surl)
                            .header("Content-Type", "text/xml; charset=utf-8")
                            .body(soapStr).execute();

                    System.out.println("test result:"+execute.body());
                }
            });
        }

        ThreadUtil.sleep(10*1000);
    }

    @Test
    public void testSoapClient1(){
        //SoapClient
        String surl = base+"/services/Nscs/kf?wsdl";
        String str = SoapClient.create(surl)
                .setMethod("soap:hello", "http://soap.nscs.boco.com/")
                .setParam("name", "cc",false)
                .send();

        System.out.println(str);
    }

    @Test
    public void parseXmlTest(){
        String str ="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:helloResponse xmlns:ns2=\"http://soap.nscs.boco.com/\"><return>hello cc 2019-05-13 14:18:14</return></ns2:helloResponse></soap:Body></soap:Envelope>";

        Document document = XmlUtil.parseXml(str);
        Element element = XmlUtil.getElementByXPath("//*[local-name()='Envelope']/*[local-name()='Body']/*[local-name()='helloResponse']/return", document);
        System.out.println(element.getTextContent());
    }
}