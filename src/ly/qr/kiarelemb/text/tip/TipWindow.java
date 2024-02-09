package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import swing.qr.kiarelemb.window.basic.QREmptyDialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-16 16:16
 **/
public class TipWindow extends QREmptyDialog {
	public final TipPanel tipPanel;

	public static final TipWindow TIP_WINDOW = new TipWindow();

	private TipWindow() {
		super(MainWindow.INSTANCE, false);

		this.tipPanel = new TipPanel();
		this.contentPane.setLayout(new BorderLayout());
		this.contentPane.add(this.tipPanel);
		TyperTextPane.TYPER_TEXT_PANE.addTypeActions(e -> {
			if (TipWindow.this.isVisible()) {
				updateLocation();
			}
		});

		TextPane.TEXT_PANE.addSetTextFinishedAction(e -> {
			if (TypingData.tipEnable && TypingData.tipPanelEnable && TextLoad.TEXT_LOAD.tipData != null) {
				if (TypingData.tipWindowEnable) {
					setVisible(true);
					updateLocation();
					return;
				}
			}
			setVisible(false);
		});
		updateTipWindowLocation();
	}

	@Override
	public void updateLocation() {
		if (TextPane.TEXT_PANE.isShowing()) {
			pack();
			//位置跟随光标
			Point location = TextPane.TEXT_PANE.getLocationOnScreen();
			if (TypingData.tipWindowLocation == 0) {
				Rectangle r = TextPane.TEXT_PANE.positionRectangle(TypingData.currentTypedIndex).getBounds();
				if (r != null) {
					JScrollBar scrollBar = TextPane.TEXT_PANE.addScrollPane().getVerticalScrollBar();
					int x = location.x + r.x + TextPane.INSECT;
					int y = location.y + r.y - getHeight() - (scrollBar.isVisible() ? scrollBar.getValue() : 0) - 5;
					setLocation(x, y);
				} else {
					setLocation(location.x + TextPane.INSECT, location.y + TextPane.INSECT - getHeight());
				}
			} else {
				setLocation(MainWindow.INSTANCE.getX() + (MainWindow.INSTANCE.getWidth() - getWidth()) / 2, MainWindow.INSTANCE.getY() + 3);
			}
		}
	}

	public void updateTipWindowLocation() {
		if (Keys.boolValue(Keys.TEXT_TIP_WINDOW_ENABLE)) {
			setVisible(true);
			updateLocation();
		} else {
			setVisible(false);
		}
	}

	@Override
	public void pack() {
		this.tipPanel.pack();
		super.pack();
	}
}
