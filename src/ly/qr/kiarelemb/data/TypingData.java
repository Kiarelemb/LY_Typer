package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.contract.state.LookModelCheckBox;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRSleepUtils;
import method.qr.kiarelemb.utils.QRThreadBuilder;
import method.qr.kiarelemb.utils.QRTools;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-22 16:08
 **/
public class TypingData {
	public static final ArrayList<Integer> WRONG_WORDS_INDEX = new ArrayList<>();
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
	public static int finishModel = 3;
	public static float lineSpacing = 3;
	public static StringBuffer typedKeyRecord = new StringBuffer();
	public static boolean pausing = false;
	public static boolean typing = false;
	public static boolean typeEnd = false;
	public static int pausedTimes = 0;
	public static int lookfontSize = 0;
	public static int typefontSize = 0;
	public static int wordSaveInterval = 0;
	public static int bModel;
	public static int spaceModel;
	public static int tipWindowLocation;
	public static String fontName;
	public static final String LEFT = "FVGTXE2AWSDCRZQ13456\t`fvgtxe2awsdcrzq";
	public static final String RIGHT = "HLUIYOKMJNP，=；、[;/.,。']\"7890-hluiyokmjnp";
	public static String grade;
	public static double currentSpeed = 0;
	public static boolean lookModel = false;
	public static boolean paintSelection = false;
	public static boolean paintCode = false;
	public static boolean charEnable = false;
	public static boolean tipEnable = false;
	public static boolean tipPanelEnable = false;
	public static boolean tipWindowEnable = false;
	public static boolean backspaceAutoRestart = false;
	public static boolean wordAutoSave = true;
	public static long restTime;
	private static long pauseStartTime;
	private static long pauseEndTime;
	private static final ThreadPoolExecutor tre_statistics = QRThreadBuilder.singleThread("statistics");

	static {
		TextPane.TEXT_PANE.addSetTextFinishedAction(e -> {
			typeEnd = false;
			windowFresh();
		});
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
		currentSpeed = 0;
		pausedTimes = 0;
		bModel = 0;
		spaceModel = 0;
		pauseStartTime = 0L;
		pauseEndTime = 0L;
		typing = false;
		pausing = false;
		ContractiblePanel.SPEED_LABEL.setText("0.0");
		ContractiblePanel.KEY_STROKE_LABEL.setText("0.0");
		ContractiblePanel.CODE_LEN_LABEL.setText("0.0");
		ContractiblePanel.TIME_LABEL.setText("0.0");
		windowFresh();
	}

	private static void runTyping() {
		switch (Keys.intValue(Keys.TYPE_STATISTICS_UPDATE)) {
			case 1 -> restTime = 1000L;
			case 2 -> restTime = 5000L;
			default -> restTime = 100L;
		}
		tre_statistics.submit(() -> typingStatisticsUpdate(restTime));
	}

	/**
	 * 每次载本都会更新
	 */
	public static void dataUpdate() {
		finishModel = Keys.intValue(Keys.TYPE_FINISH_MODEL);
		wordSaveInterval = Keys.intValue(Keys.TYPE_WORD_AUTO_SAVE_MINUTE);
		wordAutoSave = Keys.boolValue(Keys.TYPE_WORD_AUTO_SAVE);
		lookModel = Keys.boolValue(Keys.TYPE_MODEL_LOOK);
		paintSelection = Keys.boolValue(Keys.TEXT_TIP_PAINT_SELECTION);
		paintCode = Keys.boolValue(Keys.TEXT_TIP_PAINT_CODE);
		charEnable = Keys.boolValue(Keys.TEXT_TIP_CHAR_ENABLE);
		tipEnable = Keys.boolValue(Keys.TEXT_TIP_ENABLE);
		tipPanelEnable = Keys.boolValue(Keys.TEXT_TIP_PANEL_ENABLE);
		tipWindowEnable = Keys.boolValue(Keys.TEXT_TIP_WINDOW_ENABLE);
		backspaceAutoRestart = Keys.boolValue(Keys.TEXT_TYPE_BACKSPACE_AUTO_RESTART);
		lookfontSize = Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK);
		typefontSize = Keys.intValue(Keys.TEXT_FONT_SIZE_TYPE);
		tipWindowLocation = Keys.intValue(Keys.TEXT_TIP_WINDOW_LOCATION);
		fontName = Keys.strValue(Keys.TEXT_FONT_NAME_LOOK);
		lineSpacing = Keys.floatValue(Keys.TEXT_LINE_SPACE);
		bModel = Keys.intValue(Keys.TYPE_KEY_METHOD_B);
		spaceModel = Keys.intValue(Keys.TYPE_KEY_METHOD_SPACE);
	}

	private static void typingStatisticsUpdate(long restTime) {
		while (typing && !pausing) {
			long endTime = System.currentTimeMillis();
			//用时_秒
			double totalTimeInSec = (endTime - startTime) / 1000.0;
			//用时_分
			double totalTimeInMin = totalTimeInSec / 60;
			currentSpeed = ((currentTypedIndex - 5 * WRONG_WORDS_INDEX.size()) / totalTimeInMin);
			//速度
			String speeds;
			if (WRONG_WORDS_INDEX.size() != 0) {
				speeds = String.format("%.2f", (Math.max(currentTypedIndex - 5 * WRONG_WORDS_INDEX.size(), 0) / totalTimeInMin));
			} else {
				speeds = String.format("%.2f", currentTypedIndex / totalTimeInMin);
			}
			//击键
			String keyStrokes = String.format("%.2f", keyCounts / totalTimeInSec);
			//码长
			String codeLengths = String.format("%.2f", keyCounts / (double) (currentTypedIndex));
			if (!LookModelCheckBox.lookModelCheckBox.checked()) {
				ContractiblePanel.SPEED_LABEL.setText(speeds);
				ContractiblePanel.KEY_STROKE_LABEL.setText(keyStrokes);
				ContractiblePanel.CODE_LEN_LABEL.setText(codeLengths);
			}
			ContractiblePanel.TIME_LABEL.setText(QRMathUtils.doubleFormat(totalTimeInSec));
			windowFresh();
			QRSleepUtils.sleep(restTime);
		}
	}

	public synchronized static void windowFresh() {
		if (MainWindow.INSTANCE.backgroundImageSet()) {
			MainWindow.INSTANCE.repaint();
		}
	}

	public static void startTyping(long startTime) {
		if (!typing && !typeEnd && TextLoad.TEXT_LOAD != null) {
			TypingData.startTime = startTime;
			typing = true;
			typedKeyRecord = new StringBuffer();
			runTyping();
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
				QRTools.doNothing(e);
			}
		}
	}

	public static void continueTyping() {
		pauseEndTime = System.currentTimeMillis();
		long diff = pauseEndTime - pauseStartTime;
		startTime += diff;
	}
}