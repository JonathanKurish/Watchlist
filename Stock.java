import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.text.*;

public class Stock implements java.io.Serializable {

  //The first instance variables describes the stock itself
  public String symbol;
  public double open, high, low, close, change, changeInPer, SMA200, SMA50, 
			MACDline, MACDtrigger, MACDtriggerOld, EMA8, EMA12, EMA20, rsi, lastEMA8, 
			lastEMA12, lastEMA20, lastEMA26, EMA26, lastPrice, rsiUnder, rsiOver;
  public int volumen, avgVolumen;  
  
  public boolean MACDstart; // This is set to TRUE if MACD line was under the trigger-line, and FALSE if it start above
  
  double[] pastPrices = new double[52];
  
  //These instance variables describes the target for the indicators
  
  public double targetPrice;   // targetPrice is the price that the user wants to buy/sell at
  public double SMA200target, SMA50target, EMA8target, EMA12target, EMA20target;
  public boolean overbought;  // If this value is TRUE the user wants to test to see if the stock is overbought
  public boolean oversold;    // If this value is TRUE the user wants to test to see if the stock is oversold
  public boolean goldencross; // If this value is TRUE the user wants to be notified when a goldencross forms
  public boolean deathcross;  // If this value is TRUE the user wants to be notified when a deathcross forms
  public boolean MACDcrossPositive;   // If this value is TRUE the user wants to be notified when the MACD lines 
  public boolean MACDcrossNegative;   // If this value is TRUE the user wants to be notified when the MACD lines crosses
  public boolean highVolume;  // If this value is TRUE the user wants to be notified when volume is high
  public boolean targetAU; // Target above or under describes if the target price is under the current price or above. Above = TRUE, under = FALSE
  
  // TILFOJET AF JONATHAN
  public boolean hasAlarm; // If this value is TRUE, then a criteria has been met
  
  public String webData;
  public String webDataTemp;
  
  public Stock(String StockSymbol) throws IOException {
    
    symbol = StockSymbol;
    
    String date = "2014-01-01";
    
    String url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yql.query.multi%20WHERE%20queries%3D%27%0A%20%20%20%20" +
		 "SELECT%20*%20FROM%20csv%20WHERE%20url%3D%22http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D" + symbol + 
		 "%26f%3Dl1ohgvc6a2p2m4m3%26e%3D.csv%22%0A%20%20%20%20AND%20" + 
		 "columns%3D%22lastPrice%2Copen%2Chigh%2Clow%2Cvolumen%2Cchange%2CavgVolumen%2CchangePer%2CMA200%2CMA50%22%3B%0A%20%20%20%20" + 
		 "SELECT%20Symbol%2CDate%2CClose%20FROM%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22" + symbol + 
		 "%22%20and%20startDate%20%3D%20%22" + date + "%22%20and%20endDate%20%3D%20%22now%22%0A%27%3B&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    try{
      URL pageLocation = new URL(url);
      
      Scanner input = new Scanner(pageLocation.openStream());
      webData = input.nextLine() + "\n";
      
      while(input.hasNextLine()) {
	webData +=  input.nextLine() + "\n";
      }
      
      webDataTemp = webData;
      
    }
      
    catch(MalformedURLException e) { 
      System.out.printf("Error");
    }
      final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      int startIndex, endIndex, test; // Here we initialize the two int variables to be used to get the data 
      String time;
      test = 1;
	  
	  Calendar cal = Calendar.getInstance();
		time = (sdf.format(cal.getTime()));
		System.out.println(time);
		int Hour = Integer.parseInt(time.substring(0,2));
		int Min = Integer.parseInt(time.substring(3));
      
      // Here we grab the lastest price from the xml
      startIndex = webDataTemp.indexOf("<lastPrice>");
      endIndex = webDataTemp.indexOf("</lastPrice>", startIndex);

      if(Hour < 15 || (Hour == 15 && Min > 45) || Hour > 15 ) {

      test = 0;
      
      lastPrice = Double.parseDouble(webDataTemp.substring(startIndex + 11,endIndex));
      
      // Here we grab the open price from the xml
      startIndex = webDataTemp.indexOf("<open>");
      endIndex = webDataTemp.indexOf("</open>", startIndex);
      open = Float.parseFloat(webDataTemp.substring(startIndex + 6,endIndex));
      
      // Here we grab the High price of the day from the xml
      startIndex = webDataTemp.indexOf("<high>");
      endIndex = webDataTemp.indexOf("</high>", startIndex);
      high = Float.parseFloat(webDataTemp.substring(startIndex + 6,endIndex));
      
      // Here we grab the Low price of the day from the xml
      startIndex = webDataTemp.indexOf("<low>");
      endIndex = webDataTemp.indexOf("</low>", startIndex);
      low = Float.parseFloat(webDataTemp.substring(startIndex + 5,endIndex));
      
      // Here we grab the volume
      startIndex = webDataTemp.indexOf("<volumen>");
      endIndex = webDataTemp.indexOf("</volumen>", startIndex);
      volumen = Integer.parseInt(webDataTemp.substring(startIndex + 9,endIndex));
      
      // Here we grab the average volume
      startIndex = webDataTemp.indexOf("<avgVolumen>");
      endIndex = webDataTemp.indexOf("</avgVolumen>", startIndex);
      avgVolumen = Integer.parseInt(webDataTemp.substring(startIndex + 12,endIndex));
      
      // Here we grab the change of price
      startIndex = webDataTemp.indexOf("<change>");
      endIndex = webDataTemp.indexOf("</change>", startIndex);
      change = Float.parseFloat(webDataTemp.substring(startIndex + 8,endIndex));
      
      // Here we grab the change of price in percent
      startIndex = webDataTemp.indexOf("<changePer>");
      endIndex = webDataTemp.indexOf("</changePer>", startIndex);
      changeInPer = Float.parseFloat(webDataTemp.substring(startIndex + 11,endIndex - 1));
      
      // Here we grab the 200 Moving Average
      startIndex = webDataTemp.indexOf("<MA200>");
      endIndex = webDataTemp.indexOf("</MA200>", startIndex);
      SMA200 = Float.parseFloat(webDataTemp.substring(startIndex + 7,endIndex));
      
      // Here we grab the 200 Moving Average
      startIndex = webDataTemp.indexOf("<MA50>");
      endIndex = webDataTemp.indexOf("</MA50>", startIndex);
      SMA50 = Float.parseFloat(webDataTemp.substring(startIndex + 6,endIndex));
      
      }
      // Her ligger vi de tidligere dages priser ind, 20 dages data
	  for(int i = 0; i < 51; i++) {
	startIndex = webDataTemp.indexOf("<Close>", endIndex);
	endIndex = webDataTemp.indexOf("</Close>", startIndex);
	pastPrices[i] = Double.parseDouble(webDataTemp.substring(startIndex + 7,endIndex));
      }
      
      if(test == 1) {
	lastPrice = pastPrices[0];
      }

      //Here we calculate the RSI and put it into the instance variable
      rsi = rsiCalculate(pastPrices);
    //  System.out.println("RSI = "+rsi);
      // Here we call the function to calculate the EMA's and return an array
      emaCalculate(pastPrices);
      macdCalculator(pastPrices);
	  
	  
				System.out.println("symbol: " + symbol);
				System.out.println("last price: " + lastPrice);
				System.out.println("open price: " + open);
				System.out.println("high of day: " + high);
				System.out.println("low of day: " + low);
				System.out.println("volume: " + volumen);
				System.out.println("avg volume: " + avgVolumen);
				System.out.println("change: " + change);
				System.out.println("changeInPer: " + changeInPer);
				System.out.println("SMA200: " + SMA200);
				System.out.println("SMA50: " + SMA50);
				
       

    }
    
    public double rsiCalculate(double[] pastPrices) {
      double RSI = 0;
      double loss = 0;
      double gain = 0;
	  
      int c = 14, i = 13, counter = 1;
      
      while(counter < 15) {
      
	if(pastPrices[c] > pastPrices[i]) {
	  loss = loss + (pastPrices[c] - pastPrices[i]);
	}
	
	
	if(pastPrices[c] < pastPrices[i]) {
	  gain = gain + (pastPrices[i] - pastPrices[c]);
	}
	counter++;
	c--;
	i--;
      }
   //   System.out.println(loss);
   //   System.out.println(gain);
      
      loss = (loss/14);
      gain = (gain/14);
      
   //   System.out.println(loss);
   //   System.out.println(gain);
      
      double RS = (gain / loss);
      
      RSI = 100 - (100 / (1 + RS));
      
      return RSI;
    }
    
    public double[] emaCalculate(double[] pastPrices) {
      double s8 = 0;
      double k = 0;
	  double s12 = 0;
	  double s20 = 0;
	  double ema8 = 0;
	  double ema12 = 0;
	  double ema20 = 0;
      double[] emas = new double[3];
      
      s8  = calculateMA(8,pastPrices,16);
      s12 = calculateMA(12,pastPrices,24);
      s20 = calculateMA(20,pastPrices,40);
      
      //Here we calculate all the EMAS
      lastEMA8 = Math.round((calculateEmaDays(8,pastPrices,0,s8)) * 100.0) / 100.0;
      System.out.println("EMA 8 = " + EMA8);
      
      lastEMA12 = Math.round((calculateEmaDays(12,pastPrices,0,s12)) * 100.0) / 100.0;
      System.out.println("EMA 12 = " + EMA12);
      
      lastEMA20 = Math.round((calculateEmaDays(20,pastPrices,0,s20)) * 100.0) / 100.0;
      System.out.println("EMA 20 = " + EMA20);

      return emas;
    }
    
    public static double calculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday) {
	double k = 2 / (numberOfDays + 1);
	return todaysPrice * k + EMAYesterday * (1 - k);
    }
    
    public double calculateMA(int days, double[] pastPrices, int index) {
      double MA = 0;
      
      for(int i = index - 1; i > days - 1; i--) {
	MA = MA + pastPrices[i];
      }
      
      MA = MA/days;
      System.out.println(MA);
      return MA;
    }
    
    public double calculateEmaDays(int days, double[] pastPrices, int to, double startMA) {
      double EMA = 0;
      EMA = calculateEMA(pastPrices[days-1], days, startMA);
      
      for(int c = days-2; c >= to; c--) {
	EMA = calculateEMA(pastPrices[c], days, EMA);
      }
      return EMA;
    }
    
    public void macdCalculator(double[] pastPrices) {
    
      double MA26 = 0;
      double MA12 = 0;
      double EMA12 = 0;
      
      MA26 = Math.round((calculateMA(26,pastPrices,52)) * 100.0 ) / 100.0;
      MA12 = Math.round((calculateMA(12,pastPrices,24)) * 100.0 ) / 100.0;

      lastEMA26 = Math.round((calculateEmaDays(26, pastPrices, 0, MA26))* 100.0 ) / 100.0;
      EMA12 = Math.round((calculateEmaDays(12, pastPrices, 0, MA12))* 100.0 ) / 100.0;
      System.out.println("EMA 26 = " + lastEMA26);
      System.out.println("EMA 12 = " + EMA12);
      
      MACDline = Math.round((EMA12 - lastEMA26)* 100.0 ) / 100.0;
  
      MACDtriggerOld = Math.round((calculateEMA(MACDline,9,MACDline)) * 1000.0) / 1000.0;
      
      System.out.println("MACD line = " + MACDline);
      //System.out.println("Trigger = " + MACDtrigger);
      
      
    }
  
}