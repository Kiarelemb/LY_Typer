package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import method.qr.kiarelemb.utils.QRCodePack;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.QRTimeCountUtil;
import method.qr.kiarelemb.utils.QRTimeUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRLabel;

import javax.swing.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 21:05
 **/
public class WordLabel extends QRLabel {
	private static int today;
	private static int total;

	private static String todayDate = QRTimeUtils.getDateNow();

	public static final QRTimeCountUtil wordUpdateTimeCount = new QRTimeCountUtil(Keys.intValue(Keys.TYPE_WORD_AUTO_SAVE_MINUTE));
	public static final WordLabel wordLabel = new WordLabel();

	private WordLabel() {
		total = loadWordNum(false, "Kiarelemb QR");
		if (!todayDate.equals(Keys.strValue(Keys.TYPE_WORD_TOTAL_LAST_UPDATE))) {
			todayWordFresh();
		} else {
			today = loadWordNum(true, todayDate);
		}
		//退出软件后自动保存字数
		QRSwing.registerSystemExitAction(e -> wordSave());
		runCheck();
		setText(today + " / " + total);
	}

	private int loadWordNum(boolean today, String decryptKey) {
		String key = today ? Keys.TYPE_WORD_TODAY : Keys.TYPE_WORD_TOTAL;
		String value = Keys.strValue(key);
		if (value == null) {
			if (today) {
				todayWordFresh();
			}
			return 0;
		}
		String word = QRCodePack.decrypt(value, decryptKey);
		if (word == null || !QRStringUtils.isNumber(word)) {
			if (today) {
				System.out.println("解密失败，已重刷");
				todayWordFresh();
			}
			return 0;
		}
		return QRStringUtils.stringToInt(word);
	}

	public static void typeAChar(char c) {
		//TODO
	}

	public static void typedOneWord() {
		today++;
		total++;
		wordLabel.setText(today + " / " + total);
		if (TypingData.restTime != 10L) {
			TypingData.windowFresh();
		}
	}

	private void wordSave() {
		QRSwing.setGlobalSetting(Keys.TYPE_WORD_TODAY, QRCodePack.encrypt(String.valueOf(today), todayDate));
		QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL, QRCodePack.encrypt(String.valueOf(total), "Kiarelemb QR"));
		QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL_LAST_UPDATE, todayDate);
	}

	private void runCheck() {
		Timer timer = new Timer(500, e -> {
			if (!QRTimeUtils.getDateNow().equals(todayDate)) {
				todayWordFresh();
			}
			if (wordUpdateTimeCount.isPassedLongTime() && TypingData.wordAutoSave) {
				wordSave();
			}
		});
		timer.start();
	}

	private void todayWordFresh() {
		todayDate = QRTimeUtils.getDateNow();
		today = 0;
		QRSwing.setGlobalSetting(Keys.TYPE_WORD_TODAY, QRCodePack.encrypt("0", todayDate));
		QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL_LAST_UPDATE, todayDate);
	}
}