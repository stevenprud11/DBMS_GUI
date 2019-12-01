package mypackage;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomerPage extends JFrame implements ActionListener{
	JPanel mpp;
	JButton logout;
	JButton book_search, account_info, book_list, cart;
	
	
	public CustomerPage(){
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		book_search = new JButton("Book Search");
		addComp(mpp, book_search, 0, 2, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		account_info = new JButton("Account Info");
		addComp(mpp, account_info, 6, 2, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		book_list = new JButton("Book List");
		addComp(mpp, book_list, 0, 6, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		cart = new JButton("Cart");
		addComp(mpp, cart, 6, 6, 2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
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


