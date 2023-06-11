package com.example.demo.service;

import com.example.demo.dto.Message;
import com.example.demo.validate.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class CheckStreamHandler {
//    @Value("...")
//    private String topicOut;
//    @Autowired
//    private Dispatcher dispatcher;
//    @Autowired
//    private TransformMsgStrToObject transformMsgStrToObject;
//    @Autowired
//    private FormatControlService formatControlService;
//    @Autowired
//    private RegistrationExchangeService registrationExchangeService;
//    @Autowired
//    private PublishConfirmService publishConfirmService;
//    @Autowired
//    private KafkaService kafkaService;
///*
//    @Transactional(transactionManager = "chainedTransactionManager", rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
//    @KafkaListener(clientIdPrefix = "${}", groupId = "${}", topics = {"${}"}, containerFactory = "batchFactory")
//    public void handlerMsg(String msgStr, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        receiveMsg(msgStr, topic);
//    }
// */
//
//    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
//    private void receiveMsg(String msgStr, String sourceMsg) {
//        Message message;
//        try {
//            message = transformMsgStrToObject.strToMessage(msgStr);
//        } catch (TransformToMessageError e) {
//            log.error(e.getMessage(), e);
//            registrationExchangeService.registrationErrorTransform(msgStr, "ASYNC", sourceMsg);
//            return;
//        }
//        ValidationResult validationResult = formatControlService.validate(message);
//        if (!validationResult.isSuccess()) {
//            registrationExchangeService.registrationErrorValidation(message, validationResult, "ASYNC", sourceMsg);
//            Confirm confirm = new Confirm();
//            publishConfirmService.sendConfirm(message, confirm);
//        } else {
//            kafkaService.send(topicOut, msgStr);
//        }
//    }
}