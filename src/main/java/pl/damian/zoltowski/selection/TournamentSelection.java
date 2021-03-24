package pl.damian.zoltowski.selection;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.damian.zoltowski.pcb.PCBIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
public class TournamentSelection implements SelectionAlgorithm {
    private int K;

    @Override
    public PCBIndividual select(List<PCBIndividual> population) {
        List<Integer> pickedCandidates = new ArrayList<>();
        List<PCBIndividual> selectedCandidates = new ArrayList<>();
        pickedCandidates.addAll(IntStream.range(0, population.size()).boxed().collect(Collectors.toList()));
        Collections.shuffle(pickedCandidates);
        for(int i = 0; i < K; i++) {
            selectedCandidates.add(population.get(pickedCandidates.get(i)));
        }

        return selectedCandidates.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get();
    }

    public int getK() {
        return this.K;
    }
}
