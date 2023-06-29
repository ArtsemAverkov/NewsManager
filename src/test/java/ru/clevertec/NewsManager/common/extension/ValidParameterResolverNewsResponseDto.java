package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.response.CommentResponseDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverNewsResponseDto implements ParameterResolver {


    List<CommentResponseDto> commentResponseDtoList = Arrays.asList(
            new CommentResponseDto(
                    LocalDateTime.now(),
                    " text_comment",
                    "user"
            )
    );

    private final List<NewsResponseDto> newsDtoList = Arrays.asList(
            new NewsResponseDto
                    (LocalDateTime.now(),
                            "test_title",
                            "test_text",
                            "admin",
                            commentResponseDtoList)
    );
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsResponseDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return newsDtoList.get(new Random().nextInt(newsDtoList.size()));
    }
}
