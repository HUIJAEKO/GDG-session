package gdsc.practice.question.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

@Valid
public record SearchDto (
        @Nullable
        String subject
){
}

