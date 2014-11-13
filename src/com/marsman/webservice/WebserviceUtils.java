package com.marsman.webservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class WebserviceUtils {

	private static final int HTTP_POST = 1;
	private static final int HTTP_GET = 0;

	public Object getLogin(String[] params) {

		return null;
	}

	private final String userAgent = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	private Object getResult(String url, int type,
			List<NameValuePair> nameValuePairs) {

		Object result = null;
		HttpRequestBase httpRequestBase = getHttpRequestBase(url, type,
				nameValuePairs);

		if (null == httpRequestBase) {
			return "数据错误";
		}
		httpRequestBase.setHeader("User-Agent", userAgent);// 向浏览器表明自己的身份
		httpRequestBase.setHeader("Content-Type", "application/json");
		httpRequestBase.setHeader("charset", "utf-8");
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 20000);// 連接超時
		HttpConnectionParams.setSoTimeout(httpParams, 20000);// 請求超時

		HttpClient client = new DefaultHttpClient(httpParams);

		try {
			HttpResponse response = client.execute(httpRequestBase);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private HttpRequestBase getHttpRequestBase(String uri, int type,
			List<NameValuePair> nameValuePairs) {
		switch (type) {
		case HTTP_POST:
			HttpPost post = new HttpPost(uri);

			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
			return post;
		case HTTP_GET:
			HttpGet get = new HttpGet(uri);
			return get;
		default:
			break;
		}

		return null;
	}

}
