package com.example.demo.service;

import com.example.demo.dto.Message;
import com.example.demo.dto.ResultAppModule;
import com.example.demo.repository.JdbcTemplateRepository;
import com.example.demo.validate.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegistrationExchangeService {

    private final JdbcTemplateRepository jdbcTemplateRepository;

    @Autowired
    public RegistrationExchangeService(JdbcTemplateRepository jdbcTemplateRepository) {
        this.jdbcTemplateRepository = jdbcTemplateRepository;
    }

    /**
     * Сообщение успешно обработано
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationSuccess(Message message, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        String errorMessage = ""; // Пустая строка, т.к. ошибок не обнаружено. С уровня выше ошибки не переданы
        return jdbcTemplateRepository.registerSuccess(message, errorMessage, resultAppModule, transmissionMode, sourceMsg);
    }


    /**
     * Сообщение успешно обработано, есть предупреждения
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationWarning(Message message, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        String errorMessage = ""; // С уровня выше ошибки не переданы
        return jdbcTemplateRepository.registerWarning(message, errorMessage, resultAppModule, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки логического контроля
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorLogical(Message message, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        String errorMessage = ""; // С уровня выше ошибки не переданы
        return jdbcTemplateRepository.registerErrorLogical(message, errorMessage, resultAppModule, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки валидации сообщения по JSON - схеме
     * Ошибка фиксации форматного контроля
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorValidation(Message message, ValidationResult validationResult, String transmissionMode, String sourceMsg) {
        int status = 14; // Hardcode. Не передан объект ResultAppModule
        String errorMessage = "";
        for (Map<String, String> map : validationResult.getBodyFormatError()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                errorMessage += entry.getValue();
            }
        }
        String errorText = "Ошибка форматного контроля"; // Hardcode. Не передан объект ResultAppModule
        return jdbcTemplateRepository.registerErrorValidation(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки обработки сообщения прикладным модулем (ошибка в SP) (Модуль ведения функциональной задачи)
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorStoredProcedure(Message message, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        String errorMessage = ""; // С уровня выше ошибки не переданы
        return jdbcTemplateRepository.registerErrorStoredProcedure(message, errorMessage, resultAppModule, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки - не определен маршрут обработки сообщения в прикладном модуле
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorDispatcherRoute(Message message, String errorMessage, String transmissionMode, String sourceMsg) {
        int status = 10; // Ошибка прикладного сервиса
        String errorText = "Ошибка прикладного сервиса"; // Объект ResultAppModule с уровня выше не передан
        return jdbcTemplateRepository.registerErrorDispatcherRoute(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки обработки сообщения прикладным модулем (ошибка в JAVA) (Модуль ведения функциональной задачи)
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorAppService(Message message, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        String errorMessage = ""; // С уровня выше ошибки не переданы
        return jdbcTemplateRepository.registrationErrorAppService(message, errorMessage, resultAppModule, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки обработки сообщения прикладным модулем (неизвестная ошибка) (Модуль ведения функциональной задачи)
     *
     * @param message           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public long registrationErrorUndefined(Message message, String errorMessage, String transmissionMode, String sourceMsg) {
        int status = 10; // Ошибка прикладного сервиса
        String errorText = "Ошибка прикладного сервиса"; // Объект ResultAppModule с уровня выше не передан
        return jdbcTemplateRepository.registerErrorUndefined(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }


    /**
     * Фиксация ошибки трансформации сообщения в объект Message, если строку не удалось преобразовать в объект Message
     *
     * @param error           - исходное сообщение в формате JSON
     * @param transmissionMode - метод получения сообщения SYNC,ASYNC
     * @param sourceMsg        - источник SyncMsgService или имя топика kafka
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void registrationErrorTransform(String error, String transmissionMode, String sourceMsg) {
        jdbcTemplateRepository.registerErrorTransform(error, transmissionMode, sourceMsg);
    }
}
