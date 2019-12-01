package mypackage;

import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Cart extends JFrame {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	JPanel mpp;
	
	
	public Cart(){
		sshConnection();
		
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Cart");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		this.add(mpp);
		this.setVisible(true);
		
		//executeCart();
		
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
	        	String sql = "";//"select * from Cart where CName = customer name";
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
}
