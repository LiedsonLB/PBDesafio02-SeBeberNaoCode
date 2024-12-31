package com.sebebernaocode.products.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PageableDto {

    private List content = new ArrayList();
    private boolean first;
    private boolean last;

    @JsonProperty("page")
    private int number;

    @JsonProperty("linesPerPage")
    private int size; // isso é o que regula o tamanho da pagina, mas só aceita se passar como size

    private int totalPages;

    private int totalElements;
}
