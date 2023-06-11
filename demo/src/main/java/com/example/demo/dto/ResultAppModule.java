package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultAppModule {
    /**
     * 40 - все ок
     * 10 - ошибка прикладного сервиса
     * 11 - ошибка хранимой процедуры
     * 14 - ошибка форматного контроля
     * 15 - логический контроль не пройден
     * 30 - обработка выполнена, есть предупреждения
     */
    private int status;

    //Поля для кода 10
    private String errorText;
    private String stackTrace;

    //Поля для кода 11, 30
    private int outExitCode;
    private int outSqlState;
    private int outMessageText;

    //Поля для кода 15
    private List<String> logicalErrors=new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public int getOutExitCode() {
        return outExitCode;
    }

    public void setOutExitCode(int outExitCode) {
        this.outExitCode = outExitCode;
    }

    public int getOutSqlState() {
        return outSqlState;
    }

    public void setOutSqlState(int outSqlState) {
        this.outSqlState = outSqlState;
    }

    public int getOutMessageText() {
        return outMessageText;
    }

    public void setOutMessageText(int outMessageText) {
        this.outMessageText = outMessageText;
    }

    public List<String> getLogicalErrors() {
        return logicalErrors;
    }

    public void setLogicalErrors(List<String> logicalErrors) {
        this.logicalErrors = logicalErrors;
    }
}