package layout;

import agent.SellerAgent;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SellerGUI extends JFrame{
    private JButton jButton;
    private SellerAgent sellerAgent;
    private JLabel jLabel1,jLabel2;
    private JTextField nameOfProduct, priceOfProduct, numberOfProduct;
    private Product product;

    JFrame f = new JFrame();

    public SellerGUI(SellerAgent sellerAgent) {
        super(sellerAgent.getLocalName());

        jButton = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        nameOfProduct = new JTextField();
        priceOfProduct = new JTextField();
        numberOfProduct = new JTextField();

        this.sellerAgent = sellerAgent;
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 3));
        p.add(new JLabel("Product title:"));
        nameOfProduct = new JTextField(15);
        p.add(nameOfProduct);

        p.add(new JLabel("Product price:"));
        priceOfProduct = new JTextField(15);
        p.add(priceOfProduct);

        p.add(new JLabel("Product number:"));
        numberOfProduct = new JTextField(15);
        p.add(numberOfProduct);

        getContentPane().add(p, BorderLayout.CENTER);
        jButton.setText("Add");
        p = new JPanel();
        p.add(jButton);

        getContentPane().add(p, BorderLayout.SOUTH);

        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
            private void jButton2ActionPerformed(ActionEvent evt) {
                try {
                    String title = nameOfProduct.getText();
                    int price = Integer.parseInt(priceOfProduct.getText());
                    int n = Integer.parseInt(numberOfProduct.getText());
                    product = new Product(n, price, title);
                    sellerAgent.updateListOfProduct(product);
                    nameOfProduct.setText("");
                    priceOfProduct.setText("");
                    numberOfProduct.setText("");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(SellerGUI.this, "Invalid values. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                sellerAgent.doDelete();
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
