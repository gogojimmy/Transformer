package com.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame extends JFrame {

	private JPanel btnPanel;
	private JPanel txtPanel;
	private JPanel ckPanel;
	private JButton openFileButton;
	private JButton outputButton;
	private JButton setOutputButton;
	private JLabel companyLabel;
	private JCheckBox[] chuck;
	private JCheckBox erowaCheckBox = new JCheckBox();
	private JCheckBox[] selectAll;
	private static Vector<String> coords = new Vector<String>();
	private JFileChooser fileChooser = new JFileChooser();
	private JFileChooser pathChooser = new JFileChooser();
	private JTextField outputPath;
	private JTextArea textArea;
	private String filePath;
	private String[] content;
	private static Vector tools = new Vector();
	private static ReadFileApi tmp;
	private static String fileName;
	protected int chuckRows;
	protected int chuckColumns;
	protected int chuckHeight;
	protected int chuckWidth;

	public Frame() {

		super("TRANSFORMER  v1.0");
		btnPanel = new JPanel(new FlowLayout());
		ckPanel = new JPanel();
		ckPanel.setLayout(null);

		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		openFileButton = new JButton("開啟檔案");
		outputButton = new JButton("輸出檔案");

		Box box = Box.createHorizontalBox();

		textArea = new JTextArea(25, 15);
		textArea.setEditable(false);
		companyLabel = new JLabel(
				"力永實業有限公司開發製作   電話：02-82123006  Mail:erowa@lyedm.com");
		companyLabel.setFont(new Font("新細明體", Font.BOLD, 12));
		companyLabel.setBounds(10, 450, 500, 20);
		box.add(new JScrollPane(textArea));

		fileChooser.addChoosableFileFilter(new Filter());

		chuckRows = 4;
		chuckColumns = 5;
		chuckHeight = 50;
		chuckWidth = 0;

		chuck = new JCheckBox[chuckRows * chuckColumns];
		selectAll = new JCheckBox[chuckRows];

		for (int i = 0; i < (chuckRows * chuckColumns); i++) {
			chuck[i] = new JCheckBox();
			chuck[i].setText((i + 1) + " 號夾頭");
		}

		int k = 0;
		CheckBoxHandler handler = new CheckBoxHandler();

		for (int i = 0; i < chuckRows; i++) {
			for (int j = 0; j < chuckColumns; j++) {
				chuck[k].setBounds(chuckWidth, chuckHeight, 100, 20);
				chuckHeight += 50;
				k++;
			}
			selectAll[i] = new JCheckBox();
			selectAll[i].setBounds(chuckWidth, chuckHeight, 100, 20);
			selectAll[i].setText("全選");
			selectAll[i].addItemListener(handler);
			chuckWidth += 100;
			chuckHeight = 50;
		}

		erowaCheckBox.setBounds(0, 350, 100, 20);
		erowaCheckBox.setText("EROWA");
		erowaCheckBox.addItemListener(handler);

		outputButton.setBounds(150, 350, 200, 50);

		ButtonHandler btnHandler = new ButtonHandler();
		openFileButton.addActionListener(btnHandler);
		outputButton.addActionListener(btnHandler);

		btnPanel.add(openFileButton);
		btnPanel.add(box);
		btnPanel.setBounds(0, 0, 200, 450);

		for (int i = 0; i < chuck.length; i++)
			ckPanel.add(chuck[i]);
		for (int i = 0; i < selectAll.length; i++)
			ckPanel.add(selectAll[i]);

		ckPanel.add(erowaCheckBox);

		ckPanel.add(outputButton);
		ckPanel.setBounds(220, 0, 450, 450);

		contentPane.add(btnPanel);
		contentPane.add(ckPanel);
		contentPane.add(companyLabel);
	}

	private class CheckBoxHandler implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getSource() == selectAll[0]) {
				if (selectAll[0].isSelected()) {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i].setSelected(true);
				} else {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i].setSelected(false);
				}
			}

			if (event.getSource() == selectAll[1]) {
				if (selectAll[1].isSelected()) {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 5].setSelected(true);
				} else {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 5].setSelected(false);
				}
			}

			if (event.getSource() == selectAll[2]) {
				if (selectAll[2].isSelected()) {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 10].setSelected(true);
				} else {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 10].setSelected(false);
				}
			}

			if (event.getSource() == selectAll[3]) {
				if (selectAll[3].isSelected()) {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 15].setSelected(true);
				} else {
					for (int i = 0; i < chuckColumns; i++)
						chuck[i + 15].setSelected(false);
				}
			}

			if (event.getSource() == erowaCheckBox) {
				if (erowaCheckBox.isSelected()) {
					for (int i = 0; i < chuckColumns + 1; i++)
						chuck[i].setSelected(true);
				} else {
					for (int i = 0; i < chuckColumns + 1; i++)
						chuck[i].setSelected(false);
				}
			}

		}

	}

	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	private void setFrameTitle(String title) {
		this.setTitle("Transformer v1.0 正在開啟：" + title);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getActionCommand().equals("開啟檔案")) {

				int ret = fileChooser.showOpenDialog(null);
				File file = fileChooser.getSelectedFile();
				if (ret == JFileChooser.APPROVE_OPTION) {
					filePath = file.getPath();
					fileName = file.getName();
					setFrameTitle(fileName);
					ReadFileApi rf = new OutputPrg(filePath);
					tmp = rf;
					rf.analyze();
					tools = rf.getTools();

					for (int i = 0; i < tools.size(); i++) {
						textArea.append(tools.get(i) + "\n");
					}

					// 判斷座標最後一個字元判斷是否為數字來確定是否為雙位數
					char c = rf.getCoord().charAt(rf.getCoord().length() - 1);
					String str = rf.getCoord();

					if (!Character.isDigit(c))
						str = str.substring(0, 7);
					// 判斷座標字元長度來擷取後面數字部分
					if ((str.length() == 8)) {
						int num = Integer.valueOf(str.substring(6));
						for (int i = 0; i < chuck.length; i++) {
							chuck[i].setText(str.substring(0, 6) + num);
							num++;
						}
					} else if (str.length() == 7) {
						int num = Integer.valueOf(str.substring(6));
						for (int i = 0; i < chuck.length; i++) {
							chuck[i].setText(str.substring(0, 6) + num);
							num++;
						}
					} else {
						int num = Integer.valueOf(str.substring(1));
						for (int i = 0; i < chuck.length; i++) {
							chuck[i].setText(str.substring(0, 1) + num);
							num++;
						}
					}
				}
			} else if (event.getActionCommand().equals("輸出檔案")) {
				for (int i = 0; i < chuck.length; i++) {
					if (chuck[i].isSelected() != false)
						coords.add(chuck[i].getText());
				}
				System.out.println(coords);

				String[] strings = new String[coords.size()];

				for (int i = 0; i < coords.size(); i++) {
					strings[i] = coords.get(i).toString();
				}

				String file = "EROWA_" + fileName;

				pathChooser.setDialogTitle("儲存檔案");
				pathChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				pathChooser.setSelectedFile(new File(file));

				int ret = pathChooser.showSaveDialog(null);
				File saveFile = pathChooser.getSelectedFile();
				if (ret == JFileChooser.APPROVE_OPTION) {
					filePath = saveFile.getPath();
				}

				tmp.outputFile(strings, filePath);
				textArea.setText("");
				for (int i = 0; i < chuck.length; i++) {
					chuck[i].setText((i + 1) + "號夾頭");
					chuck[i].setSelected(false);
				}
				for (int i = 0; i < selectAll.length; i++) {
					selectAll[i].setSelected(false);
				}
				erowaCheckBox.setSelected(false);
				setFrameTitle("");
				coords.removeAllElements();
				tools.removeAllElements();
			}
		}
	}
}
