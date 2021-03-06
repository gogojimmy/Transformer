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
			// 建立金鑰對產生器����
			KeyPairGenerator KPG = KeyPairGenerator.getInstance("RSA");
			// 初始化該產生器
			KPG.initialize(1024);
			// 產生金鑰對
			KeyPair KP = KPG.genKeyPair();
			// 取得公開金鑰及私密金鑰
			PublicKey pbkey = KP.getPublic();
			PrivateKey prkey = KP.getPrivate();
			// 將公開金鑰寫入檔案
			FileOutputStream filePbkey = new FileOutputStream(
					"Key\\RSAPubkey.dat");
			ObjectOutputStream filePbkeyStream = new ObjectOutputStream(
					filePbkey);
			filePbkeyStream.writeObject(pbkey);
			// 將金鑰寫入檔案
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
