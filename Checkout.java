package mypackage;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jcraft.jsch.Session;

public class Checkout extends JFrame {
	static int lport;
    static String rhost;
    static int rport; 
	static Session session;
	static JPanel mpp;
	static JButton checkout;
	static int CID;
	
	
	public Checkout(int CID, double price, ArrayList<Book> list){
		this.CID = CID;
		//set size of obj
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setTitle("Checkout");
		
		//set JPanel
		mpp = new JPanel();
		mpp.setLayout(new GridBagLayout());
	
		JLabel total = new JLabel("Price $" + Double.toString(price));
		addComp(mpp, total, 0,0,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		JLabel books = new JLabel("Book List");
		addComp(mpp, books, 0,4,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JLabel line = new JLabel("-------------------------------");
		JLabel line2 = new JLabel("-------------------------------");
		addComp(mpp, line, 0,8,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		int y = 12;
		for(int i = 0; i < list.size(); i++){
			addComp(mpp, new JLabel(Integer.toString(list.get(i).ISBN) + "   " + list.get(i).title), 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			y+=4;
		}
		
		addComp(mpp, line2, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel name = new JLabel("Name");
		JTextField name_t = new JTextField();
		addComp(mpp, name, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		name_t.setPreferredSize( new Dimension( 200, 24 ) );
		addComp(mpp, name_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		JLabel shipping = new JLabel("Shipping Address");
		JTextField shipping_t = new JTextField();
		shipping_t.setPreferredSize( new Dimension( 400, 24 ) );
		addComp(mpp, shipping, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		addComp(mpp, shipping_t, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		y+=4;
		//should print final price on this page
		checkout = new JButton("Checkout");
		addComp(mpp, checkout, 0,y,2,3, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		
		this.add(mpp);
		this.setVisible(true);
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
	
}
