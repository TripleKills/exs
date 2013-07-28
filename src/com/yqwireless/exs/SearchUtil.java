package com.yqwireless.exs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;






/**

 * 
 * @author wen 
 * @version 1.0
 * @ date: 2012. 12. 16
 */
public class SearchUtil {
	
	public static String search(String company, String code){
		HttpClient client = new DefaultHttpClient(); 
		StringBuffer  urlBuffer = new StringBuffer("http://www.kuaidi100.com/query?type=");
		urlBuffer.append(company);
		urlBuffer.append("&postid=");
		urlBuffer.append(code);
		System.out.println(urlBuffer.toString());
		
		HttpUriRequest request = new HttpGet(urlBuffer.toString());
		try {
			HttpResponse resp = client.execute(request);
			int status_code = resp.getStatusLine().getStatusCode();
			if (status_code == 200) {
				String result = EntityUtils.toString(resp.getEntity());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Internet error");
		}
		return null;
		
	}
}

