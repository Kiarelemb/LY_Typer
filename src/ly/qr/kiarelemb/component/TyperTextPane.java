package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.dl.DangLangManager;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRScrollPane;
import swing.qr.kiarelemb.component.basic.QRTextPane;
import swing.qr.kiarelemb.component.listener.QRGlobalKeyboardHookListener;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:35
 **/
public class TyperTextPane extends QRTextPane {
	public KeyListener globalKeyListener = null;
	private final LinkedList<QRActionRegister> typeActions = new LinkedList<>();
	public static final TyperTextPane TYPER_TEXT_PANE = new TyperTextPane();

	private TyperTextPane() {
		addKeyListener();
		addMouseListener();
		timeCountInit();
		this.typeActions.add(e -> scrollUpdate());
		TextPane.TEXT_PANE.addSetTextBeforeAction(e -> {
			clear();
			this.caret.setVisible(true);
			setFont(QRFontUtils.getFont(TypingData.fontName, TypingData.typefontSize));
		});
	}

	public void addTypeActions(QRActionRegister ar) {
		this.typeActions.add(ar);
	}

	private void scrollUpdate() {
		JScrollBar verticalScrollBar = TextPane.TEXT_PANE.addScrollPane().getVerticalScrollBar();
		if (verticalScrollBar.isVisible()) {
			//更新模式
//			boolean updateEveryTimes = tsd.scrollbarValueUpdate();
			final int[] lineAndRow = TextPane.TEXT_PANE.currentLineAndRow(TypingData.currentTypedIndex);
			final int currentLine = lineAndRow[0];
			final double currentRow = lineAndRow[1];
//			final boolean isAtLast = currentRow == 0;
//			boolean updateCondition = TypingData.currentTypedIndex % 5 == 0;
			boolean updateCondition = currentRow == 0;
			//行尾更新
			final int lineWords = TextPane.TEXT_PANE.lineWords();
			if (updateCondition) {
				double startUpdateLine = 3;
				if (currentLine >= startUpdateLine) {
					int max = verticalScrollBar.getMaximum() - verticalScrollBar.getHeight();
					double value = ((currentLine - startUpdateLine) + currentRow / lineWords) * TextPane.TEXT_PANE.linePerHeight();
					verticalScrollBar.setValue((int) (Math.min(value, max)));
				}
			}
		}
	}

	/**
	 * 从这里可接收输入的内容
	 */
	@Override
	protected void keyType(KeyEvent e) {
		if (TextLoad.TEXT_LOAD == null) {
			return;
		}
		int keyCode = e.getKeyCode();
		if (e.getModifiersEx() != 0 || (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12)) {
			return;
		}
		if (keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_WINDOWS) {
			return;
		}
		if (TypingData.typing && !TypingData.typeEnd) {
			try {
				char keyChar = e.getKeyChar();
				if (keyChar == KeyEvent.VK_BACK_SPACE) {
					TextPane.TEXT_PANE.deleteUpdates(e);

				} else {
					TextPane.TEXT_PANE.insertUpdates(keyChar);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			e.consume();
		}
	}

	public void runTypeActions() {
		QRComponentUtils.runActions(this.typeActions);
	}

	private void keyPressAction(KeyStroke keyStroke, long time) {
		//屏蔽组合键
		int keyCode = keyStroke.getKeyCode();
		int modifiers = keyStroke.getModifiers();
		if (modifiers != 0 || (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12)) {
			return;
		}
//		if (keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_WINDOWS) {
//			return;
//		}
		TypingData.startTyping(time);
//		KeyEvent.VK_OPEN_BRACKET
		TypingData.endTime = time;
		long timeDiff = TypingData.endTime - TypingData.startTime;
		char keyChar = (char) keyCode;
		if (MainWindow.INSTANCE.isVisible() && TYPER_TEXT_PANE.hasFocus()) {
			//region 按键统计
			TypingData.keyCounts++;
			boolean flag = true;
			switch (keyCode) {
				case 'B' -> {
					TypingData.bCounts++;
					TypingData.typedKeyRecord.append('b');
					flag = false;
				}
				case ' ' -> {
					TypingData.spaceCounts++;
					TypingData.typedKeyRecord.append('_');
					DangLangManager.DANG_LANG_MANAGER.put('_', timeDiff);
					flag = false;
				}
				case KeyEvent.VK_ENTER -> {
					TypingData.enterCount++;
					TypingData.rightCounts++;
					TypingData.typedKeyRecord.append('↵');
					DangLangManager.DANG_LANG_MANAGER.put('↵', timeDiff);
					flag = false;
				}
				case KeyEvent.VK_BACK_SPACE -> {
					TypingData.backSpaceCount++;
					TypingData.rightCounts++;
					TypingData.typedKeyRecord.append('←');
					DangLangManager.DANG_LANG_MANAGER.put('←', timeDiff);
					flag = false;
				}
			}
			if (flag) {
				if (TypingData.LEFT.indexOf(keyChar) != -1) {
					TypingData.leftCounts++;
				} else if (TypingData.RIGHT.indexOf(keyChar) != -1) {
					TypingData.rightCounts++;
				} else {
					TypingData.typedKeyRecord.append('⊗');
					DangLangManager.DANG_LANG_MANAGER.put('⊗', timeDiff);
					return;
				}
				TypingData.typedKeyRecord.append(keyChar);
				DangLangManager.DANG_LANG_MANAGER.put(QRStringUtils.toLowerCase(keyChar), timeDiff);
			}
			//endregion 按键统计

		}
	}

	private void timeCountInit() {
		if (QRSystemUtils.IS_WINDOWS) {
			this.globalKeyListener = new KeyListener();
		} else {
			KeyboardFocusManager keyRecord = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			keyRecord.addKeyEventPostProcessor(e -> {
				if (e.getID() != KeyEvent.KEY_LAST) {
					return false;
				}
				keyPressAction(QRStringUtils.getKeyStroke(e), System.currentTimeMillis());
				return true;
			});
		}
	}

	@Override
	protected void pasteAction() {
		//TODO 发文时不载文
//		if (ifTextUnloadable()) {
//			return;
//		}
		String text = QRSystemUtils.getSysClipboardText();
		if (text == null) {
			return;
		}
		text = text.trim();
		final int lastIndexOf = text.lastIndexOf(QRStringUtils.AN_ENTER);
		int diIndex = text.indexOf(TextLoad.DI, lastIndexOf);
		int duanIndex = text.indexOf(TextLoad.DUAN, diIndex + 1);
		if (diIndex == -1 || duanIndex <= diIndex + 1 || !QRStringUtils.isNumber(text.substring(diIndex + 1, duanIndex))) {
			if (lastIndexOf != -1 && text.indexOf(QRStringUtils.AN_ENTER) < lastIndexOf) {
				text = QRStringUtils.lineSeparatorClear(text, true);
			}
			text += "\n-----第" + QRRandomUtils.getRandomInt(999999) + "段";
		}
		if (QRStringUtils.markCount(text, '\n') == 1) {
			text = "剪贴板\n" + text;
		}
		TextPane.TEXT_PANE.setTypeText(text);
//		TextPane.TEXT_PANE.setText(text);
	}

	@Override
	protected void mouseEnter(MouseEvent e) {
		grabFocus();
	}

	@Override
	public QRScrollPane addScrollPane() {
		return super.addScrollPane(1);
	}

	@Override
	public void componentFresh() {
		super.componentFresh();
		this.textFont = QRFontUtils.getFontInSize(Keys.intValue(Keys.TEXT_FONT_SIZE_TYPE));
	}

	class KeyListener extends QRGlobalKeyboardHookListener {
		@Override
		protected void keyPress(KeyStroke keyStroke) {
			keyPressAction(keyStroke, System.currentTimeMillis());
		}
	}
}