package mypackage;

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
import javax.swing.JPanel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class BookSearch extends JFrame implements ActionListener{
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	JPanel mpp;
	static String title_value;
	static String title;
	static int ISBN, quantity;
	static double price;
	static JButton button;
	static ArrayList<JButton> add_button = new ArrayList<>();
	public static ArrayList<Integer> ISBN_Location = new ArrayList<Integer>();
	int CID;
	
	public BookSearch(String title, int CID){
		this.CID = CID;
		if(title.compareTo("")!=0)
			this.title_value = title;
		else
			this.title_value="*";
		
		sshConnection();
		
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Book Search");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		//adds labels to JPanel for data to be under
		JLabel title_label = new JLabel("Title");
		addComp(mpp, title_label, 0,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel ISBN_label = new JLabel("ISBN");
		addComp(mpp, ISBN_label, 4,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	
		JLabel price_label = new JLabel("Price");
		addComp(mpp, price_label, 8,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel quantity_label = new JLabel("Quantity");
		addComp(mpp, quantity_label, 12,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		executeBookSearch();
		
		this.add(mpp);
		this.setVisible(true);
		
		
		
	}
	
	/*
	 * this function executes the book search query
	 * it works by passing in the title of the book in the constructor
	 * and searches the Book table for the book title
	 * if no title is found nothing is printed
	 * else, it puts the data for the book in the JPanel
	 */
	public void executeBookSearch(){
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
	        	String sql = "select * from Book where Title = '" + title_value + "';";
	        	ResultSet response = st.executeQuery(sql);
	        	int y = 6;
	        	while(response.next()){
	        		title = response.getString("Title");
	        		JLabel title_label = new JLabel(title);
	        		addComp(mpp, title_label, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		ISBN = response.getInt("ISBN");
	        		JLabel ISBN_label = new JLabel(Integer.toString(ISBN));
	        		addComp(mpp, ISBN_label, 4,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		price = response.getDouble("Price");
	        		JLabel price_label = new JLabel(Double.toString(price));
	        		addComp(mpp, price_label, 8,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		quantity = response.getInt("Quantity");
	        		JLabel quantity_label = new JLabel(Integer.toString(quantity));
	        		addComp(mpp, quantity_label, 12,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		//add button to cart
	        		
	        		if(quantity > 0) {
		        		JButton button = new JButton("ADD");
		        		addComp(mpp, button, 18, y, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		        		button.addActionListener(this);
		        		add_button.add(button);
		        		ISBN_Location.add(ISBN);
	        		}
	        		

	        		y+=6;
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
	/*
	 * ssh connection to database
	 */
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
	/*
	 * add component to JPanel
	 */
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
	/*
	 * Listens to see if button is pressed
	 * if so then adds book to cart and decreases quantity 
	 * of that book available in book table
	 */
	public void actionPerformed(ActionEvent e) {
		String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost +":" + lport + "/";
        String db = "zatheiss";
        String dbUser = "zatheiss";
	    String dbPasswd = "password";
		// TODO Auto-generated method stub
		for(int i = 0; i < add_button.size(); i++){
			if(e.getSource()==add_button.get(i)){
				System.out.println("correlates to " + ISBN_Location.get(i));
				sshConnection();
	    	    try{
	    	    	Class.forName(driver);
	    	    	String reasonForFail = "";
	    	        Connection con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	    	        try{
	    	        	Statement st = con.createStatement();
	    	        	Date date = new Date();
	    	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
	    	        	System.out.println(formatter.format(date));
	    	        	String sql = "INSERT INTO Cart_Detail (Cart_ID, ISBN, Quantity, Date_Created)"
	    	        			+ "VALUES (( SELECT Cart_ID FROM Shopping_Cart WHERE CID ='"+CID+"'), '"+ISBN_Location.get(i)+"', '"+1+"', '"+formatter.format(date)+"')";
	    	        	int response = st.executeUpdate(sql);
	    	        	sql = "SELECT Quantity FROM Book WHERE ISBN='"+ISBN_Location.get(i)+"'";
	    	        	ResultSet queryResult = st.executeQuery(sql);
	    	        	queryResult.next();
	    	        	int currentQuantity = queryResult.getInt("Quantity") - 1;
	    	        	sql = "UPDATE Book SET Quantity = '"+currentQuantity+"' WHERE ISBN='"+ISBN_Location.get(i)+"'";
	    	        	int updateResponse = st.executeUpdate(sql);
	    	        	if (response == 1) {
	    	        		session.disconnect();
	    	        	}
	    	        	else {
	    	        		session.disconnect();
	    	        	}
	    	        	
	    	        }
	    	        catch (SQLException x){
	    	        	System.out.println(x);
	    	      	}
	    	    }
	    	    catch (Exception e1){
	    	    	e1.printStackTrace();
	    	    }
	    	 
	    	    session.disconnect();
	    	    this.dispose();
	    	    BookSearch booksearch = new BookSearch(title_value, CID);
	    	    sshConnection();
	    	    executeBookSearch();
	    	    mpp.validate();
	    	    mpp.repaint();
			}
		}	
	}
}
