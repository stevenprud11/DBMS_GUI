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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

class LoginPage extends JFrame implements ActionListener{ //login page
	static int lport;
    static String rhost;
    static int rport; 
	JPanel mpp;
	static JTextField accountID;
	static JTextField password;
	JButton submit;
	static boolean found = false;
	static Session session;
	
	public LoginPage(){
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		//add username label
		JLabel username_label = new JLabel("Account ID");
		addComp(mpp, username_label, 0,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add password label
		JLabel password_label = new JLabel("Password");
		addComp(mpp, password_label, 6,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add username textbox
		accountID = new JTextField();
		accountID.setPreferredSize( new Dimension( 100, 24 ) );
		addComp(mpp, accountID, 0,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add password textbox
		password = new JTextField();
		password.setPreferredSize(new Dimension(100,24));
		addComp(mpp, password, 6,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add submit button
		submit = new JButton("Submit");
		submit.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, submit, 3,10,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		submit.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==submit){
			attemptLogin();	
		}
	}

	public static void attemptLogin() {

		try {
			sshConnection();
			databaseLogin();
			if (found == true) {
				CustomerPage test = new CustomerPage();
			}
		} catch (Exception e) {
			failedAction("login");
		}
	}
	
	public static void failedAction(String fail) {
		JPanel panel = new JPanel();
		JLabel label;
		panel.setMinimumSize(new Dimension(200, 200));
		JFrame frame = new JFrame();
		frame.setTitle("Error");
		if (fail == "login") {
			label = new JLabel("Please enter a valid ID number.");  
		}
		else if (fail == "user") {
			label = new JLabel("User was not found, please enter correct ID.");  
		}
		else if (fail == "password") {
			label = new JLabel("The password was incorrect, please enter a correct password.");  
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
	
	public static void databaseLogin(){
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
	        	String sql = "select * from Customer";
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
    	        	if (accountID.getText().contentEquals(Integer.toString(CID))) {
    	        		if (password.getText().contentEquals(userPassword)) {
        	        		found = true;
        	        		System.out.println(CID+" "+Email+" "+userPassword+" "+Phone+" "+Address+" "+City+" "+State+" "+ZipCode);
        	        		break;
    	        		}
    	        		reasonForFail = "password";
    	        		break;
    	        	}
    	        	reasonForFail = "user";
	        	}
	        	if (found == false) {
	        		failedAction(reasonForFail);
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
	
	private void addComp(JPanel thePanel, JComponent comp, int xP, int yP, int w, int h, int place, int stretch)
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
