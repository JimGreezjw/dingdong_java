package com.dingdong.sys.vo.util;

import java.util.Collection;

/**
 * 集合的公共类,主要检测集合是否为空
 * 
 * @author niukai
 * 
 */
public class DDCollectionUtils {

	public static <T> boolean isEmpty(Collection<T> collection) {
		boolean isEmpty = false;

		if (collection == null) {
			return true;
		}

		if (collection.size() < 1) {
			return true;
		}

		return isEmpty;
	}

	public static <T> boolean isEmpty(T[] array) {
		boolean isEmpty = false;

		if (array == null) {
			return true;
		}

		if (array.length < 1) {
			return true;
		}

		return isEmpty;
	}
}
