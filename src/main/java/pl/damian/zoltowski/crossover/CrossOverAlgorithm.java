package pl.damian.zoltowski.crossover;

import pl.damian.zoltowski.pcb.PCBIndividual;

public interface CrossOverAlgorithm {
    PCBIndividual cross(PCBIndividual firstParent, PCBIndividual secondParent) throws CloneNotSupportedException;
}
