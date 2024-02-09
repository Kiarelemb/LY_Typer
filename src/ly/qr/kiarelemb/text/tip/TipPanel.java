package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TipData;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-16 14:36
 **/
public class TipPanel extends QRPanel {
	private final QRPanel singlePanel = new QRPanel();
	private final QRPanel phrasePanel = new QRPanel();
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
		Font enFont = QRFontUtils.getFont(TextStyleManager.PREFERRED_ENGLISH_FONT_NAME, 20);
		this.singleWordCodeLabel.setFont(enFont);
		this.phraseWordCodeLabel.setFont(enFont);
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
					Font font = QRFontUtils.fontMap.computeIfAbsent(tpC, t -> QRFontUtils.getFont(t, 20));
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
		setLayout(new BorderLayout(this.gap, this.gap));
		if (Keys.boolValue(Keys.TEXT_TIP_DIVIDE)) {
			add(this.singlePanel, BorderLayout.NORTH);
			add(this.phrasePanel, BorderLayout.SOUTH);
		} else {
			add(this.singlePanel, BorderLayout.WEST);
			add(this.phrasePanel, BorderLayout.EAST);
		}
		pack();
	}

	public void pack() {
		int width = this.gap * 2;
		int height = this.gap * 2;
		Rectangle2D singleWordBounds = QRFontUtils.getStringBounds(this.singleWordLabel.getText(), this.singleWordLabel.getFont());
		Rectangle2D singleWordCodeBounds = QRFontUtils.getStringBounds(this.singleWordCodeLabel.getText(), this.singleWordCodeLabel.getFont());
		width += singleWordBounds.getWidth() + singleWordCodeBounds.getWidth() + this.gap;
		height += singleWordBounds.getHeight();
		if (this.phraseWordLabel.getText() != null) {
			Rectangle2D phraseWordBounds = QRFontUtils.getStringBounds(this.phraseWordLabel.getText(), this.phraseWordLabel.getFont());
			Rectangle2D phraseWordCodeBounds = QRFontUtils.getStringBounds(this.phraseWordCodeLabel.getText(), this.phraseWordCodeLabel.getFont());
			if (Keys.boolValue(Keys.TEXT_TIP_DIVIDE)) {
				height += phraseWordBounds.getHeight() + this.gap * 2;
				width += (int) Math.max(width, phraseWordBounds.getWidth() + phraseWordCodeBounds.getWidth() + this.gap * 2);
			} else {
				width += phraseWordBounds.getWidth() + phraseWordCodeBounds.getWidth() + this.gap * 2;
			}
		}
		setPreferredSize(width, height);
	}

	@Override
	protected void mouseEnter(MouseEvent e) {
		setCursorDefault();
	}
}