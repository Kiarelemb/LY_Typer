package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TipData;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRPanel;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-16 14:36
 **/
public class TipPanel extends QRPanel {
    private final QRPanel singlePanel = new QRPanel(false);
    private final QRPanel phrasePanel = new QRPanel(false);
    private final QRLabel singleWordLabel = new QRLabel("单字");
    private final QRLabel singleWordCodeLabel = new QRLabel("abcd");
    private final QRLabel phraseWordLabel = new QRLabel("词组");
    private final QRLabel phraseWordCodeLabel = new QRLabel("abcd");
    private final int gap = 5;

    public TipPanel() {
        addMouseListener();
        this.singlePanel.add(this.singleWordLabel);
        this.singlePanel.add(this.singleWordCodeLabel);
        this.phrasePanel.add(this.phraseWordLabel);
        this.phrasePanel.add(this.phraseWordCodeLabel);
        layoutUpdate();
        //每次输入或回改需要更改词提
        QRActionRegister updateAction = e -> tipUpdate();
        TyperTextPane.TYPER_TEXT_PANE.addTypeActions(updateAction);
        TextPane.TEXT_PANE.addSetTextFinishedAction(updateAction);
    }


    public void tipUpdate() {
        if (TypingData.tipEnable) {
            TipData data = TextLoad.TEXT_LOAD.tipData;
            if (data != null) {
                int index = TypingData.currentTypedIndex;
                ArrayList<TipCharStyleData> tcsd = data.tcsd;
                if (tcsd.size() <= index) {
                    return;
                }
                ArrayList<TipPhraseStyleData> tpsd = data.tpsd;
                TipCharStyleData charData = tcsd.get(index);
                TipPhraseStyleData phraseData = tpsd.get(index);
                if (charData != null) {
                    //更新字体
                    final String tpC = StyleConstants.getFontFamily(charData.getStyle());
                    Font font = QRFontUtils.FONT_MAP.computeIfAbsent(tpC, t -> QRFontUtils.getFont(t, 20));
                    if (this.singleWordLabel.getFont() != font) {
                        this.singleWordLabel.setFont(font);
                        this.phraseWordLabel.setFont(font);
                    }
                    this.singleWordLabel.setText(charData.word());
                    this.singleWordCodeLabel.setText(charData.code());

                    if (phraseData != null && !phraseData.phrase().equals(charData.word())) {
                        this.phraseWordLabel.setText(phraseData.phrase());
                        this.phraseWordCodeLabel.setText(phraseData.code());
                    } else {
                        this.phraseWordLabel.setText(null);
                        this.phraseWordCodeLabel.setText(null);
                    }
                }
                pack();
            }
        }
    }

    public void layoutUpdate() {
        this.singlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, this.gap, this.gap));
        this.phrasePanel.setLayout(new FlowLayout(FlowLayout.LEFT, this.gap, this.gap));
        remove(this.singlePanel);
        remove(this.phrasePanel);
        if (Keys.boolValue(Keys.TEXT_TIP_DIVIDE)) {
            setLayout(new BorderLayout(this.gap, this.gap));
            add(this.singlePanel, BorderLayout.NORTH);
            add(this.phrasePanel, BorderLayout.SOUTH);
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT, this.gap, this.gap));
            add(this.singlePanel);
            add(this.phrasePanel);
        }
        pack();
    }

    public void pack() {
        int width = this.gap * 2;
        int height = this.gap * 2;
        Rectangle singleWordBounds = QRFontUtils.getStringBounds(this.singleWordLabel.getText(), this.singleWordLabel.getFont()).getBounds();
        Rectangle singleWordCodeBounds = QRFontUtils.getStringBounds(this.singleWordCodeLabel.getText(), this.singleWordCodeLabel.getFont()).getBounds();
        width += singleWordBounds.width + singleWordCodeBounds.width + this.gap;
        height += singleWordBounds.height;
        if (this.phraseWordLabel.getText() != null) {
            Rectangle phraseWordBounds = QRFontUtils.getStringBounds(this.phraseWordLabel.getText(), this.phraseWordLabel.getFont()).getBounds();
            Rectangle phraseWordCodeBounds = QRFontUtils.getStringBounds(this.phraseWordCodeLabel.getText(), this.phraseWordCodeLabel.getFont()).getBounds();
            int phraseWidth = phraseWordBounds.width + phraseWordCodeBounds.width + this.gap * 2;
            if (Keys.boolValue(Keys.TEXT_TIP_DIVIDE)) {
                height += phraseWordBounds.height + this.gap;
                width = Math.max(width, phraseWidth + this.gap * 2);
            } else {
                width += phraseWidth;
            }
        }
        setPreferredSize(width, height);
        revalidate();
        repaint();
    }

    @Override
    protected void mouseEnter(MouseEvent e) {
        setCursorDefault();
    }
}