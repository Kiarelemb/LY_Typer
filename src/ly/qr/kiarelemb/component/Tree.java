package ly.qr.kiarelemb.component;

import swing.qr.kiarelemb.basic.QRTree;
import swing.qr.kiarelemb.resource.QRSwingInfo;

import javax.swing.*;
import javax.swing.tree.TreeNode;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 10:56
 **/
public class Tree extends QRTree {
	public Tree(TreeNode root) {
		super(root);
		setCellRenderer(new TreeCellRenderer());
	}

	static class TreeCellRenderer extends QRTreeCellRenderer {
		public TreeCellRenderer() {
			super();
			this.openIcon = new ImageIcon(QRSwingInfo.loadUrl("minus.png"));
			this.closedIcon = new ImageIcon(QRSwingInfo.loadUrl("plus.png"));
		}
	}
}