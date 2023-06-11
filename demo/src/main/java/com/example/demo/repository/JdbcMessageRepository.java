package com.example.demo.repository;

import com.example.demo.dto.Message;
import com.example.demo.dto.ResultAppModule;
import com.example.demo.dto.Structure;
import org.springframework.stereotype.Repository;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Repository
public class JdbcMessageRepository {
    private PreparedStatement preparedStatement;

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
        try {
            String insert = "INSERT INTO reg_data_in_unparsed_msg (date_time_recive, message_str, transmission_mode, source) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = JdbcUtils.getPreparedStatement(insert);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTime = LocalDateTime.now().format(formatter); // Пишем текущее время, т.к. объект Message не получен
            preparedStatement.setString(1, currentTime);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, transmissionMode);
            preparedStatement.setString(4, sourceMsg);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
        long messageId = -1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // Вставка записи в таблицу reg_data_in
            String insertIntoRegDataInTable = "INSERT INTO reg_data_in (msg_id, ref_msg_id, msg_code, sender_code, " +
                    "sender_user_id, version, date_time_create, date_time_recive, status, error_message, " +
                    "transmission_mode, source) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = JdbcUtils.getPreparedStatement(insertIntoRegDataInTable);
            preparedStatement.setString(1, message.getMsgId());
            preparedStatement.setString(2, message.getRefMsgId());
            preparedStatement.setString(3, message.getMsgCode());
            preparedStatement.setInt(4, message.getSenderCode());
            preparedStatement.setInt(5, message.getSenderUserId());
            preparedStatement.setString(6, message.getVersion());
            preparedStatement.setString(7, message.getDateTimeCreate().format(formatter));
            preparedStatement.setString(8, message.getDateTimeRecive().format(formatter));
            preparedStatement.setInt(9, status);
            preparedStatement.setString(10, errorMessage);
            preparedStatement.setString(11, transmissionMode);
            preparedStatement.setString(12, sourceMsg);
            preparedStatement.executeUpdate();

            ResultSet generatedIds = preparedStatement.getGeneratedKeys();
            if (generatedIds.next()) {
                messageId = generatedIds.getLong(1);
            }


            // Вставка записи в таблицу reg_data_in_message_str
            String insertIntoRegDataInMessageStrTable = "INSERT INTO reg_data_in_message_str (msg_id, date_time_recive, " +
                    "message_str) VALUES (?, ?, ?)";
            preparedStatement = JdbcUtils.getPreparedStatement(insertIntoRegDataInMessageStrTable);
            preparedStatement.setString(1, message.getMsgId());
            preparedStatement.setString(2, message.getDateTimeRecive().format(formatter));
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(message.getMsgStr());
            preparedStatement.setObject(3, jsonObject);
            preparedStatement.executeUpdate();


            // Вставка записи в таблицу reg_data_in_struct
            String insertIntoRegDataInStructTable = "INSERT INTO reg_data_in_struct (msg_id, id_doc, code_struct, " +
                    "date_time_recive, status, error_text) VALUES (?, ?, ?, ?, ?, ?)";
            for (Structure structure : message.getStructures()) {
                preparedStatement = JdbcUtils.getPreparedStatement(insertIntoRegDataInStructTable);
                preparedStatement.setString(1, message.getMsgId());
                preparedStatement.setString(2, structure.getIdDoc());
                preparedStatement.setString(3, structure.getKodStruct());
                preparedStatement.setString(4, message.getDateTimeRecive().format(formatter));
                preparedStatement.setInt(5, status);
                preparedStatement.setString(6, errorText);
                preparedStatement.executeUpdate();
            }


            // Вставка записи в таблицу reg_data_in_struct_str
            String insertIntoRegDataInStructStrTable = "INSERT INTO reg_data_in_struct_str (id_doc, date_time_recive, " +
                    "struct_str) VALUES (?, ?, ?)";
            for (Structure structure : message.getStructures()) {
                preparedStatement = JdbcUtils.getPreparedStatement(insertIntoRegDataInStructStrTable);
                preparedStatement.setString(1, structure.getIdDoc());
                preparedStatement.setString(2, message.getDateTimeRecive().format(formatter));
                jsonObject = new PGobject();
                jsonObject.setType("json");
                jsonObject.setValue(structure.getStructureStr());
                preparedStatement.setObject(3, jsonObject);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return messageId;
    }
}
