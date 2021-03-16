package pl.damian.zoltowski.mutation;

import pl.damian.zoltowski.pcb.PCBIndividual;

public interface MutationAlgorithm {
    PCBIndividual mutate(PCBIndividual individual);
}
