package com.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Properties;

import com.util.RegisterMark;

public class encrypt {
	public BigInteger Execencrypt(String str, String register) {
		BigInteger codeStringBigint = BigInteger.ZERO;
		try {
			File f = new File("Key\\a.ini"); // 產生a.ini檔的file物件
			// System.out.println(!f.exists());
			if (!f.exists()) {
				new genKey(); // 將公開金鑰與私密金鑰寫入DAT類型的檔案中
				// 讀取公開金鑰
				FileInputStream FIS = new FileInputStream("Key\\RSAPubKey.dat");
				ObjectInputStream OIS = new ObjectInputStream(FIS);
				RSAPublicKey RSAPK = (RSAPublicKey) OIS.readObject();
				// 得到兩個公開金鑰的參數
				BigInteger e = RSAPK.getPublicExponent();
				BigInteger n = RSAPK.getModulus();

				System.out.println(str + " " + register);
				byte[] strByte = (str.concat(register)).getBytes("UTF-8");
				System.out.println(strByte);
				BigInteger m = new BigInteger(strByte);
				// 進行加密操作
				codeStringBigint = m.modPow(e, n);
				System.out.println("��敺�撖��綽�" + codeStringBigint);

				// 儲存密文
				Properties pro = new Properties(); // 初始化Properties物件
				// 使用put()方法將鍵與鍵值放入Properties物件中，這裡是將該註冊碼加密後的資訊加入到a.ini檔案中
				pro.put("register", String.valueOf(codeStringBigint));
				try {
					// 將資訊儲存在a.ini檔案中
					pro.store(new FileOutputStream("Key\\a.ini", true), null);
					// 可以從a.ini檔案中透過Properties.get()方法讀取組態資訊
					pro.load(new FileInputStream("Key\\a.ini"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// String codeString =codeStringBigint.toString();
			// BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new
			// FileOutputStream("RSAmi.dat")));
			// out.write(codeString,0,codeString.length());
			// out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeStringBigint; // 將密文傳回
	}

	public static void main(String[] args) {
		new encrypt()
				.Execencrypt(RegisterMark.getMAC(), "11923036320024028839");
		System.out.println(new decrypt().Execdecrypt());
	}
}
