/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenovo.lic.manager.managerBack.services;

import com.lenovo.lic.manager.managerBack.dto.MachineDto;
import com.lenovo.lic.manager.managerBack.model.Machine;
import com.lenovo.lic.manager.managerBack.repository.IMachineRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.File;

/**
 *
 * @author vntrotq
 */
@Service
@Configurable
public class ManagerService {

    @Autowired
    private IMachineRepository machineRepository;

    public List<MachineDto> getAllMachinesFromDb() {
        Iterable<Machine> allMachines = machineRepository.findAll();
        List<MachineDto> result = new ArrayList();
        for (Machine mac : allMachines) {
            result.add(parseModelToDto(mac, true));
        }
        return result;
    }

    public MachineDto getAllMachineFromDb(long id) {
        MachineDto result = parseModelToDto(machineRepository.findOne(id), true);
        return result;
    }

    public MachineDto createMachine(MachineDto dto) {
        if (dto.getId() == null) {
            dto.setId((long) 0);
        }
        Machine mac = machineRepository.save(parseDtoToModel(dto));
        return parseModelToDto(mac, true);
        // interface to machine project
    }

    public MachineDto editMachine(MachineDto dto) {
        Machine updatedmachine = machineRepository.findOne(dto.getId());
        updatedmachine.setName(dto.getName());
        updatedmachine.setModel(dto.getModel());
        updatedmachine.setSerialNumber(dto.getSerialNumber());
        updatedmachine.setProcessor(dto.getProcessor());
        updatedmachine.setHd(dto.getHd());
        updatedmachine.setMemory(dto.getMemory());
        machineRepository.save(updatedmachine);
        Machine mac = machineRepository.save(parseDtoToModel(dto));
        return parseModelToDto(mac, true);
        // interface to machine project
    }

    public void excludeMachine(MachineDto dto) {
        machineRepository.delete(dto.getId());
        // interface to machine project
    }

    public Machine parseDtoToModel(MachineDto dto) {
        Machine result = new Machine(
                dto.getId(),
                dto.getModel(),
                dto.getSerialNumber(),
                dto.getName(),
                dto.getProcessor(),
                dto.getMemory(),
                dto.getHd()
        );
        return result;
    }

    public void createPreConfiguredMachines() {
        //create machines o machineProject
    }

    public void getMachines() {
        //getMachine from Id at machineProject
    }

    public MachineDto parseModelToDto(Machine machine, boolean isFromDb) {
        MachineDto dto = new MachineDto(
                machine.getId(),
                machine.getName(),
                machine.getSerialNumber(),
                machine.getModel(),
                machine.getProcessor(),
                machine.getMemory(),
                machine.getHd(),
                isFromDb ? "" : "",
                isFromDb ? 0 : 0,
                isFromDb ? 0 : 0,
                isFromDb ? new int[]{0, 0, 0, 0} : new int[]{127, 0, 0, 2}
        );
        return dto;
    }

    public MachineDto parseJsonToMachine(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonInString = json;
            MachineDto dto1 = mapper.readValue(jsonInString, MachineDto.class);
            System.out.println(dto1);
            return dto1;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
