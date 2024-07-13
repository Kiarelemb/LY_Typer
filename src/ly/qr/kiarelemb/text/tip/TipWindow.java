package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import swing.qr.kiarelemb.window.basic.QREmptyDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

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
            if (TypingData.tipWindowEnable) {
                updateLocation();
            }
        });

        TextViewPane.TEXT_VIEW_PANE.addSetTextFinishedAction(e -> {
            if (TypingData.tipEnable && TextLoad.TEXT_LOAD.tipData != null) {
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
        if (TextViewPane.TEXT_VIEW_PANE.isShowing()) {
            SwingUtilities.invokeLater(() -> {
                pack();
                Point location = TextViewPane.TEXT_VIEW_PANE.getLocationOnScreen();
                //位置跟随光标
                if (TypingData.tipWindowLocation == 0) {
                    Rectangle2D r2 = TextViewPane.TEXT_VIEW_PANE.positionRectangle(TypingData.currentTypedIndex);
                    if (r2 != null) {
                        Rectangle r = r2.getBounds();
                        int x = location.x + r.x;
                        int y = location.y + r.y - getHeight() - 5;
                        setLocation(x, y);
                    } else {
                        setLocation(location.x + TextViewPane.INSECT, location.y + TextViewPane.INSECT - getHeight());
                    }
                } else {
                    setLocation(MainWindow.INSTANCE.getX() + (MainWindow.INSTANCE.getWidth() - getWidth()) / 2, MainWindow.INSTANCE.getY() + 3);
                }
                MainWindow.INSTANCE.grabFocus();
            });
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