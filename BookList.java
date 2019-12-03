package mypackage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class BookList extends JFrame {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static String title;
	static int ISBN, quantity;
	static double price;
	
	public BookList(){
		sshConnection();
		
		
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Book List");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		executeBookList();
		
		this.add(mpp);
		this.setVisible(true);
		
		
		
	}
	
	public static void executeBookList(){
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
	        	int y = 0;
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
	        		System.out.println(title + " " + ISBN + " " + price + " " + quantity);
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
}
