package com.dingdong.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dingdong.sys.service.impl.DDMZServiceImpl;

public class HttpUtils {
	private static final Log log = LogFactory.getLog(DDMZServiceImpl.class);

	/**
	 * 向指定 URL 发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendGet(String urlString)
			throws UnsupportedEncodingException {
		String res = "";
		try {
			log.info("the fetch  url is " + urlString);
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
					.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(),
							"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			log.error("error in wapaction,and e is " + e.getMessage());
		}
		// log.info(res);
		return res;
	}

	/**
	 * 发送Post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param list
	 *            请求参数
	 * 
	 * @return 请求结果
	 * 
	 * @throws IOException
	 */
	public static String sendPost(String url, String content) {

		try {
			StringBuffer result = new StringBuffer(); // 用来接受返回值
			URL httpUrl = null; // HTTP URL类 用这个类来创建连接
			URLConnection connection = null; // 创建的http连接

			BufferedReader bufferedReader; // 接受连接受的参数
			// 创建URL
			httpUrl = new URL(url);
			// 建立连接
			connection = httpUrl.openConnection();
			connection
					.setRequestProperty("accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("connection", "keep-alive");
			connection
					.setRequestProperty("user-agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.connect();
			// 获取URLConnection对象对应的输出流

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());

			// 发送请求参数

			// out.write(URLEncoder.encode(params,"UTF-8"));

			out.write(content);

			out.flush();

			out.close();
			// 接受连接返回参数
			bufferedReader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			bufferedReader.close();
			return result.toString();

		} catch (Exception e) {
			log.error(e);
		}

		return null;
	}

	public static void main(String args[]) {
		String accessToken = "NKNm_FZ8Kr_g2OYnbhP4rPtO3IZBq09qL4W5DmlWlXd0fI7-h0-WgOlAV5unox4SY1jyydBQggHKxsR3X3XLffMIQURZYLQSSoypxMY0HQcXYChAAANOS";

		// //查询所有分组
		// String url =
		// "https://api.weixin.qq.com/cgi-bin/groups/get?access_token="
		// + accessToken;
		//
		// try {
		// System.out.println(HttpUtils.sendPost(url, ""));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// 建立公众号菜单
		// String url =
		// "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		//
		// String params =
		// "{\"button\":[{\"type\":\"view\",\"name\":\"叮咚门诊\",\"url\":\"http://www.yushansoft.com/dingdong/v1/ddpatient/index.html?appId=wx5af408bd693d0918\",\"sub_button\":[]},{\"name\":\"关于我们\",\"sub_button\":[{\"type\":\"view\",\"name\":\"公司简介\",\"url\":\"http://www.yushansoft.com/dingdong/company/introduce.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"邀请好友\",\"url\":\"http://www.yushansoft.com/dingdong/company/invite.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"服务条款\",\"url\":\"http://www.yushansoft.com/dingdong/company/terms.html\",\"sub_button\":[]},{\"type\":\"click\",\"name\":\"反馈建议\",\"key\":\"SUGGESTION\",\"sub_button\":[]}]}]}";
		// try {
		// System.out.println(HttpUtils.sendPost(url, params));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// 为医生分组建立单独的菜单
		String customUrl = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="
				+ accessToken;

		String customParams = "{\"button\":[{\"type\":\"view\",\"name\":\"叮咚门诊\",\"url\":\"http://www.yushansoft.com/dingdong/v1/dddoctor/index.html?appId=wx5af408bd693d0918\",\"sub_button\":[]},{\"name\":\"关于我们\",\"sub_button\":[{\"type\":\"view\",\"name\":\"公司简介\",\"url\":\"http://www.yushansoft.com/dingdong/company/introduce.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"邀请好友\",\"url\":\"http://www.yushansoft.com/dingdong/company/invite.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"服务条款\",\"url\":\"http://www.yushansoft.com/dingdong/company/terms.html\",\"sub_button\":[]},{\"type\":\"click\",\"name\":\"反馈建议\",\"key\":\"SUGGESTION\",\"sub_button\":[]}]}],\"matchrule\":{\"group_id\":\"100\"}}";

		try {
			System.out.println(HttpUtils.sendPost(customUrl, customParams));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
