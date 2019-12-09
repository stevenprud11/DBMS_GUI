package mypackage;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Checkout extends JFrame implements ActionListener {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JTextField name_t, shipping_t, ccnumber_t, ccode_t, exp_t, billing_t;
	static JPanel mpp;
	static JButton checkout;
	static ArrayList<Integer> Cart_List = new ArrayList<Integer>();
	static int CID;
	static boolean error = false;
	
	
	public Checkout(int CID, double price, ArrayList<Book> list){
		this.CID = CID;
		//set size of obj
		this.setSize(500,700);
		this.setLocationRelativeTo(null);
		this.setTitle("Checkout");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		JLabel total = new JLabel("Price $" + Double.toString(price));
		addComp(mpp, total, 0,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		JLabel books = new JLabel("Book List");
		addComp(mpp, books, 0,4,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel line = new JLabel("-------------------------------");
		JLabel line2 = new JLabel("-------------------------------");
		addComp(mpp, line, 0,8,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		int y = 12;
		for(int i = 0; i < list.size(); i++){
			addComp(mpp, new JLabel(Integer.toString(list.get(i).ISBN) + "   " + list.get(i).title), 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			y+=4;
		}
		
		addComp(mpp, line2, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel name = new JLabel("Name");
		name_t = new JTextField();
		addComp(mpp, name, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		name_t.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, name_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel shipping = new JLabel("Shipping Address");
		shipping_t = new JTextField();
		shipping_t.setPreferredSize( new Dimension( 400, 24 ) );
		addComp(mpp, shipping, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		addComp(mpp, shipping_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel billing = new JLabel("Billing Address");
		billing_t = new JTextField();
		billing_t.setPreferredSize( new Dimension( 400, 24 ) );
		addComp(mpp, billing, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		addComp(mpp, billing_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel ccnumber = new JLabel("Credit Card Number");
		addComp(mpp, ccnumber, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		ccnumber_t = new JTextField();
		ccnumber_t.setPreferredSize( new Dimension( 400, 24 ) );
		addComp(mpp, ccnumber_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel ccode = new JLabel("Credit Card Security Code");
		addComp(mpp, ccode, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		ccode_t = new JTextField();
		ccode_t.setPreferredSize( new Dimension( 100, 24 ) );
		addComp(mpp, ccode_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel exp = new JLabel("Credit Card Expiration Date");
		addComp(mpp, exp, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		exp_t = new JTextField();
		exp_t.setPreferredSize( new Dimension( 100, 24 ) );
		addComp(mpp, exp_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		//should print final price on this page
		checkout = new JButton("Checkout");
		addComp(mpp, checkout, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		checkout.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(name_t.getText().contentEquals("") || shipping_t.getText().contentEquals("") || ccnumber_t.getText().contentEquals("") || 
				ccode_t.getText().contentEquals("") || exp_t.getText().contentEquals("") || billing_t.getText().contentEquals("")) {
			failedAction("missing");
		}
		if (ccnumber_t.getText().length() < 16) {
			failedAction("cc");
		}
		else if (e.getSource()==checkout){
			finishCheckout();
			failedAction("succeed");
			this.dispose();
		}
	}
	
	
	public static void failedAction(String fail) {
		JPanel panel = new JPanel();
		JLabel label;
		panel.setMinimumSize(new Dimension(200, 200));
		JFrame frame = new JFrame();
		frame.setTitle("Error");
		if (fail == "missing") {
			label = new JLabel("Please fill out every field.");  
		}
		else if (fail == "cc") {
			label = new JLabel("The Credit Card number is bad.");  
		}
		else if(fail == "succeed") {
			frame.setTitle("Success");
			label = new JLabel("The purchase was processed successfully."); 
		}
		else {
			label = new JLabel("An error occured.");  
		}
		JOptionPane.showMessageDialog(frame, label);
	}
	
	
	public static void sshConnection(){
        String user = "zatheiss";
        String password = "Grad2015!";
        String host = "turing.csce.uark.edu";
        int port=22;
        try
            {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            lport = 2110;
            rhost = "localhost";
            rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
	        session.connect();
	        int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
            }
        catch(Exception e){System.err.print(e);}
    }
	
	public static void finishCheckout(){
		sshConnection();
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost +":" + lport + "/";
        String db = "zatheiss";
        String dbUser = "zatheiss";
	    String dbPasswd = "password";
	    try{
	    	Class.forName(driver);
	        con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	        try{
	        	
				//list now has all the carts/books checked
				//checkout the book
				//remove from list
				//update book table

	        	Statement st = con.createStatement();
	        	Date date = new Date();
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
	        	String sql = "SELECT Cart_ID FROM Shopping_Cart WHERE CID='"+CID+"';";
	        	ResultSet response = st.executeQuery(sql);
	        	while(response.next()) {
	        		Cart_List.add(response.getInt("Cart_ID"));
	        	}
	        	for (int i=0; i< Cart_List.size(); i++) {
		        	sql = "UPDATE Cart_Detail SET Date_Updated ='"+formatter.format(date)+"' WHERE Cart_ID = '"+Cart_List.get(i)+"';";
		        	st.executeUpdate(sql);
		        	sql = "UPDATE Shopping_Cart SET Date_Purchased ='"+formatter.format(date)+"' WHERE Cart_ID = '"+Cart_List.get(i)+"' AND CID='"+CID+"';";
		        	st.executeUpdate(sql);
	        	}
	        }
	        catch (SQLException x){
	        	System.out.println(x);
	      	}
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    }
    	session.disconnect();
    }
	
	
	private static void addComp(JPanel thePanel, JComponent comp, int xP, int yP, int w, int h, int place, int stretch)
	{
		GridBagConstraints gridC = new GridBagConstraints();
		gridC.gridx = xP;
		gridC.gridy = yP;
		gridC.gridwidth = w;
		gridC.gridheight = h;
		gridC.insets = new Insets(5,5,5,5);
		gridC.anchor = place;
		gridC.fill = stretch;
		thePanel.add(comp, gridC);	
	}
	
}
