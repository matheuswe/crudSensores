package br.edu.utfpr.td.tsi.controller;

import br.edu.utfpr.td.tsi.service.SensorService;
import model.Sensor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensorController {

    private SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @PostMapping("/sensores")
    public Sensor criar(@RequestBody Sensor sensor) {
        return service.salvar(sensor);
    }

    @GetMapping("/sensores")
    public List<Sensor> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/sensores/{sensorId}")
    public List<Sensor> buscarPorSensor(@PathVariable String sensorId) {
        return service.buscarPorSensor(sensorId);
    }

    @GetMapping("/sensores/{sensorId}/{timestamp}")
    public Sensor buscarPorSensorETimestamp(@PathVariable String sensorId, @PathVariable Long timestamp) {
        return service.buscarPorSensorETimestamp(sensorId, timestamp);
    }

    @GetMapping("/sensores/{sensorId}/temperatura")
    public List<Sensor> buscarPorTemperatura(@PathVariable String sensorId,
                                             @RequestParam Double min,
                                             @RequestParam Double max) {
        return service.buscarPorTemperatura(sensorId, min, max);
    }

    @GetMapping("/localizacoes/{localizacao}")
    public List<Sensor> buscarPorLocalizacao(@PathVariable String localizacao) {
        return service.buscarPorLocalizacao(localizacao);
    }

    @GetMapping("/localizacoes/{localizacao}/{sensorId}")
    public List<Sensor> buscarSensorNaLocalizacao(@PathVariable String localizacao,
                                                  @PathVariable String sensorId) {
        return service.buscarSensorNaLocalizacao(localizacao, sensorId);
    }

    @PutMapping("/sensores/{sensorId}/{timestamp}")
    public Sensor atualizar(@PathVariable String sensorId,
                            @PathVariable Long timestamp,
                            @RequestBody Sensor dados) {
        return service.atualizar(sensorId, timestamp, dados);
    }

    @DeleteMapping("/sensores/{sensorId}/{timestamp}")
    public Sensor deletar(@PathVariable String sensorId, @PathVariable Long timestamp) {
        return service.deletar(sensorId, timestamp);
    }
}
