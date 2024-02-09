package ly.qr.kiarelemb.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author Kiarelemb QR
 * @description:
 * @create 2023-04-27 17:57
 **/
public class PhraseUpdate {
	public static String getUrlText(String urls) throws Exception {
		URL url = new URL(urls);
		URLConnection urlcon = url.openConnection();
		urlcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
		InputStreamReader in = new InputStreamReader(urlcon.getInputStream(), StandardCharsets.UTF_8);
		BufferedReader bufferRead = new BufferedReader(in);
		StringBuilder str = new StringBuilder();
		String lineText;
		while ((lineText = bufferRead.readLine()) != null) {
			str.append(lineText);
		}
		return str.toString();
	}

	public static String[] getPhraseText(String phrase) {
		try {
			String urlText = getUrlText("https://hanyu.baidu.com/s?wd=" + phrase + "&device=pc&from=home");
			int textIndex = urlText.indexOf("tab-content");
			int textEndIndex = urlText.indexOf("div", textIndex);
			String mainText = urlText.substring(textIndex, textEndIndex);
			int readerIndex = mainText.indexOf("pinyin-font") + 13;
			int readerEndIndex = mainText.indexOf("<", readerIndex);
			//拼音
			String reader = mainText.substring(readerIndex, readerEndIndex).trim();
			int meanIndex = mainText.indexOf("<p>") + 3;
			int meanEndIndex = mainText.indexOf("</p>", meanIndex);
			//释义
			String mean = mainText.substring(meanIndex, meanEndIndex).trim();
			return new String[]{reader, mean};
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String str = "三言两语";
		String[] phraseText = getPhraseText(str);
		if (phraseText != null) {
			System.out.println(phraseText[0]);
			System.out.println(phraseText[1]);
		}
	}
}
