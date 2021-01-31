package com.matera.bootcamp.digitalbank.dto.response;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseDto<T> {

    private T dados;
    private List<ErroResponseDto> erros;

    public ResponseDto(T dados) {
        this.dados = dados;
    }

    public static ResponseDto<Object> comErros(List<ErroResponseDto> erros) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setErros(erros);
        return responseDto;
    }

    public static ResponseDto<Object> comErro(ErroResponseDto erro) {
        return comErros(Arrays.asList(erro));
    }
}