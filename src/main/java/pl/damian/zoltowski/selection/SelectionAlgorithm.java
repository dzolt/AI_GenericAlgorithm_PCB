package pl.damian.zoltowski.selection;

import pl.damian.zoltowski.pcb.PCBIndividual;

import java.util.List;

public interface SelectionAlgorithm {
    PCBIndividual select(List<PCBIndividual> population);
}
