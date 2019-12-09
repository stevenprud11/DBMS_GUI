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
	static ArrayList<Integer> ISBN_LookUp = new ArrayList<Integer>();
	static ArrayList<Integer> Book_List = new ArrayList<Integer>();
	static int cart_ID;
	
	public Cart(int CID){
		sshConnection();
		this.CID = CID;
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Cart");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		checkout = new JButton("Checkout");
		addComp(mpp, checkout, 4,22,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
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
	        con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	        try{
	        	String sql;
	        	Statement st = con.createStatement();
	        	sql = "SELECT Cart_ID FROM Shopping_Cart WHERE CID='"+CID+"' AND Date_Purchased is NULL;";
	        	ResultSet response = st.executeQuery(sql);
	        	while(response.next()) {
	        		Book_List.add(response.getInt("Cart_ID"));
	        	}
	        	for (int i=0; i < Book_List.size(); i++) {
	        		sql = "SELECT ISBN FROM Cart_Detail WHERE Cart_ID='"+Book_List.get(i)+"';";
	        		ResultSet cartResponse = st.executeQuery(sql);
	        		System.out.println(cartResponse.wasNull());
	        		while(cartResponse.next()){
						ISBN_LookUp.add(cartResponse.getInt("ISBN"));
						System.out.println(cartResponse.getInt("ISBN"));
					}
	        	}
	       
				int y = 0;
				for(int i = 0; i < ISBN_LookUp.size(); i++){
					sql = "select ISBN, Price, Title from Book WHERE ISBN='"+ISBN_LookUp.get(i)+"';";
					ResultSet ISBNresponse = st.executeQuery(sql);
					ISBNresponse.next();
		        	
	        		JCheckBox box = new JCheckBox(); //stores check box and book next to each other to check to see if box is checked
	        		checkbox.add(box);
	        		
	        		int ISBN = ISBNresponse.getInt("ISBN");
	        		double price = ISBNresponse.getDouble("Price");
	        		String title = ISBNresponse.getString("Title");
	        		ISBN_Location.add(new Book(price, ISBN, title));

	        		JLabel num = new JLabel(Integer.toString(ISBN));
	        		JLabel str = new JLabel(title);
	        		
	        		addComp(mpp, num, 4,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, box, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, str, 7,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		
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
	    ISBN_LookUp.clear();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==checkout){
			ArrayList<Book> list = new ArrayList<>();
			double total = 0.0;
			for(int i = 0; i < checkbox.size(); i++){
				JCheckBox box = checkbox.get(i);
				if(box.isSelected()){
					list.add(ISBN_Location.get(i));
					total+=ISBN_Location.get(i).price;
				}
					
			}
			
			Checkout checkout_page = new Checkout(CID, total, list);
			this.dispose();
		}
		
	}
}
