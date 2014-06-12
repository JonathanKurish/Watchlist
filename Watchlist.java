import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class Watchlist extends JFrame {
	
	public static ArrayList<Stock> stocks = new ArrayList<Stock>();
	public static ArrayList<Double> stockPrices = new ArrayList<Double>();
	
	public static int currentInterval = 5;
	public static int newInterval = 5;
	public static Update update;
	public int amountOfStocks;
	
	public String webDataTemp;
	private String webData;
	
	public static Watchlist watchlist;

	JButton addNewStock, updateInterval, viewStock, remove;
	JList stocklist, stocklistPrice;
	JLabel currentIntervalLabel, stocksAdded, listTitle, listPrices;
	JScrollPane listScroller, listScroller2;
	AddWindow addWindow;
	ViewWindow viewWindow;
	JFrame frame;

	
	public void alarm() {	
		WatchlistRenderer renderer = new WatchlistRenderer();
		stocklist.setCellRenderer(renderer.createAlarmRenderer(watchlist));
	}
	
	
	
	public void openAddAsChange(Stock stock) {
		addWindow.setTitle("Change Criteria for " + stock.symbol);
		addWindow.initTF.setText(stock.symbol);
		addWindow.searchShare.setEnabled(false);
		addWindow.searchResult.setText("Result: " + stock.symbol);
		addWindow.sharePrice.setText("Current Price: " + stock.lastPrice);
		addWindow.alarmShareTF.setText("" + stock.targetPrice);
		addWindow.alarmShareTF.setEnabled(true);
		addWindow.setMACDpos.setSelected(stock.MACDcrossPositive);
		addWindow.setMACDpos.setEnabled(true);
		addWindow.setMACDneg.setSelected(stock.MACDcrossNegative);
		addWindow.setMACDneg.setEnabled(true);
		addWindow.goldencrossCB.setSelected(stock.goldencross);
		addWindow.goldencrossCB.setEnabled(true);
		addWindow.deathcrossCB.setSelected(stock.deathcross);
		addWindow.deathcrossCB.setEnabled(true);
		addWindow.highVolumeCB.setSelected(stock.highVolume);
		addWindow.highVolumeCB.setEnabled(true);
		addWindow.rsiOverCB.setSelected(stock.overbought);
		addWindow.rsiOverCB.setEnabled(true);
		addWindow.rsiUnderCB.setSelected(stock.oversold);
		addWindow.rsiUnderCB.setEnabled(true);
		addWindow.ema8TF.setText("" + stock.EMA8target);
		addWindow.ema8TF.setEnabled(true);
		addWindow.ema12TF.setText("" + stock.EMA12target);
		addWindow.ema12TF.setEnabled(true);
		addWindow.ema20TF.setText("" + stock.EMA20target);
		addWindow.ema20TF.setEnabled(true);
		addWindow.sma50TF.setText("" + stock.SMA50target);
		addWindow.sma50TF.setEnabled(true);
		addWindow.sma200TF.setText("" + stock.SMA200target);
		addWindow.sma200TF.setEnabled(true);
		addWindow.addStock.setEnabled(true);
		addWindow.rsiOverTF.setText("" + stock.rsiOver);
		addWindow.rsiUnderTF.setText("" + stock.rsiUnder);
		addWindow.rsiUnderTF.setEnabled(true);
		addWindow.rsiOverTF.setEnabled(true);
		
		addWindow.setVisible(true);
	}
	
	public float getLastPrice(String search) {
		String url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20csv%20WHERE%20url%3D\"http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D" + search + 
							 "%26f%3Dl1%26e%3D.csv\"%0A%20%20%20%20AND%20columns%3D\"lastPrice\"%3B";
				try{
					URL pageLocation = new URL(url);
					Scanner input = new Scanner(pageLocation.openStream());
					String webData = input.nextLine() + "\n";
					while(input.hasNextLine()) {webData +=  input.nextLine() + "\n";}
					webDataTemp = webData;
				} catch(Exception e) {}
				
				int startIndex = webDataTemp.indexOf("<lastPrice>");
				int endIndex = webDataTemp.indexOf("</lastPrice>", startIndex);		
				float lastPrice = Float.parseFloat(webDataTemp.substring(startIndex + 11,endIndex));
				return lastPrice;
			}
		

	// Sets the Updater Interval
    void setInterval(String newText) {
        currentIntervalLabel.setText(newText);
    }
	
	void createObject(String symbol, String setPrice, boolean MACDcrossPos, boolean MACDcrossNeg, boolean goldenCross,
		boolean deathCross, String ema8, String ema12, String ema20, String sma50, String sma200, 
		boolean highVolume, boolean rsiOverbought, boolean rsiOversold, String rsiOver, String rsiUnder) {
		try {
			
			for (int i=0; i < stocks.size();i++) {
				String stockSymbol = "" + symbol;
				String compareSymbol = "" + stocks.get(i).symbol;
				if (compareSymbol.equals(stockSymbol)) {
					stocks.remove(i);
					stockPrices.remove(i);
				}
				updateList();
			}

			Stock newStock = new Stock(symbol);
			newStock.symbol = symbol;
			if (!setPrice.isEmpty()) {newStock.targetPrice = Double.parseDouble(setPrice);}	
			newStock.MACDcrossPositive = MACDcrossPos;
			newStock.MACDcrossNegative = MACDcrossNeg;
			newStock.goldencross = goldenCross;
			newStock.deathcross = deathCross;	
			if (!ema8.isEmpty()) {newStock.EMA8target = Double.parseDouble(ema8);}
			if (!ema12.isEmpty()) {newStock.EMA12target = Double.parseDouble(ema12);}
			if (!ema20.isEmpty()) {newStock.EMA20target = Double.parseDouble(ema20);}
			if (!sma50.isEmpty()) {newStock.SMA50target = Double.parseDouble(sma50);}
			if (!sma200.isEmpty()) {newStock.SMA200target = Double.parseDouble(sma200);}
			newStock.highVolume = highVolume;
			newStock.overbought = rsiOverbought;
			newStock.oversold = rsiOversold;
			if (!rsiOver.isEmpty()) {newStock.rsiOver = Double.parseDouble(rsiOver);
			}
			if (!rsiUnder.isEmpty()) {newStock.rsiUnder = Double.parseDouble(rsiUnder);
			}
			if (newStock.lastPrice > newStock.targetPrice) {
				newStock.targetAU = true;
			} else {newStock.targetAU = false;}
			stocks.add(newStock);
			stockPrices.add(newStock.lastPrice);
			updateList();
			alarm();
				
				System.out.println("symbol: " + newStock.symbol);
				//System.out.println("targetPrice: " + lastPrice);
				System.out.println("MACDcrossPos: " + MACDcrossPos);
				System.out.println("MACDcrossNeg: " + MACDcrossNeg);
				System.out.println("goldencross: " + goldenCross);
				System.out.println("deathcross: " + deathCross);
				System.out.println("target ema8: " + ema8);
				System.out.println("target ema12: " + ema12);
				System.out.println("target ema20: " + ema20);
				System.out.println("target SMA200: " + sma200);
				System.out.println("target SMA50: " + sma50);
				System.out.println("target rsiOver: " + rsiOver);
				System.out.println("target rsiUnder: " + rsiUnder);
				System.out.println("overbought: " + rsiOverbought);
				System.out.println("oversold: " + rsiOversold);
				System.out.println("high Volume: " + highVolume);




		} catch (IOException ex) {}
	}
	
	
	public void setListStocks(String[] symbols) {
		if (stocklist.getSelectedIndex() == -1) {
			stocklist.setListData(symbols);
			listScroller.revalidate();
			listScroller.repaint();
		}
	}
	// Updates list of stocks after addition of new stock
	void updateListAdd(Stock stock) {
		stocks.add(stock);
		stockPrices.add(stock.lastPrice);
		updateList();
	}
	
	// Updates list of stocks
	void updateList() {
		amountOfStocks = stocks.size();
		System.out.println(amountOfStocks + " YOLO");
		String[] symbols = new String[amountOfStocks];
		Double[] prices = new Double[amountOfStocks];
			for(int i = 0; i < amountOfStocks; i++) {
				
				symbols[i] = stocks.get(i).symbol;
				prices[i] = stocks.get(i).lastPrice;
			}
		stocksAdded.setText("Stocks Added: " + amountOfStocks);
		setListStocks(symbols);
		stocklistPrice.setListData(prices);
		listScroller.revalidate();
		listScroller.repaint();
		listScroller2.revalidate();
		listScroller2.repaint();
	}
	
	// Constructs the Main Window
	public Watchlist() {
		this.frame = frame;
		addWindow = new AddWindow(frame, this);
		addWindow.setSize(300,700);	
		addWindow.setVisible(false);
		addWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addWindow.setLocation(200,200);
		addWindow.clearAndHide();
		
		this.frame = frame;
		viewWindow = new ViewWindow(frame, this);
		viewWindow.pack();
		viewWindow.setVisible(false);
		viewWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		viewWindow.setLocation(300,300);
		viewWindow.setSize(400,600);

		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(5,1,2,2));
		
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,2));
		top.setBackground(Color.BLACK);
		top.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		addNewStock = new JButton("Add Stock");
		addNewStock.setBackground(Color.RED);
		top.add(addNewStock);
		updateInterval = new JButton("Update Timer");
		updateInterval.setBackground(Color.YELLOW);
		top.add(updateInterval);
		pane.add(top);
		
		JPanel topmid = new JPanel();
		topmid.setLayout(new GridLayout(1,2));
		topmid.setSize(new Dimension(30, 100));
		topmid.setBorder(BorderFactory.createEmptyBorder(1, 5, 5, 1));
		stocksAdded = new JLabel("Stocks Added: " + stocks.size());
		topmid.add(stocksAdded);
		currentIntervalLabel = new JLabel("Update Interval: " + currentInterval);
		topmid.add(currentIntervalLabel);
		pane.add(topmid);
		
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1,2));
		middle.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		listTitle = new JLabel("Stocks to Watch: ");
		middle.add(listTitle);
		listPrices = new JLabel("Current Stock Prices: ");
		middle.add(listPrices);		
		pane.add(middle);
		
		
		JPanel list = new JPanel();
		list.setLayout(new GridLayout(1,2));
		list.setBackground(Color.BLACK);
		list.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		stocklist = new JList(stocks.toArray());
		stocklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stocklist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		stocklist.setVisibleRowCount(10);
		stocklist.setBounds(200,200,100,50);
		listScroller = new JScrollPane(stocklist);
		list.add(listScroller);
		
		stocklistPrice = new JList();
		stocklistPrice.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		stocklistPrice.setVisibleRowCount(20);
		stocklistPrice.setBounds(200,200,100,50);
		stocklistPrice.setEnabled(false);
		listScroller2 = new JScrollPane(stocklistPrice);
		list.add(listScroller2);
		pane.add(list);
		
		JPanel belowList = new JPanel();
		belowList.setLayout(new GridLayout(1,2));
		viewStock = new JButton("View Stock");
		belowList.add(viewStock);
		remove = new JButton("Remove Stock");
		belowList.add(remove);
		pane.add(belowList);
		
	
		AddWindowClass ac = new AddWindowClass();
		addNewStock.addActionListener(ac);
		
		UpdaterClass uc = new UpdaterClass();
		updateInterval.addActionListener(uc);
		
		RemoveClass rc = new RemoveClass();
		remove.addActionListener(rc);
		
		ViewClass vc = new ViewClass();
		viewStock.addActionListener(vc);
	
	}

	// Class used to remove Stock objects from list
	public class RemoveClass implements ActionListener {
		public void actionPerformed(ActionEvent rc) {
			try {
				int index = stocklist.getSelectedIndex();
				stocklist.clearSelection();
				if(index != -1){ //Remove only if a particular item is selected
					stocks.remove(index);
					stockPrices.remove(index);
				} 
				updateList();
			} catch (Exception ex) {}
		}
	}
	
	// Class used to view Stock information
	public class ViewClass implements ActionListener {
		public void actionPerformed(ActionEvent vc) {
			try {
				int index = stocklist.getSelectedIndex();
				stocklist.clearSelection();
				if(index != -1) {
					Stock stock = stocks.get(index);
					viewWindow.stock = stock;
					viewWindow.stockSymbol.setText(stock.symbol);
					viewWindow.currentPrice.setText("Current Price: " + stock.lastPrice);
					viewWindow.targetPrice.setText("Target Price: " + stock.targetPrice);
					if (stock.MACDcrossPositive) {viewWindow.MACDpos.setText("MACD pos set: Yes");}
					else {viewWindow.MACDpos.setText("MACD pos set: No");}
					if (stock.MACDcrossNegative) {viewWindow.MACDneg.setText("MACD neg set: Yes");}
					else {viewWindow.MACDneg.setText("MACD neg set: No");}
					if (stock.goldencross) {viewWindow.GC.setText("Golden Cross set: Yes");}
					else {viewWindow.GC.setText("Golden Cross set: No");}
					if (stock.deathcross) {viewWindow.DC.setText("Death Cross set: Yes");}
					else {viewWindow.DC.setText("Death Cross set: No");}
					viewWindow.EMA8.setText("Target EMA8: " + stock.EMA8target);
					viewWindow.EMA12.setText("Target EMA12: " + stock.EMA12target);
					viewWindow.EMA20.setText("Target EMA20: " + stock.EMA20target);
					viewWindow.SMA50.setText("Target SMA50: " + stock.SMA50target);
					viewWindow.SMA200.setText("Target SMA200: " + stock.SMA200target);
					if (stock.highVolume) {viewWindow.highvol.setText("High Volume set: Yes");}
					else {viewWindow.highvol.setText("High Volume set: No");}
					if (stock.overbought) {viewWindow.OB.setText("Overbought set: Yes");}
					else {viewWindow.OB.setText("Overbought set: No");}
					if (stock.oversold) {viewWindow.OS.setText("Oversold set: Yes");}
					else {viewWindow.OS.setText("Overbought set: No");}
					viewWindow.RSIover.setText("Target RSIover: " + stock.rsiOver);
					viewWindow.RSIunder.setText("Target RSIunder: " + stock.rsiUnder);
					viewWindow.high.setText("High of Day: " + stock.high);
					viewWindow.low.setText("Low of Day: " + stock.low);
					
					viewWindow.MACDline.setText("MACD line: " + stock.MACDline);
					viewWindow.MACDtrigger.setText("MACD trigger: " + stock.MACDtrigger);
					viewWindow.EMA82.setText("Current EMA8: " + stock.lastEMA8);
					viewWindow.EMA122.setText("Current EMA12: " + stock.lastEMA12);
					viewWindow.EMA202.setText("Current EMA20: " + stock.lastEMA20);
					viewWindow.SMA502.setText("Current SMA50: " + stock.SMA50);
					viewWindow.SMA2002.setText("Current SMA200: " + stock.SMA200);
					viewWindow.highvol2.setText("Current Volume: " + stock.volumen);
					viewWindow.rsi.setText("Current rsi: " + stock.rsi);
					
					if (stock.hasAlarm) {viewWindow.line1.setBackground(Color.RED);} 
					else {viewWindow.line1.setBackground(Color.WHITE);}
					viewWindow.setVisible(true);	
				}
			} catch (Exception ex) {}
		}
	}
	
	// Class used to add new Stock objects to the list
	public class AddWindowClass implements ActionListener {
		public void actionPerformed(ActionEvent awc) {
			try {
				//The window has already been created. This function just makes it visible
				addWindow.clearAndHide();
				addWindow.setVisible(true); 
			} catch (Exception ex) {}
		}
	}
	
	// Class used to set new Update Interval
	public class UpdaterClass implements ActionListener {
		public void actionPerformed(ActionEvent uc) {
			try {
				String s = (String)JOptionPane.showInputDialog(
                            frame,
                            "Current Interval: " + currentInterval + "\n"
                            + "New Interval: ",
                            "Set Update Interval",
                            JOptionPane.PLAIN_MESSAGE,
                            null, null,
                            newInterval);
 
                    newInterval = Integer.parseInt(s);
					currentInterval = newInterval;
					//If a string was returned, say so.
                        setInterval("Update Interval: " + newInterval);
						update.stop();
						System.out.println("stopped");
						update = new Update(newInterval, watchlist);
						System.out.println("made new");
						update.start();
						System.out.println("started new");
						return;
			} catch (Exception ex) {}
		}
	}
	
	
	
	// Main Method
	public static void main(String args[]) {
		Watchlist watchlist = new Watchlist();
		watchlist.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		watchlist.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				IOStocks.saveStocks();
				System.exit(0);
			}
		});
		
		int load = IOStocks.loadStocks(watchlist);
		String Symbols[] = new String[stocks.size()];
		for (int g = 0; g < stocks.size();g++) {
			
				System.out.println("symbol: " + stocks.get(g).symbol);
				System.out.println("MACDcrossPos: " + stocks.get(g).MACDcrossPositive);
				System.out.println("MACDcrossNeg: " + stocks.get(g).MACDcrossNegative);
				System.out.println("goldencross: " + stocks.get(g).goldencross);
				System.out.println("deathcross: " + stocks.get(g).deathcross);
				System.out.println("target ema8: " + stocks.get(g).EMA8target);
				System.out.println("target ema12: " + stocks.get(g).EMA12target);
				System.out.println("target ema20: " + stocks.get(g).EMA20target);
				System.out.println("target SMA200: " + stocks.get(g).SMA200target);
				System.out.println("target SMA50: " + stocks.get(g).SMA50target);
				System.out.println("target rsiOver: " + stocks.get(g).rsiOver);
				System.out.println("target rsiUnder: " + stocks.get(g).rsiUnder);
				System.out.println("overbought: " + stocks.get(g).overbought);
				System.out.println("oversold: " + stocks.get(g).oversold);
				System.out.println("high Volume: " + stocks.get(g).highVolume);
			
			Symbols[g] = stocks.get(g).symbol;
		}
		watchlist.setListStocks(Symbols);
		watchlist.updateList();
		watchlist.alarm();
		watchlist.setVisible(true);
		watchlist.setTitle("Watchlist");
		watchlist.setSize(350,400);
		update = new Update(currentInterval, watchlist);
		update.start();
	}
}
	