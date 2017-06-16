/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenovo.lic.manager.managerBack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lenovo.lic.manager.managerBack.dto.MachineDto;
import com.lenovo.lic.manager.managerBack.model.ConnectParams;
import com.lenovo.lic.manager.managerBack.services.ManagerService;
import io.mikael.urlbuilder.UrlBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vntrotq
 */
@RestController
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @RequestMapping(value = "/getmachines", method = RequestMethod.GET)
    public Collection<MachineDto> GetMachines() throws IOException {
        Collection<MachineDto> fromPool = getAllMachinesFromPool();
        Collection<MachineDto> fromDB = managerService.getAllMachinesFromDb();
        if (fromPool.size() == fromDB.size()) {
            return fromPool;
        } else {
            for (MachineDto machine : fromPool) {
                removeFromPool(machine.getId());
            }
            for (MachineDto machine : fromDB) {
                boolean isdone = false;
                while (!isdone) {
                    isdone = addPoolMachine(machine);
                }
            }
        }
        return fromPool;
    }

    @RequestMapping(value = "/getmachine", method = RequestMethod.GET)
    public MachineDto getMachine(long id) {
        MachineDto result = managerService.getAllMachineFromDb(id);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean CreateMachine(@RequestBody MachineDto machine) {
        MachineDto dto = managerService.createMachine(machine);
        addPoolMachine(dto);
        return true;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public boolean editMachine(@RequestBody MachineDto machine) {
        try {
            MachineDto dto = managerService.editMachine(machine);
            editPoolMachine(dto);
            return true;
        } catch (Exception e) {
            throw e;
        }

    }

    @RequestMapping(value = "/exclude", method = RequestMethod.GET)
    public boolean excludeMachine(long id) {
        //  managerService.deleteFromDb(id);
        boolean result = false;
        try {
            result = removeFromPool(id);
        } catch (IOException ex) {
            Logger.getLogger(ManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //url for test:
    //localhost:9001/power?id=1&togglePower=1 --> machine on
    //localhost:9001/power?id=1&togglePower=0 --machine off
    @RequestMapping(value = "/power", method = RequestMethod.GET)
    public boolean powerMachine(long id, int togglePower) throws IOException {
        boolean result = false;
        String paramValue1 = Long.toString(id);
        String paramValue2 = Integer.toString(togglePower);
        try {

            String urlText = UrlBuilder.fromString("http://localhost:9001/power")
                    .addParameter("id", paramValue1)
                    .addParameter("togglePower", paramValue2)
                    .toString();
            URL url = new URL(urlText);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            result = true;

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;

        }
        return result;
    }

    //url for test:
    //localhost:9001/connect
    //Payload example:
    //[127,0,0,2]
    /**
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public boolean connectToMachine(@RequestBody ConnectParams params) {
        boolean result = connect(params.getAddress(), params.getUser(), params.getPassword());
        return result;
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public boolean disconnectToMachine(@RequestBody int[] address) {
        return disconnect(address);
    }

    private List<MachineDto> getAllMachinesFromPool() throws MalformedURLException, IOException {
        List<MachineDto> result = new ArrayList();
        try {

            URL url = new URL("http://localhost:9001/getmachines");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            ObjectMapper mapper = new ObjectMapper();
            while ((output = br.readLine()) != null) {
                result = mapper.readValue(output, TypeFactory.defaultInstance().constructCollectionType(List.class, MachineDto.class));
            }
            conn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return result;
    }

    private boolean addPoolMachine(MachineDto dto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpPost request = new HttpPost("http://localhost:9001/add");
                StringEntity params = new StringEntity(jsonInString);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
                String output;
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean editPoolMachine(MachineDto dto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpPost request = new HttpPost("http://localhost:9001/edit");
                StringEntity params = new StringEntity(jsonInString);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
                String output;
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean connect(int[] address, String user, String password) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);

        try {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build()) {

                HttpPost request = new HttpPost("http://localhost:9001/connect");

                StringEntity params = new StringEntity(jsonInString);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
                String output;
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean disconnect(int[] address) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpPost request = new HttpPost("http://localhost:9001/disconnect");
                StringEntity params = new StringEntity(jsonInString);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
                String output;
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean removeFromPool(long id) throws MalformedURLException, IOException {
        boolean result = false;

        String paramValue1 = Long.toString(id);
        try {

            String urlText = UrlBuilder.fromString("http://localhost:9001/exclude")
                    .addParameter("id", paramValue1)
                    .toString();
            URL url = new URL(urlText);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            ObjectMapper mapper = new ObjectMapper();
            while ((output = br.readLine()) != null) {
            }
            result = true;
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
