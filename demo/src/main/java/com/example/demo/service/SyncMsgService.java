package com.example.demo.service;

import com.example.demo.validate.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class SyncMsgService {
//    @Autowired
//    private Dispatcher dispatcher;
//    @Autowired
//    private TransformMsgStrToObject transformMsgStrToObject;
//    @Autowired
//    private FormatControlService formatControlService;
//    @Autowired
//    private RegistrationExchangeService registrationExchangeService;
//
//    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
//    public Confirm receiveMsg(String msgStr) throws Exception {
//        Message message;
//        try {
//            message = transformMsgStrToObject.strToMessage(msgStr);
//        } catch (TransformToMessageError e) {
//            log.error(e.getMessage(), e);
//            registrationExchangeService.registrationErrorTransform(msgStr, "SYNC", "SyncMsgService");
//            throw e;
//        }
//        ValidationResult validationResult = formatControlService.validate(message);
//        if (!validationResult.isSuccess()) {
//            registrationExchangeService.registrationErrorValidation(message, validationResult, "SYNC", "SyncMsgService");
//            return new Confirm();
//        }
//        return dispatcher.receiveMsg(message, "SYNC", "SyncMsgService");
//    }
}