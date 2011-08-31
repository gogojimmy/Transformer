package com.encrypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Properties;

public class decrypt {

	public String Execdecrypt() {
		String s = "";
		try {
			Properties pro = new Properties(); // 取得Properties物件
			try {
				// 將資訊包存在a.ini檔案中
				pro.store(new FileOutputStream("Key\\a.ini", true), null);
				// 可以從a.ini檔中透過Properties.get()方法讀取組態資訊
				pro.load(new FileInputStream("Key\\a.ini"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取得a.ini檔案中的register鍵值
			String ctext = String.valueOf(String.valueOf(pro.get("register")));
			System.out.println(ctext);
			// 將該值轉型為BigInteger類型，該資料為加密後的註冊碼
			BigInteger c = new BigInteger(ctext);
			// 取得私密金鑰
			FileInputStream f = new FileInputStream("Key\\RSAPrikey.dat");
			ObjectInputStream b = new ObjectInputStream(f);
			RSAPrivateKey prk = (RSAPrivateKey) b.readObject();
			// 得到公開金鑰的兩個參數
			BigInteger d = prk.getPrivateExponent();
			BigInteger n = prk.getModulus();
			// 解密處理
			BigInteger m = c.modPow(d, n);
			byte[] mt = m.toByteArray();

			for (int i = 0; i < mt.length; i++) {
				s += (char) mt[i];
				// 字串s為解密後的明文
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		System.out.println(new decrypt().Execdecrypt());
	}

}
