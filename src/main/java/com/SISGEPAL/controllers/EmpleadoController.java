package com.SISGEPAL.controllers;


import com.SISGEPAL.DTO.empleados.request.NewEmpleadoDTO;
import com.SISGEPAL.DTO.empleados.request.UpdateEmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.services.EmpleadoService;
import com.SISGEPAL.services.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private MailingService mailingService;

    @GetMapping
    public ResponseEntity<EmpleadosDTO> getEmpleados(){
        EmpleadosDTO empleadosDTO =
                empleadoService.mapToEmpleadosDTo(
                        empleadoService.findAll()
                );
        ResponseEntity<EmpleadosDTO> response
                = new ResponseEntity<EmpleadosDTO>(empleadosDTO, HttpStatus.OK);

        return response;
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> postEmpleado(@RequestBody NewEmpleadoDTO newEmpleadoDTO)
            throws BadRequestException, ConflictException, MessagingException, IOException {
        EmpleadoDTO empleadoDTO =
                empleadoService.mapToEmpleadoDTO(empleadoService.createEmpleado(newEmpleadoDTO));
        ResponseEntity<EmpleadoDTO> response
                = new ResponseEntity<EmpleadoDTO>(empleadoDTO,HttpStatus.OK);
        return response;

    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> putEmpleado(@RequestBody UpdateEmpleadoDTO updateEmpleadoDTO,
        @PathVariable int id)
            throws BadRequestException, ConflictException, MessagingException, IOException, NotFoundException {
        EmpleadoDTO empleadoDTO =
                empleadoService.mapToEmpleadoDTO(empleadoService.updateEmpleado(updateEmpleadoDTO,id));
        ResponseEntity<EmpleadoDTO> response
                = new ResponseEntity<EmpleadoDTO>(empleadoDTO,HttpStatus.OK);
        return response;

    }

}
