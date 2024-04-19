package ly.qr.kiarelemb.component.contract.state;

import com.sun.jna.platform.win32.User32;
import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.qq.WindowAPI;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRThreadBuilder;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 21:05
 **/
public class GroupButton extends QRRoundButton {
    private static final ThreadPoolExecutor GROUP_CHANGE = QRThreadBuilder.singleThread("groupChange");
    public static final GroupButton groupBtn = new GroupButton();

    private GroupButton() {
        super(Keys.strValue(Keys.QUICK_KEY_GROUP) + " / 点击换群");
        setToolTipText(Keys.strValue(Keys.QUICK_KEY_GROUP) + " / 点击换群");
        QRSwing.registerGlobalAction(Keys.strValue(Keys.QUICK_KEY_GROUP),
                e -> GroupButton.groupBtn.clickInvokeLater(), true);
    }

    private final ArrayList<String> windows = new ArrayList<>();
    /**
     * 是否已找到群聊
     */
    private boolean groupLinked = false;
    /**
     * 找到的群聊名
     */
    private String groupName;
    /**
     * 是否为QQ NT
     */
    private boolean isQQNT = false;
    private int groupIndex = -1;

    @Override
    protected void actionEvent(ActionEvent o) {
        //放线程就不会卡
        GROUP_CHANGE.submit(() -> {
            ArrayList<String> windows = WindowAPI.getQQWindows();
            if (!QRArrayUtils.isEqualList(this.windows, windows)) {
                if (this.groupName() != null) {
                    this.groupIndex = windows.indexOf(this.groupName());
                }
                this.windows.clear();
                this.windows.addAll(windows);
            }
            int size = this.windows.size();
            if (size == 0) {
                this.setGroupLinked(false);
                QRSmallTipShow.display(MainWindow.INSTANCE, "没有找到群聊");
                return;
            }
            this.groupIndex++;
            if (this.groupIndex == size) {
                this.groupIndex = 0;
            }
            this.setGroupName(this.windows.get(this.groupIndex));
            Matcher matcher = Pattern.compile("等[0-9]+个会话").matcher(this.groupName());
            if (matcher.find()) {
                setText(this.groupName().substring(0, matcher.start()));
            } else {
                if ("QQ".equals(this.groupName()) && User32.INSTANCE.FindWindow("TXGuiFoundation", "QQ") == null) {
                    setText("QQ NT");
                    this.isQQNT = true;
                } else {
                    setText(this.groupName());
                    this.isQQNT = false;
                }
            }
            this.setGroupLinked(true);
            TypingData.windowFresh();
        });
    }

    /**
     * 找到的群聊名
     */
    public String groupName() {
        return this.groupName;
    }

    public boolean isQQNT() {
        return this.isQQNT;
    }

    private void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 是否已找到群聊
     */
    public boolean groupLinked() {
        return this.groupLinked;
    }

    private void setGroupLinked(boolean groupLinked) {
        this.groupLinked = groupLinked;
    }
}