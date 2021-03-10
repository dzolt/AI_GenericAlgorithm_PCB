package pl.damian.zoltowski;

import pl.damian.zoltowski.utils.Constants;


public class GenericAlgorithmPCBApplication {

    public static void main(String[] args) {
        Config config = new Config().readConfigFromFile("zad0.txt");
        PCB pcb = new PCB(config);
        pcb.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);

        System.out.println(pcb.getPopulation());

    }
}
