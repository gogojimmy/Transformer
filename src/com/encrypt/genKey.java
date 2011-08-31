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
			// «Ø¥ßª÷Æ_¹ï²£¥Í¾¹Ÿå™¨
			KeyPairGenerator KPG = KeyPairGenerator.getInstance("RSA");
			// ªì©l¤Æ¸Ó²£¥Í¾¹
			KPG.initialize(1024);
			// ²£¥Íª÷Æ_¹ï
			KeyPair KP = KPG.genKeyPair();
			// ¨ú±o¤½¶}ª÷Æ_¤Î¨p±Kª÷Æ_
			PublicKey pbkey = KP.getPublic();
			PrivateKey prkey = KP.getPrivate();
			// ±N¤½¶}ª÷Æ_¼g¤JÀÉ®×
			FileOutputStream filePbkey = new FileOutputStream(
					"Key\\RSAPubkey.dat");
			ObjectOutputStream filePbkeyStream = new ObjectOutputStream(
					filePbkey);
			filePbkeyStream.writeObject(pbkey);
			// ±Nª÷Æ_¼g¤JÀÉ®×
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
