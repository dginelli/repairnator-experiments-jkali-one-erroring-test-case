package ru.javazen.telegram.bot.handler.stats;

import com.google.common.base.MoreObjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.javazen.telegram.bot.handler.UpdateHandler;
import ru.javazen.telegram.bot.model.ChatEntity;
import ru.javazen.telegram.bot.repository.ChatEntityRepository;
import ru.javazen.telegram.bot.util.MessageHelper;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveChatsHandler implements UpdateHandler {
    private ChatEntityRepository chatEntityRepository;

    @Override
    public boolean handle(Update update, AbsSender sender) throws TelegramApiException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        List<ChatEntity> activeChats = chatEntityRepository.findActiveChats(calendar.getTime());
        String chats = activeChats.stream()
                .map(chat -> MoreObjects.firstNonNull(chat.getTitle(), "[id=" + chat.getChatId() + "]"))
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.joining("\n"));

        String report = MessageFormat.format("Total: {0}\n\n{1}", activeChats.size(), chats);
        sender.execute(MessageHelper.answer(update.getMessage(), report));
        return true;
    }

    @Autowired
    public void setChatEntityRepository(ChatEntityRepository chatEntityRepository) {
        this.chatEntityRepository = chatEntityRepository;
    }
}
