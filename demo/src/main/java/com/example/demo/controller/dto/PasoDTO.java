package com.example.demo.controller.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PasoDTO {

    String nombrePaso;

    int proceso;

    MultipartFile imagen;

}
