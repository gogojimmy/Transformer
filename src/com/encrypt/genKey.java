package com.encrypt;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class genKey {

	public genKey() {
		try {
			// �إߪ��_�ﲣ�;��器
			KeyPairGenerator KPG = KeyPairGenerator.getInstance("RSA");
			// ��l�ƸӲ��;�
			KPG.initialize(1024);
			// ���ͪ��_��
			KeyPair KP = KPG.genKeyPair();
			// ���o���}���_�Ψp�K���_
			PublicKey pbkey = KP.getPublic();
			PrivateKey prkey = KP.getPrivate();
			// �N���}���_�g�J�ɮ�
			FileOutputStream filePbkey = new FileOutputStream(
					"Key\\RSAPubkey.dat");
			ObjectOutputStream filePbkeyStream = new ObjectOutputStream(
					filePbkey);
			filePbkeyStream.writeObject(pbkey);
			// �N���_�g�J�ɮ�
			FileOutputStream filePrkey = new FileOutputStream(
					"Key\\SAPrikey.dat");
			ObjectOutputStream filePrkeyStream = new ObjectOutputStream(
					filePrkey);
			filePrkeyStream.writeObject(prkey);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
