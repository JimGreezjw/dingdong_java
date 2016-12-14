package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dingdong.common.util.Md5Util;

public class Md5Sign {
	// / 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名
	// / </summary>
	// / <param name="key">参数名</param>
	// / <param name="value">参数值</param>
	// / key和value通常用于填充最后一组参数
	// / <returns></returns>
	public static String CreateMd5Sign(Map<String, String> parameters,
			String key, String value) {
		StringBuilder sb = new StringBuilder();

		List<String> akeys = new ArrayList<String>();
		akeys.addAll(parameters.keySet());

		Collections.sort(akeys);

		for (String k : akeys) {
			String v = parameters.get(k);
			if (null != v && "".compareTo(v) != 0 && "sign".compareTo(k) != 0
					&& "key".compareTo(k) != 0) {
				sb.append(k + "=" + v + "&");
			}
		}

		sb.append(key + "=" + value);

		String sign = Md5Util.GetMD5Code(sb.toString()).toUpperCase();

		return sign;
	}
}
