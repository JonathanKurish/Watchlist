import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class AddWindow extends JDialog {
	
	JLabel initials, searchResult, sharePrice, alarmShare, FILLER1, FILLER2, FILLER3,
		setMACD, ema8, ema12, ema20, sma50, sma200, helpMA, rsi, rsiOver, rsiUnder;
	JTextField initTF, alarmShareTF, ema8TF, ema20TF, ema12TF, sma50TF, sma200TF, rsiOverTF, rsiUnderTF;
	JButton searchShare, addStock;
	JCheckBox setMACDpos, setMACDneg, goldencrossCB, deathcrossCB, highVolumeCB, rsiOverCB, rsiUnderCB;
	
	private Watchlist watchlist;
	private Frame frame;
	private String webData;
	
	public void clearAndHide() {
		setTitle("Add Stock");
		sharePrice.setText("Current Price: ");
		addStock.setEnabled(false);
		initTF.setText(null);
        initTF.setEnabled(true);
		searchShare.setEnabled(true);
		setMACDpos.setEnabled(false);
		setMACDneg.setEnabled(false);
		goldencrossCB.setEnabled(false);
		deathcrossCB.setEnabled(false);
		highVolumeCB.setEnabled(false);
		rsiOverCB.setEnabled(false);
		rsiUnderCB.setEnabled(false);
		alarmShareTF.setEnabled(false);
		ema8TF.setEnabled(false);
		ema12TF.setEnabled(false);
		ema20TF.setEnabled(false);
		sma50TF.setEnabled(false);
		sma200TF.setEnabled(false);
		rsiOverTF.setEnabled(false);
		rsiUnderTF.setEnabled(false);
		searchResult.setText("");
		alarmShareTF.setText("");
		ema8TF.setText("");
		ema12TF.setText("");
		ema20TF.setText("");
		sma50TF.setText("");
		sma200TF.setText("");
		rsiOverTF.setText("");
		rsiUnderTF.setText("");
		setMACDpos.setSelected(false);
		setMACDneg.setSelected(false);
		goldencrossCB.setSelected(false);
		deathcrossCB.setSelected(false);
		highVolumeCB.setSelected(false);
		rsiOverCB.setSelected(false);
		rsiUnderCB.setSelected(false);
		setVisible(false);
    }
	
	
	public AddWindow(Frame frame, Watchlist parent) {
		super(frame, "Add Stock", true);
		watchlist = parent;

		
		Container pane = this.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		
		JPanel line1 = new JPanel();
		line1.setLayout(new FlowLayout());
		initials = new JLabel("Stock Initials: ");
		line1.add(initials);
		initTF = new JTextField(10);
		line1.add(initTF);
		//pane.add(line1);
		
		//JPanel line2 = new JPanel();
		//line2.setLayout(new FlowLayout());
		searchShare = new JButton("Search");
		line1.add(searchShare);
		pane.add(line1);
		
		SearchClass sc = new SearchClass();
		searchShare.addActionListener(sc);
		
		
		JPanel line3 = new JPanel();
		line3.setLayout(new FlowLayout());
		searchResult = new JLabel("");
		line3.add(searchResult);
		pane.add(line3);
		
		
		JPanel line4 = new JPanel();
		line4.setLayout(new FlowLayout());
		sharePrice = new JLabel("Current Price: ");
		line4.add(sharePrice);
		pane.add(line4);
		
		
		JPanel line5 = new JPanel();
		line5.setLayout(new FlowLayout());
		alarmShare = new JLabel("Set Alarm at Price: ");
		line5.add(alarmShare);
		alarmShareTF = new JTextField(10);
		line5.add(alarmShareTF);
		pane.add(line5);
		
		
		JPanel line6 = new JPanel();
		line6.setLayout(new FlowLayout());
		setMACDpos = new JCheckBox("MACD crossover positive");
		line6.add(setMACDpos);
		pane.add(line6);
		
		JPanel line7 = new JPanel();
		line7.setLayout(new FlowLayout());
		setMACDneg = new JCheckBox("MACD crossover negative");
		line7.add(setMACDneg);
		pane.add(line7);
		
		JPanel line8 = new JPanel();
		line8.setLayout(new FlowLayout());
		goldencrossCB = new JCheckBox("Golden Cross");
		line8.add(goldencrossCB);
		pane.add(line8);
		
		JPanel line9 = new JPanel();
		line9.setLayout(new FlowLayout());
		deathcrossCB = new JCheckBox("Death Cross");
		line9.add(deathcrossCB);
		pane.add(line9);
		
		
		JPanel line10 = new JPanel();
		line10.setLayout(new FlowLayout());
		ema8 = new JLabel("Set Specific EMA8: ");
		line10.add(ema8);
		ema8TF = new JTextField(10);
		line10.add(ema8TF);
		pane.add(line10);
		
		
		JPanel line11 = new JPanel();
		line11.setLayout(new FlowLayout());
		ema12 = new JLabel("Set Specific EMA12: ");
		line11.add(ema12);
		ema12TF = new JTextField(10);
		line11.add(ema12TF);
		pane.add(line11);
		
		JPanel line12 = new JPanel();
		line12.setLayout(new FlowLayout());
		ema20 = new JLabel("Set Specific EMA20: ");
		line12.add(ema20);
		ema20TF = new JTextField(10);
		line12.add(ema20TF);
		pane.add(line12);
		
		
		JPanel line13 = new JPanel();
		line13.setLayout(new FlowLayout());
		sma50 = new JLabel("Set Specific SMA50: ");
		line13.add(sma50);
		sma50TF = new JTextField(10);
		line13.add(sma50TF);
		pane.add(line13);
		
		JPanel line14 = new JPanel();
		line14.setLayout(new FlowLayout());
		sma200 = new JLabel("Set Specific SMA200: ");
		line14.add(sma200);
		sma200TF = new JTextField(10);
		line14.add(sma200TF);
		pane.add(line14);
		
		JPanel line15 = new JPanel();
		line15.setLayout(new FlowLayout());
		highVolumeCB = new JCheckBox("Alarm on high volume");
		line15.add(highVolumeCB);
		pane.add(line15);
		
		JPanel line16 = new JPanel();
		line16.setLayout(new FlowLayout());
		rsiOverCB = new JCheckBox("Overbought");
		line16.add(rsiOverCB);
		pane.add(line16);
		
		JPanel line17 = new JPanel();
		line17.setLayout(new FlowLayout());
		rsiUnderCB = new JCheckBox("Oversold");
		line17.add(rsiUnderCB);
		pane.add(line17);
		
		JPanel line18 = new JPanel();
		line18.setLayout(new FlowLayout());
		rsiOver = new JLabel("Set Over RSI: ");
		line18.add(rsiOver);
		rsiOverTF = new JTextField(10);
		line18.add(rsiOverTF);
		pane.add(line18);
		
		JPanel line19 = new JPanel();
		line19.setLayout(new FlowLayout());
		rsiUnder = new JLabel("Set Under RSI: ");
		line19.add(rsiUnder);
		rsiUnderTF = new JTextField(10);
		line19.add(rsiUnderTF);
		pane.add(line19);

		JPanel line20 = new JPanel();
		line20.setLayout(new FlowLayout());
		addStock = new JButton("Add Stock");
		addStock.setEnabled(false);
		line20.add(addStock);
		pane.add(line20);
		
		AddClass ac = new AddClass();
		addStock.addActionListener(ac);
		
	
	}
	
	// Class used to see if searched stock actually exists
	public class SearchClass implements ActionListener {
		public String webDataTemp;
		
		public void actionPerformed(ActionEvent sc) {
			try {
				String search = initTF.getText();
				
				float lastPrice = watchlist.getLastPrice(search);
				
				
				if (lastPrice != 0.0) { 
					alarmShareTF.setEnabled(true);
					setMACDpos.setEnabled(true);
					setMACDneg.setEnabled(true);
					goldencrossCB.setEnabled(true);
					deathcrossCB.setEnabled(true);
					highVolumeCB.setEnabled(true);
					rsiOverCB.setEnabled(true);
					rsiUnderCB.setEnabled(true);
					ema8TF.setEnabled(true);
					ema12TF.setEnabled(true);
					ema20TF.setEnabled(true);
					sma50TF.setEnabled(true);
					sma200TF.setEnabled(true);
					rsiOverTF.setEnabled(true);
					rsiUnderTF.setEnabled(true);
					searchResult.setText(search);
					searchResult.setHorizontalAlignment(JLabel.CENTER);
					searchResult.setFont(new Font("Arial", Font.PLAIN, 20));
					initTF.setEnabled(false);
					searchShare.setEnabled(false);
					addStock.setEnabled(true);
					sharePrice.setText("Current Price: " + lastPrice);
				} else {
					searchResult.setFont(new Font("Arial", Font.PLAIN, 20));
					searchResult.setText("Didn't find stock");
				}
				
				
			} catch (Exception ex) {
				searchResult.setText("Check you internet connection!");
			}
		}
	}
	
	public class AddClass implements ActionListener {
		public void actionPerformed(ActionEvent ac) {
			try {
				String symbol = initTF.getText();
				String setPrice = alarmShareTF.getText();
				boolean MACDcrossPos = setMACDpos.isSelected();
				boolean MACDcrossNeg = setMACDneg.isSelected();
				boolean goldenCross = goldencrossCB.isSelected();
				boolean deathCross = deathcrossCB.isSelected();
				String ema8 = ema8TF.getText();
				String ema12 = ema12TF.getText();
				String ema20 = ema20TF.getText();
				String sma50 = sma50TF.getText();
				String sma200 = sma200TF.getText();
				boolean highVolume = highVolumeCB.isSelected();
				boolean rsiOverbought = rsiOverCB.isSelected();
				boolean rsiOversold = rsiUnderCB.isSelected();
				String rsiOver = rsiOverTF.getText();
				String rsiUnder = rsiUnderTF.getText();
				
				
				watchlist.alarm();
				clearAndHide();
				watchlist.createObject(symbol, setPrice, MACDcrossPos, MACDcrossNeg, goldenCross, deathCross, 
					ema8, ema12, ema20, sma50, sma200, highVolume, rsiOverbought, rsiOversold, rsiOver, rsiUnder);
				
				
			} catch (Exception ex) {}
		}
	}
}