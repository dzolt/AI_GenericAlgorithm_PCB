package pl.damian.zoltowski;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.Tuple;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Individual {
    private List<Tuple<String, Integer>> segments;

}
