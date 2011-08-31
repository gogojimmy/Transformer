package com.wsy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.encrypt.decrypt;
import com.encrypt.encrypt;
import com.main.Frame;
import com.util.ReadWriteRegisty;
import com.util.RegisterMark;

public class RegisterFrame extends JFrame {

	mainFrame m = new mainFrame();
	static String s = "";
	private Clipboard clipbd = getToolkit().getSystemClipboard();
	final JTextField t1, t2, t3, t4;
	JLabel rightPanel;
	JPanel JTextJP;

	public RegisterFrame() {
		super();

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(150);

		JPanel rightPanel = new JPanel();

		// ImageIcon icon = CreatecdIcon.add("pic/11.png");
		// rightPanel = new JLabel();
		rightPanel.setLayout(null);
		JLabel j1 = new JLabel("<html><font=14>請輸入使用者名稱與序號</font></html>");
		rightPanel.add(j1);
		j1.setBounds(10, 20, 160, 30);
		// rightPanel.setIcon(icon);

		JLabel userNameJ = new JLabel("使用者名稱：");
		final JTextField userNameT = new JTextField("EROWA", 40);
		rightPanel.add(userNameJ);
		rightPanel.add(userNameT);
		userNameJ.setBounds(80, 80, 100, 25);
		userNameT.setBounds(200, 80, 100, 25);
		t1 = new JTextField(10);
		t2 = new JTextField(10);
		t3 = new JTextField(10);
		t4 = new JTextField(10);
		JTextJP = new JPanel();
		JTextJP.setBackground(new Color(234, 241, 247));
		JTextJP.setLayout(null);
		JTextJP.add(t1);
		JTextJP.add(t2);
		JTextJP.add(t3);
		JTextJP.add(t4);
		t1.setBounds(10, 15, 50, 25);
		t2.setBounds(60, 15, 50, 25);
		t3.setBounds(110, 15, 50, 25);
		t4.setBounds(160, 15, 50, 25);
		t1.addMouseListener(new JCopy());
		t2.addMouseListener(new JCopy());
		t3.addMouseListener(new JCopy());
		t4.addMouseListener(new JCopy());
		rightPanel.add(JTextJP);
		JTextJP.setBounds(80, 130, 220, 50);
		JButton back = new JButton("上一步");
		JButton forwards = new JButton("下一步");
		rightPanel.add(back);
		rightPanel.add(forwards);
		back.setBounds(130, 200, 90, 20);
		forwards.setBounds(230, 200, 90, 20);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				m.setVisible(true);
			}
		});
		forwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SimpleDateFormat sf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				s = userNameT.getText().trim(); // 取得使用者名稱
				String t1Text = t1.getText().trim(); // 取得註冊碼
				String t2Text = t2.getText().trim();
				String t3Text = t3.getText().trim();
				String t4Text = t4.getText().trim();
				// 如果註冊碼不為空
				if (t1Text.length() != 0 && t2Text.length() != 0
						&& t3Text.length() != 0 && t4Text.length() != 0) {
					if (s != null) {
						// 呼叫getRegister()方法將使用者名稱轉為密文
						String[] a = RegisterMark.getRegister(s).split("-");
						// 如果使用者名稱和註冊碼不匹配
						if (!a[0].equals(t1Text) || !a[1].equals(t2Text)
								|| !a[2].equals(t3Text) || !a[3].equals(t4Text)) {
							// 彈出錯誤提示對話方塊
							JOptionPane.showMessageDialog(RegisterFrame.this,
									"使用者名稱與註冊碼不符，請確定後重新輸入");
							t1.setText(""); // 將註冊碼文字方塊清空
							t2.setText("");
							t3.setText("");
							t4.setText("");
							return; // 傳回
						} else {
							// 如果使用者名稱與註冊碼匹配
							String register = "";
							for (int i = 0; i < a.length; i++) {
								register += a[i]; // 將註冊碼傳回給register變數
							}
							// 加密註冊該軟體的電腦的MAC位址
							BigInteger codeStringBigint = new encrypt()
									.Execencrypt(RegisterMark.getMAC(),
											register);

							System.out.println(ReadWriteRegisty.ReadValue(
									"transformerv1", "register1").equals(
									"登錄編輯程式中沒有該值"));

							// 如果程式沒有將註冊資訊寫入登錄編輯程式
							if (ReadWriteRegisty.ReadValue("transformerv1",
									"register1").equals("登錄編輯程式中沒有該值")) {
								// 將加密後的MAC位址資訊寫入登錄編輯程式
								ReadWriteRegisty.WriteValue("transformerv1",
										"register1",
										String.valueOf(codeStringBigint));
								// 將註冊時間寫入登錄編輯程式
								ReadWriteRegisty.WriteValue("transformerv1",
										"registertime", sf.format(new Date()));
								// 將執行註冊目前時間寫入登錄編輯程式
								ReadWriteRegisty.WriteValue("transformerv1",
										"registertimetest",
										sf.format(new Date()));
								// 將登錄編輯程式中softwareStatus節點修改為actived表示已註冊
								ReadWriteRegisty.WriteValue("transformerv1",
										"softwareStatus", "actived");
							} else {

								if (!new decrypt().Execdecrypt().contains(
										RegisterMark.getMAC())) {
									JOptionPane.showMessageDialog(
											RegisterFrame.this,
											"一個註冊碼只能在唯一一台電腦上使用");
									return;
								} else {
									// 使用目前時間與登錄編輯程式中首次註冊時間相比較，如果小於0說明註冊期限已滿
									try {
										if (new Date().compareTo(sf.parse(ReadWriteRegisty
												.ReadValue("transformerv1",
														"registertimetest"))) < 0) {
											JOptionPane
													.showMessageDialog(
															RegisterFrame.this,
															"您的軟體使用時間已經到期，如果希望繼續使用，請註冊");
											// 將登錄編輯程式中softwareStatus的值改為trial
											ReadWriteRegisty.WriteValue(
													"transformerv1",
													"softwareStatus", "trial");
											return;
										} else
											// 如果使用者註冊時間沒滿，將目前系統時間更新到登錄編輯程式中的registerimetest節點中
											ReadWriteRegisty.WriteValue(
													"transformerv1",
													"registertimetest",
													sf.format(new Date()));
									} catch (ParseException e1) {
										e1.printStackTrace();
									}
									try {
										Calendar cal_start = Calendar
												.getInstance();
										Calendar cal_end = Calendar
												.getInstance();
										cal_start.setTime(new Date());
										cal_end.setTime(sf.parse(ReadWriteRegisty
												.ReadValue("transformerv1",
														"firstUseTime")));
										System.out.println(cal_start
												.get(Calendar.YEAR));
										System.out.println(cal_end
												.get(Calendar.YEAR));
										if (new Date().before(sf.parse(ReadWriteRegisty
												.ReadValue("transformerv1",
														"registertime")))) {
											JOptionPane
													.showMessageDialog(
															RegisterFrame.this,
															"您的軟體時間使用已經到期，如果希望繼續使用，請註冊");
											ReadWriteRegisty.WriteValue(
													"transformerv1",
													"softwareStatus", "trial");
											return;
										}
										if (cal_start.get(Calendar.YEAR)
												- cal_end.get(Calendar.YEAR) >= 1
												|| cal_start.get(Calendar.YEAR)
														- cal_end
																.get(Calendar.YEAR) < 0) {
											ReadWriteRegisty
													.WriteValue(
															"transformerv1",
															"softwareStatus",
															"expired");
											JOptionPane
													.showMessageDialog(
															RegisterFrame.this,
															"您的軟體時間使用已經到期，如果希望繼續使用，請註冊");
											ReadWriteRegisty.WriteValue(
													"transformerv1",
													"softwareStatus", "trial");
											return;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							JOptionPane.showMessageDialog(RegisterFrame.this,
									"註冊成功！ 感謝您的使用");
							setVisible(false);
							Frame frame = new Frame();
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							frame.setSize(500, 500);
							frame.setVisible(true);
							frame.setLocationRelativeTo(null);
							frame.setResizable(false);
						}
					}
				} else {
					JOptionPane.showMessageDialog(RegisterFrame.this,
							"請輸入完整的註冊碼");
				}
			}
		});
		rightPanel.setBounds(0, 0, 400, 356);

		// rightPanel.add(rightPanel, new Integer(Integer.MIN_VALUE));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBackground(Color.WHITE);
		// JLabel image = new JLabel();
		// ImageIcon icon2 = CreatecdIcon.add("/pic/00002.jpg");
		// image.setIcon(icon2);
		JLabel letter1 = new JLabel();
		letter1.setText("<html>請您填寫序號</html>");
		JLabel letter2 = new JLabel();
		// leftPanel.add(image);
		// image.setBounds(20, 20, 130, 100);
		leftPanel.add(letter1);
		letter1.setBounds(10, 120, 130, 200);
		leftPanel.add(letter2);
		letter2.setBounds(10, 200, 130, 50);

		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);
		getContentPane().add(splitPane);
		setSize(new Dimension(570, 400));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		m.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("軟體註冊模組");
	}

	class JCopy extends MouseAdapter {
		@Override
		public void mouseDragged(MouseEvent arg0) {
			super.mouseDragged(arg0);
		}

		public void mouseClicked(MouseEvent arg0) {
			super.mouseClicked(arg0);
			// System.out.println(arg0.getButton());
			if (arg0.getButton() == 3) {
				final JPopupMenu popup = new JPopupMenu();
				JMenuItem itemcut = new JMenuItem("剪下");
				popup.add(itemcut);
				popup.addSeparator();
				JMenuItem itemcopy = new JMenuItem("複製");
				popup.add(itemcopy);
				itemcopy.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						t1.selectAll();
						t2.selectAll();
						t3.selectAll();
						t4.selectAll();
						JTextJP.requestFocus(true);
						System.out.println(JTextJP.requestFocusInWindow());
						String t1Selection = t1.getSelectedText();
						String t2Selection = t2.getSelectedText();
						String t3Selection = t3.getSelectedText();
						String t4Selection = t4.getSelectedText();
						popup.setVisible(false);
						String IntegrationString = t1Selection.concat("-")
								.concat(t2Selection).concat("-")
								.concat(t3Selection).concat("-")
								.concat(t4Selection);
						if (IntegrationString == null)
							return;
						StringSelection clipString = new StringSelection(
								IntegrationString);
						clipbd.setContents(clipString, clipString);

					}
				});
				popup.addSeparator();
				JMenuItem itempaste = new JMenuItem("貼上");
				popup.add(itempaste);
				itempaste.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Transferable clipData = clipbd
								.getContents(RegisterFrame.this);
						try {
							String clipString = (String) clipData
									.getTransferData(DataFlavor.stringFlavor);
							if (clipString.contains("-")) { // 如果取得的字串中包含『-』字元
								String[] a = clipString.split("-"); // 將字串以『-』字元分割放入陣列
								for (int i = 0; i < a.length; i++)
									System.out.println(a[i]);
								t1.setText(a[0]); // 將陣列中的每個元素放入文字方塊
								t2.setText(a[1]);
								t3.setText(a[2]);
								t4.setText(a[3]);
							} else {
								JOptionPane.showMessageDialog(
										RegisterFrame.this,
										"您貼上的註冊碼格式不正確，請重新貼上");
							}
							popup.setVisible(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
			}
		}
	}
}
