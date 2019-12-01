package mypackage;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame implements ActionListener{
	static int lport;
    static String rhost;
    static int rport;    
    JPanel mpp;
    JButton query1,query2,query3,query4,query5,query6;
    JButton login, create_account;
    
    public Main(){
    	
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Group 21 DBMS GUI");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
		
		login = new JButton("Login");
		addComp(mpp, login, 0,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		create_account = new JButton("Create Account");
		addComp(mpp, create_account, 6,2,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		query1 = new JButton("Query 1");
		
		login.addActionListener(this);
		create_account.addActionListener(this);
		
		this.add(mpp);
		this.setVisible(true);

    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==login){
			LoginPage test = new LoginPage();
		}
		if(e.getSource()==create_account){
			CreateAccountPage test = new CreateAccountPage();
		}
		dispose();
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
    
    public static void main(String[] args) {

    	Main test = new Main();

    }
}


