import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ViewWindow extends JDialog {

	JLabel stockSymbol, currentPrice, MACDpos, MACDneg, GC, DC, 
		EMA8, EMA12, EMA20, SMA50, SMA200, highvol, OB, OS, RSIover, RSIunder,
		
		high, low,
		
		targetPrice, MACDline, MACDtrigger, GC2, DC2, EMA82, EMA122, EMA202, 
		SMA502, SMA2002, highvol2, OB2, OS2, rsi;
		
	JButton changeCriteria;
	JPanel line1;
	
	private Watchlist watchlist;
	private Frame frame;
	public static Stock stock;
	
	public ViewWindow(Frame frame, Watchlist parent) {
		super(frame, "View Stock", true);
		watchlist = parent;
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(10,2,2,2));
		
		line1 = new JPanel();
		line1.setLayout(new GridLayout(1,1));
		line1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		line1.setBackground(Color.WHITE);
		stockSymbol = new JLabel("");
		stockSymbol.setHorizontalAlignment(JLabel.CENTER);
		stockSymbol.setFont(new Font("Arial", Font.PLAIN, 32));
		line1.add(stockSymbol);
		pane.add(line1);
		
		
		
		JPanel line2 = new JPanel();
		line2.setLayout(new GridLayout(1,2));
		currentPrice = new JLabel("Current Price: ");
		line2.add(currentPrice);
		targetPrice = new JLabel("Target Price: ");
		line2.add(targetPrice);
		pane.add(line2);
		
		
		JPanel line25 = new JPanel();
		line25.setLayout(new GridLayout(1,2));
		high = new JLabel("High of day: ");
		line25.add(high);
		low = new JLabel("Low of day: ");
		line25.add(low);
		pane.add(line25);
		
		
		JPanel line3 = new JPanel();
		line3.setLayout(new GridLayout(2,2));
		MACDpos = new JLabel("MACDpos set: ");
		line3.add(MACDpos);
		MACDline = new JLabel("MACDline: ");
		line3.add(MACDline);
		MACDneg = new JLabel("MACDneg set: ");
		line3.add(MACDneg);
		MACDtrigger = new JLabel("MACDtrigger: ");
		line3.add(MACDtrigger);
		pane.add(line3);
		
		
		
		JPanel line4 = new JPanel();
		line4.setLayout(new GridLayout(2,2));
		GC = new JLabel("Golden Cross set: ");
		line4.add(GC);
		//current goldencross value
		DC = new JLabel("Death Cross set: ");
		line4.add(DC);
		//current deathcross value
		pane.add(line4);
		
		JPanel line5 = new JPanel();
		line5.setLayout(new GridLayout(3,2));
		EMA8 = new JLabel("Target EMA8: ");
		line5.add(EMA8);
		EMA82 = new JLabel("Current EMA8: ");
		line5.add(EMA82);
		EMA12 = new JLabel("Target EMA12: ");
		line5.add(EMA12);
		EMA122 = new JLabel("Current EMA12: ");
		line5.add(EMA122);
		EMA20 = new JLabel("Target EMA20: ");
		line5.add(EMA20);
		EMA202 = new JLabel("Current EMA20: ");
		line5.add(EMA202);
		pane.add(line5);
		
		JPanel line6 = new JPanel();
		line6.setLayout(new GridLayout(2,2));
		SMA50 = new JLabel("Target SMA50: ");
		line6.add(SMA50);
		SMA502 = new JLabel("Current SMA50: ");
		line6.add(SMA502);
		SMA200 = new JLabel("Target SMA200: ");
		line6.add(SMA200);
		SMA2002 = new JLabel("Current SMA200: ");
		line6.add(SMA2002);
		pane.add(line6);
		
		JPanel line7 = new JPanel();
		line7.setLayout(new GridLayout(3,2));
		highvol = new JLabel("High Volume set: ");
		line7.add(highvol);
		highvol2 = new JLabel("Current Volume: ");
		line7.add(highvol2);
		OB = new JLabel("Overbought set: ");
		line7.add(OB);
		rsi = new JLabel("Current rsi: ");
		line7.add(rsi);
		OS = new JLabel("Oversold set: ");
		line7.add(OS);
		pane.add(line7);
		
		JPanel line8 = new JPanel();
		line8.setLayout(new GridLayout(2,2));
		RSIover = new JLabel("Target RSIover: ");
		line8.add(RSIover);
		RSIunder = new JLabel("Target RSIunder: ");
		line8.add(RSIunder);
		pane.add(line8);
		
		JPanel line9 = new JPanel();
		line9.setLayout(new FlowLayout());
		changeCriteria = new JButton("Change Criteria");
		line9.add(changeCriteria);
		pane.add(line9);
		
		ChangeClass cc = new ChangeClass();
		changeCriteria.addActionListener(cc);
		
	}
	
	public class ChangeClass implements ActionListener {
		public void actionPerformed(ActionEvent cc) {
			try {
				setVisible(false);
				watchlist.openAddAsChange(stock);
				
			} catch (Exception ex) {}
		}
	}
}