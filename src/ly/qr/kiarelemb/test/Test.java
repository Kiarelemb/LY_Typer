package ly.qr.kiarelemb.test;

import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-13 11:43
 **/
public class Test {


	public static void maino(String[] args) {
		//输入简词
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\[词提]SM.dict.txt", "\t", ((lineText, split) -> {
			if (split.length == 2 && split[0].length() >= 2 && split[1].length() == 2 && !split[1].startsWith(",")) {
				System.out.println(lineText);
			}
		}));
	}

	public static void mainl(String[] args) {
		LinkedList<String> list = QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Desktop\\三矧优秀空码.txt");
		Set<String> set = new HashSet<>(list);
		for (int i = 'a'; i <= 'z'; i++) {
			String alp = String.valueOf((char) i);
			String[] part = {"kl", "cj", "lg", "uo"};
			int count = 0;
			for (String s : part) {
				String code = alp + s;
				if (set.contains(code)) {
					count++;
				}
			}
			if (count == 2) {
				System.out.println(alp);
			}
		}
	}


	public static void maina(String[] args) {

		Map<String, ArrayList<String>> map2 = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				ArrayList<String> lists = map2.computeIfAbsent(split[1], k -> new ArrayList<>());
				lists.add(split[0]);
			}
		});

		Map<String, ArrayList<String>> map = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				String word = split[0];
				if ("EX①②③④⓪⑤⑥⑦⑧⑨⑩".contains(word)) {
					return;
				}
				ArrayList<String> list = map.computeIfAbsent(word, k -> new ArrayList<>());
				list.add(split[1]);
				if (list.size() > 1) {
					String c1 = list.get(0);
					String c2 = list.get(1);
					String temp = c1.length() == 2 ? c1 : c2;
					if (temp.length() == 2) {
						//有重二码字
						ArrayList<String> words = map2.get(temp);
						if (!words.get(0).equals(split[0]) && words.size() != 1) {
//							System.out.println(word + "\t" + c1 + " -> " + c2);
//							String code = c2.length() == 3 ? c2 : c1;
//							if (hardCode(code)) {
//								ArrayList<String> words = map2.get(c2);
							words.remove(word);
							System.out.println("移除：" + temp + word);
							int size = words.size();
							if (size > 1) {
								for (int i = size - 1; i >= 0; i--) {
									if ("EX[".contains(words.get(i))) {
										String remove = words.remove(i);
										System.out.println("删除：" + remove + "\t" + temp);
									} else {
										break;
									}
								}
							}
//							}
						}
					}
				}
			}
		});
		LinkedList<String> codes = new LinkedList<>();
		map2.forEach((k, v) -> {
			for (String s : v) {
				codes.add(s + "\t" + k);
			}
		});
		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict6.yaml", codes);
	}

	private static boolean hardCode(String code) {
		char[] chars = code.toCharArray();
		if (chars[0] == chars[1] || chars[1] == chars[2] || chars[0] == chars[2]) {
			return true;
		}
		if (QRStringUtils.stringContainsAny(code, hard) || ArrayUtils.indexOf(hard, String.valueOf(chars[0]) + chars[2]) != -1) {
			return true;
		}
		boolean flag = false;
		for (String part : parts) {
			int l = 0;
			for (; l < 3; l++) {
				if (part.indexOf(code.charAt(l)) == -1) {
					break;
				}
			}
			if (l == 3) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	static String[] element = QRStringUtils.splitToCharStr("abcdfghjklmnopqrstuvwyz;./");
	static String[] parts = {"qwertgfdsazxcv234", "yuioplkjhbnm890;,./"};
	static String[] hard = {"qz", "zq", "az", "za", "qa", "aq", "zw", "wz", "qc", "cq", "ct", "tc", "zt", "tz", "cr", "rc", "zr", "rz", "cw", "wc", "rv", "vr", "tr", "rt", "rg", "gr", "yj", "jy", "yu", "uy", "by", "yb", "tv", "vt", "td", "dt", "zs", "sz", "./", "/.", ".;", ";.", "/;", ";/", "/p", "p/", "/m", "m/", "o.", ".o", "/o", "o/", "/y", "y/", "ly", "yl", ".y", "y.", ".u", "u.", "/u", "u/", "l.", ".l", "/b", "b/", "my", "ym", "ny", "yn", "bu", "ub", "mu", "um", "nu", "un", ".p", "p.", ".h", "h."};

	public static void maisn(String[] args) {
		Set<String> set = new TreeSet<>();
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				set.add(split[1]);
			}
		});
		Integer[] i = {1, 2, 3, 4};
		Set<Integer> sets = new HashSet<>(List.of(i));

		List<String> list = new LinkedList<>();
		for (String item : element) {
			for (String value : element) {
				for (String s : element) {
					String code = item + value + s;
					if (set.contains(code) || hardCode(code)) {
						continue;
					}
					list.add("\t" + code);
				}
			}
		}
		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Desktop\\三矧\\三矧优秀空码.txt", list);
	}

	public static void mainq(String[] args) {
		Map<String, LinkedList<String>> map2 = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				LinkedList<String> lists = map2.computeIfAbsent(split[1], k -> new LinkedList<>());
				lists.add(split[0]);
			}
		});

		Map<String, String> map = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				char c = split[0].charAt(0);
				if (QRStringUtils.isChineseNormal(c)) {
					if (map.containsKey(split[0])) {
						String code = map.get(split[0]);
						if (code.length() == 2) {
							LinkedList<String> list = map2.get(code);
							if (list.indexOf(split[0]) == 0 && list.size() == 2 && !hardCode(split[1])) {
								System.out.println(split[0] + "\t" + code + "\t->\t" + split[1]);
							}
						}
						return;
					}
					map.put(split[0], split[1]);
				}
			}
		});
	}

	public static void makisn(String[] args) {


		String rest = QRFileUtils.fileReaderWithUtf8All("C:\\Users\\Kiare\\Desktop\\txt.txt");
		String fore8150 = QRFileUtils.fileReaderWithUtf8All("C:\\Users\\Kiare\\Desktop\\8105.txt");


		Map<String, String> fullCodes = new HashMap<>();
		QRFileUtils.fileReaderWithUtf8("D:\\软件开发\\Ghcm_practise\\Config\\矧码单字全码表.txt", "\t", ((lineText, split) -> {
			fullCodes.put(split[0], split[1]);
		}));

		Map<String, LinkedList<String>> map = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				LinkedList<String> lists = map.computeIfAbsent(split[1], k -> new LinkedList<>());
				lists.add(split[0]);
			}
		});
		String[] words = QRStringUtils.getChineseExtraPhrase(rest);
		for (String word : words) {
			String single = fullCodes.get(word);
			String newCode = single.charAt(0) + "" + single.charAt(1) + "" + single.charAt(3);
			String oldCode = single.charAt(0) + "" + single.charAt(1) + "" + single.charAt(2);
			LinkedList<String> wordsList = map.get(newCode);
			LinkedList<String> oldList = map.get(oldCode);
			if (wordsList == null) {
				if (oldList != null) {
					if (oldList.remove(word)) {
						map.put(newCode, new LinkedList<>() {{
							add(word);
						}});
						System.out.println(word + "\t" + newCode);
					}
				}
			} else {
				boolean flag = true;
				for (String s : wordsList) {
					if (fore8150.contains(s)) {
						flag = false;
						break;
					}
				}
				if (flag && oldList != null && oldList.remove(word)) {
					//全是8105外的
					map.remove(newCode);
					map.put(newCode, new LinkedList<>() {{
						add(word);
					}});
					System.out.println(word + "\t" + newCode);
					for (String s : wordsList) {
						String code = fullCodes.get(s);
						LinkedList<String> lists = map.computeIfAbsent(code, k -> new LinkedList<>());
						if (!lists.contains(s)) {
							lists.add(s);
						}
					}
				}
			}
		}


//
//		String ex = "EX";
		LinkedList<String> codes = new LinkedList<>();
		map.forEach((k, v) -> {
			for (String s : v) {
				codes.add(s + "\t" + k);
			}
		});
		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict7.yaml", codes);

//
//
//		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\[词提]SM.dict.txt", "\t", ((lineText, split) -> {
//			simpleCodes.put(split[0], split[1]);
//		}));
//
//
//		String[] phrase = QRStringUtils.getChineseExtraPhrase(words);
//		List<String> write = new ArrayList<>();
//
//		Scanner scan = new Scanner(System.in);
//
//		StringBuilder sb = new StringBuilder();
//		final AtomicReference<String> tmp = new AtomicReference<>();
//		try {
//			QRFileUtils.fileDelete("C:\\Users\\Kiare\\Documents\\rime\\SM.dict9.yaml");
//			FileWriter fileWriter = new FileWriter("C:\\Users\\Kiare\\Documents\\rime\\SM.dict9.yaml", StandardCharsets.UTF_8);
//			BufferedWriter bw = new BufferedWriter(fileWriter);
//			QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
//				if (split.length == 2) {
//					String ch = split[0];
//					if (words.contains(ch)) {
//						boolean flag = false;
//						String code = split[1];
//						String fullCode = fullCodes.get(ch);
//						String threeCode = fullCode.substring(0, 3);
//						ArrayList<String> list = map.get(threeCode);
//						if (list == null) {
//							System.out.println(ch + "\t" + simpleCodes.get(ch) + " --> " + threeCode + "\t三码空码");
//						} else {
//							if (list.size() == 1) {
//								String s = list.get(0);
//								if (!eight.contains(s)) {
//									System.out.println(ch + "\t" + simpleCodes.get(ch) + " --> " + threeCode + "\t非8105的字，可替换");
//									System.out.println("拓\t" + s + "\t" + fullCodes.get(s) + "\t该字非8105，可改全码");
//									flag = true;
//									tmp.set(s);
//								} else {
//									System.out.println("顶\t" + ch + "\t" + simpleCodes.get(ch) + " --> " + threeCode + "\t8105的字，要考虑");
//								}
//							}
//						}
//						String s = scan.nextLine();
//						if (!"n".equals(s)) {
//							lineText = ch + "\t" + threeCode;
//							if (flag) {
//								sb.append(tmp.get());
//								tmp.set(null);
//							}
//						}
//						try {
//							bw.flush();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//					if (sb.indexOf(ch) != -1) {
//						lineText = ch + "\t" + fullCodes.get(ch);
//					}
//				}
//				try {
//					bw.write(lineText);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			});
//			bw.flush();
//			bw.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	public static void mais(String[] args) {

		LinkedList<String> list = QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Desktop\\code.txt");
		Set<String> set = new HashSet<>(list);
		String words = QRFileUtils.fileReaderWithUtf8All("C:\\Users\\Kiare\\Desktop\\8105.txt");
		Map<String, ArrayList<String>> map = new TreeMap<>(String::compareTo);
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict.yaml", "\t", (lineText, split) -> {
			if (split.length == 2) {
				ArrayList<String> lists = map.computeIfAbsent(split[1], k -> new ArrayList<>());
				lists.add(split[0]);
			}
		});

//		String words = QRFileUtils.fileReaderWithUtf8All("D:\\IdeaProjects\\MyTypeTool\\res\\articles\\Individual Character\\常用单字前3.5k.txt").trim();
//		List<String> list = new ArrayList<>(8105);
//		String[] phrase = QRStringUtils.getChineseExtraPhrase(words);
//		for (String c : phrase) {
//			String code = map2.get(c);
//			if (code != null && code.length() == 3) {
//				ArrayList<String> list = map.get(code);
//				String s = list.get(0);
//				if (!s.equals(c) && !words.contains(s)) {
//					System.out.println(c + "\t" + code + "\t" + (list.indexOf(c) + 1));
//				}
//			}
//		}
//		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Desktop\\8105码.txt", list);
//
		List<String> table = new LinkedList<>();
		map.forEach((k, v) -> {
			for (String s : v) {
				table.add(s + "\t" + k);
			}
		});
		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict3.yaml", table);
	}

	public static void mains(String[] args) {
		String words = QRFileUtils.fileReaderWithUtf8All("C:\\Users\\Kiare\\Desktop\\8105.txt");
		Map<String, ArrayList<String>> map = new HashMap<>();
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict - 副本 (2).yaml", "\t", (lineText, split) -> {
			ArrayList<String> list = map.computeIfAbsent(split[1], k -> new ArrayList<>());
			list.add(split[0]);
		});

		List<String> list = new LinkedList<>();
		StringBuilder sb = new StringBuilder();
		QRFileUtils.fileReaderWithUtf8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict - 副本 (2).yaml", "\t", (lineText, split) -> {
			String code = split[1];
			ArrayList<String> word = map.get(code);
			int codeLen = code.length();
			String cut = code.substring(0, codeLen - 1);
			if (codeLen == 4 && map.get(cut) == null) {
				ArrayList<String> newWord = new ArrayList<>();
				if (word.size() == 1) {
					list.add(word.get(0) + "\t" + cut);
					newWord.add(word.get(0));
				} else {
					boolean notFind = true;
					for (String s : word) {
						if (words.contains(s)) {
							list.add(s + "\t" + cut);
							notFind = false;
							newWord.add(s);
							break;
						}
					}
					if (!notFind) {
						word.remove(newWord.get(0));
						for (String s : word) {
							if (words.contains(s)) {
								sb.append(s);
							}
						}
					}
					if (notFind) {
						list.add(word.get(0) + "\t" + cut);
						newWord.add(word.get(0));
					}
				}
				map.put(cut, newWord);
				return;
			}
			list.add(lineText);
		});
		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Documents\\rime\\SM.dict2.yaml", list);
//		QRFileUtils.fileWriterWithUTF8("C:\\Users\\Kiare\\Documents\\rime\\rest.txt", sb.toString());
	}
}
