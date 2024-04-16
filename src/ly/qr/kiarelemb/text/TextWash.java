package ly.qr.kiarelemb.text;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.window.enhance.QROpinionDialog;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
		String[] enSymbol = {"@", "/", "「", "」", "〔", "〕", "&", "\\^", "　", "\t", "<", ">", "\\[", "]", "\\*", "\\+",
				"\\^", "\\|"};
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
		String[] enSymbol = {"\\)", "\\(", "\\?", "\\.", "\"", "'", ",", ";", "\\.\\.\\.", "!", ":", "\\{", "}", "-",
				"\\\\", " "};
		String[] cnSymbol = {"）", "（", "？", "。", "", "", "，", "；", "……", "！", "：", "", "", "", "", "", "", "", "", "",
				"", "", "", "", "", ""};
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


	public static boolean fileCopyAndWash(String originalFilePath, String targetFilePath) {
		return fileCopyAndWash(new File(originalFilePath), new File(targetFilePath));
	}

	public static boolean fileCopyAndWash(File originalFile, File targetFile) {
		if (targetFile.exists()) {
			//noinspection ResultOfMethodCallIgnored
			targetFile.delete();
		}
		QRFileUtils.fileCreate(targetFile);
		final Charset charset = QRFileUtils.getFileCode(originalFile.getAbsolutePath());
		if (charset == null) {
			QROpinionDialog.messageErrShow(MainWindow.INSTANCE, "文件编码识别失败，请手动将文件编码转为UTF-8！");
			return false;
		}
		try {
			RandomAccessFile ra = new RandomAccessFile(originalFile, "r");
			FileOutputStream fos = new FileOutputStream(targetFile, true);
			OutputStreamWriter otw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			long currentLong = 0;
			ra.seek(currentLong);
			final int bl = 8192;
			byte[] b = new byte[bl];
			byte[] b0 = null;
			ra.read(b);
			int endIndex = 0;
			boolean flag = true;
			boolean langCheck = true;
			boolean english = false;
			while (flag) {
				if (b[b.length - 1] == 0) {
					flag = false;
					for (int i = b.length - 1; i >= 0; i--) {
						if (b[i] == 0) {
							endIndex = i;
						} else {
							break;
						}
					}
					b = Arrays.copyOfRange(b, 0, endIndex);
				}
				if (b0 != null) {
					b = QRArrayUtils.arrayContact(b0, b);
					b0 = null;
				}
				String tmp = new String(b, charset);
				if (langCheck) {
					english = QRStringUtils.isEnglish(tmp);
					langCheck = false;
				}
				int index = tmp.length();
				if (flag) {
					//找到结束标志
					int indexes;
					if (!english) {
						final int dou = tmp.lastIndexOf('，');
						final int ju = tmp.lastIndexOf('。');
						index = Math.max(dou, ju);
						if (index == -1) {
							final CharIndexData indexData = getNextCharIndex(b, charset, false);
							if (indexData == null) {
								final String message = "内容格式化错误！";
								QROpinionDialog.messageInfoShow(MainWindow.INSTANCE, message);
								return false;
							}
							indexes = indexData.byteIndex();
							index = indexData.charIndex();
						} else {
							if (dou == index) {
								indexes = QRArrayUtils.byteIndex(b, "，", charset);
							} else {
								indexes = QRArrayUtils.byteIndex(b, "。", charset);
							}
						}
					} else {
						final int space = tmp.lastIndexOf(QRStringUtils.A_WHITE_SPACE_CHAR);
						if (space == -1) {
							final CharIndexData indexData = getNextCharIndex(b, charset, true);
							if (indexData == null) {
								final String message = "内容格式化错误！";
								QROpinionDialog.messageInfoShow(MainWindow.INSTANCE, message);
								return false;
							}
							indexes = indexData.byteIndex();
							index = indexData.charIndex();
						} else {
							indexes = QRArrayUtils.byteIndex(b, QRStringUtils.A_WHITE_SPACE, charset);
						}
					}
					//保存乱码的
					b0 = Arrays.copyOfRange(b, indexes, b.length);
				}
				String text = tmp.substring(0, index);
				String writeText = english ? TextWash.lightWashForEnglish(text) : TextWash.lightWashForChinese(text);
				otw.write(writeText);
				otw.flush();
				if (flag) {
					b = new byte[bl];
					ra.read(b);
				}
			}
			otw.close();
			ra.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static CharIndexData getNextCharIndex(byte[] b, Charset charset, boolean isEnglish) {
		String text = new String(b, charset);
		char[] ch = text.toCharArray();
		int i;
		char c;
		for (i = ch.length - 1; i >= 0; i--) {
			c = ch[i];
			if (isEnglish) {
				if (QRStringUtils.isEnglish(c)) {
					break;
				}
			} else {
				if (QRStringUtils.isWholeSingleChinese(String.valueOf(c))) {
					break;
				}
			}
		}
		if (i > -1) {
			final String chars = text.substring(0, ++i);
			final int length = chars.getBytes(charset).length;
			return new CharIndexData(length, chars.length());
		} else {
			return null;
		}
	}

	record CharIndexData(int byteIndex, int charIndex) {
	}
}