package ly.qr.kiarelemb.text.tip;

import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRStringUtils;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * @author 取自长流，QR狂改
 * @program: 揽月跟打器
 * @apiNote: 词提
 * @create 2023-01-22 14:32
 **/
public class TextTipEnhance extends AbstractTextTip {

	@Override
	public void changeColorTip(String text) {
		changeColorTip(QRStringUtils.getChineseExtraPhrase(text));
	}

	/**
	 * 支持三码和拓展字
	 */
	private void changeColorTip(String[] texts) {
		final int articleLength = texts.length;
		if (articleLength == 0) {
			return;
		}
		String symbol = "。，";
		String codeTemp;
		int maxLen = this.codeLength + 1;
		String strTemp;
		this.subscriptInstances = new AbstractTextTip.SubscriptInstance[articleLength];
		try {
			for (int i = articleLength - 1; i >= 0; i--) {
				strTemp = texts[i];
				codeTemp = this.wordCode.get(strTemp);
				if (codeTemp == null) {
					/* + "?"*/
					codeTemp = strTemp;
				} else if (articleLength > i + 1 && codeTemp.endsWith("_") && symbol.contains(this.subscriptInstances[i + 1].getWord())
				           && !(articleLength > i + 2
				                && this.symbolEntry.contains(this.subscriptInstances[i + 1].getWord() + this.subscriptInstances[i + 2].getWord()))) {
					codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
				} else if (articleLength > i + 1 && codeTemp.endsWith("_") && codeTemp.length() == maxLen) {
					codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
				}
				AbstractTextTip.SubscriptInstance subscriptInstance = new AbstractTextTip.SubscriptInstance(i, strTemp, codeTemp);
				this.subscriptInstances[i] = subscriptInstance;
			}
			this.subscriptInstances[0].setCodeLengthTemp(this.subscriptInstances[0].getWordCode().length());
			for (int j = 0; j < articleLength; j++) {
				//获取前一字符的最短编码长度。
				int preCodeLengthTemp = j == 0 || this.subscriptInstances[j - 1] == null ? 0 : this.subscriptInstances[j - 1].getCodeLengthTemp();
				//判断每个长度是否有词
//                for (int i = wordsCodeList.size() - 1; i >= 0; i--) {
				for (int i = Math.min(articleLength, this.wordsCodeList.size() - 1); i >= 0; i--) {
					final HashMap<String, String> hashMap = this.wordsCodeList.get(i);
					if (hashMap.size() == 0) {
						continue;
					}
//                    if (articleLength >= j + i + 2 && hashMap.containsKey(strTemp = article.substring(j, j + i + 2))) {
					if (articleLength >= j + i + 2) {
						strTemp = QRArrayUtils.getArraySubstring(texts, j, i + 2);
						if (hashMap.containsKey(strTemp)) {
							codeTemp = hashMap.get(strTemp);
							if (articleLength > j + i + 2 && (codeTemp.endsWith("_") || codeTemp.length() == this.codeLength)) {
								final AbstractTextTip.SubscriptInstance subscriptInstance1 = this.subscriptInstances[j + i + 1];
								if (subscriptInstance1 == null) {
									break;
								}
								if (symbol.contains(subscriptInstance1.getWord())
								    && !(articleLength > j + i + 3
								         && this.symbolEntry.contains(subscriptInstance1.getWord() + this.subscriptInstances[j + i + 3].getWord()))) {
									if (codeTemp.endsWith("_")) {
										codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
									}
								}
							}
							int nextCodeLengthTemp = preCodeLengthTemp + codeTemp.length();
							final AbstractTextTip.SubscriptInstance subscriptInstance = this.subscriptInstances[j + i + 1];
							subscriptInstance.addPre(nextCodeLengthTemp, j, strTemp, codeTemp, getType(codeTemp));
							final int temp = subscriptInstance.getCodeLengthTemp();
							if (temp == 0 || temp > nextCodeLengthTemp) {
								subscriptInstance.setCodeLengthTemp(nextCodeLengthTemp);
							}
						}
					}
					if (j > 0 && j < this.subscriptInstances.length) {
						final AbstractTextTip.SubscriptInstance instance = this.subscriptInstances[j];
						if (instance == null) {
							continue;
						}
						int wordCodeLength = instance.getWordCode().length();
						int thisCodeLength = instance.getCodeLengthTemp();
						int nextCodeLengthTemp = preCodeLengthTemp + wordCodeLength;
						if (thisCodeLength == 0) {
							instance.setCodeLengthTemp(nextCodeLengthTemp);
						} else if (thisCodeLength > nextCodeLengthTemp) {
							instance.setCodeLengthTemp(nextCodeLengthTemp);
						}
					}
				}
			}
			for (int i = articleLength - 1; i >= 0; i--) {
				boolean sign = true;
				AbstractTextTip.SubscriptInstance subscriptInstance = this.subscriptInstances[i];
				int codeLengthTemp = subscriptInstance.getCodeLengthTemp();
				AbstractTextTip.SubscriptInstance.PreInfo preInfo = subscriptInstance.getPreInfoMap().get(codeLengthTemp);
				int pre = 0;
				if (preInfo == null || preInfo.getPre().size() == 0) {
					sign = false;
				} else {
					pre = preInfo.getMinPre();
				}
				//subscriptInstances[i].getShortCodePreInfo().getType(subscriptInstance.getPreInfoMap().get(subscriptInstances[i].getCodeLengthTemp())getMinPre())
				final AbstractTextTip.SubscriptInstance instance = this.subscriptInstances[pre];
				if (sign && instance.isNotUseWordSign() && !(!instance.isUseSign() && this.subscriptInstances[i].isUseSign())) {
					instance.setType(this.subscriptInstances[i].getShortCodePreInfo().getType(pre));
					instance.setNext(i);
					instance.setUseWordSign(true);
					for (int i2 = pre; i2 <= i; i2++) {
						instance.setUseSign(true);
					}
				}
				for (Integer key : subscriptInstance.getPreInfoMap().keySet()) {
					AbstractTextTip.SubscriptInstance.PreInfo preinfo = this.subscriptInstances[i].getPreInfoMap().get(key);
					for (int preTemp : preinfo.getPre().keySet()) {
						if (preTemp > pre) {
							final AbstractTextTip.SubscriptInstance instance1 = this.subscriptInstances[preTemp];
							if (instance1.isNotUseWordSign()) {
								instance1.setNext(i);
								instance1.setType(preinfo.getType(preTemp));
							}
						}
					}
				}
				if (sign) {
					i = pre;
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "changeColorTip", e);
		}
	}
}