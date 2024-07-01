package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.dl.DangLangManager;
import ly.qr.kiarelemb.input.InputManager;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRScrollPane;
import swing.qr.kiarelemb.component.basic.QRTextPane;
import swing.qr.kiarelemb.component.listener.QRGlobalKeyboardHookListener;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:35
 **/
public class TyperTextPane extends QRTextPane {
    private static final Logger logger = QRLoggerUtils.getLogger(TyperTextPane.class);
    public KeyListener globalKeyListener = null;
    private final LinkedList<QRActionRegister> typeActions = new LinkedList<>();
    public static final TyperTextPane TYPER_TEXT_PANE = new TyperTextPane();

    private final Map<Integer, Character> specialKeyMap = Map.of(
            (int) 'B', 'b',
            (int) ' ', '_',
            KeyEvent.VK_ENTER, '↵',
            KeyEvent.VK_SHIFT, '↑',
            KeyEvent.VK_ALT, 'ᐃ',
            KeyEvent.VK_CAPS_LOCK, '⇧',
            KeyEvent.VK_BACK_SPACE, '←'
    );

    private TyperTextPane() {
        addKeyListener();
        addMouseListener();
        timeCountInit();
        setOpaque(false);
        addScrollPane().setOpaque(false);
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
        if (!verticalScrollBar.isVisible()) {
            return;
        }
        //更新模式
        final int[] lineAndRow = TextPane.TEXT_PANE.currentLineAndRow(TypingData.currentTypedIndex);
        final int currentLine = lineAndRow[0];
        final double currentRow = lineAndRow[1];
        boolean updateCondition = currentRow == 0;
        //行尾更新
        if (!updateCondition) {
            return;
        }
        final int lineWords = TextPane.TEXT_PANE.lineWords();
        double startUpdateLine = 3;
        if (currentLine >= startUpdateLine) {
            QRComponentUtils.runLater(100, e -> {
                int max = verticalScrollBar.getMaximum() - verticalScrollBar.getHeight();
                double value = ((currentLine - startUpdateLine) + currentRow / lineWords) * TextPane.TEXT_PANE.linePerHeight();
                verticalScrollBar.setValue((int) (Math.min(value, max)));
            });
        }
    }

    private static boolean keyCheck(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar == '\n' || keyChar == '\t') {
            return true;
        }
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_WINDOWS || (e.getModifiersEx() == KeyEvent.META_DOWN_MASK && !QRSystemUtils.IS_OSX)
            || keyCode == KeyEvent.VK_SHIFT || keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12
            || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            return true;
        }
        if (TypingData.typing) {
            return e.isControlDown() || e.isAltDown();
        }
        return TypingData.typeEnd;
    }

    /**
     * 每输入或回改事件，即光标移动事件
     */
    public void runTypedActions() {
        SwingUtilities.invokeLater(() -> QRComponentUtils.runActions(this.typeActions));
    }

    public void keyPressAction(KeyStroke keyStroke, long time) {
        if (!MainWindow.INSTANCE.isFocused() && !InputManager.INPUT_MANAGER.isLoaded() || TextLoad.TEXT_LOAD == null) {
            return;
        }
        //屏蔽组合键
        int keyCode = keyStroke.getKeyCode();
        char keyChar = (char) keyCode;
        int modifiers = keyStroke.getModifiers();
        if (modifiers != 0 || (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12)
            || (keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT
                || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_WINDOWS) && !TypingData.typing) {
            QRLoggerUtils.log(logger, Level.INFO, "按键屏蔽：[%s]", QRStringUtils.getKeyStrokeString(keyStroke));
            return;
        }
        if (TypingData.typeEnd) {
            return;
        }

        // 开始计时
        TypingData.startTyping(time);
        TypingData.endTime = time;
        long timeDiff = TypingData.endTime - TypingData.startTime;

        //region 按键统计
        TypingData.keyCounts++;
        boolean flag = specialKeyMap.containsKey(keyCode);
        if (flag) {
            keyChar = specialKeyMap.get(keyCode);
            TypingData.typedKeyRecord.append(keyChar);
            DangLangManager.DANG_LANG_MANAGER.put(keyChar, timeDiff);
        }
        switch (keyCode) {
            case 'B' -> TypingData.bCounts++;
            case ' ' -> TypingData.spaceCounts++;
            case KeyEvent.VK_ENTER -> TypingData.enterCount++;
            case KeyEvent.VK_BACK_SPACE -> TypingData.backSpaceCount++;
        }

        if (TypingData.LEFT.indexOf(keyChar) != -1) {
            TypingData.leftCounts++;
        } else if (TypingData.RIGHT.indexOf(keyChar) != -1) {
            TypingData.rightCounts++;
        } else if (!flag) {
            QRLoggerUtils.log(logger, Level.WARNING, "未知按键：[%s]", QRStringUtils.getKeyStrokeString(keyStroke));
            keyChar = '⊗';
        }
        if (!flag) {
            // TYPE_DISCARD_UNKNOWN_KEY: true 则屏蔽未知按键
            if (Keys.boolValue(Keys.TYPE_DISCARD_UNKNOWN_KEY) && keyChar == '⊗') {
                TypingData.keyCounts--;
            } else {
                TypingData.typedKeyRecord.append(keyChar);
                DangLangManager.DANG_LANG_MANAGER.put(QRStringUtils.toLowerCase(keyChar), timeDiff);
            }
        }
        //endregion 按键统计
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
                try {
                    keyPressAction(QRStringUtils.getKeyStroke(e), System.currentTimeMillis());
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "timeCountInit", ex);
                }
                return true;
            });
        }
    }

    protected void caretPositionAdjust() {
        if (TypingData.typeEnd || !TypingData.typing) return;
        int caretPosition = caret.getDot();
        if (TypingData.currentTypedIndex == caretPosition) {
            return;
        }
        try {
            setCaretPosition(TypingData.currentTypedIndex);
        } catch (Exception e) {
            logger.log(Level.WARNING, "caretPositionAdjust", e);
        }
    }

    /**
     * 从这里可接收输入的内容
     */
    @Override
    public void keyType(KeyEvent e) {
        if (TextLoad.TEXT_LOAD == null || keyCheck(e) || !TypingData.typing) {
            e.consume();
            return;
        }
        try {
            char keyChar = e.getKeyChar();
            if (keyChar == KeyEvent.VK_BACK_SPACE) {
                TextPane.TEXT_PANE.deleteUpdates(e);
                return;
            }
            TextPane.TEXT_PANE.insertUpdates(keyChar);
        } catch (Exception e1) {
            logger.log(Level.SEVERE, "keyType", e1);
        }
    }

    @Override
    protected void keyPress(KeyEvent e) {
        if (keyCheck(e)) {
            e.consume();
        }
    }

    @Override
    protected void pasteAction() {
        String text = QRSystemUtils.getSysClipboardText();
        if (text == null || text.isBlank()) {
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
    }

    @Override
    protected void mouseEnter(MouseEvent e) {
        grabFocus();
    }

    @Override
    protected void mouseClick(MouseEvent e) {
        caretPositionAdjust();
    }

    @Override
    public void changeTextsStyle(int offset, int length, AttributeSet attrs, boolean replace) {
        super.changeTextsStyle(offset, length, attrs, replace);
        TypingData.windowFresh();
    }

    @Override
    public QRScrollPane addScrollPane() {
        return super.addScrollPane(1);
    }

    @Override
    public void componentFresh() {
        this.textFont = QRColorsAndFonts.STANDARD_FONT_TEXT.deriveFont((float) Keys.intValue(Keys.TEXT_FONT_SIZE_TYPE));
        super.componentFresh();
    }

    public class KeyListener extends QRGlobalKeyboardHookListener {
        @Override
        protected void keyPress(KeyStroke keyStroke) {
            try {
                keyPressAction(keyStroke, System.currentTimeMillis());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "timeCountInit", e);
            }
        }
    }
}