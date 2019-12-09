package mypackage;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
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
import javax.swing.JTextField;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class AccountInfo extends JFrame implements ActionListener {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static int CID;
	static JButton submit;
	static JTextField CName_Label;
	static JTextField Email_Label;
	static JTextField Password_Label;
	static JTextField Phone_Label;
	static JTextField Address_Label;
	static JTextField City_Label;
	static JTextField State_Label;
	static JTextField ZipCode_Label;
	
	
	public AccountInfo(int CID) {
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
		
		submit = new JButton("Submit");
		addComp(mpp, submit, 12,12,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		submit.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
		
	}
	
	
	public static void failedAction(String fail) {
		JPanel panel = new JPanel();
		JLabel label;
		panel.setMinimumSize(new Dimension(200, 200));
		JFrame frame = new JFrame();
		frame.setTitle("Error");
		if (fail == "information") {
			label = new JLabel("Information is missing.");  
		}
		else {
			label = new JLabel("An error occured.");  
		}
		JOptionPane.showMessageDialog(frame, label);
	}

	public static void executeAccountInfo(){
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
	        		
	        		CName_Label = new JTextField(CName);
	        		Email_Label = new JTextField(Email);
	        		Password_Label = new JTextField(userPassword);
	        		Phone_Label = new JTextField(Phone);
	        		Address_Label = new JTextField(Address);
	        		City_Label = new JTextField(City);
	        		State_Label = new JTextField(State);
	        		ZipCode_Label = new JTextField(ZipCode);
	        		
	        		CName_Label.setPreferredSize( new Dimension( 100, 24 ) );
	        		addComp(mpp, CName_Label, 0,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Email_Label, 4,6, 2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Password_Label, 8,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Phone_Label, 12,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, Address_Label, 16,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, City_Label, 20,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, State_Label, 24,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	        		addComp(mpp, ZipCode_Label, 28,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
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
		// TODO Auto-generated method stub
		if(e.getSource() == submit){
			if (CName_Label.getText().contentEquals("") || Email_Label.getText().contentEquals("") || Password_Label.getText().contentEquals("")
					|| Phone_Label.getText().contentEquals("") || Address_Label.getText().contentEquals("") || City_Label.getText().contentEquals("")
					|| State_Label.getText().contentEquals("") || ZipCode_Label.getText().contentEquals("")) {
				failedAction("information");
			}	
			else {
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
			        	Statement st = con.createStatement();
			        	String sql = "UPDATE Customer SET CName='"+CName_Label.getText()+"', Email='"+Email_Label.getText()+"', userPassword='"+Password_Label.getText()
			        			+"', Phone='"+Phone_Label.getText()+"', Address='"+Address_Label.getText()+"', City='"+City_Label.getText()
			        			+"', State='"+State_Label.getText()+"', ZipCode='"+ZipCode_Label.getText()+"' WHERE CID='"+CID+"';";
			        	st.executeUpdate(sql);
	
			        }
			        	catch(Exception er){
			        		er.printStackTrace();
			        	}
			    }
			    catch (Exception err){
			    	err.printStackTrace();
			    }
		    	session.disconnect();
		    	this.dispose();
			}
			
		}
		
	}
}
