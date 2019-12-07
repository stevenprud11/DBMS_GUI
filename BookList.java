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

public class BookList extends JFrame implements ActionListener {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static String title;
	static int ISBN, quantity;
	static double price;
	static ArrayList<JButton> add_button = new ArrayList<>();
	static ArrayList<Integer> ISBN_Location = new ArrayList<Integer>();
	static ArrayList<Integer> Cart_List = new ArrayList<Integer>();
	int CID;
	static int cart_ID = 0, newCart;
	static boolean notfound = false;
	
	public BookList(int CID){
		sshConnection();
		this.CID = CID;
		
		
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Book List");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		//add labels for data to go under
		JLabel title_label = new JLabel("Title");
		addComp(mpp, title_label, 0,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel ISBN_label = new JLabel("ISBN");
		addComp(mpp, ISBN_label, 4,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	
		JLabel price_label = new JLabel("Price");
		addComp(mpp, price_label, 8,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel quantity_label = new JLabel("Quantity");
		addComp(mpp, quantity_label, 12,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		executeBookList();
		
		this.add(mpp);
		this.setVisible(true);
		
		
		
	}
	
	/*
	 * execute query to get all data from Book table
	 * it loops through all the books, grabs all the data
	 * and displays it row by row on the JPanel
	 * 
	 * it also adds the ISBN and Button to different arraylist
	 * these correlate indexes to know which button corresponds
	 * to which book, in the button listener
	 * button[i] corresponds to ISBN_Location[i] (they are in arraylist)
	 */
	public void executeBookList(){
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
	        	String sql = "select * from Book";
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
	        session.connect();
	        session.setPortForwardingL(lport, rhost, rport);
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
	/*
	 * listen for button press loops through button arraylist
	 * if button[i] is pressed, go to ISBN arraylist[i] and that book is chosen
	 * decrease quantity and add book to cart
	 * need more queries to update cart and update book quantity number
	 */
	@Override
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
	    	        Connection con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	    	        try{
	    	        	Statement st = con.createStatement();
	    	        	Date date = new Date();
	    	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
	    	        	String sql = "SELECT * FROM Shopping_Cart WHERE CID='"+CID+"';";
	    	        	ResultSet carts = st.executeQuery(sql);
	    	        	System.out.println(newCart);
	    	        	
	    	        	while (carts.next()) {
	    	        		Cart_List.add(carts.getInt("Cart_ID"));
	    	        	}
	    	        	for(int x = 0; x < Cart_List.size(); x++){
	    	        		if (Cart_List.get(x) != null) {
	    	        			sql = "SELECT Cart_ID FROM Shopping_Cart";
		    	        		ResultSet ids = st.executeQuery(sql);
		    	        		while (ids.next()) {
		    	        			if (ids.getInt("Cart_ID") > cart_ID)
		    	        				cart_ID = ids.getInt("Cart_ID");
		    	        		}
		    	        		notfound = true;
	    	        		}
	    	        	}
	    	        	if (notfound == true) {
		    	        	newCart = cart_ID + 1;
	    	        		sql = "INSERT INTO Shopping_Cart (Cart_ID, CID) VALUES ('"+newCart+"', '"+CID+"');";
	    	        		st.executeUpdate(sql);
	    	        	}
	    	        	
	    	        	if (newCart == 0) {
		    	        	sql = "INSERT INTO Cart_Detail (Cart_ID, ISBN, Quantity, Date_Created, Date_Updated)"
		    	        			+ "VALUES ((SELECT Cart_ID FROM Shopping_Cart WHERE CID ='"+CID+"'), '"+ISBN_Location.get(i)+"', '"+1+"', '"+formatter.format(date)+"', '"+formatter.format(date)+"')";
		    	        	st.executeUpdate(sql);
	    	        	}
	    	        	
	    	        	else if (newCart > 0) {
	    	        		sql = "INSERT INTO Cart_Detail (Cart_ID, ISBN, Quantity, Date_Created, Date_Updated)"
		    	        			+ "VALUES ('"+newCart+"', '"+ISBN_Location.get(i)+"', '"+1+"', '"+formatter.format(date)+"', '"+formatter.format(date)+"')";
		    	        	st.executeUpdate(sql);
	    	        		
	    	        	}
	    	        	sql = "SELECT Quantity FROM Book WHERE ISBN='"+ISBN_Location.get(i)+"'";
	    	        	ResultSet queryResult = st.executeQuery(sql);
	    	        	queryResult.next();
	    	        	int currentQuantity = queryResult.getInt("Quantity") - 1;
	    	        	System.out.println("Quantity"+currentQuantity);
	    	        	sql = "UPDATE Book SET Quantity = '"+currentQuantity+"' WHERE ISBN='"+ISBN_Location.get(i)+"'";
	    	        	st.executeUpdate(sql);
	    	        	session.disconnect();
	    	        	
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
	    	    BookList booklist = new BookList(CID);
	    	    sshConnection();
	    	    executeBookList();
	    	    mpp.validate();
	    	    mpp.repaint();
			}
		}	
	}
}
