package com.util;

import java.util.prefs.Preferences;

public class ReadWriteRegisty {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		System.out.println(ReadWriteRegisty.ReadValue("transformerv1",
				"firstUseTime").equals("�n���s��{�����S���ӭ�"));
	}

	public static String ReadValue(String node, String key) {
		Preferences pre = Preferences.systemRoot().node(node);
		return pre.get(key, "�n���s��{�����S���ӭ�");

	}

	// �妸�g�J�n���s��{���A�ѼƬ��`�I�B��ȡB��ȹ�������
	public static void WriteValue(String node, String[] keys, Object[] values) {
		Preferences pre = Preferences.systemRoot().node(node); // ���oPreferences����
		for (int i = 0; i < keys.length; i++) {
			pre.put(keys[i], String.valueOf(values[i])); // �N��ȻP�������ȼg�J�n���s��{��
		}

	}

	public static void WriteValue(String node, String keys, String values) {
		Preferences pre = Preferences.systemRoot().node(node); // ���oPreferences����
		pre.put(keys, values); // �N��ȻP�������ȼg�J�n���s��{��
	}

}
