package net.islbd.kothabondhu;

import java.util.List;

import net.islbd.kothabondhu.model.pojo.Agent;
import net.islbd.kothabondhu.model.pojo.Communication;

public class MockData {
    private Agent agent;
    private List<Agent> agentList;
    private String[] packageArray = {"package1", "package2", "package3"};
    private Communication communication;

    public List<Agent> generateMockList(List<Agent> agentList) {
        /*for (int i = 100; i < 105; i++) {
            Agent agent = new Agent();
            agent.setId(String.valueOf(i));
            agent.setName("Dummy dummy");
            agent.setFullcontentUrl("https://i.ytimg.com/vi/TZiQK81Rjfw/maxresdefault.jpg");
            agentList.add(agent);
        }*/
        Agent agent = new Agent();
        agent.setId(String.valueOf(50));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.ytimg.com/vi/TZiQK81Rjfw/maxresdefault.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(51));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.pinimg.com/736x/1b/3c/7e/1b3c7e0a90b03101e175cf14598c41e4--abstract-portrait-portrait-paintings.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(52));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://npg.si.edu/exhibit/feature/images/schoeller_full.jpg");
        agentList.add(agent);

        agent = new Agent();
        agent.setId(String.valueOf(53));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.ytimg.com/vi/TZiQK81Rjfw/maxresdefault.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(54));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.pinimg.com/736x/1b/3c/7e/1b3c7e0a90b03101e175cf14598c41e4--abstract-portrait-portrait-paintings.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(55));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://npg.si.edu/exhibit/feature/images/schoeller_full.jpg");
        agentList.add(agent);

        agent = new Agent();
        agent.setId(String.valueOf(56));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.ytimg.com/vi/TZiQK81Rjfw/maxresdefault.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(57));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://i.pinimg.com/736x/1b/3c/7e/1b3c7e0a90b03101e175cf14598c41e4--abstract-portrait-portrait-paintings.jpg");
        agentList.add(agent);
        agent = new Agent();
        agent.setId(String.valueOf(58));
        agent.setName("Dummy dummy");
        agent.setFullcontentUrl("https://npg.si.edu/exhibit/feature/images/schoeller_full.jpg");
        agentList.add(agent);


        return agentList;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<Agent> agentList) {
        this.agentList = agentList;
    }

    public String[] getPackageArray() {
        return packageArray;
    }

    public void setPackageArray(String[] packageArray) {
        this.packageArray = packageArray;
    }

    public Communication getCommunication() {
        Communication communication = new Communication();
        communication.setConversationRoom("blabla");
        communication.setDuration(10);
        communication.setFromUserId(1);
        communication.setToUserId(2);
        communication.setStatus(1);
        communication.setTime("2000");
        communication.setType(100);
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }
}
