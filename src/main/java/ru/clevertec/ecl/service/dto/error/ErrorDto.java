package ru.clevertec.ecl.service.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private String errorType;
    private String errorMessage;
    private String code;
}
