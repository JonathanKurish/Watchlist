import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class AddWindow extends JDialog {
	
	public ArrayList<Stock> stuff = new ArrayList<Stock>(); //ER VIDST UNODVENDIG!!!
	
	JLabel initials, searchResult, sharePrice, alarmShare, 
		setMACD, setMA, ema, sma, helpMA, highVolume, FILLER, rsi, rsiOver, rsiUnder;
	JTextField initTF, alarmShareTF, emaTF, smaTF, rsiOverTF, rsiUnderTF;
	JButton searchShare, addStock;
	JComboBox dropdownMA;
	JCheckBox setAlarm, setMACDpos, setMACDneg, checkMA, setEMA, 
		setSMA, highVolumeCB, rsiOverCB, rsiUnderCB, rsiCB;
	
	private String[] choicesMA = {"50", "100", "200"}; 
	private Watchlist dd;
	private Frame frame;
	private String webData;
	
	public void clearAndHide() {
		initTF.setText(null);
        searchResult.setText("Result: ");
		setVisible(false);
    }
	
	
	public AddWindow(Frame frame, Watchlist parent, ArrayList<Stock> stocks) { //FORMENTLIG UNODVENDIG) {
		super(frame, "Add Stock", true);
		dd = parent;

		for (int i = 0; i < stocks.size(); i++) { //NOK OGSA UNODVENDIGT
			stuff.add(stocks.get(i));
		}
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(9,1,2,2));
		
		
		JPanel line1 = new JPanel();
		line1.setLayout(new GridLayout(1,3));
		
		initials = new JLabel("Stock Initials: ");
		line1.add(initials);
		
		initTF = new JTextField(20);
		line1.add(initTF);
		
		searchShare = new JButton("Search");
		line1.add(searchShare);
		pane.add(line1);
		
		SearchClass sc = new SearchClass();
		searchShare.addActionListener(sc);
		
		
		JPanel line2 = new JPanel();
		line2.setLayout(new GridLayout(1,1));

		searchResult = new JLabel("Result: ");
		line2.add(searchResult);
		pane.add(line2);
		
		/*
		JPanel line3 = new JPanel();
		line3.setLayout(new GridLayout(1,1));
		
		sharePrice = new JLabel("Current KURS: " + 34);
		line3.add(sharePrice);
		pane.add(line3);
		
		
		JPanel line4 = new JPanel();
		line4.setLayout(new GridLayout(1,3));
		
		alarmShare = new JLabel("Set Alarm at rate: ");
		line4.add(alarmShare);
		
		alarmShareTF = new JTextField();
		line4.add(alarmShareTF);
		
		setAlarm = new JCheckBox();
		line4.add(setAlarm);
		pane.add(line4);
		
		
		JPanel line5 = new JPanel();
		line5.setLayout(new GridLayout(1,3));
		
		setMACD = new JLabel("Alarm on MACD: ");
		line5.add(setMACD);
		
		setMACDpos = new JCheckBox("Positive");
		line5.add(setMACDpos);
		
		setMACDneg = new JCheckBox("Negative");
		line5.add(setMACDneg);
		pane.add(line5);
		
		
		JPanel line6 = new JPanel();
		line6.setLayout(new GridLayout(3,3));
		
		setMA = new JLabel("Set Moving Average: ");
		line6.add(setMA);
		
		dropdownMA = new JComboBox(choicesMA);
		line6.add(dropdownMA);
		
		checkMA = new JCheckBox();
		line6.add(checkMA);
		
		ema = new JLabel("Set Specific EMA: ");
		line6.add(ema);
		
		emaTF = new JTextField();
		line6.add(emaTF);
		
		setEMA = new JCheckBox();
		line6.add(setEMA);
		
		sma = new JLabel("Set Specific SMA: ");
		line6.add(sma);
		
		smaTF = new JTextField();
		line6.add(smaTF);
		
		setSMA = new JCheckBox();
		line6.add(setSMA);
		pane.add(line6);
		
		
		JPanel line7 = new JPanel();
		line7.setLayout(new GridLayout(1,2));
		
		highVolume = new JLabel("Alarm on High Volume");
		line7.add(highVolume);
		
		highVolumeCB = new JCheckBox("");
		line7.add(highVolumeCB);
		pane.add(line7);
		
		
		JPanel line8 = new JPanel();
		line8.setLayout(new GridLayout(3,3));
		
		rsi = new JLabel("RSI");
		line8.add(rsi);
		
		FILLER = new JLabel("       ");
		line8.add(FILLER);
		
		rsiCB = new JCheckBox("over/under");
		line8.add(rsiCB);
		
		rsiOver = new JLabel("Set Over RSI: ");
		line8.add(rsiOver);
		
		rsiOverTF = new JTextField();
		line8.add(rsiOverTF);
		
		rsiOverCB = new JCheckBox();
		line8.add(rsiOverCB);
		
		rsiUnder = new JLabel("Set Under RSI: ");
		line8.add(rsiUnder);

		rsiUnderTF = new JTextField();
		line8.add(rsiUnderTF);
		
		rsiUnderCB = new JCheckBox();
		line8.add(rsiUnderCB);
		pane.add(line8);
		
		*/
		
		
		JPanel line9 = new JPanel();
		line9.setLayout(new GridLayout(1,1));
		
		addStock = new JButton("Add Stock");
		line9.add(addStock);
		pane.add(line9);
		
		AddClass ac = new AddClass();
		addStock.addActionListener(ac);
		
	
	}
	
	// Class used to see if searched stock actually exists
	public class SearchClass implements ActionListener {
		public String webDataTemp;
		
		public void actionPerformed(ActionEvent sc) {
			try {
				String search = initTF.getText();
				
				
				if (search != "Result: Didn't find stock") {
					initTF.setEnabled(false);
				}
				
				
				String url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20csv%20WHERE%20url%3D\"http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D" + search + 
							 "%26f%3Dl1%26e%3D.csv\"%0A%20%20%20%20AND%20columns%3D\"lastPrice\"%3B";
				try{
					URL pageLocation = new URL(url);
			
					Scanner input = new Scanner(pageLocation.openStream());
					String webData = input.nextLine() + "\n";
      
					while(input.hasNextLine()) {
						webData +=  input.nextLine() + "\n";
					}
					
				webDataTemp = webData;
				
				} catch(MalformedURLException e) { 
					System.out.printf("Error");
				}
				
				int startIndex = webDataTemp.indexOf("<lastPrice>");
				int endIndex = webDataTemp.indexOf("</lastPrice>", startIndex);		
				float lastPrice = Float.parseFloat(webDataTemp.substring(startIndex + 11,endIndex));

				
				if (lastPrice != 0.0) { 
					searchResult.setText("Result: " + search);
				} else {
					searchResult.setText("Result: Didn't find stock");
				}
			} catch (Exception ex) {}
		}
	}
	
	public class AddClass implements ActionListener {
		public void actionPerformed(ActionEvent ac) {
			try {
				
				String symbol = initTF.getText();
				System.out.println("davs");
				System.out.println(symbol);
				// Stock newStock = new Stock(symbol); NOT POSSIBLE YET
				System.out.println("halli hallo");
				clearAndHide();
				dd.updateListAdd(newStock); //den dor efter den her action
				
				
			} catch (Exception ex) {}
		}
	}
}