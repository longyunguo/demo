package com.jourwon.httpclient.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TtsMain {

	public static void main(String[] args) throws Exception {
		(new TtsMain()).run();
	}

	// 填写申请百度语音申请的appkey
	private final String appKey = "730ogbc0yEK00DGSOTxYd8tk";

	// 填写申请百度语音申请的APP SECRET
	private final String secretKey = "T0z45LzKiL8lBhx1n3HYp5PYoGtqB9qM";

	// text 的内容为"欢迎使用百度语音合成"的urlencode,utf-8 编码
	private final String text = "百度百科是百度公司推出的一部内容开放、自由的网络百科全书。其测试版于2006年4月20日上线，正式版在2008年4月21日发布，截至2019年8月，百度百科已经收录了超1600万词条，参与词条编辑的网友超过680万人，几乎涵盖了所有已知的知识领域。 [1] “世界很复杂，百度更懂你”，百度百科旨在创造一个涵盖各领域知识的中文信息收集平台。百度百科强调用户的参与和奉献精神，充分调动互联网用户的力量，汇聚上亿用户的头脑智慧，积极进行交流和分享。同时，百度百科实现与百度搜索、百度知道的结合，从不同的层次上满足用户对信息的需求。";
	// 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
	private final int per = 0;
	// 语速，取值0-9，默认为5中语速
	private final int spd = 5;
	// 音调，取值0-9，默认为5中语调
	private final int pit = 5;
	// 音量，取值0-9，默认为5中音量
	private final int vol = 5;
	// 可以使用https
	public final String url = "http://tsn.baidu.com/text2audio";
	// 用户唯一标识，用来区分用户，填写机器 MAC 地址或 IMEI 码，长度为60以内
	private String cuid = "1234567JAVA";

	private void run() throws Exception {
		TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
		holder.resfresh();
		String token = holder.getToken();

		String url2 = url + "?tex=" + ConnUtil.urlEncode(text);
		url2 += "&per=" + per;
		url2 += "&spd=" + spd;
		url2 += "&pit=" + pit;
		url2 += "&vol=" + vol;
		url2 += "&cuid=" + cuid;
		url2 += "&tok=" + token;
		url2 += "&lan=zh&ctp=1";
		// 反馈请带上此url，浏览器上可以测试
		// System.out.println(url2);
		HttpURLConnection conn = (HttpURLConnection) new URL(url2).openConnection();
		conn.setConnectTimeout(5000);
		String contentType = conn.getContentType();
		if (contentType.contains("mp3")) {
			byte[] bytes = ConnUtil.getResponseBytes(conn);
			// 打开mp3文件即可播放
			File file = new File("result.mp3");
			// System.out.println( file.getAbsolutePath());
			FileOutputStream os = new FileOutputStream(file);
			os.write(bytes);
			os.close();
			System.out.println("mp3 file write to " + file.getAbsolutePath());
		} else {
			System.err.println("ERROR: content-type= " + contentType);
			String res = ConnUtil.getResponseString(conn);
			System.err.println(res);
		}
	}
}