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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class LoginPage extends JFrame implements ActionListener{ //login page
	JPanel mpp;
	JTextField accountID, password;
	JButton submit;
	
	
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
		// TODO Auto-generated method stub
		if(e.getSource()==submit){ //on submit try to load customer page
			//check data against database
			//if correct 
			CustomerPage test = new CustomerPage();
		}
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
