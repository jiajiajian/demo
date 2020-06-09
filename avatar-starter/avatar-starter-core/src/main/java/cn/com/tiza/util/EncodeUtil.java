/*******************************************************************************
 * Copyright (c) 2016-2017 天泽信息 www.tiza.com.cn
 *
 *******************************************************************************/

package cn.com.tiza.util;

import com.google.common.io.BaseEncoding;
import com.vip.vjtools.vjkit.base.ExceptionUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * @author tz0781
 */
public abstract class EncodeUtil {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	private static final String AES = "AES";

	private static final String CRYPT_KEY = "TIZAtestEVMT1947";

	/**
	 * Hex编码, 将byte[]编码为String，默认为ABCDEF为大写字母.
	 */
	public static String encodeHex(byte[] input) {
		return BaseEncoding.base16().encode(input);
	}

	/**
	 * Hex解码, 将String解码为byte[].
	 * 
	 * 字符串有异常时抛出IllegalArgumentException.
	 */
	public static byte[] decodeHex(CharSequence input) {
		return BaseEncoding.base16().decode(input);
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return BaseEncoding.base64().encode(input);
	}

	/**
	 * Base64解码.
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64(CharSequence input) {
		return BaseEncoding.base64().decode(input);
	}

	public static String decodeBase64Str(CharSequence input) {
		return new String(decodeBase64(input));
	}

	/**
	 * Base64编码, URL安全.(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeBase64UrlSafe(byte[] input) {
		return BaseEncoding.base64Url().encode(input);
	}

	/**
	 * Base64解码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64UrlSafe(CharSequence input) {
		return BaseEncoding.base64Url().decode(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 加密
	 *
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] src, String key) {
		try {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
			// 设置密钥和加密形式
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			return cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String key) {
		try {
			Cipher cipher = Cipher.getInstance(AES);
			// 设置加密Key
			SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
			// 设置密钥和解密形式
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			return cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		try {
			return new String(decrypt(decodeHex(data), CRYPT_KEY));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 */
	public final static String encrypt(String data) {
		try {
			return encodeHex(encrypt(data.getBytes(), CRYPT_KEY));
		} catch (Exception e) {
		}
		return null;
	}

}
