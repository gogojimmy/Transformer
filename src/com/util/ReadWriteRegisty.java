package com.util;

import java.util.prefs.Preferences;

public class ReadWriteRegisty {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		System.out.println(ReadWriteRegisty.ReadValue("transformerv1",
				"firstUseTime").equals("登錄編輯程式中沒有該值"));
	}

	public static String ReadValue(String node, String key) {
		Preferences pre = Preferences.systemRoot().node(node);
		return pre.get(key, "登錄編輯程式中沒有該值");

	}

	// 批次寫入登錄編輯程式，參數為節點、鍵值、鍵值對應的值
	public static void WriteValue(String node, String[] keys, Object[] values) {
		Preferences pre = Preferences.systemRoot().node(node); // 取得Preferences物件
		for (int i = 0; i < keys.length; i++) {
			pre.put(keys[i], String.valueOf(values[i])); // 將鍵值與對應的值寫入登錄編輯程式
		}

	}

	public static void WriteValue(String node, String keys, String values) {
		Preferences pre = Preferences.systemRoot().node(node); // 取得Preferences物件
		pre.put(keys, values); // 將鍵值與對應的值寫入登錄編輯程式
	}

}
