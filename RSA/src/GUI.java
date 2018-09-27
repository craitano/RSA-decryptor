import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.TRAILING;


public class GUI extends JFrame {
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);
	}
	
	private GUI() {
		// Set JFrame properties
		setTitle("RSA decryptor");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Setup the layout
        Container window = getContentPane();
        GroupLayout layout = new GroupLayout(window);
        window.setLayout(layout);
        
        // add inputs
        addComponents(layout);

        pack();
	}
	
	private void addComponents(GroupLayout layout) {

        JLabel cLabel = new JLabel("ciphertext (c): ");
		JLabel nLabel = new JLabel("modulus (n): ");
        JLabel eLabel = new JLabel("public key exponent (e): ");
        JLabel dLabel = new JLabel("private key exponent (d): ");
        JLabel pLabel = new JLabel("prime 1 (p): ");
        JLabel qLabel = new JLabel("prime 2 (q): ");
        JLabel dpLabel = new JLabel("dp: ");
        JLabel dqLabel = new JLabel("dq: ");
        JLabel whichKey = new JLabel("key used for encryption: ");
        JLabel mInt = new JLabel("Message (Integer): ");
        JLabel mASCII = new JLabel("Message (ASCII): ");

        JTextField cInput = new JTextField(25);
        JTextField nInput = new JTextField(25);
        JTextField eInput = new JTextField(25);
        JTextField dInput = new JTextField(25);
        JTextField pInput = new JTextField(25);
        JTextField qInput = new JTextField(25);
        JTextField dpInput = new JTextField(25);
        JTextField dqInput = new JTextField(25);
        JTextField intOut = new JTextField(25) {@Override public void setBorder(Border border) {}};
        JTextField asciiOut = new JTextField(25) {@Override public void setBorder(Border border) {}};
        
        JButton decryptButton = new JButton("Decrypt");
        
        JRadioButton encryptedWithPublic = new JRadioButton("Public");
        JRadioButton encryptedWithPrivate = new JRadioButton("Private");
        
        encryptedWithPublic.setSelected(true);
        encryptedWithPrivate.setSelected(false);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(encryptedWithPublic);
        buttonGroup.add(encryptedWithPrivate);
        
        intOut.setEditable(false);
        asciiOut.setEditable(false);
        
        decryptButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent ae) {
        		BigInteger n = null, e = null, c = null, d = null, p = null, q = null, dp = null, dq = null;
        		Map<String, BigInteger> args = new HashMap<>();
        		// Parse inputs and add non-empty inputs to args
        		try {
        			c = new BigInteger(cInput.getText());
        			args.put("c", c);
        		} catch(NumberFormatException nfe) {}
        		try {
        			n = new BigInteger(nInput.getText());
        			args.put("n", n);
        		} catch(NumberFormatException nfe) {}
        		try {
        			e = new BigInteger(eInput.getText());
        			args.put("e", e);
        		} catch(NumberFormatException nfe) {}
        		try {
        			d = new BigInteger(dInput.getText());
        			args.put("d", d);
        		} catch(NumberFormatException nfe) {}
        		try {
        			p = new BigInteger(pInput.getText());
        			args.put("p", p);
        		} catch(NumberFormatException nfe) {}
        		try {
        			q = new BigInteger(qInput.getText());
        			args.put("q", q);
        		} catch(NumberFormatException nfe) {}
        		try {
        			dp = new BigInteger(dpInput.getText());
        			args.put("dp", dp);
        		} catch(NumberFormatException nfe) {}
        		try {
        			dq = new BigInteger(dqInput.getText());
        			args.put("dq", dq);
        		} catch(NumberFormatException nfe) {}
        		
        		BigInteger m = Decryption.decrypt(encryptedWithPublic.isSelected(), args);
        		if(m != null) {
        			intOut.setText(m.toString());
        			asciiOut.setText(ASCIIConverter.toASCII(m));
        		}else {
        			intOut.setText("Cannot decrypt using the given parameters");
        			asciiOut.setText("");
        		}
        		
            }
        });

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Define horizontal layout of components
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(TRAILING)
                        .addComponent(cLabel)
                        .addComponent(nLabel)
                        .addComponent(eLabel)
                        .addComponent(dLabel)
                        .addComponent(pLabel)
                        .addComponent(qLabel)
                        .addComponent(dpLabel)
                        .addComponent(dqLabel)
                        .addComponent(whichKey)
                        .addComponent(decryptButton)
                        .addComponent(mInt)
                        .addComponent(mASCII))
                .addGroup(layout.createParallelGroup()
                        .addComponent(cInput)
                        .addComponent(nInput)
                        .addComponent(eInput)
                        .addComponent(dInput)
                        .addComponent(pInput)
                        .addComponent(qInput)
                        .addComponent(dpInput)
                        .addComponent(dqInput)
                        .addGroup(layout.createSequentialGroup()
                    			.addComponent(encryptedWithPublic)
                    			.addComponent(encryptedWithPrivate))
                        .addComponent(intOut)
                        .addComponent(asciiOut))
        );

        // Define vertical layout of components
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(cLabel)
                        .addComponent(cInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(nLabel)
                        .addComponent(nInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(eLabel)
                        .addComponent(eInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(dLabel)
                        .addComponent(dInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(pLabel)
                        .addComponent(pInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(qLabel)
                        .addComponent(qInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(dpLabel)
                        .addComponent(dpInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(dqLabel)
                        .addComponent(dqInput))
                .addGroup(layout.createParallelGroup(BASELINE)
                		.addComponent(whichKey)
                		.addComponent(encryptedWithPublic)
            			.addComponent(encryptedWithPrivate))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(decryptButton))
                .addGroup(layout.createParallelGroup(BASELINE)
                		.addComponent(mInt)
                        .addComponent(intOut))
                .addGroup(layout.createParallelGroup(BASELINE)
                		.addComponent(mASCII)
                		.addComponent(asciiOut))
        );

	}
}
