import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class RunApp {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl("localhost", 1000, "ProjectPlatform");
        ContainerController containerController = runtime.createMainContainer(profile);

        AgentController agentController = null;
        AgentController agentController1 = null;
        AgentController agentController2 = null;
        AgentController agentController3 = null;
        AgentController agentController4 = null;

        try {
            agentController = containerController.createNewAgent("jade", "jade.tools.rma.rma", null);
            agentController.start();

            agentController1 = containerController.createNewAgent("SellerAgent", "agent.SellerAgent", null);
            agentController1.start();

            agentController2 = containerController.createNewAgent("c1", "agent.CustomerAgent", null);
            agentController2.start();


            agentController3 = containerController.createNewAgent("c2", "agent.CustomerAgent", null);
            agentController3.start();


            agentController4 = containerController.createNewAgent("c3", "agent.CustomerAgent", null);
            agentController4.start();

        } catch (StaleProxyException e) {
            System.out.println(e);
        }
    }

//    public static void main(String[] args) {
//        String message = "15 of car q= 120";
//        int productNum = Integer.parseInt(message.substring(0, message.indexOf("of")-1));
//        String productName = message.substring(message.indexOf("of") + 3, message.indexOf("q=") - 1);
//        int q = Integer.parseInt(message.substring(message.indexOf("q=") + 3));
//
//        System.out.println(productNum);
//        System.out.println(productName);
//        System.out.println(q);
//
//    }

}
