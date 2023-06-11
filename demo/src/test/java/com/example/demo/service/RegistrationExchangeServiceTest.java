package com.example.demo.service;

import com.example.demo.DemoApplication;
import com.example.demo.dto.Message;
import com.example.demo.dto.ResultAppModule;
import com.example.demo.dto.Structure;
import com.example.demo.repository.JdbcTemplateRepository;
import com.example.demo.validate.ValidationResult;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {DemoApplication.class})
@Transactional
class RegistrationExchangeServiceTest {

    private RegistrationExchangeService registrationExchangeService;
    private JdbcTemplateRepository jdbcTemplateRepository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private long messageId;

    @Autowired
    public RegistrationExchangeServiceTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplateRepository = new JdbcTemplateRepository(namedParameterJdbcTemplate);
        registrationExchangeService = new RegistrationExchangeService(jdbcTemplateRepository);
    }

    /**
     * Создает транзакцию для каждого метода. После выполнения каждого метода транзакция удаляет тестовую запись
     * Проверяет, что тестовая запись удалена
     * Тест должен вернуть true
     */
    @AfterTransaction
    void afterVerifyFinalDatabaseState() {
        List<String> strings = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        assertEquals(0, strings.size());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationSuccess() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "", getResultAppModuleWithStatus40(),
                getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        messageId = registrationExchangeService.registrationSuccess(getMessage(), getResultAppModuleWithStatus40(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationWarning() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "",
                getResultAppModuleWithStatus30(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationWarning(getMessage(), getResultAppModuleWithStatus30(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorLogical() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "",
                getResultAppModuleWithStatus15(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationErrorLogical(getMessage(), getResultAppModuleWithStatus15(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorValidation() {
        String errorMessage = "";
        for (Map<String, String> map : getValidationResult().getBodyFormatError()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                errorMessage += entry.getValue();
            }
        }
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), errorMessage,
                getResultAppModuleWithStatus14(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);

        long messageId = registrationExchangeService.registrationErrorValidation(getMessage(), getValidationResult(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorStoredProcedure() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "",
                getResultAppModuleWithStatus11(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationErrorStoredProcedure(getMessage(), getResultAppModuleWithStatus11(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorDispatcherRoute() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "Тестовая ошибка",
                getResultAppModuleWithStatus10(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationErrorDispatcherRoute(getMessage(), "Тестовая ошибка",
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorAppService() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "",
                getResultAppModuleWithStatus10(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationErrorAppService(getMessage(), getResultAppModuleWithStatus10(),
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorUndefined() {
        List<String> expectedFieldList = getExpectedFieldList(getMessage(), "Тестовая ошибка",
                getResultAppModuleWithStatus10(), getListStructures(), "SYNC", "SyncMsgService");
        System.out.println(expectedFieldList);
        long messageId = registrationExchangeService.registrationErrorUndefined(getMessage(), "Тестовая ошибка",
                "SYNC", "SyncMsgService");
        List<String> actualFieldList = jdbcTemplateRepository.getListRecordsFromAllTablesByMessageId(messageId);
        System.out.println(actualFieldList);
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Сравнивает List ожидаемых значений с List значений, записанных и полученных из БД
     * Тест должен вернуть true
     */
    @Test
    void testRegistrationErrorTransform() {
        String messageStr = "Тестовая ошибка";

        // Выполняем запись в БД
        registrationExchangeService.registrationErrorTransform(messageStr,"SYNC", "SyncMsgService");

        // Считываем записанные данные из БД, кладем в List
        List<String> actualFieldList = jdbcTemplateRepository.getListFromRegDataInUnparsedMsgTableByMessageStr(messageStr);

        // Получаем время записи в БД, т.к. в БД пишется текущее время
        String currentTime = actualFieldList.get(0).substring(0, 19).replaceAll("T", " ");

        // Инициализируем List ожидаемых значений
        List<String> expectedFieldList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        expectedFieldList.add(LocalDateTime.parse(currentTime, formatter).toString());
        expectedFieldList.add(messageStr);
        expectedFieldList.add("SYNC");
        expectedFieldList.add("SyncMsgService");

        System.out.println(expectedFieldList);
        System.out.println(actualFieldList);

        // Сравниваем списки
        assertArrayEquals(expectedFieldList.toArray(), actualFieldList.toArray());
    }

    /**
     * Возвращает List ожидаемых значений, которые будут записаны в БД
     */
    private List<String> getExpectedFieldList(Message message, String errorMessage, ResultAppModule resultAppModule,
                                              List<Structure> structures, String transmissionMode, String source) {
        List<String> expectedFieldList = new ArrayList<>();
        for (Structure structure : structures) {
            expectedFieldList.add(message.getMsgId());
            expectedFieldList.add(message.getRefMsgId());
            expectedFieldList.add(message.getMsgCode());
            expectedFieldList.add(String.valueOf(message.getSenderCode()));
            expectedFieldList.add(String.valueOf(message.getSenderUserId()));
            expectedFieldList.add(message.getVersion());
            expectedFieldList.add(message.getDateTimeCreate().toString());
            expectedFieldList.add(message.getDateTimeRecive().toString());
            expectedFieldList.add(String.valueOf(resultAppModule.getStatus()));
            expectedFieldList.add(errorMessage);
            expectedFieldList.add(transmissionMode);
            expectedFieldList.add(source);
            expectedFieldList.add(message.getMsgId());
            expectedFieldList.add(message.getDateTimeRecive().toString());
            expectedFieldList.add(message.getMsgStr());
            expectedFieldList.add(message.getMsgId());
            expectedFieldList.add(structure.getIdDoc());
            expectedFieldList.add(structure.getKodStruct());
            expectedFieldList.add(message.getDateTimeRecive().toString());
            expectedFieldList.add(String.valueOf(resultAppModule.getStatus()));
            expectedFieldList.add(String.valueOf(resultAppModule.getErrorText()));
            expectedFieldList.add(structure.getIdDoc());
            expectedFieldList.add(message.getDateTimeRecive().toString());
            expectedFieldList.add(structure.getStructureStr());
        }
        return expectedFieldList;
    }

    /**
     * Возвращает тестовый инициализированный объект Message
     */
    private Message getMessage() {
        Message message = new Message();
        message.setMsgId("UUID");
        message.setRefMsgId("UUID");
        message.setMsgCode("1354");
        message.setSenderCode(10);
        message.setSenderUserId(1000);
        message.setVersion("1.0.0/1.0.0");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        message.setDateTimeCreate(LocalDateTime.parse("2023-06-08 14:20:20", formatter));
        message.setDateTimeRecive(LocalDateTime.parse("2023-06-08 14:20:20", formatter));
        message.setStructures(getListStructures());

        Map<String, String> map = new HashMap<>();
        map.put("MessageKey", "MessageValue");
        JSONObject jo = new JSONObject(map);
        message.setMsgStr(jo.toString());

        return message;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 10
     * error_text = "Ошибка прикладного сервиса"
     */
    private ResultAppModule getResultAppModuleWithStatus10() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(10);
        resultAppModule.setErrorText("Ошибка прикладного сервиса");
        return resultAppModule;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 11
     * error_text = "Ошибка хранимой процедуры"
     */
    private ResultAppModule getResultAppModuleWithStatus11() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(11);
        resultAppModule.setErrorText("Ошибка хранимой процедуры");
        return resultAppModule;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 14
     * error_text = "Ошибка форматного контроля"
     */
    private ResultAppModule getResultAppModuleWithStatus14() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(14);
        resultAppModule.setErrorText("Ошибка форматного контроля");
        return resultAppModule;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 15
     * error_text = "Логический котроль не пройден"
     */
    private ResultAppModule getResultAppModuleWithStatus15() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(15);
        resultAppModule.setErrorText("Логический котроль не пройден");
        return resultAppModule;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 30
     * error_text = "Обработка выполнена, есть предупреждения"
     */
    private ResultAppModule getResultAppModuleWithStatus30() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(30);
        resultAppModule.setErrorText("Обработка выполнена, есть предупреждения");
        return resultAppModule;
    }

    /**
     * Возвращает тестовый инициализированный объект ResultAppModule
     * status = 40
     * error_text = ""
     */
    private ResultAppModule getResultAppModuleWithStatus40() {
        ResultAppModule resultAppModule = new ResultAppModule();
        resultAppModule.setStatus(40);
        resultAppModule.setErrorText("");
        return resultAppModule;
    }

    /**
     * Возвращает List тестовых инициализированных объектов Structure
     */
    private List<Structure> getListStructures() {
        Structure firstStructure = new Structure();
        firstStructure.setIdDoc("b1f9ce0f-9b39");
        firstStructure.setKodStruct("1.1.8956");
        Map<String, String> map = new HashMap<>();
        map.put("StructureKey1", "StructureValue1");
        JSONObject jo = new JSONObject(map);
        firstStructure.setStructureStr(jo.toString());

        Structure secondStructure = new Structure();
        secondStructure.setIdDoc("d12fef6-541gtrg45");
        secondStructure.setKodStruct("1.2.5612");
        map = new HashMap<>();
        map.put("StructureKey2", "StructureValue2");
        jo = new JSONObject(map);
        secondStructure.setStructureStr(jo.toString());

        return List.of(firstStructure, secondStructure);
    }

    /**
     * Возвращает тестовый инициализированный объект ValidationResult
     */
    private ValidationResult getValidationResult() {
        ValidationResult validationResult = new ValidationResult();
        Map<String, String> map1 = Map.of("{Validation Error № 1$.body.1}", "{$.body.1: string found, object expected}");
        Map<String, String> map2 = Map.of("{Validation Error № 2$.body.2}", "{$.body.2: string found, object expected}");
        List<Map<String, String>> list = List.of(map1, map2);
        validationResult.setBodyFormatError(list);
        return validationResult;
    }
}
