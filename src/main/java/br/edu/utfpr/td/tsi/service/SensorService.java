package br.edu.utfpr.td.tsi.service;

import model.Sensor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorService {

    private DynamoDbTable<Sensor> tabela;

    public SensorService(DynamoDbEnhancedClient enhancedClient) {
        this.tabela = enhancedClient.table("SensoresToledo", TableSchema.fromBean(Sensor.class));
    }

    public Sensor salvar(Sensor sensor) {
        tabela.putItem(sensor);
        return sensor;
    }

    public List<Sensor> buscarPorSensor(String sensorId) {
        QueryConditional condicao = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(sensorId).build());

        List<Sensor> lista = new ArrayList<>();
        for (Sensor sensor : tabela.query(condicao).items()) {
            lista.add(sensor);
        }
        return lista;
    }

    public Sensor buscarPorSensorETimestamp(String sensorId, Long timestamp) {
        Key chave = Key.builder()
                .partitionValue(sensorId)
                .sortValue(timestamp)
                .build();
        return tabela.getItem(chave);
    }

    public List<Sensor> buscarPorLocalizacao(String localizacao) {
        DynamoDbIndex<Sensor> indice = tabela.index("LocalizacaoIndex");
        QueryConditional condicao = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(localizacao).build());

        List<Sensor> lista = new ArrayList<>();
        SdkIterable<Page<Sensor>> paginas = indice.query(condicao);
        for (Page<Sensor> pagina : paginas) {
            for (Sensor sensor : pagina.items()) {
                lista.add(sensor);
            }
        }
        return lista;
    }

    public List<Sensor> buscarSensorNaLocalizacao(String localizacao, String sensorId) {
        List<Sensor> todosDoSensor = buscarPorSensor(sensorId);

        List<Sensor> lista = new ArrayList<>();
        for (Sensor sensor : todosDoSensor) {
            if (sensor.getLocalizacao() != null && sensor.getLocalizacao().equals(localizacao)) {
                lista.add(sensor);
            }
        }
        return lista;
    }

    public List<Sensor> buscarPorTemperatura(String sensorId, Double min, Double max) {
        DynamoDbIndex<Sensor> indice = tabela.index("TemperaturaIndex");
        QueryConditional condicao = QueryConditional.sortBetween(
                Key.builder().partitionValue(sensorId).sortValue(min).build(),
                Key.builder().partitionValue(sensorId).sortValue(max).build());

        List<Sensor> lista = new ArrayList<>();
        SdkIterable<Page<Sensor>> paginas = indice.query(condicao);
        for (Page<Sensor> pagina : paginas) {
            for (Sensor sensor : pagina.items()) {
                lista.add(sensor);
            }
        }
        return lista;
    }

    public List<Sensor> listarTodos() {
        List<Sensor> lista = new ArrayList<>();
        for (Sensor sensor : tabela.scan().items()) {
            lista.add(sensor);
        }
        return lista;
    }

    public Sensor atualizar(String sensorId, Long timestamp, Sensor dados) {
        Sensor existente = buscarPorSensorETimestamp(sensorId, timestamp);
        if (existente == null) {
            return null;
        }

        if (dados.getTemperatura() != null) {
            existente.setTemperatura(dados.getTemperatura());
        }
        if (dados.getUmidade() != null) {
            existente.setUmidade(dados.getUmidade());
        }
        if (dados.getLocalizacao() != null) {
            existente.setLocalizacao(dados.getLocalizacao());
        }

        tabela.putItem(existente);
        return existente;
    }

    public Sensor deletar(String sensorId, Long timestamp) {
        Key chave = Key.builder()
                .partitionValue(sensorId)
                .sortValue(timestamp)
                .build();
        return tabela.deleteItem(chave);
    }
}
