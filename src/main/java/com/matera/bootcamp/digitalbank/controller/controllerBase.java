package com.matera.bootcamp.digitalbank.controller;

import java.util.ArrayList;
import java.util.List;

import com.matera.bootcamp.digitalbank.dto.response.ErroResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ResponseDto;
import com.matera.bootcamp.digitalbank.exception.ServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public abstract class controllerBase {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseDto<Object>> handleException(ServiceException exception) {
        ErroResponseDto erro = new ErroResponseDto(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.comErro(erro));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> handleException(MethodArgumentNotValidException exception) {
        List<ErroResponseDto> erros = new ArrayList<>();

        BindingResult bindResult = exception.getBindingResult();

        for (FieldError fieldError : bindResult.getFieldErrors()) {
            String campo = fieldError.getField();
            String mensagem = fieldError.getDefaultMessage();
            erros.add(new ErroResponseDto(campo, mensagem));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.comErros(erros));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleException(Exception exception) {
        ErroResponseDto erro = new ErroResponseDto("Erro inseperado ao processar requisição.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.comErro(erro));
    }

}
