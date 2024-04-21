package ly.qr.kiarelemb.component.setting;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.Tree;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.component.setting.panel.*;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.component.assembly.QRMutableTreeNode;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.combination.QRTreeTabbedPane;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.window.basic.QRDialog;

import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-30 12:51
 **/
public class SettingWindow extends QRDialog {
    private boolean sure = false;

    public SettingWindow() {
        super(MainWindow.INSTANCE);
        setTitle("设置");
        setTitlePlace(QRDialog.CENTER);
        setSize(700, 470);
        //移动时，主窗体不移动
        setParentWindowNotFollowMove();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBorder(new EmptyBorder(5, 10, 0, 10));

        QRMutableTreeNode root = new QRMutableTreeNode("设置");
        root.setCollapsable(false);
        Tree tree = new Tree(root);
        tree.setRowHeight(35);
        tree.setRootVisible(false);
        tree.setPreferredSize(new Dimension(150, 100));
        QRMutableTreeNode normalNode = root.addChild("常规");
        QRMutableTreeNode appearance = normalNode.addChild("外观");
        QRMutableTreeNode mainWindow = normalNode.addChild("窗体");
        QRMutableTreeNode gradeSend = normalNode.addChild("成绩单");

        QRMutableTreeNode typeNode = root.addChild("跟打");
        QRMutableTreeNode tip = typeNode.addChild("词提");
        QRMutableTreeNode innerInput = typeNode.addChild("内置输入");

        QRMutableTreeNode sendNode = root.addChild("发文");

        QRMutableTreeNode keyNode = root.addChild("快捷键");
        //需要记录哪些节点展开了，哪些又没展开
        tree.expendAll();

        QRTreeTabbedPane treeTabbedPane = new QRTreeTabbedPane(tree, BorderLayout.WEST) {
            @Override
            public void componentFresh() {
                super.componentFresh();
                setBorder(new MatteBorder(0, 0, 2, 0, QRColorsAndFonts.LINE_COLOR));
            }
        };
        treeTabbedPane.setBorder(new MatteBorder(0, 0, 2, 0, QRColorsAndFonts.LINE_COLOR));
        //加入面板
        AppearancePanel appearancePanel = new AppearancePanel(this);
        WindowPanel windowPanel = new WindowPanel(this);
        GradeSendPanel gradeSendPanel = new GradeSendPanel(this);
        treeTabbedPane.addTreeNodePointToPanel(appearance, appearancePanel);
        treeTabbedPane.addTreeNodePointToPanel(mainWindow, windowPanel);
        treeTabbedPane.addTreeNodePointToPanel(gradeSend, gradeSendPanel);
        treeTabbedPane.addTreeNodePointToPanel(normalNode, new JumpPanel(treeTabbedPane, appearancePanel, windowPanel, gradeSendPanel));

        TipSettingPanel tipSettingPanel = new TipSettingPanel(this);
        InnerInputPanel innerInputPanel = new InnerInputPanel(this);
        treeTabbedPane.addTreeNodePointToPanel(tip, tipSettingPanel);
        treeTabbedPane.addTreeNodePointToPanel(innerInput, innerInputPanel);
        treeTabbedPane.addTreeNodePointToPanel(typeNode, new JumpPanel(treeTabbedPane, tipSettingPanel, innerInputPanel));


        this.mainPanel.add(treeTabbedPane, BorderLayout.CENTER);

        //region 底部面板
        QRPanel bottomPanel = new QRPanel();

        QRRoundButton sureBtn = new QRRoundButton("确认") {
            @Override
            protected void actionEvent(ActionEvent o) {
                SettingWindow.this.sure = true;
                dispose();
            }
        };
        QRRoundButton cancelBtn = new QRRoundButton("取消") {
            @Override
            protected void actionEvent(ActionEvent o) {
                SettingWindow.this.sure = false;
                dispose();
            }
        };
        QRRoundButton backToDefaultBtn = new QRRoundButton("恢复默认设置并关闭") {
            @Override
            protected void actionEvent(ActionEvent o) {
                SettingsItem.CHANGE_MAP.putAll(Keys.DEFAULT_MAP);
                sureBtn.clickInvokeLater();
            }
        };

        bottomPanel.setLayout(null);
        sureBtn.setBounds(480, 10, 78, 30);
        cancelBtn.setBounds(580, 10, 78, 30);
        backToDefaultBtn.setBounds(5, 10, 180, 30);
        bottomPanel.add(sureBtn);
        bottomPanel.add(cancelBtn);
        bottomPanel.add(backToDefaultBtn);
        //endregion 底部面板

        bottomPanel.setPreferredSize(700, 50);
        this.mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    public boolean save() {
        return this.sure;
    }
}