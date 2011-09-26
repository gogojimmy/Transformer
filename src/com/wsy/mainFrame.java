package com.wsy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import com.main.Frame;
import com.util.ReadWriteRegisty;

public class mainFrame extends JFrame {

	/**
	 * @Author Jimmy Kuo
	 */
	int ZHUSE = 14;
	JLabel zhuce = new JLabel();

	public static void main(String[] args) {

		// 將測試碼寫入登錄檔
		ReadWriteRegisty.WriteValue("transformerv1", "writable", "writable");

		// 檢查登錄檔中是否有測試碼，若無表示無法正確寫入登錄檔，強制關閉程式
		if (ReadWriteRegisty.ReadValue("transformerv1", "writable").equals(
				"writable")) {
			// 若softwareStatus狀態為actived則直接啟動軟體
			if (ReadWriteRegisty.ReadValue("transformerv1", "softwareStatus").equals(
					"actived")) {
				Frame frame = new Frame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(650, 500);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
			} else {
				new mainFrame().setVisible(true);
			}
		} else {
			JOptionPane.showMessageDialog(null, "執行失敗！！請檢查是否已關閉UAC帳戶控制");
		}
	}

	public mainFrame() {
		super();
		// 建立按鈕組
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton activeButton, trialButton;
		JButton continueButton;
		final JSplitPane splitPane = new JSplitPane(); // 初始化JSplitPane物件
		splitPane.setDividerLocation(150); // 設定左右分割的位置

		JPanel rightPanel = new JPanel(); // 初始化右側面板

		// ImageIcon icon = CreatecdIcon.add("pic/11.png"); // 取得ImageIcon物件
		// JLabel rightPanel = new JLabel(); // 初始化放置圖片的JLabel物件
		rightPanel.setLayout(null); // 設定絕對版面
		// rightPanel.setIcon(icon); // 將圖片加到JLabel上
		// 設定JLabel的位置與大小

		rightPanel.setBounds(0, 0, 400, 356);

		activeButton = new JRadioButton("我有一個序號，我想使用該軟體"); // 初始化選項按鈕
		activeButton.setBackground(new Color(234, 241, 247));
		activeButton.setSelected(true); // 設定該選項按鈕
		trialButton = new JRadioButton("我想試用該軟體");
		rightPanel.add(activeButton); // 將選項按鈕加到JLabel上
		activeButton.setBounds(40, 60, 260, 30);
		rightPanel.add(trialButton);
		trialButton.setBackground(new Color(234, 241, 247));
		trialButton.setBounds(40, 100, 200, 30);
		buttonGroup.add(activeButton);
		buttonGroup.add(trialButton);
		continueButton = new JButton("下一步"); // 初始化"下一步"按鈕
		rightPanel.add(continueButton);
		continueButton.setBounds(200, 180, 90, 20);
		rightPanel.add(zhuce);
		zhuce.setBounds(50, 130, 200, 30);
		int days = 14;// 軟體試用天數
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// final DateFormat dateFormat = DateFormat.getDateInstance();
		// 判斷是否首次使用該軟體
		if (ReadWriteRegisty.ReadValue("transformerv1", "firstUseTime").equals(
				"登錄編輯程式中沒有該值")) {
			// ZHUSE = -1代表試用時間滿 ZHUSE = -2代表已註冊，其他數位代表該軟體使用的天數
			String[] key = { "firstUseTime", "currentTime", "softwareStatus" };// 節點第一個字母不要設定大寫，否則在登錄編輯程式中會加入一個『\』
			String d = simpleDateFormat.format(new Date()); // 取得目前時間
			Object[] value = { d, d, "trial" };
			// 登錄編輯程式中firstUseTime鍵值代表首次執行本軟體的時間
			// currentTime鍵值代表目前執行軟體時間
			// softwareStatus代表目前軟體狀態，trial代表軟體在試用中，expired代表軟體試用期滿，actived代表已註冊，sign4代表註冊期滿。
			ReadWriteRegisty.WriteValue("transformerv1", key, value);
		} else {
			try {
				// 如果使用者修改系統日期，直接改成試用到期
				if (new Date().compareTo(simpleDateFormat.parse(ReadWriteRegisty
						.ReadValue("transformerv1", "currentTime"))) <= 0) {// 比較兩個時間的先後
					JOptionPane.showMessageDialog(mainFrame.this,
							"軟體已經試用到期，如果您想繼續使用，請購買序號進行註冊使用");
					ReadWriteRegisty.WriteValue("transformerv1", "softwareStatus",
							"expired");
					ZHUSE = -1; // 將註冊天數設定為-1

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 如果登錄編輯程式中目前軟體狀態為trial
			if (ZHUSE > 0
					|| ReadWriteRegisty.ReadValue("transformerv1", "softwareStatus")
							.equals("trial")) {
				try {
					// 將目前的時間寫入登錄編輯程式
					ReadWriteRegisty.WriteValue("transformerv1", "currentTime",
							simpleDateFormat.format(new Date()));
					// 取得SimpleDateFormat範例，指定時間格式
					// 比較目前時間與登錄編輯程式中transformer中的時間相差的天數，以取得使用者試用的天數
					SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					// 格式化起始時間
					Date date_start = simpleDateFormat2.parse(simpleDateFormat
							.format(new Date()));
					// 格式化終止時間
					Date date_end = simpleDateFormat2.parse(ReadWriteRegisty.ReadValue(
							"transformerv1", "firstUseTime"));
					Calendar cal_start = Calendar.getInstance(); // 取得Calendar範例
					cal_start.setTime(date_start); // 將時間指定給Calendar物件
					Calendar cal_end = Calendar.getInstance();
					cal_end.setTime(date_end);
					// 呼叫自訂getDifferenceDays()方法
					ZHUSE = days - getDifferenceDays(cal_start, cal_end);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		// 如果註冊時間小於0或著登錄編輯程式中的標記為expired
		if (ZHUSE <= 0
				|| ReadWriteRegisty.ReadValue("transformerv1", "softwareStatus")
						.equals("expired")) {
			ZHUSE = 0; // 將登錄編輯程式試用天數清為0
		}
		zhuce.setText("剩餘" + ZHUSE + "天");

		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// 如果使用者選擇了『我有一個序號，我想使用該軟體』的選項按鈕
				if (activeButton.isSelected()) {
					setVisible(false); // 將目前表單隱藏
					new RegisterFrame().setVisible(true); // 使軟體註冊表單為可見
				}
				// 如果使用者選擇了『我想試用該軟體』的選項按鈕
				if (trialButton.isSelected()) {
					// 如果已到試用期限
					if (ReadWriteRegisty.ReadValue("transformerv1", "softwareStatus")
							.equals("expired")) {
						// 彈出提示對話方塊
						JOptionPane.showMessageDialog(mainFrame.this,
								"軟體已經試用到期，如果你想繼續使用，請購買序號進行註冊使用");
						return;
					}
					if (ZHUSE == 0) {
						// 如果註冊變數為0，則彈出提示對話方塊，並將登錄編輯程式中的標示修改
						JOptionPane.showMessageDialog(mainFrame.this,
								"軟體已經試用到期，如果你想繼續使用，請購買序號進行註冊使用");
						ReadWriteRegisty.WriteValue("transformerv1", "softwareStatus",
								"expired");
						return;
					} else {
						setVisible(false);
						Frame frame = new Frame();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setSize(650, 500);
						frame.setVisible(true);
						frame.setLocationRelativeTo(null);
						frame.setResizable(false);
					}
				}
			}
		});
		// rightPanel.add(rightPanel);
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBackground(Color.WHITE);
		JLabel image = new JLabel();
		JLabel letter1 = new JLabel();
		letter1
				.setText("<html><b>關於註冊</b><br>註冊時需要使用者名稱與註冊碼，請向力永公司索取<br><br>如果你不想啟動本軟體，可以在試用狀態下使用，並且可以在試用期內隨時啟動本軟體。</html>");
		JLabel letter2 = new JLabel();
		leftPanel.add(image);
		image.setBounds(20, 20, 130, 100);
		leftPanel.add(letter1);
		letter1.setBounds(10, 120, 130, 200);
		leftPanel.add(letter2);
		letter2.setBounds(10, 200, 130, 50);

		splitPane.setLeftComponent(leftPanel); // 設定主表單左方的面板
		splitPane.setRightComponent(rightPanel); // 設定主表單右方的面板

		getContentPane().add(splitPane); // 將分割面板加入到元件容器中
		setSize(new Dimension(570, 400));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 設定表單可以關閉
		setTitle("軟體註冊模組"); // 為表單設定標題
	}

	// 計算時間差
	public static int getDifferenceDays(Calendar d1, Calendar d2) {
		if (d1.after(d2)) { // 比較兩個時間的先後，將前面的時間賦予d1參數
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		// 計算兩個時間的天數差
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR)
				- d1.get(java.util.Calendar.DAY_OF_YEAR);
		// 取得第2個參數的年數
		int y2 = d2.get(java.util.Calendar.YEAR);
		// 如果第1個參數與第2個參數不為同一年
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			do {
				// 將days變數加上d1行事曆中的最大可能天數
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				// 給d1行事曆加上一年，直到d1行事曆的年分與d2行事曆的年份相同
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}

}
