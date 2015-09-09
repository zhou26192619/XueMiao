/**
 * 
 */
package cc.xuemiao.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 * <p>
 * 可以进行MD5和SHA1加密计算<br>
 * 可以进行BASE64编码解码
 * </p>
 * 
 * @ClassName:EncryptUtil
 * 
 */
public class EncryptUtil {
	/**
	 * 16进制数值
	 */
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };
	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };

	/**
	 * 使用base64对数据编码
	 * 
	 * @param data
	 * @return
	 */
	public static String base64Encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	/**
	 * 使用base64解码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] base64Decode(String str)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		byte[] data = str.getBytes("US-ASCII");
		int len = data.length;
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
			/* b1 */
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1)
				break;
			/* b2 */
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1)
				break;
			sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
			/* b3 */
			do {
				b3 = data[i++];
				if (b3 == 61)
					return sb.toString().getBytes("iso8859-1");
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1)
				break;
			sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
			/* b4 */
			do {
				b4 = data[i++];
				if (b4 == 61)
					return sb.toString().getBytes("iso8859-1");
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1)
				break;
			sb.append((char) (((b3 & 0x03) << 6) | b4));
		}
		return sb.toString().getBytes("iso8859-1");
	}

	/**
	 * 生成MD5加密校验码
	 * 
	 * @param string
	 *            待加密字符串
	 * @return MD5加密校验码
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String string) throws NoSuchAlgorithmException {
		return encryptString(getEncrypt("MD5"), string);
	}

	/**
	 * 生成SHA1加密校验码
	 * 
	 * @param string
	 *            待加密字符串
	 * @return SHA1加密校验码
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha1(String string) throws NoSuchAlgorithmException {
		return encryptString(getEncrypt("SHA1"), string);
	}

	/**
	 * 获得指定的算法加密器
	 * 
	 * @param algorithm
	 *            算法
	 *             如果没有参数algorithm指定的加密算法则抛出此异常
	 * @return 加密器
	 * @throws NoSuchAlgorithmException
	 */
	private static MessageDigest getEncrypt(String algorithm)
			throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(algorithm);
	}

	/**
	 * 计算结果转为16进制表示
	 * 
	 * @param bytes
	 *            待转换Byte数组
	 * @return 转换结果
	 */
	private static String bytesToHex(byte[] bytes) {
		int length = bytes.length;
		StringBuilder sb = new StringBuilder(2 * length);
		for (int i = 0; i < length; i++) {
			sb.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			sb.append(hexDigits[bytes[i] & 0xf]);
		}
		return sb.toString();
	}

	/**
	 * 使用加密器对目标字符串进行加密
	 * 
	 * @param digest
	 *            加密器
	 * @param string
	 *            目标字符串
	 * @return 计算结果
	 */
	private static String encryptString(MessageDigest digest, String string) {
		return bytesToHex(digest.digest(string.getBytes()));
	}
}
