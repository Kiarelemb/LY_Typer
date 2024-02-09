package ly.qr.kiarelemb.text.tip;

import java.util.HashMap;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-03 12:27
 **/
public class TextTip extends AbstractTextTip {

	@Override
	public void changeColorTip(String article) {
		int articleLength = article.length();
		String symbol = "。，";
		String codeTemp;
		int maxLen = this.codeLength + 1;
		String strTemp;
		this.subscriptInstances = new SubscriptInstance[article.length()];
		try {
			for (int i = articleLength - 1; i >= 0; i--) {
				strTemp = article.substring(i, i + 1);
				char charTemp = strTemp.toCharArray()[0];
				codeTemp = this.wordCode.get(strTemp);
				if (codeTemp == null) {
					if ((charTemp >= 'a' && charTemp <= 'z') || (charTemp >= 'A' && charTemp <= 'Z') || (charTemp >= '0' && charTemp <= '9')) {
						codeTemp = strTemp;
					} else {
						codeTemp = strTemp/* + "?"*/;
					}
				} else if (articleLength > i + 1 && codeTemp.endsWith("_") && symbol.contains(this.subscriptInstances[i + 1].getWord()) && !(articleLength > i + 2 && this.symbolEntry.contains(this.subscriptInstances[i + 1].getWord() + this.subscriptInstances[i + 2].getWord()))) {
					codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
				} else if (articleLength > i + 1 && codeTemp.endsWith("_") && codeTemp.length() == maxLen) {
					codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
				}
				SubscriptInstance subscriptInstance = new SubscriptInstance(i, strTemp, codeTemp);
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
					if (articleLength >= j + i + 2 && hashMap.containsKey(strTemp = article.substring(j, j + i + 2))) {
						if (hashMap.containsKey(strTemp)) {
							codeTemp = hashMap.get(strTemp);
							if (articleLength > j + i + 2 && (codeTemp.endsWith("_") || codeTemp.length() == this.codeLength)) {
								final SubscriptInstance subscriptInstance1 = this.subscriptInstances[j + i + 1];
								if (subscriptInstance1 == null) {
									break;
								}
								if (symbol.contains(subscriptInstance1.getWord()) && !(articleLength > j + i + 3 && this.symbolEntry.contains(subscriptInstance1.getWord() + this.subscriptInstances[j + i + 3].getWord()))) {
									if (codeTemp.endsWith("_")) {
										codeTemp = codeTemp.substring(0, codeTemp.length() - 1);
									}
								}
							}
							int nextCodeLengthTemp = preCodeLengthTemp + codeTemp.length();
							this.subscriptInstances[j + i + 1].addPre(nextCodeLengthTemp, j, strTemp, codeTemp, getType(codeTemp));
							if (this.subscriptInstances[j + i + 1].getCodeLengthTemp() == 0 || this.subscriptInstances[j + i + 1].getCodeLengthTemp() > nextCodeLengthTemp) {
								this.subscriptInstances[j + i + 1].setCodeLengthTemp(nextCodeLengthTemp);
							}
						}
						if (j > 0) {
							int wordCodeLength = this.subscriptInstances[j].getWordCode().length();
							int thisCodeLength = this.subscriptInstances[j].getCodeLengthTemp();
							int nextCodeLengthTemp = preCodeLengthTemp + wordCodeLength;
							if (thisCodeLength == 0) {
								this.subscriptInstances[j].setCodeLengthTemp(nextCodeLengthTemp);
							} else if (thisCodeLength > nextCodeLengthTemp) {
								this.subscriptInstances[j].setCodeLengthTemp(nextCodeLengthTemp);
							}
						}
					}
				}
				for (int i = article.length() - 1; i >= 0; i--) {
					boolean sign = true;
					SubscriptInstance subscriptInstance = this.subscriptInstances[i];
					int codeLengthTemp = subscriptInstance.getCodeLengthTemp();
					SubscriptInstance.PreInfo preInfo = subscriptInstance.getPreInfoMap().get(codeLengthTemp);
					int pre = 0;
					if (preInfo == null || preInfo.getPre().size() == 0) {
						sign = false;
					} else {
						pre = preInfo.getMinPre();
					}
					//subscriptInstances[i].getShortCodePreInfo().getType(subscriptInstance.getPreInfoMap().get(subscriptInstances[i].getCodeLengthTemp())getMinPre())
					if (sign && this.subscriptInstances[pre].isNotUseWordSign() && !(!this.subscriptInstances[pre].isUseSign() && this.subscriptInstances[i].isUseSign())) {
						this.subscriptInstances[pre].setType(this.subscriptInstances[i].getShortCodePreInfo().getType(pre));
						this.subscriptInstances[pre].setNext(i);
						this.subscriptInstances[pre].setUseWordSign(true);
						for (int i2 = pre; i2 <= i; i2++) {
							this.subscriptInstances[pre].setUseSign(true);
						}
					}
					for (Integer key : subscriptInstance.getPreInfoMap().keySet()) {
						SubscriptInstance.PreInfo preinfo = this.subscriptInstances[i].getPreInfoMap().get(key);
						for (int preTemp : preinfo.getPre().keySet()) {
							if (preTemp > pre && this.subscriptInstances[preTemp].isNotUseWordSign()) {
								this.subscriptInstances[preTemp].setNext(i);
								this.subscriptInstances[preTemp].setType(preinfo.getType(preTemp));
							}
						}
					}
					if (sign) {
						i = pre;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
