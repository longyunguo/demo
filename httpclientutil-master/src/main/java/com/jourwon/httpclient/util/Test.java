package com.jourwon.httpclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 文字转语音测试 jdk bin文件中需要导入jacob-1.17-M2-x64.dll
 * 
 * @author zk
 * @date: 2019年6月25日 上午10:05:21
 */
public class Test {

	/**
	 * 语音转文字并播放
	 * 
	 * @param txt
	 */
	public static void textToSpeech(String text) {
		ActiveXComponent ax = null;
		try {
			ax = new ActiveXComponent("Sapi.SpVoice");

			// 运行时输出语音内容
			Dispatch spVoice = ax.getObject();
			// 音量 0-100
			ax.setProperty("Volume", new Variant(100));
			// 语音朗读速度 -10 到 +10
			ax.setProperty("Rate", new Variant(-2));
			// 执行朗读
			Dispatch.call(spVoice, "Speak", new Variant(text));

			// 下面是构建文件流把生成语音文件

			ax = new ActiveXComponent("Sapi.SpFileStream");
			Dispatch spFileStream = ax.getObject();

			ax = new ActiveXComponent("Sapi.SpAudioFormat");
			Dispatch spAudioFormat = ax.getObject();

			// 设置音频流格式
			Dispatch.put(spAudioFormat, "Type", new Variant(22));
			// 设置文件输出流格式
			Dispatch.putRef(spFileStream, "Format", spAudioFormat);
			// 调用输出 文件流打开方法，创建一个.wav文件
			Dispatch.call(spFileStream, "Open", new Variant("./text.mp3"), new Variant(3), new Variant(true));
			// 设置声音对象的音频输出流为输出文件对象
			Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
			// 设置音量 0到100
			Dispatch.put(spVoice, "Volume", new Variant(100));
			// 设置朗读速度
			Dispatch.put(spVoice, "Rate", new Variant(-2));
			// 开始朗读
			Dispatch.call(spVoice, "Speak", new Variant(text));

			// 关闭输出文件
			Dispatch.call(spFileStream, "Close");
			Dispatch.putRef(spVoice, "AudioOutputStream", null);

			spAudioFormat.safeRelease();
			spFileStream.safeRelease();
			spVoice.safeRelease();
			ax.safeRelease();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String s = "当月亮和太阳处于地球两侧，并且月亮和太阳的黄经相差180度时，从地球上看，此时的月亮最圆，称之为“满月”，亦称为“望”。农历每月的十四、十五、十六甚至十七，都是满月可能出现的时段。而“超级月亮”指的就是月亮在满月的时候，刚好位于近地点附近。由于靠近地球，所以看上去，此时的月亮比平时更大。“超级月亮”每年都会发生，有时还不止一次。";
		textToSpeech(s);
		File file = new File("result.mp3"); // 打开mp3文件即可播放
		String fileName = "Operator.doc".toString(); // 文件的默认保存名
		// 读到流中
		try {
			InputStream inStream = new FileInputStream("c:/Operator.doc");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 文件的存放路径
	}

}