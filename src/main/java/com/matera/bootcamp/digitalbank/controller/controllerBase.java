package com.matera.bootcamp.digitalbank.controller;

import com.matera.bootcamp.digitalbank.dto.response.ErroResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ResponseDto;
import com.matera.bootcamp.digitalbank.exception.ServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public abstract class controllerBase {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseDto<Object>> handleException(ServiceException exception) {
        ErroResponseDto erro = new ErroResponseDto(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.comErro(erro));
    }
}
