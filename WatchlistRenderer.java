import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;



import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class WatchlistRenderer {
	
	
	public static Watchlist watchlist;
	public ArrayList<Stock> stocks = watchlist.stocks;
	public static Stock stock;
	
	//protected static Border noFocusBorder = new EmptyBorder(15, 1, 1, 1);

  //protected static TitledBorder focusBorder = new TitledBorder(LineBorder.createGrayLineBorder(),
   //   "title");

	public AlarmRenderer createAlarmRenderer(Watchlist parent) {
		watchlist = parent;
		AlarmRenderer ar = new AlarmRenderer();
		return ar;
	}
   
	public static class AlarmRenderer extends DefaultListCellRenderer {

		
		
		public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
			
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			try {
				if (index >= watchlist.stocks.size()) {
					//do nothing
				} else if(watchlist.stocks.size() != 0) {
					stock = watchlist.stocks.get(index);
					if ((stock.lastPrice <= stock.targetPrice && stock.targetAU) ||
						(stock.lastPrice >= stock.targetPrice && !stock.targetAU) ||
						(stock.MACDcrossPositive && stock.MACDline > stock.MACDtrigger) ||
						(stock.MACDcrossNegative && stock.MACDline < stock.MACDtrigger) ||
						(stock.goldencross && stock.SMA200 > stock.SMA50) ||
						(stock.deathcross && stock.SMA200 < stock.SMA50) ||
						(stock.overbought && stock.rsi >= 70) ||
						(stock.oversold && stock.rsi <= 30) ||
						(stock.highVolume && stock.volumen > stock.avgVolumen) ||
						stock.EMA8target >= stock.EMA8 ||
						stock.EMA12target >= stock.EMA12 ||
						stock.EMA20target >= stock.EMA20 ||
						stock.SMA50target >= stock.SMA50 ||
						stock.SMA200target >= stock.SMA200 ||
						(stock.rsiUnder >= stock.rsi && stock.rsiUnder > 0) ||
						(stock.rsiOver <= stock.rsi && stock.rsiOver > 0)) {
						c.setForeground(Color.RED);
						stock.hasAlarm = true;
					} else { 
						c.setForeground(Color.BLACK);
						stock.hasAlarm = false;
					}
				}
			} catch (NullPointerException ex) {}
			
			
			return c;
			
		}
	}
}
