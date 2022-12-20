package org.goravski.exchangeCurrencyBelBot.telegram.handlers.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.goravski.exchangeCurrencyBelBot.telegram.handlers.*;
import org.goravski.exchangeCurrencyBelBot.util.Validator;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
public class MessageHandlerFactory {
    private enum MessageEnum {
        BANKS(BankMessageHandler::new),
        START(StartHandler::new),
        ANALYTICS(AnaliticsMessageHandler::new),
        SET_CURRENCY(SetCurrencyMessageHandler::new),
        EXCHANGE_CURRENCY(ExchangeCurrencyMessageHandler::new),
        ERROR_MESSAGE(ErrorEnterMessageHandler::new);

        MessageEnum(Supplier<AbstractMessageHandler> messageHandler) {
            this.messageHandler = messageHandler.get();
        }

        private final AbstractMessageHandler messageHandler;

        public AbstractMessageHandler createMessageSend() {
            return messageHandler;
        }
    }

    public static BotApiMethod<?> getUpdateMethodFromFactory(Update update) {
        if (Validator.chekCallBackQuery(update)){
            String[] params = update.getCallbackQuery().getData().split(":");
            log.debug("CallBackQueryHandler params: {}", Arrays.toString(params));
            return MessageEnum.valueOf((params[0]).toUpperCase(Locale.ROOT))
                    .createMessageSend().getSendMessage(update);
        }
        Message message = update.getMessage();
        if (Validator.chekEntityType(message, EntityType.BOTCOMMAND)) {
            log.debug("MessageHandler for bot command = {}", message.getText());
            return MessageEnum.valueOf(message.getText().substring(1).toUpperCase(Locale.ROOT))
                    .createMessageSend().getSendMessage(update);
        } else if ( NumberUtils.isDigits(message.getText())){
            log.debug("MessageHandler for digit = {}", message.getText());
            return MessageEnum.EXCHANGE_CURRENCY
                    .createMessageSend().getSendMessage(update);
        }else {
            log.debug("MessageHandler for text = {}", message.getText());
            return MessageEnum.ERROR_MESSAGE
                    .createMessageSend().getSendMessage(update);
        }
    }


}