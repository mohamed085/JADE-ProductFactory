package agent;

import layout.CustomerGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerAgent extends Agent {
    public static List<AID> customerAgents = new ArrayList<>();
    public CustomerGUI customerGUI;
    private String product;
    private int balance;
    private int number;

    @Override
    protected void setup() {
        System.out.println("Customer agent: '"+getLocalName()+"' created");
        customerGUI = new CustomerGUI(this);
        customerGUI.showGui();
        customerAgents.add(getAID());


        /**
         * Get Message of change product price
         */
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage acl = receive(mt);
                if (acl != null){
                    String message = acl.getContent();
                    JOptionPane.showMessageDialog(null, message, "From: "+ acl.getSender().getName() +" to: "+getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Customer agent: '" + getAID().getName() + "' closed.");
    }


    public void updateName(final String p, int b, int n) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("Customer: '"+ getLocalName() + "' {balance: " + b + ", target product: " + p + ", number of product required: " + n + "}");
            }
        });
        product = p;
        balance = b;
        number = n;

        addBehaviour(new WakerBehaviour(this, 3000) {
            @Override
            protected void handleElapsedTimeout() {
                myAgent.addBehaviour(new RequestPerformer());
            }
        });

    }

    private class RequestPerformer extends Behaviour {
        int step = 0;
        MessageTemplate messageTemplate;
        ACLMessage acl;
        ACLMessage reply;

        @Override
        public void action() {
            switch (step) {
                case 0 -> {
                    System.out.println("Trying to buy " + number + " of " + product);
                    acl = new ACLMessage(ACLMessage.CFP);
                    acl.addReceiver(new AID("SellerAgent", AID.ISLOCALNAME));
                    acl.setContent(number + " of " + product + " q= " + balance);
                    send(acl);
                    step = 1;
                }
                case 1 -> {
                    reply = myAgent.receive(messageTemplate);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                            int x = Integer.parseInt(reply.getContent());
                            balance -= x;
                            JOptionPane.showMessageDialog(null,product + " successfully purchased from agent: '" + reply.getSender().getName() + "' to agent: '" + getLocalName() + "'","From: "+ reply.getSender().getName() +" to: "+getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                            System.out.println();
                            JOptionPane.showMessageDialog(null, "New balance: " + balance, getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                            step = 3;
                        } else if (reply.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                            JOptionPane.showMessageDialog(null, reply.getContent(), getLocalName(), JOptionPane.INFORMATION_MESSAGE);
                            step = 3;
                        } else if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            int n = JOptionPane.showConfirmDialog(
                                    null,
                                    reply.getContent()+"\nIf you want the product press yes",
                                    getLocalName(),
                                    JOptionPane.YES_NO_OPTION);
                            if(true){
                                step = 2;
                            }
                            else {
                                step = 3;
                            }

                            step = 2;
                        }
                    } else {
                        block();
                        step = 1;
                    }
                }
                case 2 -> {
                    acl = new ACLMessage(ACLMessage.INFORM);
                    acl.addReceiver(new AID("SellerAgent", AID.ISLOCALNAME));
                    acl.setContent(number + " of " + product + " q= " + balance);
                    send(acl);
                }
            }
        }

        @Override
        public boolean done() {
            return (step == 3);
        }
    }
}
