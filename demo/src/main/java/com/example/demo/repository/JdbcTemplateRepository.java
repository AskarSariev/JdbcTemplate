package com.example.demo.repository;

import com.example.demo.dto.Message;
import com.example.demo.dto.ResultAppModule;
import com.example.demo.dto.Structure;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcTemplateRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Сообщение успешно обработано. Регистрация в БД сообщения
     */
    public long registerSuccess(Message message, String errorMessage, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        return insertAll(message, resultAppModule.getStatus(), errorMessage, resultAppModule.getErrorText(), transmissionMode, sourceMsg);
    }


    /**
     * Сообщение успешно обработано, есть предупреждения. Регистрация в БД сообщения
     */
    public long registerWarning(Message message, String errorMessage, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        return insertAll(message, resultAppModule.getStatus(), errorMessage, resultAppModule.getErrorText(), transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки логического контроля
     */
    public long registerErrorLogical(Message message, String errorMessage, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        return insertAll(message, resultAppModule.getStatus(), errorMessage, resultAppModule.getErrorText(), transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки валидации сообщения по JSON - схеме
     */
    public long registerErrorValidation(Message message, int status, String errorMessage, String errorText, String transmissionMode, String sourceMsg) {
        return insertAll(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки обработки сообщения прикладным модулем (ошибка в SP)
     * (Модуль ведения функциональной задачи)
     */
    public long registerErrorStoredProcedure(Message message, String errorMessage, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        return insertAll(message, resultAppModule.getStatus(), errorMessage, resultAppModule.getErrorText(), transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки - не определен маршрут обработки сообщения в прикладном модуле
     */
    public long registerErrorDispatcherRoute(Message message, int status, String errorMessage, String errorText, String transmissionMode, String sourceMsg) {
        return insertAll(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки ошибки обработки сообщения прикладным модулем (ошибка в JAVA)
     * (Модуль ведения функциональной задачи)
     */
    public long registrationErrorAppService(Message message, String errorMessage, ResultAppModule resultAppModule, String transmissionMode, String sourceMsg) {
        return insertAll(message, resultAppModule.getStatus(), errorMessage, resultAppModule.getErrorText(), transmissionMode, sourceMsg);
    }


    /**
     * Регистрация в БД сообщения и ошибки обработки сообщения прикладным модулем (неизвестная ошибка)
     * (Модуль ведения функциональной задачи)
     */
    public long registerErrorUndefined(Message message, int status, String errorMessage, String errorText, String transmissionMode, String sourceMsg) {
        return insertAll(message, status, errorMessage, errorText, transmissionMode, sourceMsg);
    }

    /**
     * Регистрация в БД ошибки трансформации сообщения в объект Message, если строку не удалось преобразовать в объект Message
     * Вставка записи в таблицу reg_data_unparsed_msg
     */
    public void registerErrorTransform(String message, String transmissionMode, String sourceMsg) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter); // Пишем текущее время, т.к. объект Message не получен

        String insert = "INSERT INTO reg_data_in_unparsed_msg (date_time_recive, message_str, transmission_mode, source) " +
                                                     "VALUES (:date_time_recive, :message_str, :transmission_mode, :source)";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("date_time_recive", currentTime);
        parameterSource.addValue("message_str", message);
        parameterSource.addValue("transmission_mode", transmissionMode);
        parameterSource.addValue("source", sourceMsg);
        namedParameterJdbcTemplate.update(insert, parameterSource);
    }

    /**
     * Вернуть List значений, полученных из БД после записи
     * @param messageId - ID сохраненного объекта Message
     */
    public List<String> getListRecordsFromAllTablesByMessageId(long messageId) {
        List<String> actualFieldList = new ArrayList<>();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", messageId);
        String select = "SELECT * FROM reg_data_in rdi " +
                "JOIN reg_data_in_message_str rdims " +
                "ON rdi.msg_id = rdims.msg_id " +
                "JOIN reg_data_in_struct rdis " +
                "ON rdi.msg_id = rdis.msg_id " +
                "JOIN reg_data_in_struct_str rdiss " +
                "ON rdis.id_doc = rdiss.id_doc " +
                "WHERE rdi.id = :id";

        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(select, parameterSource);

        for (Map<String, Object> map : list) {
            actualFieldList.add(map.get("msg_id").toString());
            actualFieldList.add(map.get("ref_msg_id").toString());
            actualFieldList.add(map.get("msg_code").toString());
            actualFieldList.add(map.get("sender_code").toString());
            actualFieldList.add(map.get("sender_user_id").toString());
            actualFieldList.add(map.get("version").toString());
            actualFieldList.add(map.get("date_time_create").toString().replaceAll(" ", "T").substring(0, 19));
            actualFieldList.add(map.get("date_time_recive").toString().replaceAll(" ", "T").substring(0, 19));
            actualFieldList.add(map.get("status").toString());
            actualFieldList.add(map.get("error_message").toString());
            actualFieldList.add(map.get("transmission_mode").toString());
            actualFieldList.add(map.get("source").toString());
            actualFieldList.add(map.get("msg_id").toString());
            actualFieldList.add(map.get("date_time_recive").toString().replaceAll(" ", "T").substring(0, 19));
            actualFieldList.add(map.get("message_str").toString().replaceAll(" ", ""));
            actualFieldList.add(map.get("msg_id").toString());
            actualFieldList.add(map.get("id_doc").toString());
            actualFieldList.add(map.get("code_struct").toString());
            actualFieldList.add(map.get("date_time_recive").toString().replaceAll(" ", "T").substring(0, 19));
            actualFieldList.add(map.get("status").toString());
            actualFieldList.add(map.get("error_text").toString());
            actualFieldList.add(map.get("id_doc").toString());
            actualFieldList.add(map.get("date_time_recive").toString().replaceAll(" ", "T").substring(0, 19));
            actualFieldList.add(map.get("struct_str").toString().replaceAll(" ", ""));
        }
        return actualFieldList;
    }

    /**
     * Получить записи из таблицы reg_data_in_unparsed_msg по message_str
     */
    public List<String> getListFromRegDataInUnparsedMsgTableByMessageStr(String messageStr) {
        List<String> strings = new ArrayList<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("message_str", messageStr);
        String select = "SELECT * FROM reg_data_in_unparsed_msg WHERE message_str = :message_str";

        List<Map<String, Object>> mapList = namedParameterJdbcTemplate.queryForList(select, parameterSource);

        for (Map<String, Object> map : mapList) {
            strings.add(map.get("date_time_recive").toString().replaceAll(" ", "T").substring(0, 19));
            strings.add(map.get("message_str").toString());
            strings.add(map.get("transmission_mode").toString());
            strings.add(map.get("source").toString());
        }
        return strings;
    }

    /**
     * Одновременная вставка записей в следующие таблицы:
     *      reg_data_in table
     *      reg_data_in_message_str table
     *      reg_data_in_struct table
     *      reg_data_in_struct_str table
     *
     * @param message          - исходное сообщение типа Message
     * @param status           - код статуса
     * @param errorMessage     - текст ошибки
     * @param transmissionMode - SYNC или ASYNC
     * @param sourceMsg        - SyncMsgService или имя топика
     */
    private long insertAll(Message message, int status, String errorMessage, String errorText, String transmissionMode, String sourceMsg) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Вставка записи в таблицу reg_data_in
        String insertIntoRegDataInTable = "INSERT INTO reg_data_in (msg_id, ref_msg_id, msg_code, sender_code, " +
                "sender_user_id, version, date_time_create, date_time_recive, status, error_message, transmission_mode, " +
                "source) VALUES (:msg_id, :ref_msg_id, :msg_code, :sender_code, :sender_user_id, :version, :date_time_create, " +
                ":date_time_recive, :status, :error_message, :transmission_mode, :source)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("msg_id", message.getMsgId());
        parameterSource.addValue("ref_msg_id", message.getRefMsgId());
        parameterSource.addValue("msg_code", message.getMsgCode());
        parameterSource.addValue("sender_code", message.getSenderCode());
        parameterSource.addValue("sender_user_id", message.getSenderUserId());
        parameterSource.addValue("version", message.getVersion());
        parameterSource.addValue("date_time_create", message.getDateTimeCreate().format(formatter));
        parameterSource.addValue("date_time_recive", message.getDateTimeRecive().format(formatter));
        parameterSource.addValue("status", status);
        parameterSource.addValue("error_message", errorMessage);
        parameterSource.addValue("transmission_mode", transmissionMode);
        parameterSource.addValue("source", sourceMsg);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(insertIntoRegDataInTable, parameterSource, keyHolder, new String[] {"id"});
        long messageId = keyHolder.getKey().longValue();

                // Вставка записи в таблицу reg_data_in_message_str
        String insertIntoRegDataInMessageStrTable = "INSERT INTO reg_data_in_message_str (msg_id, date_time_recive, " +
                "message_str) VALUES (:msg_id, :date_time_recive, :message_str)";
        parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("msg_id", message.getMsgId());
        parameterSource.addValue("date_time_recive", message.getDateTimeRecive().format(formatter));
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        try {
            jsonObject.setValue(message.getMsgStr());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        parameterSource.addValue("message_str", jsonObject);
        namedParameterJdbcTemplate.update(insertIntoRegDataInMessageStrTable, parameterSource);

        // Вставка записи в таблицу reg_data_in_struct
        String insertIntoRegDataInStructTable = "INSERT INTO reg_data_in_struct (msg_id, id_doc, code_struct, " +
                "date_time_recive, status, error_text) VALUES (:msg_id, :id_doc, :code_struct, :date_time_recive, " +
                ":status, :error_text)";
        for (Structure structure : message.getStructures()) {
            parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("msg_id", message.getMsgId());
            parameterSource.addValue("id_doc", structure.getIdDoc());
            parameterSource.addValue("code_struct", structure.getKodStruct());
            parameterSource.addValue("date_time_recive", message.getDateTimeRecive().format(formatter));
            parameterSource.addValue("status", status);
            parameterSource.addValue("error_text", errorText);
            namedParameterJdbcTemplate.update(insertIntoRegDataInStructTable, parameterSource);
        }

        // Вставка записи в таблицу reg_data_in_struct_str
        String insertIntoRegDataInStructStrTable = "INSERT INTO reg_data_in_struct_str (id_doc, date_time_recive, " +
                "struct_str) VALUES (:id_doc, :date_time_recive, :struct_str)";
        for (Structure structure : message.getStructures()) {
            parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id_doc", structure.getIdDoc());
            parameterSource.addValue("date_time_recive", message.getDateTimeRecive().format(formatter));
            jsonObject = new PGobject();
            jsonObject.setType("json");
            try {
                jsonObject.setValue(structure.getStructureStr());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            parameterSource.addValue("struct_str", jsonObject);
            namedParameterJdbcTemplate.update(insertIntoRegDataInStructStrTable, parameterSource);
        }
        return messageId;
    }



//    /**
//     * Удалить тестовые записи во всех таблицах по msg_id
//     */
//    public void deleteByMsgId(String msgId, List<Structure> structures) {
//        MapSqlParameterSource parameterSource = new MapSqlParameterSource("msg_id", msgId);
//        String delete = "DELETE FROM reg_data_in WHERE msg_id = :msg_id";
//        namedParameterJdbcTemplate.update(delete, parameterSource);
//
//        parameterSource = new MapSqlParameterSource("msg_id", msgId);
//        delete = "DELETE FROM reg_data_in_message_str WHERE msg_id = :msg_id";
//        namedParameterJdbcTemplate.update(delete, parameterSource);
//
//        parameterSource = new MapSqlParameterSource("msg_id", msgId);
//        delete = "DELETE FROM reg_data_in_struct WHERE msg_id = :msg_id";
//        namedParameterJdbcTemplate.update(delete, parameterSource);
//
//        for (Structure structure : structures) {
//            String idDoc = structure.getIdDoc();
//            parameterSource = new MapSqlParameterSource("id_doc", idDoc);
//            delete = "DELETE FROM reg_data_in_struct_str WHERE id_doc = :id_doc";
//            namedParameterJdbcTemplate.update(delete, parameterSource);
//        }
//    }
//
//    /**
//     * Удалить записи из таблицы reg_data_in_unparsed_msg по message_str
//     */
//    public void deleteFromRegDataInUnparsedMsgTableByMessageStr(String messageStr) {
//        MapSqlParameterSource parameterSource = new MapSqlParameterSource("message_str", messageStr);
//        String delete = "DELETE FROM reg_data_in_unparsed_msg WHERE message_str = :message_str";
//        namedParameterJdbcTemplate.update(delete, parameterSource);
//    }
}