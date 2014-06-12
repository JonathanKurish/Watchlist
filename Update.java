import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.text.*;

public class Update {
	
	public static Timer timer;
	public int interval;
	public static Watchlist watchlist;
	public String time;
	
	public Update(int currentInterval, Watchlist gui) {
		this.timer = new Timer();
		interval = currentInterval;
		watchlist = gui;
	}
	
	public void start() {
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		TimerTask task = new TimerTask() {
			public void run() {
				
				System.out.println("updating started");
				
				
				String updateData = "";
				String updateDataTemp = "";
				ArrayList<Stock> stocks = watchlist.stocks;
				ArrayList<Double> stockPrices = watchlist.stockPrices;
				String symbolsToUrl = "";
				
				Calendar cal = Calendar.getInstance();
				time = (sdf.format(cal.getTime()));
				System.out.println(time);
				int Hour = Integer.parseInt(time.substring(0,2));
				int Min = Integer.parseInt(time.substring(3,5));
				
				if(Hour < 15 || (Hour == 15 && Min > 45) || Hour > 15 ) {
					//Here we make a string of all the stocks to be updated. We get all the data at the same time from YQL
					for (int i = 0; i <stocks.size(); i++) {
						symbolsToUrl = symbolsToUrl + (stocks.get(i).symbol + "%2b");
					}

					if (symbolsToUrl != "") {
						symbolsToUrl = symbolsToUrl.substring(0,symbolsToUrl.length() - 3);
				
						String updateUrl = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20csv%20WHERE%20" + 
						   "url%3D%22http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D" + symbolsToUrl + 
						   "%26f%3DSl1ohgvc6a2p2m4m3%26e%3D.csv%22%0A%20%20%20%20" + 
						   "AND%20columns%3D%22Symbol%2ClastPrice%2Copen%2Chigh%2Clow%2Cvolumen%2Cchange%2CavgVolumen%2CchangePer%2CMA200%2CMA50%22";

						try{
							URL pageLocation = new URL(updateUrl);
			  
							Scanner input = new Scanner(pageLocation.openStream());
							updateData = input.nextLine() + "\n";
			  
							while(input.hasNextLine()) {
								updateData +=  input.nextLine() + "\n";
							}
			  
							updateDataTemp = updateData;

						} catch(Exception e) { 
							System.out.printf("Update Error");
						}
				  
						for (int i = 0; i <stocks.size(); i++) {
				  
							int startIndex, endIndex;
							Stock stock = stocks.get(i);

							
							startIndex = updateDataTemp.indexOf("<Symbol>"+stock.symbol+"</Symbol>");
							
							startIndex = updateDataTemp.indexOf("<lastPrice>", startIndex);
							endIndex = updateDataTemp.indexOf("</lastPrice>", startIndex);
							stock.lastPrice = Double.parseDouble(updateDataTemp.substring(startIndex + 11,endIndex));
							
							startIndex = updateDataTemp.indexOf("<open>",endIndex);
							endIndex = updateDataTemp.indexOf("</open>", startIndex);
							stock.open = Double.parseDouble(updateDataTemp.substring(startIndex + 6,endIndex));
							
							startIndex = updateDataTemp.indexOf("<high>",endIndex);
							endIndex = updateDataTemp.indexOf("</high>", startIndex);
							stock.high = Double.parseDouble(updateDataTemp.substring(startIndex + 6,endIndex));
							
							startIndex = updateDataTemp.indexOf("<low>",endIndex);
							endIndex = updateDataTemp.indexOf("</low>", startIndex);
							stock.low = Double.parseDouble(updateDataTemp.substring(startIndex + 5,endIndex));
							
							startIndex = updateDataTemp.indexOf("<volumen>",endIndex);
							endIndex = updateDataTemp.indexOf("</volumen>", startIndex);
							stock.volumen = Integer.parseInt(updateDataTemp.substring(startIndex + 9,endIndex));
							
							startIndex = updateDataTemp.indexOf("<change>",endIndex);
							endIndex = updateDataTemp.indexOf("</change>", startIndex);
							stock.change = Double.parseDouble(updateDataTemp.substring(startIndex + 8,endIndex));
							
							startIndex = updateDataTemp.indexOf("<avgVolumen>",endIndex);
							endIndex = updateDataTemp.indexOf("</avgVolumen>", startIndex);
							stock.avgVolumen = Integer.parseInt(updateDataTemp.substring(startIndex + 12,endIndex));
							
							startIndex = updateDataTemp.indexOf("<changePer>",endIndex);
							endIndex = updateDataTemp.indexOf("</changePer>", startIndex);
							stock.changeInPer = Double.parseDouble(updateDataTemp.substring(startIndex + 11,endIndex - 1));
							
							startIndex = updateDataTemp.indexOf("<MA200>",endIndex);
							endIndex = updateDataTemp.indexOf("</MA200>", startIndex);
							stock.SMA200 = Double.parseDouble(updateDataTemp.substring(startIndex + 7,endIndex));
							
							startIndex = updateDataTemp.indexOf("<MA50>",endIndex);
							endIndex = updateDataTemp.indexOf("</MA50>", startIndex);
							stock.SMA50 = Double.parseDouble(updateDataTemp.substring(startIndex + 6,endIndex));
						
							stock.EMA8 = Stock.calculateEMA(stock.lastPrice, 8, stock.lastEMA8);
							stock.EMA12 = Stock.calculateEMA(stock.lastPrice, 12, stock.lastEMA12);
							stock.EMA20 = Stock.calculateEMA(stock.lastPrice, 20, stock.lastEMA20);
							stock.EMA26 = Stock.calculateEMA(stock.lastPrice, 26, stock.lastEMA26);
							stock.MACDline = (stock.EMA12 - stock.EMA26);
						}
					}
					System.out.println("HVORFOR");
					try {
						watchlist.updateList(); 
						watchlist.alarm();		
					} catch (Exception ex) {}
				}
			}
		};
		timer.scheduleAtFixedRate(task,0, interval * 1000);	
	}
	
		public void stop() {
			this.timer.cancel();
			this.timer.purge();
			
	}
}