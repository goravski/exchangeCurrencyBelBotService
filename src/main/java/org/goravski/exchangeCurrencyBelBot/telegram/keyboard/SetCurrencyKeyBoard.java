package org.goravski.exchangeCurrencyBelBot.telegram.keyboard;

import org.goravski.exchangeCurrencyBelBot.entity.CurrencyName;
import org.goravski.exchangeCurrencyBelBot.service.HashMapCurrencyModeService;
import org.goravski.exchangeCurrencyBelBot.util.LocalConstant;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetCurrencyKeyBoard implements KeyBoardInterface {
    private final HashMapCurrencyModeService currencyModeService = HashMapCurrencyModeService.getInstance();

    @Override
    public ReplyKeyboard getKeyBoard(Update update) {
        List<List<InlineKeyboardButton>> rowButtons = new ArrayList<>();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        CurrencyName originalCurrency = currencyModeService.getOriginalCurrency(chatId);
        CurrencyName targetCurrency = currencyModeService.getTargetCurrency(chatId);
        for (CurrencyName currency : CurrencyName.values()) {
            rowButtons.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                            .text(getCurrencyButton(originalCurrency, currency))
                            .callbackData(LocalConstant.SET_CURRENCY + ":"+  "Продажа" + ":" + currency)
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(getCurrencyButton(targetCurrency, currency))
                            .callbackData(LocalConstant.SET_CURRENCY + ":"+  "Покупка" + ":" + currency)
                            .build()
            ));
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowButtons);
        return inlineKeyboardMarkup;
    }

    public static String getCurrencyButton(CurrencyName saved, CurrencyName current) {
        return saved == current ? current + " ✅" : current.name();
    }

}