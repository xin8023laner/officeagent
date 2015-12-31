/**
 * 贯通云网数据处理工具类
 */
package com.gt.o2o.util;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.gt.o2o.exception.ParamEmptyException;

/**
 * 贯通云网数据处理工具类
 * 
 * @author uimagine
 */
public class GtDigestUtils {

	// Methods

	/**
	 * 生成数据签名
	 * 
	 * @param appKey
	 *            应用Key
	 * @param appSecret
	 *            应用密钥
	 * @param paramMap
	 *            参数Map
	 * @return digestSign 数据签名
	 * @throws Exception
	 *             数据验证异常
	 */
	public static String createDigestSign(String appKey, String appSecret,
			Map<String, String> paramMap) throws Exception {
		// Data Validation
		if (appKey == null || "".equals(appKey.trim())) {
			throw new ParamEmptyException("appKey could not be empty");
		}
		if (appSecret == null || "".equals(appSecret.trim())) {
			throw new ParamEmptyException("appSecret could not be empty");
		}
		if (paramMap == null || paramMap.isEmpty()) {
			throw new ParamEmptyException("paramMap could not be empty");
		}
		// Generate DigestSign
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(appKey);// appKey
		for (String key : keyArray) {
			stringBuilder.append(key).append(paramMap.get(key));
		}
		stringBuilder.append(appSecret);// appSecret
		String codes = stringBuilder.toString();
		;
		String digestSign = new String(Hex.encodeHex(DigestUtils
				.md5(new String(Hex.encodeHex(DigestUtils.sha(codes))))))
				.toUpperCase();
		return digestSign;
	}

}