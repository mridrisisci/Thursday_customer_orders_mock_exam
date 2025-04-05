package app.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(force = true)
public class PackingListDTO
{
    //private List<String> categories;
    private String element;
    public PackingListDTO(String element)
    {
        this.element = element;
    }
}
