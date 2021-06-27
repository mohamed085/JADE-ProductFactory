package layout;

import agent.CustomerAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomerGUI extends JFrame {
    CustomerAgent customerAgent;
    public String product = "";
    public int balance = 0;
    public int num = 0;
    private JTextField productTitle;
    private JTextField balanceOfCustomer;
    private JTextField numberOfProduct;

    public CustomerGUI(CustomerAgent customerAgent) {
        super(customerAgent.getLocalName());
        this.customerAgent = customerAgent;
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 3));

        p.add(new JLabel("Balance:"));
        balanceOfCustomer = new JTextField(15);
        p.add(balanceOfCustomer);

        p.add(new JLabel("Product:"));
        productTitle = new JTextField(15);
        p.add(productTitle);

        p.add(new JLabel("Number of products:"));
        numberOfProduct = new JTextField(15);
        p.add(numberOfProduct);


        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("done");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    product = productTitle.getText();
                    balance = Integer.parseInt(balanceOfCustomer.getText());
                    num = Integer.parseInt(numberOfProduct.getText());
                    customerAgent.updateName(product, balance, num);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CustomerGUI.this, "Invalid values. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                customerAgent.doDelete();
            }
        });

        setResizable(false);
    }

    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
