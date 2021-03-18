package pl.damian.zoltowski.selection;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.damian.zoltowski.pcb.PCBIndividual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class TournamentSelection implements SelectionAlgorithm {
    private int K;

    @Override
    public PCBIndividual select(List<PCBIndividual> population) {
        List<Integer> pickedCandidates = new ArrayList<>();
        List<PCBIndividual> selectedCandidates = new ArrayList<>();

        for(int i = 0; i < K; i++) {
            int candidateIndex = new Random().nextInt(population.size());
            if(pickedCandidates.contains(candidateIndex)) {
                i--;
            } else {
                pickedCandidates.add(candidateIndex);
            }
        }
        for(Integer index: pickedCandidates) {
            selectedCandidates.add(population.get(index));
        }

        return selectedCandidates.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get();
    }
}
