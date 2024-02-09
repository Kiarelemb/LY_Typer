package ly.qr.kiarelemb.text;

import ly.qr.kiarelemb.data.Keys;
import method.qr.kiarelemb.utils.QRStringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-22 14:21
 **/
public class TextWash {

	public static String actualContentFormat(String actualContent, boolean isEnglish) {
		actualContent = actualContent.trim();
		if (!Keys.boolValue(Keys.TEXT_LOAD_INTELLI)) {
			actualContent = QRStringUtils.spaceAndLineSeparatorClear(actualContent);
		} else {
			//如果是智能载文
			if (isEnglish) {
				actualContent = TextWash.lightWashForEnglish(actualContent);
			} else {
				actualContent = TextWash.lightWashForChinese(actualContent);
			}
		}
		return actualContent;
	}

	/**
	 * 中英文都要处理的
	 */
	public static String lightWashBasic(String text) {
		//全角转半角
		text = QRStringUtils.fullWidthToHalf(text);
		String[] enSymbol = {"@", "/", "「", "」", "〔", "〕", "&", "\\^", "　", "\t", "<", ">", "\\[", "]", "\\*", "\\+", "\\^", "\\|"};
		for (String s : enSymbol) {
			text = text.replaceAll(s, "");
		}
		text = text.replaceAll("[\r\n]", "");
		char[] chars = text.toCharArray();
		for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
			char c = chars[i];
			if (c > 126 && c < 161) {
				chars[i] = ' ';
			}
		}
		return new String(chars);
	}

	public static String lightWashForEnglish(String text) {
		text = lightWashBasic(text);
		String[] cnSymbol = {"“", "”", "，", "；", "…+", "！", "：", "。", "？", "（", "）", "’", "—+"};
		String[] enSymbol = {"\"", "\"", ",", ";", "...", "!", ":", ".", "?", "(", ")", "'", "-"};
		for (int i = 0; i < cnSymbol.length; i++) {
			text = text.replaceAll(cnSymbol[i], enSymbol[i]);
		}
		String[] enSymbol2 = {"\\)", "\\?", "\\.", ",", ";", "\\.\\.\\.", "!", ":", "\\\\"};
		for (String s : enSymbol2) {
			text = text.replaceAll(" +" + s, s);
		}
		final String english = QRStringUtils.notEnglishCharClear(text).replaceAll(" +", " ");
		String marks = ",.!:?;)";
		final char[] chars = english.toCharArray();
		StringBuilder markFormat = new StringBuilder(english.length());
		final char space = ' ';
		String nextChars = " \"')1234567890";
		for (int i = 0; i < chars.length - 1; i++) {
			final char c = chars[i];
			markFormat.append(c);
			final char nextChar = chars[i + 1];
			if (marks.contains(String.valueOf(c)) && !nextChars.contains(String.valueOf(nextChar))) {
				if (c == '.' && nextChar == '.') {
					continue;
				}
				markFormat.append(space);
			}
		}
		markFormat.append(chars[chars.length - 1]);
		return markFormat.toString().trim();
	}

	public static String lightWashForChinese(String text) {
		text = text.replaceAll("[a-zA-Z]+?\\.+?[a-zA-Z]*+", "")
				.replaceAll("[a-zA-Z]+?'[a-zA-Z]+?", "");
		text = lightWashBasic(text);
		String[] enSymbol = {"\\)", "\\(", "\\?", "\\.", "\"", "'", ",", ";", "\\.\\.\\.", "!", ":", "\\{", "}", "-", "\\\\", " "};
		String[] cnSymbol = {"）", "（", "？", "。", "", "", "，", "；", "……", "！", "：", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		for (int i = 0; i < enSymbol.length; i++) {
			text = text.replaceAll(enSymbol[i], cnSymbol[i]);
		}
		text = QRStringUtils.notSupportedCharClear(text);
		text = text.replaceAll("（）", "")
				.replaceAll("：“”", "")
				.replaceAll("“”", "")
				.replaceAll("·{3,}", "……")
				.replaceAll("。{2,}", "……")
				.replaceAll("…", "……")
				.replaceAll("…{3,}", "……")
				.replaceAll("”{2,}", "”")
				.replaceAll("“[。，：！？、]”", "");
		StringBuilder sb = new StringBuilder(text);
		while (true) {
			final Matcher matcher = Pattern.compile("[0-9]+?。[0-9]+").matcher(sb.toString());
			if (matcher.find()) {
				int i = matcher.start();
				final int of = sb.indexOf("。", i);
				if (of == -1) {
					break;
				}
				sb.replace(of, of + 1, ".");
				continue;
			}
			break;
		}
		return sb.toString();
	}
}
