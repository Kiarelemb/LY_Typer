package ly.qr.kiarelemb.component;

import com.github.kwhat.jnativehook.GlobalScreen;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.dl.DangLangManager;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.event.QRNativeKeyEvent;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.listener.QRNativeKeyListener;
import swing.qr.kiarelemb.listener.key.QRNativeKeyPressedListener;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import javax.swing.*;
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
    public QRNativeKeyListener globalKeyListener = null;
    public KeyboardFocusManager keyboardFocusManager = null;
    private final LinkedList<QRActionRegister<Integer>> typeActions = new LinkedList<>();
    public static final TyperTextPane TYPER_TEXT_PANE = new TyperTextPane();

    private final Map<Integer, Character> specialKeyMap = Map.of(
            (int) 'B', 'B',
            (int) ' ', '_',
            KeyEvent.VK_ENTER, '↵',
            KeyEvent.VK_SHIFT, '↑',
            KeyEvent.VK_ALT, 'ᐃ',
            KeyEvent.VK_CAPS_LOCK, '⇧',
            KeyEvent.VK_BACK_SPACE, '←'
    );

    private TyperTextPane() {
        addKeyListener();
        setOpaque(false);
        timeCountInit();
        TextViewPane.TEXT_VIEW_PANE.addSetTextBeforeAction(e -> {
            clear();
            this.caret.setVisible(true);
            setFont(QRFontUtils.getFont(TypingData.fontName, TypingData.typefontSize));
        });
    }

    public void addTypeActions(QRActionRegister<Integer> ar) {
        this.typeActions.add(ar);
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
        return false;
    }

    /**
     * 每输入或回改事件，即光标移动事件
     */
    public void runTypedActions(int currentIndex) {
        SwingUtilities.invokeLater(() -> QRComponentUtils.runActions(this.typeActions, currentIndex));
    }

    public void keyPressAction(KeyStroke keyStroke, long time) {
        if (!this.hasFocus() || TextLoad.TEXT_LOAD == null || keyStroke == null) {
            return;
        }
        //屏蔽组合键
        int keyCode = keyStroke.getKeyCode();
        char keyChar = (char) keyCode;
        if ((keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT
             || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_WINDOWS || keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12)
            && !TypingData.typing) {
            QRLoggerUtils.log(logger, Level.INFO, "按键屏蔽：[%s]", QRStringUtils.getKeyStrokeString(keyStroke));
            return;
        }
        int modifiers = keyStroke.getModifiers();
        if ((modifiers != 0 && (!TypingData.typing || TypingData.typeEnd)) && keyStroke.getModifiers() != 65) {
            QRLoggerUtils.log(logger, Level.INFO, "按键屏蔽：[%s]", QRStringUtils.getKeyStrokeString(keyStroke));
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
        this.globalKeyListener = new QRNativeKeyPressedListener();
        GlobalScreen.addNativeKeyListener(this.globalKeyListener);
        this.globalKeyListener.add(true, this::keyPressLead);
    }

    private void keyPressLead(QRNativeKeyEvent e) {
        try {
            keyPressAction(e.getKeyStroke(), System.currentTimeMillis());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "keyPressLoad", ex);
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
                TextViewPane.TEXT_VIEW_PANE.deleteUpdates(e);
                return;
            }
            TextViewPane.TEXT_VIEW_PANE.insertUpdates(keyChar);
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
        TextViewPane.TEXT_VIEW_PANE.setTypeText(text);
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
    public void componentFresh() {
        this.textFont = QRColorsAndFonts.STANDARD_FONT_TEXT.deriveFont((float) Keys.intValue(Keys.TEXT_FONT_SIZE_TYPE));
        super.componentFresh();
    }
}