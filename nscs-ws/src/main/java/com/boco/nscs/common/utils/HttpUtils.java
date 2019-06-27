package com.boco.nscs.common.utils;

import com.boco.nscs.core.exception.NscsException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	private HttpUtils() {
	}

	/**
	 * @param httpUrl :请求接口
	 * @param httpArg :参数
	 * @return 返回结果
	 */
	public static String requestGet(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuilder sbf = new StringBuilder();
		try {
			if (httpArg != null && httpArg != "") {
				httpUrl = httpUrl + "?" + httpArg;
			}
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			connection.setReadTimeout(6 * 1000);            //设置超时时间
			connection.setConnectTimeout(6 * 1000);
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			log.warn("get fail", e);
		}
		return result;
	}

	public static String requestPost(String url, String requestString) {
		try {
			// 建立连接
			URL requestUrl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) requestUrl.openConnection();
			//设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			log.debug("当前连接状态：{}", responseCode);
			if (HttpURLConnection.HTTP_OK == responseCode) { // 连接成功
				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				return sb.toString();
			}
		} catch (Exception ex) {
			log.warn("post fail", ex);
		}
		return null;
	}

	public static String requesrHttpPost(String url, String requestString, String soapAction) throws Exception {
		HttpPost request = new HttpPost(url);
		//soap 1.1需要
		request.setHeader("Content-Type", "text/xml; charset=utf-8");
		request.setHeader("SOAPAction", soapAction);
		//添加参数
		HttpEntity re = new StringEntity(requestString, "UTF-8");
		request.setEntity(re);
		return sendRequest(request);
	}

	public static String requestIPTVPost(String url, String requestString) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("jks");
		keyStore.load(new FileInputStream(new File(Configuration.getInstance().getValue("IPTV_name"))), Configuration.getInstance().getValue("IPTV_password").toCharArray());
		SSLContext sslcontext = SSLContexts.custom()
				//忽略掉对服务器端证书的校验
				.loadTrustMaterial(new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				})

				//加载服务端提供的truststore(如果服务器提供truststore的话就不用忽略对服务器端证书的校验了)
				//.loadTrustMaterial(new File("D:\\truststore.jks"), "123456".toCharArray(),
				//        new TrustSelfSignedStrategy())
				.loadKeyMaterial(keyStore, Configuration.getInstance().getValue("IPTV_password").toCharArray())
				.build();
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
				sslcontext,
				new String[]{"TLSv1"},
				null,
				NoopHostnameVerifier.INSTANCE
				);//SSLConnectionSocketFactory.getDefaultHostnameVerifier()
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.build();
		HttpPost request = new HttpPost(url);
		//soap 1.1需要
		request.setHeader("SOAPAction", "");
		request.setHeader("Content-Type", "text/xml; charset=utf-8");
		//添加参数
		HttpEntity re = new StringEntity(requestString, "UTF-8");
		request.setEntity(re);
		ResponseHandler<String> response = new BasicResponseHandler();
		String responseBody = null;
		try {
			responseBody = httpclient.execute(request, response);
			log.debug("调用返回结果: {}",responseBody);
		} catch (IOException e) {
			log.warn("调用HttpRequest失败",e);
		}
		return responseBody;
	}

	private static String sendRequest(HttpEntityEnclosingRequestBase request){
        String connectTimeout = Configuration.getInstance().getValue("connectTimeout");
        String socketTimeout = Configuration.getInstance().getValue("socketTimeout");
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Integer.parseInt(connectTimeout)).setSocketTimeout(Integer.parseInt(socketTimeout)).build();//设置连接超时时间与获取数据的超时时间
		request.setConfig(requestConfig);
		String responseBody = null;
		try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode()!=200){
                log.warn("调用HttpRequest失败,statusCode:{}",response.getStatusLine().getStatusCode());
                throw new NscsException("调用HttpRequest失败");
            }
            if(response.getEntity()!=null){
                responseBody = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
		} catch (IOException e) {
			log.warn("调用HttpRequest失败",e);
			throw new NscsException("调用HttpRequest失败");
		}
		finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  responseBody;
	}

	public static String sendPostToCallCause(String jsonStr) {
		try {
			Client client;
			String requestUrl = "http://10.231.251.18:9090/services/location?wsdl";
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			client = dcf.createClient(requestUrl);
			HTTPConduit http = (HTTPConduit) client.getConduit();
			HTTPClientPolicy httpClientPolicy =  new HTTPClientPolicy();
			int time = 1000 * 60 * 5;
			httpClientPolicy.setConnectionTimeout( time);
			httpClientPolicy.setAllowChunking( false );
			httpClientPolicy.setReceiveTimeout( time );
			http.setClient(httpClientPolicy);
			log.debug("客户端调用参数为:{}" , jsonStr);
			QName name = new QName("http://service.mserver.com/", "getCallCauseData");
			Object[] objects = client.invoke(name,jsonStr);
			log.debug("客户端调用结果为:{}" , objects[0].toString());
			String result = objects[0].toString();
			return result;
		} catch (Exception ex) {
			/*ex.printStackTrace();*/
			log.warn("post fail", ex);
		}
		return null;
	}

	public static String requesrHttpPostOlt(String url,String requestString)throws Exception{
		HttpPost request = new HttpPost(url);
		request.setHeader("Content-Type", "application/json; charset=utf-8");
		//添加参数
		HttpEntity re =new StringEntity(requestString,"UTF-8");
		request.setEntity(re);
		return sendRequest(request);
	}

	//家庭宽带装维服务过程透明化接口
	public static String requestZWServicePost(String url, String requestString) {
		try {
			String XransId = "01";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String dStr = df.format(new Date());
			XransId = XransId + dStr + String.valueOf((int)((Math.random()*9 + 1)*10000000));
			// 建立连接
			URL requestUrl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) requestUrl.openConnection();
			//设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("utf-8");
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("X-Trans-Id", XransId);
			httpConn.setRequestProperty("X-Channel-Id", "01");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept-Charset", "UTF-8");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			log.debug("当前连接状态：{}", responseCode);
			if (HttpURLConnection.HTTP_OK == responseCode) { // 连接成功
				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				return sb.toString();
			}
		} catch (Exception ex) {
			log.warn("post fail", ex);
		}
		return null;
	}

	public static Document strXmlToDocument(String parseStrXml) throws Exception {
		StringReader read = new StringReader(parseStrXml);
		//创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		//创建一个新的SAXBuilder
		SAXReader reader = new SAXReader();   // 新建立构造器
		Document doc = null;
		doc = reader.read(source);
		return doc;
	}
}
