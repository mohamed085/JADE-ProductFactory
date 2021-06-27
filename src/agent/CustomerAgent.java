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
                    System.out.println(message+" from: "+acl.getSender());
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

        addBehaviour(new WakerBehaviour(this, 5000) {
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
                            System.out.println(product + " successfully purchased from agent: '" + reply.getSender().getName() + "' to agent: '" + getLocalName() + "'");
                            System.out.println("New balance: " + balance);
                        } else if (reply.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                            System.out.println(reply.getContent());
                        }
                        step = 2;
                    } else {
                        block();
                        step = 1;
                    }
                }
            }
        }

        @Override
        public boolean done() {
            return (step == 2);
        }
    }
}
