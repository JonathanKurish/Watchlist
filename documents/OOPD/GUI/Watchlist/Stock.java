import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Stock {

  //The first instance variables describes the stock itself
  public String symbol;
  public float lastPrice, open, high, low, close, change, changeInPer, rsi, MACDfast, MACDslow, MA200, MA50, EMA8, EMA12, EMA20;
  public int volumen, avgVolumen;  
  
  float[] pastPrices = new float[20];
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
  
  
  public Stock(String StockSymbol) throws IOException {
  
    
    symbol = StockSymbol;
    
    String date = "2014-04-13";
    
    String url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yql.query.multi%20WHERE%20queries%3D%27%0A%20%20%20%20" +
		 "SELECT%20*%20FROM%20csv%20WHERE%20url%3D%22http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D" + symbol + 
		 "%26f%3Dl1ohgvc6a2p2m4m3%26e%3D.csv%22%0A%20%20%20%20AND%20" + 
		 "columns%3D%22lastPrice%2Copen%2Chigh%2Clow%2Cvolumen%2Cchange%2CavgVolumen%2CchangePer%2CMA200%2CMA50%22%3B%0A%20%20%20%20" + 
		 "SELECT%20Symbol%2CDate%2CClose%20FROM%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22" + symbol + 
		 "%22%20and%20startDate%20%3D%20%22" + date + "%22%20and%20endDate%20%3D%20%22now%22%0A%27%3B&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    try{
      URL pageLocation = new URL(url);
      
      Scanner input = new Scanner(pageLocation.openStream());
      String webData = input.nextLine() + "\n";
      
      while(input.hasNextLine()) {
	webData +=  input.nextLine() + "\n";
      }
    }
      
    catch(MalformedURLException e) { 
      System.out.printf("Error");
    }
      
      int startIndex, endIndex; // Here we initialize the two int variables to be used to get the data 
      
      // Here we grab the lastest price from the xml
      startIndex = webData.indexOf("<lastPrice>");
      endIndex = webData.indexOf("</lastPrice>", startIndex);
      lastPrice = Float.parseFloat(webData.substring(startIndex + 11,endIndex));
      
      if(lastPrice == 0.00) {
	/* Udvikle error da aktien ikke findes */
      }

      // Here we grab the open price from the xml
      startIndex = webData.indexOf("<open>");
      endIndex = webData.indexOf("</open>", startIndex);
      open = Float.parseFloat(webData.substring(startIndex + 6,endIndex));
      
      // Here we grab the High price of the day from the xml
      startIndex = webData.indexOf("<high>");
      endIndex = webData.indexOf("</high>", startIndex);
      high = Float.parseFloat(webData.substring(startIndex + 6,endIndex));
      
      // Here we grab the Low price of the day from the xml
      startIndex = webData.indexOf("<low>");
      endIndex = webData.indexOf("</low>", startIndex);
      low = Float.parseFloat(webData.substring(startIndex + 5,endIndex));
      
      // Here we grab the volume
      startIndex = webData.indexOf("<volumen>");
      endIndex = webData.indexOf("</volumen>", startIndex);
      volumen = Integer.parseInt(webData.substring(startIndex + 9,endIndex));
      
      // Here we grab the average volume
      startIndex = webData.indexOf("<avgVolumen>");
      endIndex = webData.indexOf("</avgVolumen>", startIndex);
      avgVolumen = Integer.parseInt(webData.substring(startIndex + 12,endIndex));
      
      // Here we grab the change of price
      startIndex = webData.indexOf("<change>");
      endIndex = webData.indexOf("</change>", startIndex);
      change = Float.parseFloat(webData.substring(startIndex + 8,endIndex));
      
      // Here we grab the change of price in percent
      startIndex = webData.indexOf("<changePer>");
      endIndex = webData.indexOf("</changePer>", startIndex);
      changeInPer = Float.parseFloat(webData.substring(startIndex + 11,endIndex - 1));
      
      // Here we grab the 200 Moving Average
      startIndex = webData.indexOf("<MA200>");
      endIndex = webData.indexOf("</MA200>", startIndex);
      MA200 = Float.parseFloat(webData.substring(startIndex + 7,endIndex));
      
      // Here we grab the 200 Moving Average
      startIndex = webData.indexOf("<MA50>");
      endIndex = webData.indexOf("</MA50>", startIndex);
      MA50 = Float.parseFloat(webData.substring(startIndex + 6,endIndex));
      
      rsi = rsiCalculate(pastPrices);
      
       emas = emaCalculate(pastPrices);
    }
    
    public float rsiCalculate(float[] pastPrices) {
      float RSI = 0;
      float loss = 0;
	  float gain = 0;
	  
      int c = 19, i = 18, counter = 1;
      
      while(counter < 15) {
      
	if(pastPrices[c] < pastPrices[i]) {
	  loss = loss + pastPrices[c];
	}
	
	if(pastPrices[c] > pastPrices[i]) {
	  gain = gain + pastPrices[c];
	}
	
	counter++;
	c--;
	i--;
      }
      
      float RS = gain / loss;
      
      RSI = 100 / 1 + RS;
      
      return RSI;
    }
    
    public float[] emaCalculate(float[] pastPrices) {
    
      int c = 0;
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
      
      while(c < 8) {
	s8 = s8 + pastPrices[11 + c];
	c++;
      }
      s8 = s8 / 8;
      c = 0;
      
      while(c < 12) {
	s12 = s12 + pastPrices[7 + c];
	c++;
      }
      s12 = s12 / 12;
      c = 0;
      
      while(c < 20) {
	s20 = s20 + pastPrices[c];
	c++;
      }
      s20 = s20 / 20;
      
      multiplier8 = (2 / (8 + 1) );
      multiplier12 = (2 / (12 + 1));
      multiplier20 = (2 / (20 + 1));
      
      ema8 = (pastPrices[12] - s8) * multiplier8 + s8;
      c = 1;
      
      while(c < 8) {
	ema8 = (pastPrices[12 + c] - ema8) * multiplier8 + ema8;
      }
      emas[0] = ema8;
      
      c = 1;
      ema12 = (pastPrices[7] - s12) * multiplier12 + s12;
      
      while(c < 12) {
	ema12 = (pastPrices[7 + c] - ema12) * multiplier12 + ema12;
      }
      emas[1] = ema12;
      
      c = 1;
      ema20 = (pastPrices[0] - s20) * multiplier20 + s20;
      
      while(c < 20) {
	ema20 = (pastPrices[0 + c] - ema20) * multiplier20 + ema20;
      }
      emas[2] = ema20;
      
      return emas;
    }
    
    
  
}