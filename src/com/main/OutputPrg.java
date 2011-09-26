package com.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JOptionPane;

public class OutputPrg implements ReadFileApi {
	private static Vector<String> allFile = new Vector<String>();
	private static Vector<String> init = new Vector<String>();
	private static Vector<String> end = new Vector<String>();
	private static Vector<String> tools = new Vector<String>();
	private static Vector[] _prog;
	private static Vector[] _end;
	private static Vector[][] tmp;
	private static Vector<Integer> startPoint = new Vector<Integer>();
	private static Vector<Integer> endPoint = new Vector<Integer>();
	private static int progNum = 0;
	private static String startMark = "M6";
	private static String endMark = "M5";
	private static int coordOffset;
	private static int startOffset = 1;
	private static int endOffset = -1;
	private static String coord = ""; // 儲存所有座標
	private static boolean isPrepare;// 是否有預備刀
	private static boolean isDebug = true;

	// 建構子
	public OutputPrg(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = reader.readLine()) != null) {
				allFile.add(line);
				// 將使用者選擇的檔案全部讀入allFile的Vector
			}
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Throw: " + e + " Exception",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setstartMark(String startMark) {
		this.startMark = startMark;
	}

	public void setendMark(String endMark) {
		this.endMark = endMark;
	}

	public void setstartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public void setendOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public Vector<String> getTools() {
		return tools;
	}

	public String getCoord() {
		return coord;
	}

	public void setDebug(boolean b) {
		this.isDebug = b;
	}

	public void analyze() {
		try {
			searchTools(allFile, startMark, -1);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "找不到刀具 錯誤：" + e);
		}
		// 找到所有的刀具
		int startIndex = allFile.indexOf(startMark);
		if (startIndex == -1)
			startIndex = allFile.indexOf("M06");

		String str = (String) allFile.get(startIndex + 1);
		// 判斷座標是否在startIndex的下一行，若不是則是下兩行
		if (str.substring(0, 1).equals("G")) {
			coordOffset = 1;
			isPrepare = false;
			System.out.println("此為無備刀程式");
		} else {
			coordOffset = 2;
			isPrepare = true;
			System.out.println("此為有備刀程式");
		}
		str = (String) allFile.get(startIndex + coordOffset);
		// 判斷座標系是否有P值，並取座標值
		try {
			if (str.indexOf('P') != -1)
				coord = str.substring(3, 11);
			else
				coord = str.substring(3, 6);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "取得座標值時發生錯誤：" + e);
		}
		// 設定程式數目等於刀具數目
		progNum = tools.size();
		_prog = new Vector[progNum]; // 用來放程式
		_end = new Vector[progNum]; // 用來放程式結尾
		for (int i = 0; i < progNum; i++) {
			_prog[i] = new Vector<String>();
			_end[i] = new Vector<String>();
		}
		// 記錄所有程式起始指標的索引值
		try {
			searchIndex(allFile, startMark, startOffset, endMark, endOffset);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "無法建立索引 錯誤：" + e);
		}
		// 記錄檔頭
		try {
			copyInit(startPoint, isPrepare);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "無法複製程式檔頭 錯誤：" + e);
		}
		try {
			// 記錄所有程式
			copyProgram(startPoint, endPoint, isPrepare);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "無法複製程式 錯誤：" + e);
		}
		try {
			// 記錄所有程式的結尾，除了最後一支程式
			copyProgEnd(startPoint, endPoint, isPrepare);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "無法複製程式結尾 錯誤：" + e);
		}
		try {
			// 記錄檔尾
			copyEnd(endPoint);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "無法複製檔尾 錯誤：" + e);
		}
	}

	public void outputFile(String[] coords, String fileName) {
		File file = new File("EROWA_" + fileName);
		int copyTimes = coords.length;
		tmp = new Vector[_prog.length][copyTimes];

		try {
			// loop new tmp Vector
			for (int i = 0; i < _prog.length; i++)
				for (int j = 0; j < copyTimes; j++) {
					tmp[i][j] = new Vector<String>();
				}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "建立Vector錯誤！ " + e);
		}

		try {
			// loop copy _prog to tmp
			for (int i = 0; i < _prog.length; i++)
				for (int j = 0; j < copyTimes; j++)
					for (int k = 0; k < _prog[i].size(); k++)
						tmp[i][j].add((String) _prog[i].get(k));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "複製程式時發生錯誤 ：" + e);
		}

		// loop replace oldCoord to newCoord except the last program
		try {
			for (int i = 0; i < tmp.length - 1; i++)
				for (int j = 0; j < tmp[i].length; j++) {
					if (isPrepare) {
						if (coords[0].length() == 8) {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 11);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(0, str1);
						} else if (coords[0].length() == 7) {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 10);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(0, str1);
						} else {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 6);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(0, str1);
						}
					} else {
						if (coords[0].length() == 8) {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 11);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(2, str1);
						} else if (coords[0].length() == 7) {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 10);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(2, str1);
						} else {
							String str = (String) tmp[i][j].get(0);
							String tmpStr = str.substring(3, 6);
							String str1 = str.replaceFirst(tmpStr, coords[j]);
							tmp[i][j].set(2, str1);
						}
					}
				}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "取代座標時發生錯誤：" + e);
		}

		// loop for replace oldCoord to newCoord to the last program

		if (progNum > 1) {
			if (isPrepare) {
				for (int i = 0; i < copyTimes; i++) {
					if (coords[0].length() == 8) {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 11);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(0, str1);
					} else if (coords[0].length() == 7) {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 10);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(0, str1);
					} else {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 6);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(0, str1);
					}
				}
			} else {
				for (int i = 0; i < copyTimes; i++) {
					if (coords[0].length() == 8) {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 11);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(2, str1);
					} else if (coords[0].length() == 7) {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 10);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(2, str1);
					} else {
						String str = (String) tmp[_prog.length - 1][i].get(0);
						String tmpStr = str.substring(3, 6);
						String str1 = str.replaceFirst(tmpStr, coords[i]);
						tmp[_prog.length - 1][i].set(2, str1);
					}
				}
			}
		} else {
			try {
				for (int i = 0; i < tmp.length; i++)
					for (int j = 0; j < tmp[i].length; j++) {
						if (isPrepare) {
							if (coords[0].length() == 8) {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 11);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								System.out.println(tmp[i][j]);
								tmp[i][j].set(0, str1);
							} else if (coords[0].length() == 7) {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 10);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								tmp[i][j].set(0, str1);
							} else {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 6);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								tmp[i][j].set(0, str1);
							}
						} else {
							if (coords[0].length() == 8) {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 11);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								System.out.println(tmp[i][j]);
								tmp[i][j].set(0, str1);
								System.out.println(tmp[i][j]);
							} else if (coords[0].length() == 7) {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 10);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								tmp[i][j].set(2, str1);
							} else {
								String str = (String) tmp[i][j].get(0);
								String tmpStr = str.substring(3, 6);
								String str1 = str.replaceFirst(tmpStr, coords[j]);
								tmp[i][j].set(2, str1);
							}
						}
					}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "取代座標時發生錯誤：" + e);
			}
		}

		// open fileWriter
		try {
			String defaultEncodingName = System.getProperty("file.encoding");
			System.setProperty("file.encoding", defaultEncodingName);
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
					fileName)));

			// write init
			for (int i = 0; i < init.size(); i++)
				writer.println((String) init.get(i));
			if (isDebug) {
				writer.println("(----------------------------)");
				writer.println("(         檔頭結束           )");
				writer.println("(----------------------------)");
			}

			// write tmp
			for (int i = 0; i < tmp.length; i++) {
				for (int j = 0; j < tmp[i].length; j++) {
					for (int k = 0; k < tmp[i][j].size(); k++) {
						writer.println((String) tmp[i][j].get(k));
					}
					if (isDebug) {
						writer.println("(---------------------------------)");
						writer.println("(  第 [" + (i + 1) + "] 把刀的第 [" + (j + 1)
								+ "] 個座標    )");
						writer.println("(---------------------------------)");
					}
				}
				writeProgEnd(writer, i);
				if (isDebug) {
					if (i != tmp.length - 1) {
						// 最後一把刀不印出程式結尾
						writer.println("(---------------------------------)");
						writer.println("(      第 " + (i + 1) + " 把刀的程式結尾       )");
						writer.println("(---------------------------------)");
					}
				}
			}

			// write end
			for (int i = 0; i < end.size(); i++)
				writer.println((String) end.get(i));
			if (isDebug) {
				writer.println("(----------------------------)");
				writer.println("(          程式檔尾          )");
				writer.println("(----------------------------)");
			}

			writer.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Throw: " + e + " Exception",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		allFile.removeAllElements();
		init.removeAllElements();
		end.removeAllElements();
		tools.removeAllElements();
		startPoint.removeAllElements();
		endPoint.removeAllElements();
	}

	private static void writeProgEnd(PrintWriter writer, int index) {
		for (int i = 0; i < _end[index].size(); i++)
			writer.println((String) _end[index].get(i));
	}

	public static void searchTools(Vector vector, String mark, int offset) {
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i).equals(mark) || vector.get(i).equals("M06")) {
				tools.addElement((String) vector.get(i + offset));
			}
		}
	}

	public static void searchIndex(Vector vector, String startMark,
			int startOffset, String endMark, int endOffset) {
		for (int i = 0; i < vector.size(); i++)
			if (vector.get(i).equals(startMark) || vector.get(i).equals("M06"))
				startPoint.addElement(i + startOffset);
			else if (vector.get(i).equals(endMark) || vector.get(i).equals("M05")) {
				if (vector.get(i - 1).equals("M9") || vector.get(i - 1).equals("M09")) {
					endPoint.addElement(i - 1);
				} else if (vector.get(i - 2).equals("M9")
						|| vector.get(i - 2).equals("M09")) {
					endPoint.addElement(i - 2);
				} else if (vector.get(i - 1).equals("G05P0")) {
					endPoint.addElement(i - 1);
				} else {
					endPoint.addElement(i);
				}
			}
	}

	private static void copyInit(Vector startIndex, boolean isPrepare) {
		if (isPrepare == false) {
			for (int i = 0; i < (Integer) startIndex.get(0); i++)
				init.addElement(allFile.get(i));
		} else {
			for (int i = 0; i < (Integer) startIndex.get(0) + 1; i++)
				init.addElement(allFile.get(i));
		}
	}

	private static void copyProgram(Vector startIndex, Vector endIndex,
			boolean isPrepare) {
		if (isPrepare == false) {
			for (int i = 1; i <= progNum; i++)
				for (int j = (Integer) startIndex.get(i - 1); j < (Integer) endIndex
						.get(i - 1); j++) {
					_prog[i - 1].addElement(allFile.get(j));
				}
		} else {
			for (int i = 1; i <= progNum; i++)
				for (int j = (Integer) startIndex.get(i - 1) + 1; j < (Integer) endIndex
						.get(i - 1); j++) {
					_prog[i - 1].addElement(allFile.get(j));
				}
			int num = (Integer) startIndex.get(progNum - 1);
			String str = allFile.get(num);
			_prog[progNum - 1].insertElementAt(str, 0);
		}
	}

	private static void copyProgEnd(Vector startIndex, Vector endIndex,
			boolean isPrepare) {
		if (isPrepare == false) {
			for (int i = 1; i < progNum; i++)
				for (int j = (Integer) endIndex.get(i - 1); j < (Integer) startIndex
						.get(i); j++)
					_end[i - 1].addElement(allFile.get(j));
		} else {
			for (int i = 1; i < progNum; i++)
				for (int j = (Integer) endIndex.get(i - 1); j < (Integer) startIndex
						.get(i); j++)
					_end[i - 1].addElement(allFile.get(j));
		}
	}

	private static void copyEnd(Vector endIndex) {
		for (int i = ((Integer) endIndex.get(progNum - 1)); i < allFile.size(); i++)
			end.addElement(allFile.get(i));
	}

}
