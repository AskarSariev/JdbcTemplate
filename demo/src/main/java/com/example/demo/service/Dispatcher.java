package com.example.demo.service;

import com.example.demo.dto.Message;
import com.example.demo.dto.ResultAppModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Slf4j
//@Service
public class Dispatcher {
    //   @Autowired
    // @Qualifier(value = "dispatcherRoute")
    //  private Map<String, String> dispatcherRoute;
    // @Autowired
    //  private RestTemplate restTemplate;
//    @Autowired
//    private RegistrationExchangeService registrationExchangeService;
//
//    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
//    public Confirm receiveMsg(@NotNull Message message, String transmissionMode, String sourceMsg) {
//        try {
//            ResultAppModule resultAppModule = dispatch(message);
//            if (resultAppModule.getStatus() == 40) {
//                registrationExchangeService.registrationSuccess(message, resultAppModule, transmissionMode, sourceMsg);
//                return new Confirm();
//            } else if (resultAppModule.getStatus() == 10) {
//                registrationExchangeService.registrationErrorAppService(message, resultAppModule, transmissionMode, sourceMsg);
//                return new Confirm();
//            } else if (resultAppModule.getStatus() == 11) {
//                registrationExchangeService.registrationErrorStoredProcedure(message, resultAppModule, transmissionMode, sourceMsg);
//                return new Confirm();
//            } else if (resultAppModule.getStatus() == 15) {
//                registrationExchangeService.registrationErrorLogical(message, resultAppModule, transmissionMode, sourceMsg);
//                return new Confirm();
//            } else if (resultAppModule.getStatus() == 30) {
//                registrationExchangeService.registrationWarning(message, resultAppModule, transmissionMode, sourceMsg);
//                return new Confirm();
//            } else {
//                registrationExchangeService.registrationErrorUndefined(message, "Код статуса не определен, равен - " + resultAppModule.getStatus(), transmissionMode, sourceMsg);
//                return new Confirm();
//            }
//        } catch (DispatcherRouteException e) {
//            registrationExchangeService.registrationErrorDispatcherRoute(message, e.getMessage(), transmissionMode, sourceMsg);
//            return new Confirm();
//        } catch (Exception e) {
//            log.error("Не определенная ошибка", e);
//            registrationExchangeService.registrationErrorUndefined(message, "Не определенная ошибка - " + e.getMessage(), transmissionMode, sourceMsg);
//            return new Confirm();
//        }
//    }
//
//    private ResultAppModule dispatch(@NotNull Message message) throws DispatcherRouteException {
//        //  String endpoint = dispatcherRoute.get(message.getMsgCode());
//        String endpoint = null;
//        if (endpoint == null || endpoint.trim().equals("")) {
//            throw new DispatcherRouteException("Для сообщения " + message.getMsgCode() + " не определен маршрут, значение маршрута - " + endpoint);
//        }
//        // Далее через restTemplate идем в АПИ модуля
//        return new ResultAppModule();
//    }
}
