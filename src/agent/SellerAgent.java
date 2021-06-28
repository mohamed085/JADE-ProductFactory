package agent;

import jade.core.behaviours.Behaviour;
import layout.SellerGUI;
import model.Product;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SellerAgent extends Agent {
    public static List<Product> products;
    private SellerGUI sellerGUI;
    private int fullPrice;

    @Override
    protected void setup() {
        System.out.println("Seller agent: '"+getLocalName()+"' Created");
        sellerGUI = new SellerGUI(this);
        sellerGUI.showGui();
        products = new ArrayList<>();


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    String message = msg.getContent();
                    ACLMessage reply = msg.createReply();

                    String product = message.substring(message.indexOf("of") + 3, message.indexOf("q=") - 1);
                    int quantity = Integer.parseInt(message.substring(0, message.indexOf("of")-1));
                    int balance = Integer.parseInt(message.substring(message.indexOf("q=") + 3));

                    for (Product product1: products) {
                        if (product1.getName().equals(product) && product1.getNumber() >= quantity) {
                            if (quantity >= 10) {
                                JOptionPane.showMessageDialog(null, "Factory offer you a 10% discount for you", getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                                fullPrice = (int) (product1.getPrice() * quantity * .9);
                                JOptionPane.showMessageDialog(null, "Price before discount: " + product1.getPrice() * quantity + "\nPrice after discount: " + fullPrice, getLocalName(), JOptionPane.INFORMATION_MESSAGE);

                            } else {
                                fullPrice = product1.getPrice() * quantity;
                            }
                            if (fullPrice <= balance){
                                product1.setNumber(product1.getNumber() - quantity);
                                JOptionPane.showMessageDialog(null, "Current quantity in product list: "+product1.getNumber(), getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                reply.setContent(String.valueOf(fullPrice));
                                JOptionPane.showMessageDialog(null, "FullPrice: " + fullPrice, getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                reply.setPerformative(ACLMessage.PROPOSE);
                                if (quantity >= 2) {
                                    reply.setContent("Balance not enough. You can borrow maximum 2 product");
                                } else {
                                    reply.setContent("Balance not enough. You can borrow the product");
                                }
                            }
                        } else {
                            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                            reply.setContent("Product not found");
                        }
                    }
                    myAgent.send(reply);
                } else {
                    block();
                }
            }
        });


        addBehaviour(new Behaviour() {
            int step = 0;
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    String message = msg.getContent();

                    String product = message.substring(message.indexOf("of") + 3, message.indexOf("q=") - 1);
                    int quantity = Integer.parseInt(message.substring(0, message.indexOf("of")-1));
                    int balance = Integer.parseInt(message.substring(message.indexOf("q=") + 3));

                    int x = 0, y = 0, z = 0;

                    for (Product product1: products) {
                        if (product1.getName().equals(product) && product1.getNumber() >= quantity) {
                            x = balance / product1.getPrice();
                            y = x + 2;
                            z = x * product1.getPrice();
                            product1.setNumber(product1.getNumber() - y);
                            JOptionPane.showMessageDialog(null, "Current quantity in product list: "+product1.getNumber(), getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null,y + " of " + product + " successfully purchased from agent: '" + getLocalName() + "' to agent: '" + msg.getSender().getName() + "'","From: " + getLocalName() +" to: " + msg.getSender().getName(), JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null, "New balance: " + (balance - z), msg.getSender().getLocalName(), JOptionPane.INFORMATION_MESSAGE);

                            step = 1;
                        }
                    }
                } else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return (step == 1);
            }
        });


        /**
         * Change price of products by add 2
         */
        addBehaviour(new TickerBehaviour(this, 90000) {
             @Override
             protected void onTick() {
                 for (Product product: products) {
                     product.setPrice(product.getPrice()+2);
                     System.out.println("New change");
                 }
                 for (int i = 0; i <= CustomerAgent.customerAgents.size(); i++) {
                     ACLMessage acl = new ACLMessage(ACLMessage.CFP);
                     acl.addReceiver(new AID("c"+i, AID.ISLOCALNAME));
                     acl.setContent("New increase in product for: Agent c"+i);
                     send(acl);
                 }
             }
         });

        /**
         * Discount price of products by add 2
         */
        addBehaviour(new TickerBehaviour(this, 100000) {
            @Override
            protected void onTick() {
                for (Product product: products) {
                    product.setPrice(product.getPrice()-2);
                    System.out.println("New discount");
                }
                for (int i = 0; i <= CustomerAgent.customerAgents.size(); i++) {
                    ACLMessage acl = new ACLMessage(ACLMessage.CFP);
                    acl.addReceiver(new AID("c"+i, AID.ISLOCALNAME));
                    acl.setContent("New discount is available for: Agent c"+i);
                    send(acl);
                }
            }
        });

    }

    @Override
    protected void takeDown() {
        sellerGUI.dispose();
        System.out.println("Seller agent: '"+getAID().getName()+"' closed.");
    }

    public void updateListOfProduct(Product product) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                products.add(product);
                System.out.println("Product : '"+product.toString()+"' is add.");
            }
        });
    }

}

