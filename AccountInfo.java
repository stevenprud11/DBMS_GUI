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

public class AccountInfo extends JFrame {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static int CID;
	
	public AccountInfo(int CID){
		this.CID = CID;
		sshConnection();
		
		//set size of obj
		this.setSize(800,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Account Info");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		//Add labels to JPanel that the query response will go under
		JLabel CName_Label = new JLabel("Name");
		JLabel Email_Label = new JLabel("Email");
		JLabel Password_Label = new JLabel("Password");
		JLabel Phone_Label = new JLabel("Phone");
		JLabel Address_Label = new JLabel("Address");
		JLabel City_Label = new JLabel("City");
		JLabel State_Label = new JLabel("State");
		JLabel ZipCode_Label = new JLabel("ZipCode");
		addComp(mpp, CName_Label, 0,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, Email_Label, 4,0, 2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, Password_Label, 8,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, Phone_Label, 12,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, Address_Label, 16,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, City_Label, 20,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, State_Label, 24,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, ZipCode_Label, 28,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);	
		
		executeAccountInfo();
		
		this.add(mpp);
		this.setVisible(true);
		
		
		
	}
	/*
	 * This function executes the account info query
	 * it will call the query, get the response back, orgainze it into variables
	 * then it creates labels with the values back
	 * and puts them on the JPanel
	 */
	public static void executeAccountInfo(){
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
	        	String sql = "select * from Customer where CID = '" + CID + "';";//"select * from Customer where CName = user name";
	        	ResultSet response = st.executeQuery(sql);
	        	while(response.next()){
	        		int CID = response.getInt("CID");
	        		String CName = response.getString("CName");
	        		String Email = response.getString("Email");
	        		String userPassword = response.getString("userPassword");
	        		String Phone = response.getString("Phone");
	        		String Address = response.getString("Address");
	        		String City = response.getString("City");
	        		String State = response.getString("State");
	        		String ZipCode = response.getString("ZipCode");
	        		
	        		JLabel CName_Label = new JLabel(CName);
	        		JLabel Email_Label = new JLabel(Email);
	        		JLabel Password_Label = new JLabel(userPassword);
	        		JLabel Phone_Label = new JLabel(Phone);
	        		JLabel Address_Label = new JLabel(Address);
	        		JLabel City_Label = new JLabel(City);
	        		JLabel State_Label = new JLabel(State);
	        		JLabel ZipCode_Label = new JLabel(ZipCode);
	        		
	        		addComp(mpp, CName_Label, 0,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Email_Label, 4,6, 2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Password_Label, 8,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Phone_Label, 12,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Address_Label, 16,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, City_Label, 20,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, State_Label, 24,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, ZipCode_Label, 28,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		
	        		System.out.println(CID + " " + CName);
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
	 * creates ssh connection to turing database
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
	 * add components to JPanel
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
}
