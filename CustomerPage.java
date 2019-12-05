package mypackage;

import java.awt.Dimension;
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class CustomerPage extends JFrame implements ActionListener{
	JPanel mpp;
	JButton logout;
	JButton book_search, account_info, book_list, cart;
	JTextField book_title;
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	int CID;
	
	public CustomerPage(int CID){
		this.CID = CID;
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		book_search = new JButton("Book Search");
		book_title = new JTextField("");
		book_title.setPreferredSize(new Dimension(100,24));
		addComp(mpp, book_search, 0, 3, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addComp(mpp, book_title, 0, 0, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		book_search.addActionListener(this);
		
		account_info = new JButton("Account Info");
		addComp(mpp, account_info, 6, 3, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		account_info.addActionListener(this);
		
		book_list = new JButton("Book List");
		addComp(mpp, book_list, 0, 7, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		book_list.addActionListener(this);
		
		
		cart = new JButton("Cart");
		addComp(mpp, cart, 6, 7, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		cart.addActionListener(this);
		
		//add submit button
		logout = new JButton("Logoff");
		logout.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, logout, 3,40,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		logout.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==logout){
			logoff();
			this.setVisible(false);
		}
		else {
			if(e.getSource()==book_list){
				System.out.println("Executing book list");
				BookList booklist = new BookList(CID);
			}
			else if(e.getSource()==book_search){
				System.out.println("Executing book search");
				if(book_title.getText().compareTo("")==0)
					book_title.setText("*");
				BookSearch booksearch = new BookSearch(book_title.getText(), CID);
			}
			else if(e.getSource()==account_info){
				System.out.println("Executing account info");
				AccountInfo accountinfo = new AccountInfo(CID);
			}
			else if(e.getSource()==cart){
				System.out.println("Executing cart");
				Cart cart = new Cart(CID);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	private void logoff() {
		System.out.println("Made it here");
		LoginPage login = new LoginPage();
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


