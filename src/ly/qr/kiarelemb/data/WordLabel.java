package ly.qr.kiarelemb.data;

import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRLabel;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 21:05
 **/
public class WordLabel extends QRLabel {
    private int today;
    private int total;
    private boolean updated = false;
    private final Logger logger = QRLoggerUtils.getLogger(WordLabel.class);
    private String todayDate = QRTimeUtils.getDateNow();
    public final QRTimeCountUtil wordUpdateTimeCount = new QRTimeCountUtil(Keys.intValue(Keys.TYPE_WORD_AUTO_SAVE_MINUTE));

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
                logger.severe("解密失败，已重刷");
                todayWordFresh();
            }
            return 0;
        }
        return QRStringUtils.stringToInt(word);
    }

    public static void typePlus(int num) {
        WordLabel.wordLabel.updated = true;
        wordLabel.today += num;
        wordLabel.total += num;
        wordLabel.setText(wordLabel.today + " / " + wordLabel.total);
    }

    public static void typedOneWord() {
        WordLabel.wordLabel.updated = true;
        wordLabel.today++;
        wordLabel.total++;
        wordLabel.setText(wordLabel.today + " / " + wordLabel.total);
    }

    private void wordSave() {
        QRSwing.setGlobalSetting(Keys.TYPE_WORD_TODAY, QRCodePack.encrypt(String.valueOf(today), todayDate));
        QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL, QRCodePack.encrypt(String.valueOf(total), "Kiarelemb QR"));
        QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL_LAST_UPDATE, todayDate);
        QRLoggerUtils.log(logger, Level.CONFIG, "已保存字数：今日 %d 字，总计 %d 字。", today, total);
    }

    private void runCheck() {
        // swing.timer 会自动循环
        Timer timer = new Timer(wordUpdateTimeCount.min() * 60000, e -> {
            if (!QRTimeUtils.getDateNow().equals(todayDate)) {
                todayWordFresh();
            }
            if (wordUpdateTimeCount.isPassedLongTime() && updated) {
                wordSave();
                updated = false;
            }
        });
        timer.start();
    }

    /**
     * 更新当天的单词量和最后更新时间。
     * 本方法用于每日零点或应用启动时重置当天的打字计数，并记录当前时间作为最后更新时间。
     * 它通过QRCodePack加密当天日期并存储为全局设置，以便于后续查询和统计。
     */
    private void todayWordFresh() {
        // 获取当前日期和时间
        todayDate = QRTimeUtils.getDateNow();
        // 重置当天打字计数为0
        today = 0;
        // 重置当天打字数
        QRSwing.setGlobalSetting(Keys.TYPE_WORD_TODAY, QRCodePack.encrypt("0", todayDate));
        // 设置最后更新时间为当前日期
        QRSwing.setGlobalSetting(Keys.TYPE_WORD_TOTAL_LAST_UPDATE, todayDate);
        QRLoggerUtils.log(logger, Level.CONFIG, "过零点，已更新今日打字数：今日 %d 字，总计 %d 字。", today, total);
    }
}