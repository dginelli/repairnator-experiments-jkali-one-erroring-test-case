package ru.javazen.telegram.bot.handler.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.javazen.telegram.bot.handler.UpdateHandler;
import ru.javazen.telegram.bot.model.UserEntity;
import ru.javazen.telegram.bot.repository.UserEntityRepository;
import ru.javazen.telegram.bot.util.MessageHelper;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActiveUsersHandler implements UpdateHandler {
    private UserEntityRepository userEntityRepository;

    @Override
    public boolean handle(Update update, AbsSender sender) throws TelegramApiException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        List<UserEntity> activeUsers = userEntityRepository.findActiveUsers(calendar.getTime());
        String chats = activeUsers.stream()
                .map(user -> {
                    String fullName = Stream.of(user.getFirstName(), user.getLastName())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(" "));
                    return fullName.isEmpty() ? "[id=" + user.getUserId() + "]" : fullName;
                })
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.joining("\n"));

        String report = MessageFormat.format("Total: {0}\n\n{1}", activeUsers.size(), chats);
        sender.execute(MessageHelper.answer(update.getMessage(), report));
        return true;
    }

    @Autowired
    public void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
