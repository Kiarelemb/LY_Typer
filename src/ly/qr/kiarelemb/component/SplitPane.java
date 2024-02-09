package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.tip.TipPanel;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.component.basic.QRSplitPane;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-27 22:33
 **/
public class SplitPane extends QRSplitPane {
	public final SplitTipPanel tipPanel;
	public static final SplitPane SPLIT_PANE = new SplitPane();

	private SplitPane() {
		super(JSplitPane.VERTICAL_SPLIT);

		//底部的面板需要放词提
		this.tipPanel = new SplitTipPanel();
		this.tipPanel.setPreferredSize(200, 100);

		setTopComponent(TextPane.TEXT_PANE.addScrollPane());
		setBottomComponent(TyperTextPane.TYPER_TEXT_PANE.addScrollPane());

		setResizeWeight(Keys.floatValue(Keys.WINDOW_SPLIT_WEIGHT));
		//更新编码提示的位置
		updateTipPaneLocation();
	}

	/**
	 * 更新编码提示的位置
	 */
	public void updateTipPaneLocation() {
		if (Keys.boolValue(Keys.TEXT_TIP_PANEL_ENABLE)) {
			this.tipPanel.pack();
			int value = Keys.intValue(Keys.TEXT_TIP_PANEL_LOCATION);
			this.tipPanel.setOnNorth(value % 2 == 0);
			switch (value) {
				case 0, 1 -> {
					setTopComponent(null);
					QRPanel panel = new QRPanel();
					panel.setLayout(new BorderLayout());
					panel.add(TextPane.TEXT_PANE.addScrollPane(), BorderLayout.CENTER);
					panel.add(this.tipPanel, value == 0 ? BorderLayout.NORTH : BorderLayout.SOUTH);
					setTopComponent(panel);
				}
				default -> {
					setBottomComponent(null);
					QRPanel panel = new QRPanel();
					panel.setLayout(new BorderLayout());
					panel.add(TyperTextPane.TYPER_TEXT_PANE.addScrollPane(), BorderLayout.CENTER);
					panel.add(this.tipPanel, value == 3 ? BorderLayout.SOUTH : BorderLayout.NORTH);
					setBottomComponent(panel);
				}
			}
		} else {
			if (this.tipPanel.getParent() != null) {
				QRActionRegister loopToRemove = component -> {
					Component[] cms = ((QRPanel) component).getComponents();
					for (Component com : cms) {
						if (com instanceof QRPanel pane && pane != this.tipPanel) {
							pane.remove(this.tipPanel);
						}
					}
				};
				Component[] cms = getComponents();
				for (Component com : cms) {
					if (com instanceof QRPanel pane) {
						loopToRemove.action(pane);
					}
				}
			}
		}
	}

	@Override
	public void componentFresh() {
		super.componentFresh();
		if (this.tipPanel != null) {
			updateTipPaneLocation();
		}
	}

	static class SplitTipPanel extends TipPanel {
		private boolean isOnNorth = false;

		@Override
		public void tipUpdate() {
			super.tipUpdate();
			borderUpdate();
		}

		public void setOnNorth(boolean onNorth) {
			this.isOnNorth = onNorth;
		}

		void borderUpdate() {
			if (this.isOnNorth) {
				setBorder(new MatteBorder(0, 0, 1, 0, QRColorsAndFonts.BORDER_COLOR));
			} else {
				setBorder(new MatteBorder(1, 0, 0, 0, QRColorsAndFonts.BORDER_COLOR));
			}
		}

		@Override
		public void componentFresh() {
			super.componentFresh();
			borderUpdate();
		}
	}
}
