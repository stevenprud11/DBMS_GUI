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


class CreateAccountPage extends JFrame implements ActionListener{
	JPanel mpp;
	static JTextField name, password, phone, email, address, city, state, zip;
	JButton submit;
	static boolean foundRecord = false;
	static int lport;
    static String rhost;
    static int rport; 
	static String enteredID;
	static boolean found = false;
	static Session session;
	static int largestID = 0;
	static int newID = 1;
	
	
	public CreateAccountPage(){
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Create Account");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		//add name label
		JLabel name_label = new JLabel("Full Name");
		addComp(mpp, name_label, 0,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add username textbox
		name = new JTextField();
		name.setPreferredSize( new Dimension( 100, 24 ) );
		addComp(mpp, name, 0,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add password label
		JLabel password_label = new JLabel("Password");
		addComp(mpp, password_label, 3,4,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
				
		//add password textbox
		password = new JTextField();
		password.setPreferredSize(new Dimension(100,24));
		addComp(mpp, password, 3,8,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add email label
		JLabel email_label = new JLabel("Email Address");
		addComp(mpp, email_label, 6,2,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add email textbox
		email = new JTextField();
		email.setPreferredSize(new Dimension(100,24));
		addComp(mpp, email, 6,6,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone label
		JLabel phone_label = new JLabel("Phone Number");
		addComp(mpp, phone_label, 0,10,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone textbox
		phone = new JTextField();
		phone.setPreferredSize(new Dimension(100,24));
		addComp(mpp, phone, 0,14,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address label
		JLabel address_label = new JLabel("Address");
		addComp(mpp, address_label, 3,10,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address textbox
		address = new JTextField();
		address.setPreferredSize(new Dimension(100,24));
		addComp(mpp, address, 3,14,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address label
		JLabel city_label = new JLabel("City");
		addComp(mpp, city_label, 6,10,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address textbox
		city = new JTextField();
		city.setPreferredSize(new Dimension(100,24));
		addComp(mpp, city, 6,14,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address label
		JLabel state_label = new JLabel("State");
		addComp(mpp, state_label, 2,18,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address textbox
		state = new JTextField();
		state.setPreferredSize(new Dimension(100,24));
		addComp(mpp, state, 2,22,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address label
		JLabel zipcode_label = new JLabel("Zipcode");
		addComp(mpp, zipcode_label, 4,18,2,3,  GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add address textbox
		zip = new JTextField();
		zip.setPreferredSize(new Dimension(100,24));
		addComp(mpp, zip, 4,22,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		//add submit button
		submit = new JButton("Submit");
		submit.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, submit, 3,40,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		submit.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==submit){
			attemptAccountCreation();	
			this.setVisible(false);
		}
	}

	public static void attemptAccountCreation() {

		try {
			sshConnection();
			databaseLogin();
			if (foundRecord == false) {
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
		if (fail == "information") {
			label = new JLabel("All fields must be filled out.");  
		}
		else if (fail == "user") {
			label = new JLabel("User already exists.");  
			foundRecord = true;
		}
		else if (fail == "password") {
			label = new JLabel("The password was incorrect, please enter a correct password.");
		}
		else if (fail == "worked") {
			frame.setTitle("Success");
			label = new JLabel("Account has been created.");
		}
		else if (fail == "failedCreation") {
			label = new JLabel("There was an error in creating the user.");
			foundRecord = true;
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
	        		if (CID > largestID) {
	        			largestID = CID;
	        		}
	        		if(email.getText().contentEquals(Email)){
        	        	foundRecord = true;
        	        	System.out.println("Found a user");
        	        	reasonForFail = "user";
        	        	break;
    	        	}
    	        	if(name.getText().contentEquals("") || email.getText().contentEquals("") || password.getText().contentEquals("") 
    	        		|| phone.getText().contentEquals("") || address.getText().contentEquals("") || city.getText().contentEquals("")
    	        		|| state.getText().contentEquals("") || Integer.parseInt(zip.getText()) < 0){
    	        		
    	        		System.out.println("No information");
    	        		reasonForFail = "information";
    	        		foundRecord = true;
    	        	}
    	        	reasonForFail = "information";
	        	}
	        	if (foundRecord == true) {
	        		session.disconnect();
	        		failedAction(reasonForFail);
	        	}
	        	if (foundRecord == false) {
	        		session.disconnect();
	        		if (createAccount()) {
	        			failedAction("worked");
	        			foundRecord = false;
	        		}
	        		else {
	        			failedAction("failedCreation");
	        		}
	        	}
	        }
	        catch (NumberFormatException x) {
	        	System.out.println(x);
	        	session.disconnect();
        		failedAction(reasonForFail);
        		foundRecord = true;
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

	private static boolean createAccount() {
		// TODO Action to add the user to the database
		Connection con = null;
		sshConnection();
		if(name.getText().contentEquals("") || email.getText().contentEquals("") || password.getText().contentEquals("") 
        		|| phone.getText().contentEquals("") || address.getText().contentEquals("") || city.getText().contentEquals("")
        		|| state.getText().contentEquals("") || Integer.parseInt(zip.getText()) < 0){
        		
        		System.out.println("No information");
        		failedAction("informaiton");
        		return false;
        }
		newID = largestID + 1;
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
    	        	String sql = "INSERT INTO Customer (CID, Cname, Email, userPassword, Phone, Address, City, State, ZipCode) "
    	        			+ "VALUES ('"+newID+"', '"+name.getText()+"', '"+email.getText()+"', '"+password.getText()+"', '"+phone.getText()
    	        			+"', '"+address.getText()+"', '"+city.getText()+"', '"+state.getText()+"', '"+zip.getText()+"')";
    	        	int response = st.executeUpdate(sql);
    	        	if (response == 1) {
    	        		session.disconnect();
    	        		return true;
    	        	}
    	        	else {
    	        		session.disconnect();
    	        		return false;
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
		
		return false;
	}

}