import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Stock {

  //The first instance variables describes the stock itself
  public String symbol;
  public float lastPrice, open, high, low, close, change, changeInPer, rsi, MACDfast, MACDslow, MA200, MA50, EMA8, EMA12, EMA20;
  public int volumen, avgVolumen;  
  
  float[] pastPrices = new float[30];
  float[] emas = new float[3];
  
  //These instance variables describes the target for the indicators
  
  public float targetPrice;   // targetPrice is the price that the user wants to buy/sell at
  public boolean overbought;  // If this value is TRUE the user wants to test to see if the stock is overbought
  public boolean oversold;    // If this value is TRUE the user wants to test to see if the stock is oversold
  public boolean goldencross; // If this value is TRUE the user wants to be notified when a goldencross forms
  public boolean deathcross;  // If this value is TRUE the user wants to be notified when a deathcross forms
  public boolean MACDcross;   // If this value is TRUE the user wants to be notified when the MACD lines crosses
  public boolean highVolume;  // If this value is TRUE the user wants to be notified when volume is high
  
  public String webData;
  public String webDataTemp;
  
  public Stock(String StockSymbol) throws IOException {
    
    symbol = StockSymbol;
    
    String date = "2014-03-20";
    
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
      
      int startIndex, endIndex; // Here we initialize the two int variables to be used to get the data 
      
      // Here we grab the lastest price from the xml
      startIndex = webDataTemp.indexOf("<lastPrice>");
      endIndex = webDataTemp.indexOf("</lastPrice>", startIndex);
      lastPrice = Float.parseFloat(webDataTemp.substring(startIndex + 11,endIndex));
      
      if(lastPrice == 0.00) {
	/* Udvikle error da aktien ikke findes */
      }
      
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
      MA200 = Float.parseFloat(webDataTemp.substring(startIndex + 7,endIndex));
      
      // Here we grab the 200 Moving Average
      startIndex = webDataTemp.indexOf("<MA50>");
      endIndex = webDataTemp.indexOf("</MA50>", startIndex);
      MA50 = Float.parseFloat(webDataTemp.substring(startIndex + 6,endIndex));
      
      
      // Her ligger vi de tidligere dages priser ind, 20 dages data
      for(int i = 0; i < 30; i++) {
	startIndex = webDataTemp.indexOf("<Close>", endIndex);
	endIndex = webDataTemp.indexOf("</Close>", startIndex);
	pastPrices[i] = Float.parseFloat(webDataTemp.substring(startIndex + 7,endIndex));
      }
      
      //Here we calculate the RSI and put it into the instance variable
      rsi = rsiCalculate(pastPrices);
      System.out.println(rsi);
      // Here we call the function to calculate the EMA's and return an array
      emas = emaCalculate(pastPrices);
      EMA8 = emas[0];
      EMA12 = emas[1];
      EMA20 = emas[2];
       

    }
    
    public float rsiCalculate(float[] pastPrices) {
      float RSI = 0;
      float loss = 0;
      float gain = 0;
	  
      int c = 14, i = 13, counter = 1;
      
      for(int d = 0; d < 20; d++) {
	System.out.println(pastPrices[d]);
      }
      
      while(counter < 15) {
      
	if(pastPrices[i] < pastPrices[c]) {
	  loss = loss + (pastPrices[c] - pastPrices[i]);
	}
	
	
	if(pastPrices[i] > pastPrices[c]) {
	  gain = gain + (pastPrices[i] - pastPrices[c]);
	}
	counter++;
	c--;
	i--;
      }
      System.out.println(loss);
      System.out.println(gain);
      loss = loss/14;
      gain = gain/14;
      
      float todayGain = 0;
      float todayLoss = 0;
      
      if(lastPrice < pastPrices[0]) {
	todayLoss = pastPrices[0] - lastPrice;
      }
      
      if(lastPrice > pastPrices[0]) {
	todayGain = lastPrice - pastPrices[0];
      }
      
      float RS = gain / loss;
      
      System.out.println(c);
      
      float smooth = (((gain * 13) + todayGain) / 14) / (((loss * 13) + todayLoss) / 14);
      
      RSI = 100 - (100 / (1 + smooth));
      
      return RSI;
    }
    
    public float[] emaCalculate(float[] pastPrices) {
    
      int c = 14;
      float s8 = 0;
	  float s12 = 0;
	  float s20 = 0;
	  float ema8 = 0;
	  float ema12 = 0;
	  float ema20 = 0;
      float multiplier8 = 0;
	  float multiplier12 = 0;
	  float multiplier20 = 0;
      float[] emas = new float[3];
      
      while(c > 6) {
	s8 = s8 + pastPrices[c];
	c--;
      }
      s8 = s8 / 8;

      c = 0;

      while(c < 12) {
	s12 = s12 + pastPrices[19 - c];
	c++;
      }
      s12 = s12 / 12;
      c = 0;

      while(c < 20) {
	s20 = s20 + pastPrices[27 - c];
	c++;
      }
      s20 = s20 / 20;

      multiplier8 = (2 / (8 + 1) );
      multiplier12 = (2 / (12 + 1));
      multiplier20 = (2 / (20 + 1));
      
      ema8 = s8;
      c = 7;

      while(c >= 0) {
	ema8 = (pastPrices[c] - ema8) * multiplier8 + ema8;
	c--;
	System.out.println("EMA 8 =" + ema8);
      }

      emas[0] = (lastPrice - ema8) * multiplier8 + ema8;

      c = 12;
      ema12 = s12;
      
      while(c >= 0) {
	ema12 = (pastPrices[c] - ema12) * multiplier12 + ema12;
	c--;
      }
      
      emas[1] = (lastPrice - ema12) * multiplier12 + ema12;
      
      c = 0;
      ema20 = s20;
      
      while(c >= 19) {
	ema20 = (pastPrices[c] - ema20) * multiplier20 + ema20;
	c--;
      }
      emas[2] = (lastPrice - ema20) * multiplier20 + ema20;

      return emas;
    }
    
    
  
}