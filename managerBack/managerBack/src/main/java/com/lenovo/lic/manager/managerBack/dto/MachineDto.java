/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenovo.lic.manager.managerBack.dto;

import java.util.Collection;

/**
 *
 * @author rodtq
 */
public class MachineDto {

    private Long id;
    private String name;
    private String serialNumber;
    private String model;
    private String processor;
    private String memory;
    private String hd;
    private String temperature;
    private int powerStatus;
    private int machineStatus;
    private int[] address;
    private Collection<MachineDto> machineList;

    public MachineDto(){}
    public MachineDto(Long id, String name, String serialNumber, String model, String processor, String memory, String hd, String temperature, int powerStatus, int machineStatus, int[] address) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.model = model;
        this.processor = processor;
        this.memory = memory;
        this.hd = hd;
        this.temperature = temperature;
        this.powerStatus = powerStatus;
        this.machineStatus = machineStatus;
        this.address = address;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the processor
     */
    public String getProcessor() {
        return processor;
    }

    /**
     * @param processor the processor to set
     */
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * @return the memory
     */
    public String getMemory() {
        return memory;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(String memory) {
        this.memory = memory;
    }

    /**
     * @return the hd
     */
    public String getHd() {
        return hd;
    }

    /**
     * @param hd the hd to set
     */
    public void setHd(String hd) {
        this.hd = hd;
    }

    /**
     * @return the temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the powerStatus
     */
    public int getPowerStatus() {
        return powerStatus;
    }

    /**
     * @param powerStatus the powerStatus to set
     */
    public void setPowerStatus(int powerStatus) {
        this.powerStatus = powerStatus;
    }

    /**
     * @return the machineStatus
     */
    public int getMachineStatus() {
        return machineStatus;
    }

    /**
     * @param machineStatus the machineStatus to set
     */
    public void setMachineStatus(int machineStatus) {
        this.machineStatus = machineStatus;
    }

    /**
     * @return the address
     */
    public int[] getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(int[] address) {
        this.address = address;
    }

    /**
     * @return the machineList
     */
    public Collection<MachineDto> getMachineList() {
        return machineList;
    }

    /**
     * @param machineList the machineList to set
     */
    public void setMachineList(Collection<MachineDto> machineList) {
        this.machineList = machineList;
    }

}
