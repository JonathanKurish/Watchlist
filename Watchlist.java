import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class Watchlist extends JFrame {
	
	
	
	public final ArrayList<Stock> stocks = new ArrayList<Stock>();
	
	int currentInterval;
	JButton addNewStock, updateInterval, viewStock, remove;
	JList stocklist;
	JLabel currentIntervalLabel, stocksAdded, listTitle;
	JScrollPane listScroller;
	AddWindow addWindow;
	    
	JFrame frame;

	
	
	
	
	 // Sets the Updater Interval
    void setInterval(String newText) {
        currentIntervalLabel.setText(newText);
    }
	
	// Updates list of stocks after addition of new stock
	void updateListAdd(Stock stock) {
		stocks.add(stock);
		int amountOfStocks = stocks.size();
		stocksAdded.setText("Stocks Added: " + amountOfStocks);
		Stock[] array = stocks.toArray(new Stock[stocks.size()]);
		stocklist.setListData(array);
		listScroller.revalidate();
		listScroller.repaint();
	}
	
	// Updates list of stocks after removal of a stock
	void updateListRemove(Stock[] array) {
		
		// DEN HER KAN LAVES BEDRE RET NEMT
		
		stocks.clear();
		System.out.println(stocks);
		for (int i = 0; i < array.length; i++) {
			stocks.add(array[i]);
		}
		System.out.println(stocks);
		int amountOfStocks = array.length;
		stocksAdded.setText("Stocks Added: " + amountOfStocks);
		
		Stock[] array2 = stocks.toArray(new Stock[stocks.size()]);
		
		stocklist.setListData(array2);
		listScroller.revalidate();
		listScroller.repaint();
	}
	
	// Constructs the Main Window
	public Watchlist() {
		currentInterval = 10;

		
		
		//Creates Add Stock Window, but hides it
		this.frame = frame;
		addWindow = new AddWindow(frame, this, stocks);
		addWindow.pack();
		addWindow.setVisible(false);
		addWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addWindow.setLocation(300,300);
		
		
		
		//Designer vinduet
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(5,1,2,2));
		
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,2));
		top.setBackground(Color.BLACK);
		
		addNewStock = new JButton("Add Stock");
		addNewStock.setBackground(Color.RED);
		top.add(addNewStock);
		
		updateInterval = new JButton("Update Timer");
		updateInterval.setBackground(Color.YELLOW);
		top.add(updateInterval);
		
		pane.add(top);
		
		JPanel topmid = new JPanel();
		topmid.setLayout(new GridLayout(1,2));
		
		stocksAdded = new JLabel("Stocks Added: " + stocks.size());
		topmid.add(stocksAdded);
		
		currentIntervalLabel = new JLabel("Update Interval: " + currentInterval);
		topmid.add(currentIntervalLabel);
		
		pane.add(topmid);
		
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1,1));
		
		listTitle = new JLabel("Stocks to Watch: ");
		middle.add(listTitle);
		pane.add(middle);
		
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1,1));
		
		stocklist = new JList(stocks.toArray());
		stocklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stocklist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		stocklist.setVisibleRowCount(10);
		stocklist.setBounds(200,200,100,50);
		bottom.add(new JScrollPane(stocklist));
		pane.add(bottom);
		
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
				if(index != -1){ //Remove only if a particular item is selected
					stocks.remove(index);
					Stock[] array = stocks.toArray(new Stock[stocks.size()]);
					
					updateListRemove(array);
					
					
				}
			} catch (Exception ex) {}
		}
	}
	
	// Class used to view Stock information
	public class ViewClass implements ActionListener {
		public void actionPerformed(ActionEvent vc) {
			try {
	
			} catch (Exception ex) {}
		}
	}
	
	// Class used to add new Stock objects to the list
	public class AddWindowClass implements ActionListener {
		public void actionPerformed(ActionEvent awc) {
			try {
				//The window has already been created. This function just makes it visible
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
                            null,
                            null,
                            "10");
 
                    //If a string was returned, say so.
                    if ((s != null) && (s.length() > 0)) {
                        setInterval("Update Interval: " + s);
                        return;
                    }
			} catch (Exception ex) {}
		}
	}
	
	// Main Method
	public static void main(String args[]) {
		Watchlist gui = new Watchlist();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.setTitle("Watchlist");
		gui.setSize(350,400);
	}
}
	