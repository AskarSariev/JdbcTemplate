package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сообщение
 */
public class Message {

    private String msgId; //ИД сообщения в формате UUID

    private String refMsgId; //Ссылка на ИД сообщения в формате UUID

    private String msgCode; //Код сообщения

    private String version; //Версия АФ/спецификации (ИТ технологии)

    private LocalDateTime dateTimeCreate;//Дата и время создания сообщения

    private Integer senderCode; //Код отправителя сообщения

    private Integer senderUserId; //ИД пользователя сообщения (добавляет REST сервис при приеме)

    private Integer receiveCode; //Код получателя сообщения

    private LocalDateTime dateTimeRecive; // Дата и время получения сообщения (добавляет REST сервис при приеме)

    private String aosInfo; //АОС реквизиты (добавляет jmsAdapter при приеме)
    private String mqInfo; //MQ реквизиты (добавляет jmsAdapter при приеме)

    private List<Structure> structures = new ArrayList<>();

    private String msgStr;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getRefMsgId() {
        return refMsgId;
    }

    public void setRefMsgId(String refMsgId) {
        this.refMsgId = refMsgId;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getDateTimeCreate() {
        return dateTimeCreate;
    }

    public void setDateTimeCreate(LocalDateTime dateTimeCreate) {
        this.dateTimeCreate = dateTimeCreate;
    }

    public Integer getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(Integer senderCode) {
        this.senderCode = senderCode;
    }

    public Integer getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Integer senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Integer getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(Integer receiveCode) {
        this.receiveCode = receiveCode;
    }

    public LocalDateTime getDateTimeRecive() {
        return dateTimeRecive;
    }

    public void setDateTimeRecive(LocalDateTime dateTimeRecive) {
        this.dateTimeRecive = dateTimeRecive;
    }

    public String getAosInfo() {
        return aosInfo;
    }

    public void setAosInfo(String aosInfo) {
        this.aosInfo = aosInfo;
    }

    public String getMqInfo() {
        return mqInfo;
    }

    public void setMqInfo(String mqInfo) {
        this.mqInfo = mqInfo;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public String getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }
}
