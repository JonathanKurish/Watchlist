import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class IOStocks implements java.io.Serializable {

	public static Watchlist watchlist;
	
	
  public static int saveStocks() {
    try {
      ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("stocklist.wl"));
      save.writeObject(Watchlist.stocks);
      save.close();
    }
    catch(Exception ex) {
      return 1;
    }
    return 0;
  }
  
  public static int loadStocks(Watchlist parent) {
    
	watchlist = parent;
	
	try{
      ObjectInputStream restore = new ObjectInputStream(new FileInputStream("stocklist.wl"));
      watchlist.stocks = (ArrayList<Stock>) restore.readObject();
      restore.close();
    }
    catch(Exception ex) {
      System.out.println("Error");
      return 1;
    }
    
    return 0;
  
  }

}