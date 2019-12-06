package mypackage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

class Book{
	int ISBN;
	double price;
	String title;
	public Book(double price, int ISBN, String title){
		this.price = price;
		this.ISBN = ISBN;
		this.title = title;
	}
}

public class Cart extends JFrame implements ActionListener{
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static JButton checkout;
	static int CID;
	static ArrayList<JCheckBox> checkbox = new ArrayList<>();
	static ArrayList<Book> ISBN_Location = new ArrayList<Book>();
	
	public Cart(int CID){
		sshConnection();
		this.CID = CID;
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Cart");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		checkout = new JButton("Checkout");
		addComp(mpp, checkout, 0,22,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		checkout.addActionListener(this);
		
		
		this.add(mpp);
		this.setVisible(true);
		
		executeCart();
		
	}
	
	public static void executeCart(){
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost +":" + lport + "/";
        String db = "zatheiss";
        String dbUser = "zatheiss";
	    String dbPasswd = "password";
	    try{
	    	Class.forName(driver);
	    	String reasonForFail = "";
	        con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	        try{
	        	Statement st = con.createStatement();
	        	String sql = "select * from Book;";
	        	ResultSet response = st.executeQuery(sql);
	        	int y = 0;
	        	while(response.next()){
	        		JCheckBox box = new JCheckBox(); //stores check box and book next to each other to check to see if box is checked
	        		checkbox.add(box);
	        		int ISBN = response.getInt("ISBN");
	        		double price = response.getDouble("Price");
	        		String title = response.getString("Title");
	        		ISBN_Location.add(new Book(price, ISBN, title));
	        		JLabel num = new JLabel(Integer.toString(ISBN));
	        		
	        		addComp(mpp, num, 4,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, box, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		
	        		y+=4;
	        	}
	        }
	        	catch(Exception e){
	        		e.printStackTrace();
	        	}
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    }
    	session.disconnect();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==checkout){
			System.out.println("checking out");
			//need to see which carts are checked, 
			//ones that are checked are the ones we are going to checkout
			ArrayList<Book> list = new ArrayList<>();
			double total = 0.0;
			for(int i = 0; i < checkbox.size(); i++){
				JCheckBox box = checkbox.get(i);
				if(box.isSelected()){
					list.add(ISBN_Location.get(i));
					total+=ISBN_Location.get(i).price;
				}
					
			}
			//list now has all the carts/books checked
			//checkout the book
			//remove from list
			//update book table
			
			
			Checkout checkout_page = new Checkout(CID, total, list);
		}
		
	}
}
