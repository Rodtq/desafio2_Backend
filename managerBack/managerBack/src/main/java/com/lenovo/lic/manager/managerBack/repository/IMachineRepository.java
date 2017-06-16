/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenovo.lic.manager.managerBack.repository;

import com.lenovo.lic.manager.managerBack.model.Machine;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author rodtq
 */
public interface IMachineRepository extends CrudRepository<Machine, Long> {

}
