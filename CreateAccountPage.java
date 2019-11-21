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

class CreateAccountPage extends JFrame implements ActionListener{
	JPanel mpp;
	JTextField username, password, phone, email, address;
	JButton submit;
	
	
	public CreateAccountPage(){
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Create Account");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		//add username label
		JLabel username_label = new JLabel("Username");
		addComp(mpp, username_label, 0,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add username textbox
		username = new JTextField();
		username.setPreferredSize( new Dimension( 100, 24 ) );
		addComp(mpp, username, 0,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add password label
		JLabel password_label = new JLabel("Password");
		addComp(mpp, password_label, 6,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
				
		//add password textbox
		password = new JTextField();
		password.setPreferredSize(new Dimension(100,24));
		addComp(mpp, password, 6,6,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone label
		JLabel phone_label = new JLabel("Phone Number");
		addComp(mpp, phone_label, 0,10,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone textbox
		phone = new JTextField();
		phone.setPreferredSize(new Dimension(100,24));
		addComp(mpp, phone, 0,14,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add email label
		JLabel email_label = new JLabel("Email Address");
		addComp(mpp, email_label, 6,10,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add email textbox
		email = new JTextField();
		email.setPreferredSize(new Dimension(100,24));
		addComp(mpp, email, 6,14,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone label
		JLabel address_label = new JLabel("Home Address");
		addComp(mpp, address_label, 3,18,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add phone textbox
		address = new JTextField();
		address.setPreferredSize(new Dimension(200,24));
		addComp(mpp, address, 3,22,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//add submit button
		submit = new JButton("Submit");
		submit.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, submit, 3,40,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		submit.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==submit){ //on submit try to load customer page
			//create account ID
			//push data to database
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