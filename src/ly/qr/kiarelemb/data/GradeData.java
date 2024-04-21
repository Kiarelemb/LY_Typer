package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import method.qr.kiarelemb.utils.QRMathUtils;

import java.util.ArrayList;
import java.util.Objects;

import static ly.qr.kiarelemb.data.TypingData.*;
import static method.qr.kiarelemb.utils.QRStringUtils.A_WHITE_SPACE;

/**
 * @author Kiarelemb QR
 * @date 2021/9/14 上午7:29
 * @apiNote
 */
public final class GradeData {
	private final double totalTimeInMin;
	private final double speed;
	private final double keyStroke;
	private final double codeLength;
	private final String timeCost;

	private int leftCounts = TypingData.leftCounts;
	private int rightCounts = TypingData.rightCounts;

	/**
	 *
	 */
	public GradeData(double totalTimeInMin, double speed, double keyStroke, double codeLength, String timeCost) {
		this.totalTimeInMin = totalTimeInMin;
		this.speed = speed;
		this.keyStroke = keyStroke;
		this.codeLength = codeLength;
		this.timeCost = timeCost;
		switch (bModel) {
			case 0 -> this.leftCounts += bCounts;
			case 1 -> this.rightCounts += bCounts;
		}
		switch (spaceModel) {
			case 0 -> this.leftCounts += spaceCounts;
			case 1 -> this.rightCounts += spaceCounts;
		}
	}

	//region 基础数据

	/**
	 * @return 速度
	 */
	public String speeds() {
		int size = WRONG_WORDS_INDEX.size();
		String wrongedSpeed = (size > 0 ?
				QRMathUtils.doubleFormat((TextLoad.TEXT_LOAD.wordsLength() - 5 * size) / this.totalTimeInMin) + "/" :
				"") + QRMathUtils.doubleFormat(TextLoad.TEXT_LOAD.wordsLength() / this.totalTimeInMin);
		return wrongedSpeed + (TextLoad.TEXT_LOAD.isEnglish() ?
				(" (" + QRMathUtils.doubleFormat(TextLoad.TEXT_LOAD.englishWordsAverageLength() / this.totalTimeInMin) + "WPM)") : "");
	}

	public String keyStrokes() {
		if (TextLoad.TEXT_LOAD.isEnglish()) {
			return this.keyStroke + " (" + QRMathUtils.doubleFormat(keyCounts / this.totalTimeInMin) + "KPM)";
		} else {
			return String.valueOf(this.keyStroke);
		}
	}

	public double keyMethod() {
		return QRMathUtils.doubleFormat((this.leftCounts + 0.0) / Math.max(this.rightCounts, 1) * 100);
	}

	/**
	 * @return 键法
	 */
	public String keyMethods() {
		String str = keyMethod() + "%(" + "左" + this.leftCounts + ":" + "右" + this.rightCounts;
		if (spaceModel == 2) {
			str += ":空格" + spaceCounts;
		}
		if (bModel == 2) {
			str += ":B" + bCounts;
		}
		return str + ")";
	}

	/**
	 * @return 键准
	 */
	public String keyAccuracy() {
		String keyAccuracy;
		int size = WRONG_WORDS_INDEX.size();
		if (TextLoad.TEXT_LOAD.isEnglish()) {
			keyAccuracy =
					String.valueOf(QRMathUtils.doubleFormat(100.0 * (TextLoad.TEXT_LOAD.wordsLength() - size) / keyCounts));
		} else {
			int wrongWordLose = 0;
			final double avgCodeLength =
					QRMathUtils.doubleFormat((keyCounts + 0.0) / TextLoad.TEXT_LOAD.wordsLength());
			if (size != 0) {
				if (TextLoad.TEXT_LOAD.tipData != null && TextLoad.TEXT_LOAD.tipData.tcsd != null) {
					ArrayList<TipCharStyleData> tcsd = TextLoad.TEXT_LOAD.tipData.tcsd;
					for (int i : WRONG_WORDS_INDEX) {
						wrongWordLose += tcsd.get(i).code().length();
					}
				} else {
					//没有词提就采用平均码长
					wrongWordLose += avgCodeLength * WRONG_WORDS_INDEX.size();
				}
			}
			double ka =
					QRMathUtils.doubleFormat(100.0 * (keyCounts - backSpaceCount * 2 - backDeleteCount * avgCodeLength - wrongWordLose) / keyCounts);
			ka = Math.max(ka, 0.0);
			keyAccuracy = String.valueOf(ka);
		}
		return keyAccuracy + "%";
	}

	public double keyAccuracyNum() {
		String a = keyAccuracy();
		return Double.parseDouble(a.substring(0, a.indexOf("%")));
	}


	/**
	 * 指法
	 */
	private String typeMethod() {
		return " " + leftOrRight(Keys.intValue(Keys.TYPE_KEY_METHOD_SPACE)) + "空格 " + leftOrRight(Keys.intValue(Keys.TYPE_KEY_METHOD_B)) + "B";
	}

	private String leftOrRight(int value) {
		return switch (value) {
			case 0 -> "左手";
			case 1 -> "右手";
			default -> "不统计";
		};
	}

	/**
	 * @return 输入方案
	 */
	public String inputMethod() {
		String value = Keys.strValue(Keys.TYPE_METHOD_INPUT);
		return value == null ? "未设置" : value;
	}

	/**
	 * @return 个签
	 */
	public String personalSignature() {
		String value = Keys.strValue(Keys.TYPE_SIGNATURE);
		return value == null ? "未设置" : value;
	}

	/**
	 * @return 键盘
	 */
	public String keyboard() {
		String value = Keys.strValue(Keys.TYPE_METHOD_KEYBOARD);
		return value == null ? "未设置" : value;
	}

	/**
	 * @return 系统版本
	 */
	public String systemVersion() {
		return Info.SYSTEM_NAME + (Info.SYSTEM_NAME.contains("OS") ? " OS" : "");
	}

	@Override
	public String toString() {
		return "#" + TextLoad.TEXT_LOAD.paragraph() + A_WHITE_SPACE + this.speed + A_WHITE_SPACE + this.keyStroke + A_WHITE_SPACE + this.codeLength + A_WHITE_SPACE + TextLoad.TEXT_LOAD.wordsLength() + A_WHITE_SPACE + backDeleteCount + A_WHITE_SPACE + backSpaceCount + A_WHITE_SPACE + enterCount + A_WHITE_SPACE + WRONG_WORDS_INDEX + A_WHITE_SPACE + keyCounts + A_WHITE_SPACE + keyAccuracyNum() + A_WHITE_SPACE + keyMethod() + A_WHITE_SPACE + this.timeCost + A_WHITE_SPACE + TextLoad.TEXT_LOAD.textMD5Short();
	}

	//endregion

	public String getSimplifiedGrade() {
		return "第" + TextLoad.TEXT_LOAD.paragraph() + "段" + " 速度" + speeds() + " 击键" + keyStrokes() + " 码长" + codeLength() + (Keys.boolValue(Keys.SEND_KEY_NUM) ? " 键数" + keyCounts : "") + (Keys.boolValue(Keys.SEND_TIME_COST) ? " 用时" + timeCost() : "") + (Keys.boolValue(Keys.SEND_METHOD_KEY) ? " 键法" + keyMethods() : "");
	}

	/**
	 * 取得根据设置而得到的成绩
	 */
	public String getSetGrade() {
		String grade;
		boolean simplify = Keys.boolValue(Keys.SEND_MINIMALISM);
		if (simplify) {
			return "No." + TextLoad.TEXT_LOAD.paragraph() + " -> " + this.speed + " | " + this.keyStroke + " | " + this.codeLength;
		} else {
			grade =
					"第" + TextLoad.TEXT_LOAD.paragraph() + "段" + " 速度" + speeds() + " 击键" + keyStrokes() + " 码长" + codeLength() +
							(!TextLoad.TEXT_LOAD.isEnglish() && TextTip.TEXT_TIP.loaded() && tipEnable ?
									" 标顶" + ContractiblePanel.STANDARD_LEN_LABEL.getText() : "") + (Keys.boolValue(Keys.SEND_WORDS_NUM) ? TextLoad.TEXT_LOAD.isEnglish() ? " 词数" + TextLoad.TEXT_LOAD.englishWordsNum() : " 字数" + TextLoad.TEXT_LOAD.wordsLength() : "") + (Keys.boolValue(Keys.SEND_BACK_CHANGE) ? " 回改" + backDeleteCount : "") + (Keys.boolValue(Keys.SEND_BACKSPACE) ? " 退格" + backSpaceCount : "") + (Keys.boolValue(Keys.SEND_ENTER_COUNT) ? " 回车" + enterCount : "") + (Keys.boolValue(Keys.SEND_KEY_NUM) ? " 键数" + keyCounts : "") + (Keys.boolValue(Keys.SEND_WORD_WRONG) ? " 错字" + WRONG_WORDS_INDEX.size() + getWrongWords() : "") + (Keys.boolValue(Keys.SEND_KEY_ACCURACY) ? " 键准" + keyAccuracy() : "") + (Keys.boolValue(Keys.SEND_TIMES_PAUSE) ? " 暂停" + pausedTimes + "次" : "") + (Keys.boolValue(Keys.SEND_TIMES_RETYPE) ? " 重打" + TextLoad.TEXT_LOAD.reTypeTimes() + "次" : "") + (Keys.boolValue(Keys.SEND_TIME_COST) ? " 用时" + timeCost() : "") + (Keys.boolValue(Keys.SEND_KEY_METHOD) ? " 键法" + keyMethods() : "") + (Keys.boolValue(Keys.SEND_METHOD_TYPE) ? " 指法" + typeMethod() : "")
//			        + ((TaipinnguDeeta.highPerformModel || !BetterTyping.loaded || !tsd.tipEnable() || TextLoad
//			        .TEXT_LOAD.isSingleOnly()) && (!OtherSetting.osd.tipWindowShow() || QRTextPane.tw == null ||
//			        !QRTextPane.tw.isVisible()) && !TextLoad.TEXT_LOAD.isEnglish() ? "禁词提" : "")
							+ (Keys.boolValue(Keys.SEND_METHOD_INPUT) ? " 输入法:" + inputMethod() : "") + (Keys.boolValue(Keys.SEND_SIGNATURE) ? " 个签:" + personalSignature() : "") + (Keys.boolValue(Keys.SEND_KEYBOARD) ? " 键盘:" + keyboard() : "") + " 正文:" + TextLoad.TEXT_LOAD.textMD5Short() + " 揽月" + Info.SOFTWARE_VERSION + (Keys.boolValue(Keys.SEND_SYSTEM_VERSION) ? A_WHITE_SPACE + systemVersion() : "");
		}
//                (ssd.isSimplify() && tsd.title().isEmpty() ? "" : (ssd.title() ? " 称号：" + title() : ""));
		return getGradeContainsCode(grade);
	}

	/**
	 * 取得完全的成绩
	 */
	public String getFullGrade() {
		String grade =
				"第" + TextLoad.TEXT_LOAD.paragraph() + "段" + " 速度" + speeds() + " 击键" + keyStrokes() + " 码长" + codeLength() + " 字数" + TextLoad.TEXT_LOAD.wordsLength() + " 回改" + backDeleteCount + " 退格" + backSpaceCount + " 回车" + enterCount + " 键数" + keyCounts + " 错字" + WRONG_WORDS_INDEX.size() + getWrongWords() + " 键准" + keyAccuracy() + " 用时" + timeCost() + " 暂停" + pausedTimes + "次" + " 重打" + TextLoad.TEXT_LOAD.reTypeTimes() + "次" + " 键法" + keyMethods() + " 输入法:" + inputMethod() + " 个签:" + personalSignature() + " 正文:" + TextLoad.TEXT_LOAD.textMD5Short() + " 揽月" + Info.SOFTWARE_VERSION + A_WHITE_SPACE + systemVersion();
		return getGradeContainsCode(grade);
	}

	/**
	 * 根据成绩，取得写入的成绩
	 */
	private String getWriteGrade() {
		String grade =
				"第" + TextLoad.TEXT_LOAD.paragraph() + "段" + " 速度" + speeds() + " 击键" + keyStrokes() + " 码长" + codeLength() + " 字数" + TextLoad.TEXT_LOAD.wordsLength() + " 回改" + backDeleteCount + " 退格" + backSpaceCount + " 回车" + enterCount + " 键数" + keyCounts + " 错字" + WRONG_WORDS_INDEX.size() + getWrongWords() + " 键准" + keyAccuracy() + " 用时" + timeCost() + " 暂停" + pausedTimes + "次" + " 键法" + keyMethods() + " 输入法:" + inputMethod() + " 个签:" + personalSignature() + " 正文:" + TextLoad.TEXT_LOAD.textMD5Short() + " 揽月" + Info.SOFTWARE_VERSION + A_WHITE_SPACE + systemVersion();
		return getGradeContainsCode(grade);
	}

	private String getGradeContainsCode(String grade) {
		final int lastLine = grade.lastIndexOf("\n") + 1;
		if (lastLine != 0) {
			grade = grade.substring(0, lastLine) + grade.substring(lastLine).trim();
		}
		return grade;
	}

	private String getWrongWords() {
		if (WRONG_WORDS_INDEX.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder(WRONG_WORDS_INDEX.size());
		for (Integer i : WRONG_WORDS_INDEX) {
			sb.append(TextLoad.TEXT_LOAD.getTextAtIndex(i)).append(", ");
		}
		return "[" + sb.substring(0, sb.length() - 2) + "]";
	}

	public double totalTimeInMin() {
		return this.totalTimeInMin;
	}

	public double speed() {
		return this.speed;
	}

	public double keyStroke() {
		return this.keyStroke;
	}

	public double codeLength() {
		return this.codeLength;
	}

	public String timeCost() {
		return this.timeCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (GradeData) obj;
		return Double.doubleToLongBits(this.totalTimeInMin) == Double.doubleToLongBits(that.totalTimeInMin) &&
				Double.doubleToLongBits(this.speed) == Double.doubleToLongBits(that.speed) &&
				Double.doubleToLongBits(this.keyStroke) == Double.doubleToLongBits(that.keyStroke) &&
				Double.doubleToLongBits(this.codeLength) == Double.doubleToLongBits(that.codeLength) &&
				Objects.equals(this.timeCost, that.timeCost);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.totalTimeInMin, this.speed, this.keyStroke, this.codeLength, this.timeCost);
	}

}