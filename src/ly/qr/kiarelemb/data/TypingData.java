package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRSleepUtils;
import method.qr.kiarelemb.utils.QRThreadBuilder;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.listener.QRActionListener;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-22 16:08
 **/
public class TypingData {
    public static final ArrayList<Integer> WRONG_WORDS_INDEX = new ArrayList<>();
    private static final Logger logger = QRLoggerUtils.getLogger(TypingData.class);
    public static int keyCounts = 0;
    public static int backSpaceCount = 0;
    public static int backDeleteCount = 0;
    public static int enterCount = 0;
    public static long startTime = 0;
    public static long endTime;
    public static int leftCounts;
    public static int rightCounts;
    public static int spaceCounts;
    public static int phraseLength = 0;
    public static int bCounts;
    public static int currentTypedIndex = 0;
    public static float lineSpacing = 3;
    public static StringBuilder typedKeyRecord = new StringBuilder();
    public static boolean pausing = false;
    public static boolean typing = false;
    public static boolean typeEnd = false;
    public static int pausedTimes = 0;
    public static int lookfontSize = 0;
    public static int typefontSize = 0;
    public static int bModel;
    public static int spaceModel;
    public static int tipWindowLocation;
    public static String fontName;
    public static final String LEFT = "FVGTXE2AWSDCRZQ13456⇧↑\t`fvgtxe2awsdcrzqᐃ";
    public static final String RIGHT = "HLUIYOKMJNP，=；、[;/.,。']↵←\n\\\"7890-hluiyokmjnp";
    public static boolean paintSelection = false;
    public static boolean paintCode = false;
    public static boolean charEnable = false;
    public static boolean tipEnable = false;
    public static boolean tipWindowEnable = false;
    public static boolean backspaceAutoRestart = false;
    public static boolean textLoadIntelli;
    public static boolean instantaneous;
    public static long restTime;
    private static long pauseStartTime;
    private static long pauseEndTime;
    /**
     * 开始打字的监听器
     */
    private static final QRActionListener START_TYPING_LISTENER = new QRActionListener();
    private static final ThreadPoolExecutor tre_statistics = QRThreadBuilder.singleThread("statistics");

    static {
        TextViewPane.TEXT_VIEW_PANE.addSetTextBeforeAction(e -> typeEnd = false);
        QRSwing.registerSystemExitAction(event -> {
            if (!typedKeyRecord.isEmpty()) {
                KeyTypedRecordData.fresh(typedKeyRecord.toString());
            }
        });
        START_TYPING_LISTENER.add(event -> {
            if (!typedKeyRecord.isEmpty()) {
                KeyTypedRecordData.fresh(typedKeyRecord.toString());
            }
        });
        dataUpdate();
    }


    /**
     * 添加开始打字的动作监听器
     *
     * @param ar 操作
     */
    public static void addStartTypingAction(QRActionRegister ar) {
        START_TYPING_LISTENER.add(ar);
    }

    public static void clear() {
        WRONG_WORDS_INDEX.clear();
        keyCounts = 0;
        backSpaceCount = 0;
        backDeleteCount = 0;
        enterCount = 0;
        startTime = 0;
        endTime = 0;
        leftCounts = 0;
        rightCounts = 0;
        spaceCounts = 0;
        phraseLength = 0;
        bCounts = 0;
        currentTypedIndex = 0;
        pausedTimes = 0;
        bModel = 0;
        spaceModel = 0;
        pauseStartTime = 0L;
        pauseEndTime = 0L;
        typing = false;
        typeEnd = true;
        pausing = false;
        windowFresh();
    }

    private static void runTyping() {
        switch (Keys.intValue(Keys.TYPE_STATISTICS_UPDATE)) {
            case 1 -> restTime = 1000L;
            case 2 -> restTime = 5000L;
            default -> restTime = 100L;
        }
        statisticUpdate = tre_statistics.submit(() -> typingStatisticsUpdate(restTime));
    }

    static Future<?> statisticUpdate;

    public static void shutdown() {
        if (statisticUpdate == null) {
            return;
        }
        try {
            statisticUpdate.cancel(true);
        } catch (Exception ignore) {
        }
    }

    /**
     * 每次载文都会更新
     */
    public static void dataUpdate() {
        paintSelection = Keys.boolValue(Keys.TEXT_TIP_PAINT_SELECTION);
        paintCode = Keys.boolValue(Keys.TEXT_TIP_PAINT_CODE);
        charEnable = Keys.boolValue(Keys.TEXT_TIP_CHAR_ENABLE);
        tipEnable = Keys.boolValue(Keys.TEXT_TIP_ENABLE);
        tipWindowEnable = Keys.boolValue(Keys.TEXT_TIP_WINDOW_ENABLE);
        backspaceAutoRestart = Keys.boolValue(Keys.TEXT_TYPE_BACKSPACE_AUTO_RESTART);
        textLoadIntelli = Keys.boolValue(Keys.TEXT_LOAD_INTELLI);
        instantaneous = Keys.boolValue(Keys.TYPE_DATA_INSTANTANEOUS_VELOCITY);
        lookfontSize = Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK);
        typefontSize = Keys.intValue(Keys.TEXT_FONT_SIZE_TYPE);
        tipWindowLocation = Keys.intValue(Keys.TEXT_TIP_WINDOW_LOCATION);
        fontName = Keys.strValue(Keys.TEXT_FONT_NAME_LOOK);
        lineSpacing = Keys.floatValue(Keys.TEXT_LINE_SPACE);
        bModel = Keys.intValue(Keys.TYPE_KEY_METHOD_B);
        spaceModel = Keys.intValue(Keys.TYPE_KEY_METHOD_SPACE);
    }

    private static void typingStatisticsUpdate(long restTime) {
        if (MainWindow.INSTANCE.backgroundImageSet() && !Keys.boolValue(Keys.WINDOW_BACKGROUND_FRESH_ENHANCEMENT)) {
            QRComponentUtils.runLater(10, e -> {
                while (typing) {
                    if (!pausing) windowFresh();
                    QRSleepUtils.sleep(5);
                }
            });
        }
        int index = 0;
        while (typing && !pausing) {
            QRSleepUtils.sleep(restTime);
            if (typing && ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked()) {
                continue;
            }
            if (startTime == 0L) {
                break;
            }
            long endTime = System.currentTimeMillis();
            //用时_秒
            double totalTimeInSec = (endTime - startTime) / 1000.0;
            //用时_分
            double totalTimeInMin = totalTimeInSec / 60;
            //速度
            double speeds = 0.00;
            if (instantaneous) {
                // 瞬时速度
                try {
                    TypeRecordData recordData = TypeRecordData.updateData(endTime);
                    if (recordData != null) {
                        speeds = recordData.length() / (recordData.time() / 60000f);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "error", e);
                }
            } else {
                if (!WRONG_WORDS_INDEX.isEmpty()) {
                    speeds = (Math.max(currentTypedIndex - WRONG_WORDS_INDEX.size() * 5, 0) / totalTimeInMin);
                } else {
                    speeds = currentTypedIndex / totalTimeInMin;
                }
            }
            //击键
            String keyStrokes = String.format("%.2f", keyCounts / totalTimeInSec);
            //码长
            String codeLengths = String.format("%.2f", keyCounts / (double) (currentTypedIndex));
            ContractiblePanel.SPEED_LABEL.setText(String.format("%.2f", speeds));
            ContractiblePanel.KEY_STROKE_LABEL.setText(keyStrokes);
            ContractiblePanel.CODE_LEN_LABEL.setText(codeLengths);
            ContractiblePanel.TIME_LABEL.setText(QRMathUtils.doubleFormat(totalTimeInSec));
            if (index != currentTypedIndex) {
                index = currentTypedIndex;

            }
            windowFresh();
        }
        windowFresh();
    }

    public synchronized static void windowFresh() {
        if (MainWindow.INSTANCE.backgroundImageSet()) {
            MainWindow.INSTANCE.getContentPane().repaint();
        }
    }

    /**
     * 全局唯一开始计时的地方
     *
     * @param startTime
     */
    public static void startTyping(long startTime) {
        if (!typing && TextLoad.TEXT_LOAD != null) {
            try {
                TypingData.startTime = startTime;
                typing = true;
                START_TYPING_LISTENER.actionPerformed(null);
                typedKeyRecord = new StringBuilder();
                runTyping();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "startTyping", e);
            }
            logger.info("********** 开始跟打 **********");
        }
    }

    public static void pauseTyping() {
        pauseStartTime = System.currentTimeMillis();
        pausedTimes++;
        if (Info.IS_WINDOWS && Keys.boolValue(Keys.WINDOW_PAUSE_MINIMIZE)) {
            SystemTray st = SystemTray.getSystemTray();
            try {
                TrayIcon ti = new TrayIcon(Info.loadImage(Info.ICON_TRAY_PATH).getImage());
                st.add(ti);
                ti.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //双击托盘窗口再现
                        if (e.getClickCount() == 2) {
                            MainWindow.INSTANCE.setExtendedState(Frame.NORMAL);
                            st.remove(ti);
                            MainWindow.INSTANCE.setVisible(true);
                        }
                    }
                });
                MainWindow.INSTANCE.setVisible(false);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "pauseTyping", e);
            }
        }
    }

    public static void continueTyping() {
        pauseEndTime = System.currentTimeMillis();
        long diff = pauseEndTime - pauseStartTime;
        startTime += diff;
    }
}