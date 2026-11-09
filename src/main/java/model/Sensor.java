package model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Sensor {

    private String sensorID;
    private Long timestamp;
    private Double temperatura;
    private Double umidade;
    private String localizacao;

    public Sensor() {
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("SensorID")
    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = "LocalizacaoIndex")
    @DynamoDbAttribute("Timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbSecondarySortKey(indexNames = "TemperaturaIndex")
    @DynamoDbAttribute("Temperatura")
    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    @DynamoDbAttribute("Umidade")
    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "LocalizacaoIndex")
    @DynamoDbAttribute("Localizacao")
    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}