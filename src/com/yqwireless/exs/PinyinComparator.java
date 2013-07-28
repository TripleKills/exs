package com.yqwireless.exs;

import java.util.Comparator;

public class PinyinComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		 String str1 = PingYinUtil.getPingYin((String) o1).toUpperCase();
	     String str2 = PingYinUtil.getPingYin((String) o2).toUpperCase();
	     return str1.compareTo(str2);
	}
}
