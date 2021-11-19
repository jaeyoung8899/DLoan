package dloan.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtil.class);
	
	// 암호화 size
	final static int AES_KEY_SIZE_128 = 128;
	
	// eco book aes 암호화 key
	final static String AES_KEY = "_REALLYREALLY_AES_KEY_";

	// AES 암호화
	public static String AESEncode(String sText) {
		return AESEncode(sText, AES_KEY);
	}

	// 
	public static String AESEncode(String sText, String sKey) {

		byte[] key = null;
		byte[] text = null;
		byte[] encrypted = null;

		// 2019.05.08 소스코드 보안취약점 조치
		try {
			// UTF-8
			key = sKey.getBytes("UTF-8");
			
			// Key size 맞춤 (128bit, 16byte)
			key = Arrays.copyOf(key, AES_KEY_SIZE_128 / 8);
			
			// UTF-8
			text = sText.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

		// AES/EBC/PKCS5Padding
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
			encrypted = cipher.doFinal(text);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (NoSuchPaddingException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (IllegalBlockSizeException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (BadPaddingException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
		return toHexString(encrypted);
	}

	// AES 복호화
	public static String AESDecode(String sText) {
		return AESDecode(sText, AES_KEY);
	}

	public static String AESDecode(String sText, String sKey) {
		String retText = null;
		byte[] key = null;
		byte[] encrypted = hexToByteArray(sText);
		byte[] decrypted = null;
		try {
			key = sKey.getBytes("UTF-8");

			// Key size 맞춤 (128bit, 16byte)
			key = Arrays.copyOf(key, AES_KEY_SIZE_128 / 8);

			// AES/EBC/PKCS5Padding
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
			decrypted = cipher.doFinal(encrypted);

			retText = new String(decrypted, "UTF-8");
		} catch (Exception e) {
			return retText;
		}
		return retText;
	}

	private static String toHexString(byte[] b) {
		return Hex.encodeHexString(b);
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}
	
	// 단방향 sha-256 암호화
	public static String sha256Encode(String str) {
		String SHA = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			//System.out.println("str : "+str);
			if(str != null){
				sh.update(str.getBytes());
			}
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
			//System.out.println("SHA : "+SHA);
			//KOLAS가 UpperCase해준다.
			SHA = SHA.toUpperCase();
			//System.out.println("SHA : "+SHA);
			
		}catch(NoSuchAlgorithmException e){
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("단방향 암호화 에러");
			SHA = null; 
		}
		return SHA;
	}
}
